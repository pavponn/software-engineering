package com.github.pavponn.vk.models

import java.time.Duration

/**
 * @author pavponn
 */
data class VkConfig(val accessToken: String,
                    val urlConfig: UrlConfig,
                    val version: Version,
                    val timeout: Duration = Duration.ofMillis(500))

data class Version(val major: Int, val minor: Int)

data class UrlConfig(val schema: String, val host:String, val port: Int)
