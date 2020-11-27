package com.github.pavponn.graphdrawer.graph

import com.github.pavponn.graphdrawer.draw.Circle
import com.github.pavponn.graphdrawer.draw.DrawingApi
import com.github.pavponn.graphdrawer.draw.Point
import java.lang.Integer.min
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

abstract class Graph(private val drawingApi: DrawingApi) {

    fun drawGraph() {
        drawCircles()
        drawEdges()
    }

    private fun drawCircles() {
        for (i in 0 until getVertexesCount()) {
            drawCircleByIndex(i)
        }
    }

    private fun drawCircleByIndex(index: Int) {
        val vertexesCount = getVertexesCount()
        val radius = sqrt(min(drawingApi.drawingAreaHeight, drawingApi.drawingAreaWidth).toDouble()).toInt() / 2
        val vertexCenter = getVertexCenterByIndex(index)
        val vertexCircle = Circle(vertexCenter, radius)
        drawingApi.drawVertexCircle(vertexCircle)

    }

    private fun drawEdges() {
        val edges = getEdges()
        for (edge in edges) {
            if (edge.from != edge.to) {
                drawEdgeByIndexes(edge.from.index, edge.to.index)
            }
        }
    }

    private fun drawEdgeByIndexes(fromIndex: Int, toIndex: Int) {
        val fromPoint = getVertexCenterByIndex(fromIndex)
        val toPoint = getVertexCenterByIndex(toIndex)
        drawingApi.drawEdgeArrow(fromPoint, toPoint)
    }

    private fun getCenterCircle(): Circle {
        val centerPoint = Point(drawingApi.drawingAreaWidth / 2, drawingApi.drawingAreaHeight / 2)
        val centerRadius = min(drawingApi.drawingAreaHeight, drawingApi.drawingAreaWidth) / 7
        return Circle(centerPoint, centerRadius)
    }

    private fun getVertexCenterByIndex(index: Int): Point {
        val vertexesCount = getVertexesCount()
        val centerCircle = getCenterCircle()
        return Point(
            (centerCircle.center.x + centerCircle.radius * cos(2 * Math.PI * index / vertexesCount)).toInt(),
            (centerCircle.center.y + centerCircle.radius * sin(2 * Math.PI * index / vertexesCount)).toInt(),
        )
    }

    abstract fun getVertexesCount(): Int

    abstract fun getEdges(): List<Edge>

}