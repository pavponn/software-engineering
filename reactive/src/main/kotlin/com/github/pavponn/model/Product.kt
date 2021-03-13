package com.github.pavponn.model

data class Product(val id: Long, val name: String, val price: Double, val currency: Currency = Currency.USD)
