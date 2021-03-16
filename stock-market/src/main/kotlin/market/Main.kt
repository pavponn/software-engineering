package market

import market.dao.InMemoryStockMarketDao
import market.model.Stocks

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.runBlocking

fun main(): Unit = runBlocking {
    val stockMarketDao = InMemoryStockMarketDao()
    embeddedServer(Netty, port = 8080) {
        routing {
            // list company on the market
            post("/company") {
                val ticker = call.request.queryParameters["ticker"]
                val price = call.request.queryParameters["price"]?.toLong()
                val count = call.request.queryParameters["count"]?.toLong()
                if (ticker == null || price == null || count == null) {
                    call.badRequest()
                } else {
                    try {
                        stockMarketDao.listCompany(ticker, Stocks(ticker, price, count))
                    } catch (e: Exception) {
                        call.error(e.message)
                    }
                }
            }
            // get company stocks info
            get("/company/{ticker}") {
                val ticker = call.parameters["ticker"]
                if (ticker == null) {
                    call.badRequest()
                } else {
                    try {
                        val stocksInfo = stockMarketDao.getStocksInfo(ticker)
                        call.respondText("${stocksInfo.companyTicker},${stocksInfo.count},${stocksInfo.price}")
                    } catch (e: Exception) {
                        call.error(e.message)
                    }
                }
            }
            // buy or sell stocks
            get("/company/{ticker}/stocks") {
                val ticker = call.parameters["ticker"]
                val actionType = call.request.queryParameters["action"]
                val count = call.request.queryParameters["count"]?.toLong()
                val price = call.request.queryParameters["price"]?.toLong()
                if (ticker == null || actionType == null || count == null || (
                            actionType != "buy" && actionType != "sell")
                ) {
                    call.badRequest()
                } else if (actionType == "buy" && price == null) {
                    call.badRequest()
                } else {
                    try {
                        val result = if (actionType == "buy") {
                            stockMarketDao.buyStocks(ticker, count, price!!)
                        } else {
                            stockMarketDao.sellStocks(ticker, count)
                        }
                        call.respondText(result.toString())
                    } catch (e: Exception) {
                        call.error(e.message)
                    }
                }
            }
            // add stocks or change price
            post("/company/{ticker}/stocks") {
                val ticker = call.parameters["ticker"]
                val count = call.request.queryParameters["count"]?.toLong()
                val price = call.request.queryParameters["price"]?.toLong()
                if (ticker == null || (count == null && price == null)) {
                    call.badRequest()
                } else if (count != null && price != null) {
                    call.badRequest()
                } else {
                    try {
                        if (price != null) {
                            stockMarketDao.changeStocksPrice(ticker, price)
                            call.respondText("Price has been changed")
                        } else {
                            stockMarketDao.addStocks(ticker, count!!)
                            call.respondText("Stocks have been added")
                        }
                    } catch (e: Exception) {
                        call.error(e.message)
                    }
                }
            }

        }
    }.start(wait = true)
    Unit
}


suspend fun ApplicationCall.badRequest(): Unit = respondText("Incorrect parameters", status = HttpStatusCode.BadRequest)

suspend fun ApplicationCall.error(errorMsg: String?): Unit =
    respondText("Error occurred: $errorMsg", status = HttpStatusCode.BadRequest)