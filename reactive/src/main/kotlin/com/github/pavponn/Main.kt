package com.github.pavponn


import com.github.pavponn.converter.ConverterImpl
import com.github.pavponn.dao.MongoReactiveProductsDao
import com.github.pavponn.router.Router
import com.github.pavponn.model.Currency
import com.mongodb.rx.client.MongoClients
import com.mongodb.rx.client.MongoDatabase
import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import io.netty.buffer.ByteBuf
import io.reactivex.netty.protocol.http.server.HttpServer
import io.reactivex.netty.protocol.http.server.ResponseContentWriter
import java.nio.file.Paths

/**
 * Examples:
 *  curl -X POST "localhost:8090/users?id=1&cur=RUB&login=pavponn"
 *  curl -X POST "localhost:8090/users?id=2&cur=EUR&login=max"
 *  curl -X POST "localhost:8090/products?id=1&cur=EUR&price=400&name=ps5"
 *  curl -X POST "localhost:8090/products?id=2&cur=USD&price=300&name=xbox"
 *  curl -X GET "localhost:8090/products"
 *  curl -X GET "localhost:8090/products?userId=2"
 *  curl -X GET "localhost:8090/products?userId=1"
 *  curl -X GET "localhost:8090/products?id=2&userId=1"
 */
fun main() {
    val appConfig = getAppConfig()
    val mongoClient = getMongoDatabaseClient(appConfig)
    val converter = ConverterImpl(getExchangeRates())
    val mongoReactiveDao = MongoReactiveProductsDao(mongoClient, converter)
    val router = Router(mongoReactiveDao)
    HttpServer.newServer(8090)
        .start { request, response ->
            val resp = router.route(request)
            response.writeString(resp)
        }
        .awaitShutdown()
}

fun getAppConfig(): Config {
    val confFile = Paths.get("src/main/resources/application.conf").toFile()
    return ConfigFactory.parseFile(confFile)!!
}

fun getMongoDatabaseClient(appConfig: Config): MongoDatabase {
    val config = appConfig.getConfig("database")
    val schema = config.getString("schema")
    val host = config.getString("host")
    val port = config.getString("port")
    val name = config.getString("name")
    val url = "$schema://$host:$port"
    return MongoClients.create(url).getDatabase(name)!!
}

fun getExchangeRates(): Map<Pair<Currency, Currency>, Double> {
    return mapOf(
        Pair(Currency.USD, Currency.RUB) to 73.0,
        Pair(Currency.RUB, Currency.USD) to 0.014,
        Pair(Currency.EUR, Currency.RUB) to 89.0,
        Pair(Currency.RUB, Currency.EUR) to 0.012,
        Pair(Currency.USD, Currency.EUR) to 1.2,
        Pair(Currency.EUR, Currency.USD) to 0.83,
        Pair(Currency.RUB, Currency.RUB) to 1.0,
        Pair(Currency.EUR, Currency.EUR) to 1.0,
        Pair(Currency.USD, Currency.USD) to 1.0,
    )
}