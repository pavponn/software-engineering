package com.github.pavponn.router

import com.github.pavponn.dao.ReactiveProductsDao
import com.github.pavponn.model.Currency
import com.github.pavponn.model.Product
import com.github.pavponn.model.User
import io.reactivex.netty.protocol.http.server.HttpServerRequest
import io.netty.handler.codec.http.HttpMethod
import rx.Observable

class Router(private val reactiveMongoDao: ReactiveProductsDao) {


    fun <T> route(request: HttpServerRequest<T>): Observable<String> {
        val path = request.decodedPath.substring(1)
        return when (path) {
            "users" -> processUsers(request)
            "products" -> processProducts(request)
            else -> Observable.just("$ERROR_PATH $path")

        }
    }

    private fun <T> processUsers(request: HttpServerRequest<T>): Observable<String> {
        return when (request.httpMethod) {
            HttpMethod.GET -> getUsers(request)
            HttpMethod.POST -> registerUser(request)
            else -> Observable.just(ERROR_METHOD)
        }
    }


    private fun <T> processProducts(request: HttpServerRequest<T>): Observable<String> {
        return when (request.httpMethod) {
            HttpMethod.GET -> getProducts(request)
            HttpMethod.POST -> addProduct(request)
            else -> Observable.just(ERROR_METHOD)
        }
    }

    private fun <T> getUsers(request: HttpServerRequest<T>): Observable<String> {
        val id = request.queryParameters["id"]?.get(0)?.toLong()
        return if (id == null) {
            reactiveMongoDao.getUsers().map { it.toString() }
        } else {
            reactiveMongoDao.getUserById(id).map { it.toString() }
        }
    }

    private fun <T> registerUser(request: HttpServerRequest<T>): Observable<String> {
        val id = request.queryParameters["id"]?.get(0)?.toLong()
        val login = request.queryParameters["login"]?.get(0)
        val currencyString = request.queryParameters["cur"]?.get(0)
        if (id == null || login == null || currencyString == null) {
            return Observable.just(ERROR_QUERY_PARAMETERS)
        }
        val currency = Currency.valueOf(currencyString.toUpperCase())
        val user = User(id, login, currency)

        return reactiveMongoDao.addUser(user).map { it.toString() }
    }

    private fun <T> getProducts(request: HttpServerRequest<T>): Observable<String> {
        val id = request.queryParameters["id"]?.get(0)?.toLong()
        val userId = request.queryParameters["userId"]?.get(0)?.toLong()

        return if (id == null && userId == null) {
            reactiveMongoDao.getProducts().map { it.toString() }
        } else if (id == null && userId != null) {
            reactiveMongoDao.getProductsForUser(userId).map { it.toString() }
        } else if (id != null && userId == null) {
            reactiveMongoDao.getProductById(id).map { it.toString() }
        } else {
            reactiveMongoDao.getProductByIdForUser(id!!, userId!!).map { it.toString() }
        }
    }

    private fun <T> addProduct(request: HttpServerRequest<T>): Observable<String> {
        val id = request.queryParameters["id"]?.get(0)?.toLong()
        val name = request.queryParameters["name"]?.get(0)
        val price = request.queryParameters["price"]?.get(0)?.toDouble()
        val cur = request.queryParameters["cur"]?.get(0)

        if (id == null || name == null || price == null) {
            return Observable.just(ERROR_QUERY_PARAMETERS)
        }

        val currency = if (cur == null) {
            Currency.USD
        } else {
            Currency.valueOf(cur)
        }

        return reactiveMongoDao.addProduct(Product(id, name, price, currency)).map { it.toString() }
    }

    companion object {
        const val ERROR_QUERY_PARAMETERS = "Error: Incorrect query parameters in the request"
        const val ERROR_METHOD = "Error: unsupported method"
        const val ERROR_PATH = "Error: incorrect path"
    }

}