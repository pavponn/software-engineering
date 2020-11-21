package com.github.pavponn.graphdrawer.graph

import com.github.pavponn.graphdrawer.draw.DrawingApi
import kotlin.math.max

class ListGraph(private val vertexesCount: Int, private val edges: List<Edge>, drawingApi: DrawingApi) :
    Graph(drawingApi) {

    init {
        val maxVertex = edges.stream().mapToInt { max(it.from.index, it.to.index) }.max().asInt
        require(vertexesCount > maxVertex) { "VertexesCount should be more than maximum index of vertex in edges" }
    }

    override fun getVertexesCount(): Int {
        return vertexesCount
    }

    override fun getEdges(): List<Edge> {
        return edges
    }


}