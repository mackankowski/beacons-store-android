package pl.mackan.beaconstore

import android.app.Application
import pl.mackan.beaconstore.Singletons.BeaconStore_Authenticator
import pl.mackan.beaconstore.Singletons.BeaconStore_Database
import pl.mackan.beaconstore.Singletons.BeaconStore_Products
import pl.mackan.beaconstore.Singletons.BeaconStore_Storage

class BeaconApplication: Application() {
    private lateinit var beacon: Unit
    private lateinit var authenticator: Unit
    private lateinit var database: Unit
    private lateinit var storage: Unit
    private lateinit var productsList: Unit

    override fun onCreate() {
        super.onCreate()
        beacon = Beacon.getInstance(applicationContext)
        authenticator = BeaconStore_Authenticator.getInstance(applicationContext)
        database = BeaconStore_Database.getInstance(applicationContext)
        storage = BeaconStore_Storage.getInstance(applicationContext)
        productsList = BeaconStore_Products.getInstance(applicationContext)
        BeaconStore_Products.loadFromDatabase(BeaconStore_Database.db, BeaconStore_Storage.storage)
    }

}