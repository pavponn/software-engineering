package accounts.http

interface StockMarketHttpClient : AutoCloseable {

    suspend fun getCompaniesStockPrices(companyTickers: List<String>): Map<String, Long>

    suspend fun getCompaniesStockPrices(vararg companyTickers: String): Map<String, Long> =
        getCompaniesStockPrices(companyTickers.asList())

    suspend fun buyStocks(companyTicker: String, count: Long, price: Long): Long

    suspend fun sellStocks(companyTicker: String, count: Long): Long
}