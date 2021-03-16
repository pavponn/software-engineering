package accounts.http

import accounts.config.HttpClientConfig
import io.ktor.client.*
import io.ktor.client.features.*

class KtorStockMarketHttpClient(config: HttpClientConfig) : AbstractHttpClient(), StockMarketHttpClient {

    private val baseUrl = "${config.schema}://${config.host}:${config.port}"

    override val client = HttpClient {
        install(HttpTimeout) {
            requestTimeoutMillis = 100
        }

        expectSuccess = false
    }

    override suspend fun getCompaniesStockPrices(companyTickers: List<String>): Map<String, Long> {
        val result = HashMap<String, Long>()
        for (companyTicker in companyTickers) {
            val url = "$baseUrl/company/$companyTicker"
            val responseString = doGet(url)
            val responseParts = responseString.split(",")
            assert(responseParts.size == 3) { "Unexpected format" }
            result[responseParts[0]] = responseParts[2].toLong()
        }
        return result
    }

    override suspend fun buyStocks(companyTicker: String, count: Long, price: Long): Long {
        val url = "$baseUrl/company/$companyTicker/stocks?action=buy&count=$count&price=$price"
        return doGet(url).toLong()
    }

    override suspend fun sellStocks(companyTicker: String, count: Long): Long {
        val url = "$baseUrl/company/$companyTicker/stocks?action=sell&count=$count"
        return doGet(url).toLong()
    }

    override fun close() {
        client.close()
    }
}
