package pl.mackan.beaconstore

import android.app.Notification
import android.content.Context
import android.util.Log
import com.estimote.proximity_sdk.api.*

class Beacon {

    companion object {
        val logTag = "beacon-store-logger"
        @Volatile
        private var INSTANCE: Beacon? = null
        val cloudCredentials = EstimoteCloudCredentials(Estimote.appId, Estimote.appToken)
        var proximityObserver: ProximityObserver? = null
        var zone :ProximityZone? = null
        var notification : Notification? = null


        fun getInstance(context: Context) {
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: init(context).also { INSTANCE }
            }
        }

        fun init(context: Context) {
            buildNotification(context)
            buildProximityObserver(context)
        }

        fun buildNotification(context: Context) {
            notification = Notification.Builder(context)
                    .setSmallIcon(R.drawable.notification_icon_background)
                    .setContentTitle("Beacon scan")
                    .setContentText("Scan is running...")
                    .setPriority(Notification.PRIORITY_HIGH)
                    .build()
        }

        fun buildProximityObserver(context: Context) {
            Log.i(logTag, "buildProximityObserver()");
            proximityObserver = ProximityObserverBuilder(context, cloudCredentials)
                    .onError { throwable ->
                        Log.e(logTag, "proximity observer error: $throwable")
                        null
                    }
                    .withBalancedPowerMode()
                    .withScannerInForegroundService(notification!!)
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

            buildNotification(context)
        }

    }
}
