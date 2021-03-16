package market.dao

import market.model.Stocks

interface StockMarketReadDao {

    fun getStocksInfo(companyTicker: String): Stocks

}