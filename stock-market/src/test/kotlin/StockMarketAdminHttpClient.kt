import accounts.config.HttpClientConfig
import accounts.http.AbstractHttpClient
import io.ktor.client.*

class StockMarketAdminHttpClient(private val config: HttpClientConfig) : AbstractHttpClient() {

    override val client = HttpClient {
        expectSuccess = false
    }

    suspend fun listCompany(companyTicker: String, amount: Long, price: Long) {
        val url =
            "${config.schema}://${config.host}:${config.port}/company?ticker=$companyTicker&price=$price&count=$amount"
        doPost(url)
    }

    suspend fun changePrice(companyTicker: String, price: Long) {
        val url = "${config.schema}://${config.host}:${config.port}/company/$companyTicker/stocks?price=$price"
        doPost(url)
    }

    suspend fun changeAmount(companyTicker: String, amount: Long) {
        val url = "${config.schema}://${config.host}:${config.port}/company/$companyTicker/stocks?count=$amount"
        doPost(url)
    }
}
