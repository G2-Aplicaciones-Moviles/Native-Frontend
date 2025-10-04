package pe.edu.upc.jameofit

import android.app.Application
import pe.edu.upc.jameofit.shared.data.local.JwtStorage

class JameoFit : Application() {
    companion object {
        lateinit var instance: JameoFit
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        JwtStorage.init(applicationContext)
    }
}