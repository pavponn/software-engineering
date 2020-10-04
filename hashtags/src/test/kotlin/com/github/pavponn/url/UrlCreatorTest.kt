package com.github.pavponn.url

import org.junit.Assert
import org.junit.Test

class UrlCreatorTest {

    companion object {
        const val BASE_URL = "https://localhost:8080/method"
    }

    @Test
    fun `should create valid url without params`() {
        val url= UrlCreator().getUrlString(BASE_URL, emptyMap())
        Assert.assertEquals(url, BASE_URL)
    }
    
    @Test
    fun `should create valud url with one param`() {
        val key = "a"
        val value = "2"
        val url = UrlCreator().getUrlString(BASE_URL, mapOf(Pair(key, value)))
        Assert.assertEquals(url, "$BASE_URL?$key=$value")
    }

    @Test
    fun `should create valid url with several param`() {
        val param0 = Pair("a", "0")
        val param1 = Pair("b", "1")
        val param2 = Pair("c", "2")
        val params = mapOf(param0, param1, param2)
        val url = UrlCreator().getUrlString(BASE_URL, params)
        val baseAndParams = url.split("?")
        Assert.assertEquals(baseAndParams.size, 2)
        Assert.assertEquals(baseAndParams[0], BASE_URL)
        val paramsString = baseAndParams[1].split("&").sorted()
        println(paramsString)
        Assert.assertEquals(paramsString[0], param0.toParamString())
        Assert.assertEquals(paramsString[1], param1.toParamString())
        Assert.assertEquals(paramsString[2], param2.toParamString())
    }


    private fun Pair<String,String>.toParamString() = "${this.first}=${this.second}"
}