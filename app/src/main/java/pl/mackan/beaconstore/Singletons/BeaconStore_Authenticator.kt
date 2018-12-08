package pl.mackan.beaconstore.Singletons

import android.content.Context
import com.google.firebase.auth.FirebaseAuth

class BeaconStore_Authenticator {

    companion object {
        var INSTANCE: BeaconStore_Authenticator? = null

        lateinit var auth: FirebaseAuth

        fun getInstance(context: Context) {
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: init(context).also { INSTANCE }
            }
        }

        fun init(context: Context) {
            auth = FirebaseAuth.getInstance()
        }
    }
}