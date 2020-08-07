package dev.voleum.speedruncom

import android.app.Application

class SpeedrunCom : Application() {

    companion object {
        lateinit var instance: SpeedrunCom
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}