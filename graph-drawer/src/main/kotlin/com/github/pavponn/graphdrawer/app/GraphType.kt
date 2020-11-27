package com.github.pavponn.graphdrawer.app

import com.github.pavponn.graphdrawer.draw.DrawingApi
import com.github.pavponn.graphdrawer.graph.Graph
import com.github.pavponn.graphdrawer.graph.GraphParser

enum class GraphType {
    Matrix {
        override fun parseGraph() = GraphParser.parseMatrixGraph()
    },
    List {
        override fun parseGraph() = GraphParser.parseListGraph()

    };

    abstract fun parseGraph(): (DrawingApi) -> Graph
}