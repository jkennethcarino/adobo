package dev.jkcarino.adobo.patches.all.contentblocker.hosts

import java.io.File
import java.net.IDN
import java.net.URI

class HostsBlocker private constructor(
    private val blocklist: HashSet<String>,
) {
    /**
     * Checks if the given [host] is blocked based on the blocklist. It performs exact matching and
     * subdomain matching. For example, if "example.com" is in the blocklist, then "example.com",
     * "www.example.com", and "sub.www.example.com" would all be considered blocked.
     *
     * @param host The host to check.
     * @return true if the host is blocked, false otherwise.
     */
    fun isBlocked(host: String): Boolean {
        if (host.isBlank()) return false

        var normalizedHost = normalizeDomain(host)
            .let(::extractHost)
            ?.takeIf(::isHostValid)
            ?: return false

        if (blocklist.contains(normalizedHost)) {
            return true
        }

        while (normalizedHost.contains(DOT_CHAR)) {
            normalizedHost = normalizedHost.substringAfter(DOT_CHAR)
            if (blocklist.contains(normalizedHost)) {
                return true
            }
        }
        return false
    }

    fun close() {
        blocklist.clear()
    }

    /**
     * Normalizes a domain name to its ASCII-compatible form (Punycode) and in lowercase.
     *
     * @param domain The domain name to be normalized.
     * @return The normalized domain name in lowercase (e.g., "example.com" or "xn--48j0a0b.com").
     */
    private fun normalizeDomain(domain: String): String {
        val trimmedDomain = domain.trim().trimEnd('.')
        return runCatching { IDN.toASCII(trimmedDomain) }
            .getOrDefault(domain)
            .lowercase()
    }

    companion object {
        private const val COMMENT_CHAR = '#'
        private const val SPACE_CHAR = ' '
        private const val DOT_CHAR = '.'

        private const val MAX_DOMAIN_LENGTH = 253
        private const val MAX_PARTS = 127
        private const val MIN_PARTS = 2
        private const val MAX_PARTS_LENGTH = 63

        private val RESERVED_HOSTNAMES = setOf(
            "localhost",
            "localhost6",
            "localhost.localdomain",
            "localhost6.localdomain6",
            "local",
            "broadcasthost",

            // IPv4
            "127.0.0.1",
            "0.0.0.0",

            // IPv6
            "::1",
            "ip6-localhost",
            "ip6-loopback",
            "ip6-localnet",
            "ip6-mcastprefix",
            "ip6-allnodes",
            "ip6-allrouters",
            "ip6-allhosts",
        )

        /**
         * Creates an [HostsBlocker] instance by reading a blocklist from the given [file].
         *
         * @param file The file containing the blocklist.
         * @return An instance of [HostsBlocker] initialized with the blocklist from the file.
         */
        fun fromFile(file: File): HostsBlocker {
            val blocklist = hashSetOf<String>()
            file.useLines { lines ->
                parseLines(lines, blocklist)
            }
            return HostsBlocker(blocklist)
        }

        /**
         * Extracts the host from a given [input] string, which may be a URL or a hostname.
         *
         * @param input The input string.
         * @return The extracted host, or `null` if extraction fails.
         */
        fun extractHost(input: String): String? {
            val urlWithScheme = if (input.contains("://")) input else "http://$input"
            return runCatching { URI.create(urlWithScheme).host }.getOrNull()
        }

        /**
         * Parses blocklist lines and adds valid hosts to the output set.
         *
         * @param lines Sequence of lines to parse.
         * @param out Mutable set to store valid hosts.
         */
        private fun parseLines(
            lines: Sequence<String>,
            out: MutableSet<String>,
        ) {
            for (line in lines) {
                // 0.0.0.0 example.com # comment here -> 0.0.0.0 example.com
                // example.com # comment here         -> example.com
                // # comment only                     -> (ignored)
                val trimmed = line.substringBefore(COMMENT_CHAR).trim()
                if (trimmed.isBlank()) continue

                // 0.0.0.0 example.com     -> example.com
                // https://example.com/ads -> example.com
                // http://ads.example.com  -> ads.example.com
                val host =
                    extractHost(
                        trimmed
                            .substringAfter(SPACE_CHAR)
                            .trim()
                    ) ?: continue

                if (host in RESERVED_HOSTNAMES) continue

                if (isHostValid(host)) {
                    out.add(host.lowercase())
                }
            }
        }

        /**
         * Validates a host string based on length and format constraints.
         *
         * @param input The host string to validate.
         * @return true if the host is valid, false otherwise.
         */
        private fun isHostValid(input: String): Boolean {
            if (input.isBlank() || input.length > MAX_DOMAIN_LENGTH) return false
            if (input.startsWith(DOT_CHAR) || input.endsWith(DOT_CHAR)) return false

            val parts = input.split(DOT_CHAR)
            if (parts.size !in MIN_PARTS..MAX_PARTS) return false

            for (part in parts) {
                if (part.length > MAX_PARTS_LENGTH) return false
            }
            return true
        }
    }
}
