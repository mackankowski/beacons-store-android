package pl.mackan.beaconstore

import android.app.Activity
import android.content.Context
import android.util.Log
import com.estimote.proximity_sdk.api.EstimoteCloudCredentials
import com.estimote.proximity_sdk.api.ProximityObserverBuilder
import com.estimote.proximity_sdk.api.ProximityObserver
import com.estimote.proximity_sdk.api.ProximityZoneBuilder
import com.estimote.proximity_sdk.api.ProximityZone
import com.estimote.mustard.rx_goodness.rx_requirements_wizard.RequirementsWizardFactory

class Beacon {

    companion object {
        @Volatile
        private var INSTANCE: Beacon? = null
        val cloudCredentials = EstimoteCloudCredentials(Estimote.appId, Estimote.appToken)
        var proximityObserver: ProximityObserver? = null
        val logTag = "beacon-store"

        fun getInstance(context: Context, activity: Activity) {
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildProximityObserver(context, activity).also { INSTANCE }
            }
        }

        fun buildProximityObserver(context: Context, activity: Activity) {
            Log.i(logTag, "buildProximityObserver()");
            proximityObserver = ProximityObserverBuilder(context, cloudCredentials)
                    .onError { throwable ->
                        Log.e(logTag, "proximity observer error: $throwable")
                        null
                    }
                    .withBalancedPowerMode()
                    .build()

            val zone = ProximityZoneBuilder()
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
            checkPermission(activity, zone)
        }

        fun checkPermission(activity: Activity, zone: ProximityZone) {
            Log.i(logTag, "checkPermission()");
            RequirementsWizardFactory
                    .createEstimoteRequirementsWizard()
                    .fulfillRequirements(activity,
                            // onRequirementsFulfilled
                            {
                                Log.d(logTag, "requirements fulfilled")
                                proximityObserver?.startObserving(zone)
                                null
                            },
                            // onRequirementsMissing
                            { requirements ->
                                Log.e(logTag, "requirements missing: $requirements")
                                null
                            }
                            // onError
                    ) { throwable ->
                        Log.e(logTag, "requirements error: $throwable")
                        null
                    }
        }

    }
}
