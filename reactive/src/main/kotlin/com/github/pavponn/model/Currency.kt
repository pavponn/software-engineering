package com.github.pavponn.model

enum class Currency {
    EUR {
        override fun toString(): String {
            return "EUR"
        }

        override fun symbol(): String {
            return "€"
        }
    },

    USD {
        override fun toString(): String {
            return "USD"
        }

        override fun symbol(): String {
            return "$"
        }
    },

    RUB {
        override fun toString(): String {
            return "RUB"
        }

        override fun symbol(): String {
            return "₽"
        }
    };

    abstract fun symbol(): String
}