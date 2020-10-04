package com.github.pavponn.url

import com.github.pavponn.exceptions.UrlReaderException
import com.xebialabs.restito.builder.stub.StubHttp.whenHttp
import com.xebialabs.restito.semantics.Action
import com.xebialabs.restito.semantics.Condition
import com.xebialabs.restito.server.StubServer
import org.glassfish.grizzly.http.Method
import org.glassfish.grizzly.http.util.HttpStatus
import org.junit.Assert
import org.junit.Test
import java.io.FileNotFoundException
import java.io.IOException

/**
 * @author pavponn
 */
class UrlReaderStubTest {
    companion object {
        const val PORT = 32453
        const val BASE_URI = "http://localhost:$PORT"
        const val GET_METHOD = "/ping"
    }

    @Test
    fun `should read result`() {
        val content = "pong"
        withStubServer { s ->
            whenHttp(s)
                .match(Condition.method(Method.GET), Condition.startsWithUri(GET_METHOD))
                .then(Action.stringContent(content))

            val result = UrlReader().readAsString("$BASE_URI$GET_METHOD")
            Assert.assertEquals("$content\n", result)
        }
    }

    @Test(expected = FileNotFoundException::class)
    fun `should throw FileNotFoundException if 404`() {
        withStubServer { s ->
            whenHttp(s)
                .match(Condition.method(Method.GET), Condition.startsWithUri(GET_METHOD))
                .then(Action.status(HttpStatus.NOT_FOUND_404))
            UrlReader().readAsString("$BASE_URI$GET_METHOD")
        }
    }

    @Test(expected = IOException::class)
    fun `should throw IOException if 403`() {
        withStubServer { s ->
            whenHttp(s)
                .match(Condition.method(Method.GET), Condition.startsWithUri(GET_METHOD))
                .then(Action.status(HttpStatus.FORBIDDEN_403))
            UrlReader().readAsString("$BASE_URI$GET_METHOD")
        }
    }

    @Test(expected = UrlReaderException::class)
    fun `should throw exception if url is invalid`() {
        val content = "pong"
        withStubServer { s ->
            whenHttp(s)
                .match(Condition.method(Method.GET), Condition.startsWithUri(GET_METHOD))
                .then(Action.stringContent(content))
            UrlReader().readAsString("some_invalid_url$GET_METHOD")
        }
    }

    private fun withStubServer(callback: (StubServer?) -> Unit) {
        var stubServer: StubServer? = null
        try {
            stubServer = StubServer(PORT).run()
            callback(stubServer)
        } finally {
            stubServer?.stop()
        }
    }

}
