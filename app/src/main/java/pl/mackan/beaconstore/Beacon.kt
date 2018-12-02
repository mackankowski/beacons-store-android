package pl.mackan.beaconstore

import android.content.Context
import android.util.Log
import com.estimote.proximity_sdk.api.*

class Beacon {

    companion object {
        @Volatile
        private var INSTANCE: Beacon? = null
        val cloudCredentials = EstimoteCloudCredentials(Estimote.appId, Estimote.appToken)
        var proximityObserver: ProximityObserver? = null
        var zone :ProximityZone? = null
        val logTag = "beacon-store-logger"

        fun getInstance(context: Context) {
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildProximityObserver(context).also { INSTANCE }
            }
        }

        fun buildProximityObserver(context: Context) {
            Log.i(logTag, "buildProximityObserver()");
            proximityObserver = ProximityObserverBuilder(context, cloudCredentials)
                    .onError { throwable ->
                        Log.e(logTag, "proximity observer error: $throwable")
                        null
                    }
                    .withBalancedPowerMode()
                    .build()

            zone = ProximityZoneBuilder()
                    .forTag("desks")
                    .inNearRange()
                    .onEnter { context ->
                        val deskOwner = context.attachments["welcome"] // tag values: welcome, product, payment
                        Log.d(logTag, "Welcome to store!")
                        null
                    }
                    .onExit {
                        Log.d(logTag, "Went out of range!")
                        null
                    }
                    .build()
        }
    }
}
