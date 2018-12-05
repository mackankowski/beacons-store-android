package pl.mackan.beaconstore.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import pl.mackan.beaconstore.R
import com.google.android.gms.auth.api.credentials.Credential
import com.google.android.gms.auth.api.credentials.CredentialRequest
import com.google.android.gms.auth.api.credentials.Credentials
import com.google.android.gms.auth.api.credentials.CredentialsClient
import android.widget.Toast
import com.estimote.mustard.rx_goodness.rx_requirements_wizard.RequirementsWizardFactory
import com.estimote.proximity_sdk.api.ProximityObserver
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import pl.mackan.beaconstore.Beacon

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var observationHandler: ProximityObserver.Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

    }

    override fun onResume() {
        super.onResume()
        checkLocationPermissions()
    }

    override fun onDestroy() {
        observationHandler.stop()
        super.onDestroy()
    }

    private fun checkLocationPermissions(){
        Log.i(Beacon.logTag, "checkPermission()");
        RequirementsWizardFactory
                .createEstimoteRequirementsWizard()
                .fulfillRequirements(this,
                        // onRequirementsFulfilled
                        {
                            Log.d(Beacon.logTag, "requirements fulfilled")
                            observationHandler = Beacon.proximityObserver!!.startObserving(Beacon.zone!!)
                            getCredentials()
                            null
                        },
                        // onRequirementsMissing
                        { requirements ->
                            Log.e(Beacon.logTag, "requirements missing: $requirements")
                            null
                        },
                        // onError
                        { throwable ->
                            Log.e(Beacon.logTag, "requirements error: $throwable")
                            null
                        }
                )
    }

    private fun getCredentials() {
        val mCredentialsClient: CredentialsClient = Credentials.getClient(this)
        var mCredentialRequest = CredentialRequest.Builder()
            .setPasswordLoginSupported(true)
            .build()
        mCredentialsClient.request(mCredentialRequest).addOnCompleteListener(
            OnCompleteListener { task ->
                if (task.isSuccessful) {
                    // See "Handle successful credential requests"
                    onCredentialRetrieved(task.result!!.credential)
                    return@OnCompleteListener
                }
                //TODO: handle multiple accounts
                // See "Handle unsuccessful and incomplete credential requests"
                // ...
                else {
                    if (auth.currentUser !== null) {
                        openProfileActivity()
                    } else {
                        openLoginActivity()
                    }
                }
            })
    }

    private fun openLoginActivity() {
        val intent = Intent( this@MainActivity, LoginActivity::class.java)
        startActivity(intent)
        this.finish()
    }

    private fun openProfileActivity() {
        val intent = Intent( this@MainActivity, ProfileActivity::class.java)
        intent.putExtra("user", auth.currentUser)
        startActivity(intent)
        this.finish()
    }

    private fun onCredentialRetrieved(credential: Credential) {
        val accountType = credential.accountType
        if (accountType == null) {
            auth.signInWithEmailAndPassword(credential.id, credential.password!!)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(applicationContext,"OK-login", Toast.LENGTH_LONG).show()

                            val intent = Intent( this@MainActivity, ProfileActivity::class.java)
                            intent.putExtra("user", auth.currentUser)
                            startActivity(intent)
                            this.finish()

                        } else {
                            openLoginActivity()
                        }
                    }
        }
    }

}