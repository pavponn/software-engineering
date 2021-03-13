package com.github.pavponn.dao

import com.github.pavponn.model.Product
import com.github.pavponn.model.User
import rx.Observable

interface ReactiveProductsReadDao {

    fun getUsers(): Observable<User>
    fun getUserById(id: Long): Observable<User>

    fun getProducts(): Observable<Product>
    fun getProductById(id: Long): Observable<Product>
    fun getProductsForUser(userId: Long): Observable<Product>
    fun getProductByIdForUser(id: Long, userId: Long): Observable<Product>
}