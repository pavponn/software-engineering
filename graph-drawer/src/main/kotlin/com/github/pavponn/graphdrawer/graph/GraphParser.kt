package com.github.pavponn.graphdrawer.graph

import com.github.pavponn.graphdrawer.draw.DrawingApi

class GraphParser {
    companion object {
        private const val EDGE_FORMAT_MESSAGE = "Edge should match format: <from> <to>"
        private const val NUMBER_OF_VERTEXES_EDGES = "Specify number of vertexes and edges"
        private const val NUMBER_OF_VERTEXES = "Number of vertices should be entered"
        private const val SPACE_DELIMITER = " "

        fun parseListGraph(): (DrawingApi) -> Graph {
            val n =
                readLine()
                    ?.toInt()
                    ?: throw IllegalArgumentException(NUMBER_OF_VERTEXES_EDGES)
            val m =
                readLine()
                    ?.toInt()
                    ?: throw IllegalArgumentException(NUMBER_OF_VERTEXES_EDGES)
            val edges = List(m) {
                val line =
                    readLine()
                        ?.split(SPACE_DELIMITER)
                        ?.map { it.toInt() }
                        ?: throw IllegalArgumentException(EDGE_FORMAT_MESSAGE)
                require(line.size == 2) { EDGE_FORMAT_MESSAGE }
                val from = Vertex(line[0])
                val to = Vertex(line[1])
                Edge(from, to)
            }
            return { api -> ListGraph(n, edges, api) }
        }

        fun parseMatrixGraph(): (DrawingApi) -> Graph {
            val n =
                readLine()
                    ?.toInt()
                    ?: throw IllegalArgumentException(NUMBER_OF_VERTEXES)
            val result = MutableList(n) { List(0) { false } }
            for (i in 0 until n) {
                val edges =
                    readLine()
                        ?.split(SPACE_DELIMITER)
                        ?.map { it == "1" }
                        ?.toList() ?: throw IllegalArgumentException("${i + 1} line of matrix should be specified")
                result[i] = edges
            }
            return { api -> MatrixGraph(result.toList(), api) }
        }
    }
}