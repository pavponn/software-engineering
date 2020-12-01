package com.github.pavponn.graphdrawer.draw

import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.paint.Color
import javafx.stage.Stage
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

class JavaFXDrawingApi(
    private val stage: Stage,
    override val drawingAreaWidth: Int,
    override val drawingAreaHeight: Int
) : DrawingApi {
    private val canvas = Canvas(drawingAreaWidth.toDouble(), drawingAreaHeight.toDouble())
    private val graphicsContext = canvas.graphicsContext2D!!

    override fun drawCircle(circle: Circle) {
        graphicsContext.fillOval(
            (circle.center.x - circle.radius).toDouble(),
            (circle.center.y - circle.radius).toDouble(),
            2 * circle.radius.toDouble(),
            2 * circle.radius.toDouble()
        )
    }

    override fun show() {
        val root = Group()
        root.children.add(canvas)
        stage.scene = Scene(root, Color.WHITE)
        stage.isResizable = false
        stage.show()
    }

    override fun drawArrow(from: Point, to: Point) {
        graphicsContext.strokeLine(from.x.toDouble(), from.y.toDouble(), to.x.toDouble(), to.y.toDouble())
        drawArrowHead(from, to)
    }

    private fun drawArrowHead(from: Point, to: Point) {
        val phi = Math.toRadians(3.0)
        val dy = to.y - from.y.toDouble()
        val dx = to.x - from.x.toDouble()
        val theta = atan2(dy, dx)
        var rho: Double = theta + phi
        val xs = Array(2) { 0.0 }
        val ys = Array(2) { 0.0 }
        for (j in 0..1) {
            xs[j] = (to.x - 25 * cos(rho))
            ys[j] = (to.y - 25 * sin(rho))
            graphicsContext.strokeLine(to.x.toDouble(), to.y.toDouble(), xs[j], ys[j])
            rho = theta - phi
        }
        graphicsContext.strokeLine(xs[0], ys[0], xs[1], ys[1])
        val xsPolygon = doubleArrayOf(to.x.toDouble(), xs[0], xs[1])
        val ysPolygon = doubleArrayOf(to.y.toDouble(), ys[0], ys[1])
        graphicsContext.fillPolygon(xsPolygon, ysPolygon, 3)
    }


}