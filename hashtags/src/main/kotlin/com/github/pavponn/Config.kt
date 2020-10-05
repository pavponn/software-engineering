package com.github.pavponn

import com.natpryce.konfig.*

val config = ConfigurationProperties.fromResource("defaults.properties")

object Vk : PropertyGroup() {
    object Version : PropertyGroup() {
        val major by intType
        val minor by intType
    }
    object Url : PropertyGroup() {
        val schema by stringType
        val host by stringType
        val port by intType
    }
    val service_key by stringType
}