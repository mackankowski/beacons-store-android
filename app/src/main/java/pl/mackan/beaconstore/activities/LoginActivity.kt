package pl.mackan.beaconstore.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.google.android.gms.auth.api.credentials.Credential
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import pl.mackan.beaconstore.R

import android.R.attr.password
import android.content.Intent
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.tasks.Task
import android.support.annotation.NonNull
import com.google.android.gms.auth.api.credentials.Credentials
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.auth.api.credentials.CredentialsClient
import android.content.IntentSender









class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private var mIsResolving = false
    private val RC_CREDENTIALS_SAVE = 3


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
    }

    //region [GUI_CHANGES]
    fun showProgressBar() {
        progressBar.isIndeterminate = true
        progressBar.visibility = View.VISIBLE
        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    fun hideProgressBar() {
        if (progressBar.visibility == View.VISIBLE) {
            progressBar.visibility = View.GONE
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }
    }
    //endregion


    private fun validateForm(): Boolean {
        var valid = true
        val email = email_field.text.toString()
        if (TextUtils.isEmpty(email)) {
            email_field.error = "Required."
            valid = false
        } else {
            email_field.error = null
        }
        val password = password_field.text.toString()
        if (TextUtils.isEmpty(password)) {
            password_field.error = "Required."
            valid = false
        } else {
            password_field.error = null
        }
        return valid
    }

    private fun saveCredential(credential: Credential?) {
        if (credential == null) {
            return
        }

        val mCredentialsClient = Credentials.getClient(this)
        mCredentialsClient.save(credential).addOnCompleteListener(
                OnCompleteListener<Void> { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(applicationContext,"OK-saved", Toast.LENGTH_LONG).show()
                        return@OnCompleteListener
                    }
                    else {
                        Toast.makeText(applicationContext,"Failed-save", Toast.LENGTH_LONG).show()
                    }

                    val e = task.exception
                    if (e is ResolvableApiException) {
                        // Saving the credential can sometimes require showing some UI
                        // to the user, which means we need to fire this resolution.
                        val rae = e as ResolvableApiException?
                        if (rae != null) {
                            resolveResult(rae, RC_CREDENTIALS_SAVE)
                        }
                    } else {
                        Toast.makeText(this@LoginActivity,
                                "Unexpected error",
                                Toast.LENGTH_SHORT).show()
                    }
                })
    }

    private fun resolveResult(rae: ResolvableApiException, requestCode: Int) {
        if (!mIsResolving) {
            mIsResolving = try {
                rae.startResolutionForResult(this@LoginActivity, requestCode)
                true
            } catch (e: IntentSender.SendIntentException) {
                //Log.e(FragmentActivity.TAG, "Failed to send Credentials intent.", e)
                false
            }

        }
    }

    //TODO: extract login logic to another class
    private fun signIn(email: String, password: String) {
        if (!validateForm()) {
            return
        }
        showProgressBar()
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        //TODO: on successful login cache credentials and open profile activity
                        //val user = auth.currentUser
                        Toast.makeText(applicationContext,"OK-login", Toast.LENGTH_LONG).show()

                        val credential = Credential.Builder(email)
                                .setPassword(password)
                                .build()
                        saveCredential(credential)

                        val intent = Intent( this@LoginActivity, ProfileActivity::class.java)
                        startActivity(intent)

                    } else {
                        //TODO: on failed login
                        Toast.makeText(applicationContext,"FAIL-login", Toast.LENGTH_LONG).show()
                    }
                    //TODO: on failed login set message in gui
//                    if (!task.isSuccessful) {
//                        status.setText(R.string.auth_failed)
//                    }
                    hideProgressBar()
                }
    }

    //TODO: extract registerButtonClick logic to another class
    private fun createAccount(email: String, password: String) {
        if (!validateForm()) {
            return
        }
        showProgressBar()
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        //TODO: on successful registration cache credentials and open profile activity
//                        val user = auth.currentUser
                        Toast.makeText(applicationContext,"OK-registration", Toast.LENGTH_LONG).show()

                        val credential = Credential.Builder(email)
                                .setPassword(password)
                                .build()
                        saveCredential(credential)

                        val intent = Intent( this@LoginActivity, ProfileActivity::class.java)
                        startActivity(intent)

                    } else {
                        //TODO: on failed registartion
                        Toast.makeText(baseContext, "FAIL-registartion", Toast.LENGTH_SHORT).show()
                    }
                    hideProgressBar()
                }
    }

    //region [BUTTONS]
    fun loginButtonClick(view: View) {
        signIn(email_field.text.toString(), password_field.text.toString())
    }

    fun registerButtonClick(view: View) {
        createAccount(email_field.text.toString(), password_field.text.toString())
    }

//    TODO: sign up with google
//    fun logInGoogle(view: View) {
//
//    }

    //endregion
}