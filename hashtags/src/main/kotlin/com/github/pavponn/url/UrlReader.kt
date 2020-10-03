package com.github.pavponn.url

import com.github.pavponn.exceptions.UrlReaderException
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL

/**
 * @author pavponn
 */
class UrlReader {

    public fun readAsString(sourceUrl: String): String {
        val url = toUrl(sourceUrl)
        BufferedReader(InputStreamReader(url.openStream())).use {
            val builder = StringBuilder()
            for (line in it.lines()) {
                builder.appendLine(line)
            }
            return builder.toString()
        }
    }

    private fun toUrl(urlString: String): URL {
        try {
            return URL(urlString)
        } catch (e: MalformedURLException) {
            throw UrlReaderException("Can't create url from string $urlString")
        }
    }
}