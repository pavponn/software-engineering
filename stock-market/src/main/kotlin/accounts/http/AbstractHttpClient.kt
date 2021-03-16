package accounts.http

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

abstract class AbstractHttpClient {
    protected abstract val client: HttpClient

    protected suspend fun doGet(url: String): String {
        val response = client.get<HttpResponse>(url)
        return response.readText()
    }

    protected suspend fun doPost(url: String): String {
        val response = client.post<HttpResponse>(url)
        return response.readText()
    }
}