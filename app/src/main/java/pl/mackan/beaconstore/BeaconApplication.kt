package pl.mackan.beaconstore

import android.app.Application

class BeaconApplication: Application() {
    private lateinit var beacon: Unit

    override fun onCreate() {
        super.onCreate()
        beacon = Beacon.getInstance(applicationContext)
    }
}