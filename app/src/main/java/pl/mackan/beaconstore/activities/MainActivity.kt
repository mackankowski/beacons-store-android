package pl.mackan.beaconstore.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import pl.mackan.beaconstore.R
import com.google.android.gms.auth.api.credentials.Credential
import com.google.android.gms.auth.api.credentials.CredentialRequest
import com.google.android.gms.auth.api.credentials.CredentialRequestResponse
import com.google.android.gms.auth.api.credentials.Credentials
import com.google.android.gms.auth.api.credentials.CredentialsClient
import com.google.android.gms.auth.api.credentials.IdentityProviders
import com.google.android.gms.tasks.Task
import android.support.annotation.NonNull
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        getCredentials()
//        if(checkIfLogged()) {
//            //TODO: open profile activity with user
//            val intent = Intent( this@MainActivity, ProfileActivity::class.java)
//            startActivity(intent)
//        }
//        else {
//            val intent = Intent( this@MainActivity, LoginActivity::class.java)
//            startActivity(intent)
//        }
    }

//TODO: check if user is logged
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
                    val intent = Intent( this@MainActivity, LoginActivity::class.java)
                    startActivity(intent)
                }
            })
    }

    private fun onCredentialRetrieved(credential: Credential) {
        val accountType = credential.accountType
        if (accountType == null) {
            // Sign the user in with information from the Credential.
//            signInWithPassword(credential.id, credential.password)
            //TODO: open profile activity with user
            val intent = Intent( this@MainActivity, ProfileActivity::class.java)
            startActivity(intent)
        }
    }

}