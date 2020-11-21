package com.github.pavponn.graphdrawer.graph

import com.github.pavponn.graphdrawer.draw.DrawingApi

class GraphParser {

    fun parseListGraph(): (DrawingApi) -> Graph {
        val n = readLine()?.toInt() ?: throw IllegalArgumentException("Specify number of vertexes")
        val m = readLine()?.toInt() ?: throw IllegalArgumentException("Specify number of edges")
        val edges = List(m) {
            val line = readLine()
                ?.split(" ")
                ?.map { it.toInt() }
                ?: throw IllegalArgumentException("Edge should match format: <from> <to>")
            require(line.size == 2) { "Edge should match format: <from> <to>" }
            val from = Vertex(line[0])
            val to = Vertex(line[1])
            Edge(from, to)
        }
        return { api -> ListGraph(n, edges, api) }
    }

    fun parseMatrixGraph(): (DrawingApi) -> Graph {
        val n = readLine()?.toInt() ?: throw IllegalArgumentException("Number of vertices should be entered")
        val result = MutableList(n) {
            List(0) {
                false
            }
        }
        for (i in 0 until n) {
            val edges = readLine()?.split(" ")?.map {
                it == "1"
            }?.toList() ?: throw IllegalArgumentException("${i + 1} line of matrix should be specified")
            result[i] = edges
        }
        return { api -> MatrixGraph(result.toList(), api) }
    }
}