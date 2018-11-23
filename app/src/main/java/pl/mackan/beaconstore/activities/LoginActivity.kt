package pl.mackan.beaconstore.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import pl.mackan.beaconstore.R

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

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