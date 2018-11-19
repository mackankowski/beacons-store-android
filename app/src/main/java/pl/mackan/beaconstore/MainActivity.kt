package pl.mackan.beaconstore

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()
    }

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

    private fun signIn(email: String, password: String) {
        if (!validateForm()) {
            return
        }
        showProgressBar()
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        //TODO: on successful login
                        // Sign in success, update UI with the signed-in user's information
                        //val user = auth.currentUser
                        Toast.makeText(applicationContext,"OK-login", Toast.LENGTH_LONG).show()

                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(applicationContext,"FAIL-login", Toast.LENGTH_LONG).show()
                    }
                    //TODO: on failed login
//                    if (!task.isSuccessful) {
//                        status.setText(R.string.auth_failed)
//                    }
                    hideProgressBar()
                }
    }

    private fun createAccount(email: String, password: String) {
        if (!validateForm()) {
            return
        }
        showProgressBar()
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        //TODO: on successful registration
//                         Sign in success, update UI with the signed-in user's information
//                        val user = auth.currentUser
//                        updateUI(user)
                        Toast.makeText(applicationContext,"OK-registration", Toast.LENGTH_LONG).show()
                    } else {
                        //TODO: on failed registartion
                        // If sign in fails, display a message to the user.
                        Toast.makeText(baseContext, "FAIL-registartion", Toast.LENGTH_SHORT).show()
                        //updateUI(null)
                    }
                    hideProgressBar()
                }
    }

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

    fun logIn(view: View) {
        signIn(email_field.text.toString(), password_field.text.toString())
    }

    fun register(view: View) {
        createAccount(email_field.text.toString(), password_field.text.toString())
    }

    fun logInGoogle(view: View) {
        //TODO
    }
}