package com.github.pavponn.dao

import com.github.pavponn.converter.Converter
import com.github.pavponn.model.Currency
import com.github.pavponn.model.Product
import com.github.pavponn.model.User
import com.mongodb.client.model.Filters
import com.mongodb.rx.client.MongoCollection
import com.mongodb.rx.client.MongoDatabase
import org.bson.Document
import rx.Observable
import rx.schedulers.Schedulers

class MongoReactiveProductsDao(
    private val db: MongoDatabase,
    private val converter: Converter
) : ReactiveProductsDao {

    override fun getUsers(): Observable<User> {
        return db.getCollection(USERS)
            .find()
            .toObservable()
            .map {
                User(
                    it.getLong(ID),
                    it.getString(LOGIN),
                    Currency.valueOf(it.getString(CURRENCY))
                )
            }.subscribeOn(scheduler)
    }

    override fun getProducts(): Observable<Product> {
        return db.getCollection(PRODUCTS)
            .find()
            .toObservable()
            .map { getProduct(it) }
            .subscribeOn(scheduler)
    }

    override fun getProductsForUser(userId: Long): Observable<Product> {
        return getUserById(userId).flatMap { user ->
            db.getCollection(PRODUCTS)
                .find()
                .toObservable()
                .map { getProductForUser(it, user) }
                .subscribeOn(scheduler)
        }
    }

    override fun getProductById(id: Long): Observable<Product> {
        return db.getCollection(PRODUCTS)
            .find()
            .toObservable()
            .filter { it.getLong(ID) == id }
            .map { getProduct(it) }
            .subscribeOn(scheduler)
    }

    override fun getProductByIdForUser(id: Long, userId: Long): Observable<Product> {
        return getUserById(userId).flatMap { user ->
            db.getCollection(PRODUCTS)
                .find()
                .toObservable()
                .filter { it.getLong(ID) == id }
                .map { getProductForUser(it, user) }
                .subscribeOn(scheduler)
        }
    }

    override fun getUserById(id: Long): Observable<User> {
        return db.getCollection(USERS)
            .find(Filters.eq(ID, id))
            .toObservable()
            .map {
                User(
                    id,
                    it.getString(LOGIN),
                    Currency.valueOf(it.getString(CURRENCY))
                )
            }.subscribeOn(scheduler)
    }

    override fun addUser(user: User): Observable<Boolean> {
        val document = Document()
        document
            .append(ID, user.id)
            .append(LOGIN, user.login)
            .append(CURRENCY, user.currency.toString())
        return addById(user.id, document, db.getCollection(USERS))
    }

    override fun addProduct(product: Product): Observable<Boolean> {
        val document = Document()
        document
            .append(ID, product.id)
            .append(NAME, product.name)
            .append(PRICE, product.price)
            .append(CURRENCY, product.currency.toString())
        return addById(product.id, document, db.getCollection(PRODUCTS))
    }

    private fun getProductForUser(document: Document, user: User): Product {
        val originalPrice = document.getDouble(PRICE)
        val originalCurrency = Currency.valueOf(document.getString(CURRENCY))
        return Product(
            document.getLong(ID),
            document.getString(NAME),
            converter.convert(originalPrice, originalCurrency, user.currency),
            user.currency
        )
    }

    private fun getProduct(document: Document): Product {
        return Product(
            document.getLong(ID),
            document.getString(NAME),
            document.getDouble(PRICE),
            Currency.valueOf(document.getString(CURRENCY))
        )
    }

    private fun addById(id: Long, document: Document, collection: MongoCollection<Document>): Observable<Boolean> {
        return collection
            .find()
            .toObservable()
            .filter { it.getLong(ID) == id }
            .singleOrDefault(null)
            .flatMap { doc ->
                if (doc != null) {
                    Observable.just(false)
                } else {
                    collection
                        .insertOne(document)
                        .asObservable()
                        .isEmpty
                        .map { !it }
                }
            }.subscribeOn(scheduler)
    }

    companion object {
        private val scheduler = Schedulers.io()
        private const val USERS = "users"
        private const val PRODUCTS = "products"
        private const val ID = "id"
        private const val NAME = "name"
        private const val PRICE = "price"
        private const val CURRENCY = "currency"
        private const val LOGIN = "login"

    }
}