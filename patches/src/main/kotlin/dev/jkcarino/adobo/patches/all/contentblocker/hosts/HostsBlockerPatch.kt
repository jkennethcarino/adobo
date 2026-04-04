package dev.jkcarino.adobo.patches.all.contentblocker.hosts

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
        description = "The hosts file containing hosts or domains you want to block.",
        required = true
    ) { filePath ->
        !filePath.isNullOrEmpty() && File(filePath.trim()).isFile
    }

    val redirectionIpOption by stringOption(
        key = "redirectionIp",
        title = "Redirection IP",
        default = DEFAULT_REDIRECTION_IP,
        values = mapOf(
            "Default" to DEFAULT_REDIRECTION_IP,
            "localhost" to "127.0.0.1"
        ),
        description = "The IP address to redirect blocked domains to. " +
            "This will be used with your hosts list to block content.",
        required = true
    ) { ipAddress ->
        // Basic validation but this doesn't validate whether the IP address is valid
        val ipAddressPattern = """^\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}$""".toRegex()
        !ipAddress.isNullOrEmpty() && ipAddress.matches(ipAddressPattern)
    }

    dependsOn(
        baseHostsBlockerPatch {
            val hostsFile = File(hostsOption!!.trim())
            val hostsBlocker = HostsBlocker.fromFile(hostsFile)

            HostsBlockerConfig(
                hostsBlocker = hostsBlocker,
                redirectionIp = redirectionIpOption!!
            )
        }
    )
}
