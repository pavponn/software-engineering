package com.github.pavponn.server

import com.github.pavponn.model.SearchEngine

interface SearchService {
    fun search(query: String): String

    fun engine(): SearchEngine
}