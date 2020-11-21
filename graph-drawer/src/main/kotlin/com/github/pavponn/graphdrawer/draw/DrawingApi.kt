package com.github.pavponn.graphdrawer.draw

interface DrawingApi {

    val drawingAreaWidth: Int

    val drawingAreaHeight: Int

    fun drawVertexCircle(circle: Circle)

    fun drawEdgeArrow(from: Point, to: Point)

    fun show() {}
}