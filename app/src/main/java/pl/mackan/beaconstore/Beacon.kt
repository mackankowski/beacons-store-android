package pl.mackan.beaconstore

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.estimote.proximity_sdk.api.*
import android.os.Build

class Beacon {

    companion object {
        val logTag = "beacon-store-logger"
        var INSTANCE: Beacon? = null
        val CHANNEL_ID = "beacon-id"
        val CHANNEL_NAME="beacon-channel-name"
        var importance = NotificationManager.IMPORTANCE_HIGH
        var notification : Notification? = null
        val cloudCredentials = EstimoteCloudCredentials(Estimote.appId, Estimote.appToken)
        var proximityObserver: ProximityObserver? = null
        var zone :ProximityZone? = null

        fun getInstance(context: Context) {
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: init(context).also { INSTANCE }
            }
        }

        fun init(context: Context) {
            // If async here, use Promises
            createNotificationChannel(context)
            buildNotification(context)
            buildProximityObserver(context)
        }

        private fun createNotificationChannel(context: Context) {
            // Create the NotificationChannel, but only on API 26+ because
            val notificationChannel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(notificationChannel)
            }
        }

        fun buildNotification(context: Context) {
            notification = NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.notification_icon_background)
                    .setContentTitle("Beacon scan")
                    .setContentText("Scan is running...")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .build()
            // manual triggering but we want to use it in .withScannerInForegroundService(notification!!)
            // val notificationManager = NotificationManagerCompat.from(context)
            // notificationManager.notify(0, notification!!);
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
