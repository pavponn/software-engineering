package com.github.pavponn.converter

import com.github.pavponn.model.Currency

interface Converter {
    fun convert(amount: Double, from: Currency, to: Currency): Double

}