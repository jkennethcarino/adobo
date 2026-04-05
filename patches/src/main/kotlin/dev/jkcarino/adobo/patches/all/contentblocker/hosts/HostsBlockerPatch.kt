package dev.jkcarino.adobo.patches.all.contentblocker.hosts

import app.morphe.patcher.patch.booleanOption
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.patch.stringOption
import java.io.File

@Suppress("unused")
val hostsBlockerPatch = bytecodePatch(
    name = "Block ads, trackers, and analytics",
    description = "Blocks ads, trackers, analytics, and unwanted content in apps and games " +
        "using a hosts file.",
    default = false
) {
    val hostsOption by stringOption(
        key = "hosts",
        default = null,
        title = "Hosts file",
        description = "The hosts file containing hosts or domains you want to block. " +
            "Select a file or paste the full file path.",
        required = true
    ) { filePath ->
        !filePath.isNullOrEmpty() && File(filePath.trim()).isFile
    }

    val redirectionIpOption by stringOption(
        key = "redirectionIp",
        default = DEFAULT_REDIRECTION_IP,
        values = mapOf(
            "Default" to DEFAULT_REDIRECTION_IP,
            "localhost" to "127.0.0.1"
        ),
        title = "Redirection IP",
        description = "The IP address to redirect blocked domains to. " +
            "This will be used with your hosts list to block content.",
        required = true
    ) { ipAddress ->
        // Basic validation but this doesn't validate whether the IP address is valid
        val ipAddressPattern = """^\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}$""".toRegex()
        !ipAddress.isNullOrEmpty() && ipAddress.matches(ipAddressPattern)
    }

    val isWildcardOption by booleanOption(
        key = "isWildcard",
        default = true,
        title = "Wildcard blocking",
        description = "When enabled, entries like \"example.com\" also block subdomains like " +
            "\"www.example.com\" and \"sub.www.example.com\"."
    )

    dependsOn(
        baseHostsBlockerPatch {
            val hostsFile = File(hostsOption!!.trim())
            val hostsBlocker = HostsBlocker.fromFile(hostsFile)

            HostsBlockerConfig(
                hostsBlocker = hostsBlocker,
                redirectionIp = redirectionIpOption!!,
                wildcard = isWildcardOption!!
            )
        }
    )
}
