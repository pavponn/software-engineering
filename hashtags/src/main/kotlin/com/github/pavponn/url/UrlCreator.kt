package com.github.pavponn.url

import java.lang.StringBuilder

/**
 * @author pavponn
 */
class UrlCreator {

    public fun getUrlString(baseUrl: String, params: Map<String, String>): String {
        val urlBuilder = StringBuilder(baseUrl)
        if (params.isEmpty()) {
            return urlBuilder.toString()
        }
        urlBuilder.append("?")
        for (param in params) {
            urlBuilder.append(param.key).append("=").append(param.value).append("&")
        }
        urlBuilder.delete(urlBuilder.lastIndex, urlBuilder.lastIndex + 1)

        return urlBuilder.toString()
    }
}