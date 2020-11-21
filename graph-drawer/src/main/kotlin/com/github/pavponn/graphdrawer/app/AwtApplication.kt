package com.github.pavponn.graphdrawer.app

import com.github.pavponn.graphdrawer.draw.AwtDrawingApi
import java.awt.Frame
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import kotlin.system.exitProcess

class AwtApplication() :
    Frame(),
    DrawingApplication {
    override fun startApplication() {
        addWindowListener(
            object : WindowAdapter() {
                override fun windowClosing(e: WindowEvent?) {
                    exitProcess(0)
                }
            }
        )
        setSize(ApplicationParameters.windowParams.width, ApplicationParameters.windowParams.height)
        isVisible = true
    }

    override fun paint(graphics: Graphics) {
        super.paint(graphics)
        val graphics2D = graphics as Graphics2D
        graphics2D.clearRect(0, 0, ApplicationParameters.windowParams.width, ApplicationParameters.windowParams.height)
        val drawingApi = AwtDrawingApi(
            graphics2D,
            ApplicationParameters.windowParams.width,
            ApplicationParameters.windowParams.height
        )
        ApplicationParameters.drawer(drawingApi).drawGraph()
        isResizable = false
    }
}