package pl.mackan.beaconstore.Singletons

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore

class BeaconStore_Database {

    companion object {
        var INSTANCE: BeaconStore_Database? = null

        lateinit var db: FirebaseFirestore

        fun getInstance(context: Context) {
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: init(context).also { INSTANCE }
            }
        }

        fun init(context: Context) {
            db = FirebaseFirestore.getInstance()
        }
    }
}