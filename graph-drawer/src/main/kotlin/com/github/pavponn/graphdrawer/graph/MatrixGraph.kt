package com.github.pavponn.graphdrawer.graph

import com.github.pavponn.graphdrawer.draw.DrawingApi

class MatrixGraph(private val matrix: List<List<Boolean>>, drawingApi: DrawingApi) : Graph(drawingApi) {

    override fun getVertexesCount(): Int {
        return matrix.size
    }

    override fun getEdges(): List<Edge> {
        val edges: MutableList<Edge> = mutableListOf()
        for (i in matrix.indices) {
            for (j in matrix.indices) {
                if (matrix[i][j]) {
                    edges.add(Edge(Vertex(i), Vertex(j)))
                }
            }
        }

        return edges
    }


}