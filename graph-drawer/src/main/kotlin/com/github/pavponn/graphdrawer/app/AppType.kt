package com.github.pavponn.graphdrawer.app

enum class AppType {
    AWT {
        override fun getApp(): GraphApp = AwtApp()
    },
    JavaFX {
        override fun getApp(): GraphApp = JavaFXApp()
    };

    abstract fun getApp(): GraphApp
}