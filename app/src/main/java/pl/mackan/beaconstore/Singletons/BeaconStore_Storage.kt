package pl.mackan.beaconstore.Singletons

import android.content.Context
import com.google.firebase.storage.FirebaseStorage

class BeaconStore_Storage {

    companion object {
        var INSTANCE: BeaconStore_Storage? = null

        lateinit var storage: FirebaseStorage

        fun getInstance(context: Context) {
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: init(context).also { INSTANCE }
            }
        }

        fun init(context: Context) {
            storage = FirebaseStorage.getInstance()
        }
    }
}