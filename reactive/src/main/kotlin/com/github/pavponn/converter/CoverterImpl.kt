package com.github.pavponn.converter

import com.github.pavponn.model.Currency

class ConverterImpl(
    private val exchangeRate: Map<Pair<Currency, Currency>, Double>
) : Converter {

    override fun convert(amount: Double, from: Currency, to: Currency): Double {
        return amount * exchangeRate[Pair(from, to)]!!
    }
}