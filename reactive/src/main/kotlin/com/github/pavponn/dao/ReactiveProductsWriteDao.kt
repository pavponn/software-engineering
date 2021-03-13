package com.github.pavponn.dao

import com.github.pavponn.model.Product
import com.github.pavponn.model.User
import rx.Observable

interface ReactiveProductsWriteDao {

    fun addUser(user: User): Observable<Boolean>
    fun addProduct(product: Product): Observable<Boolean>
}