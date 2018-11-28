package pl.mackan.beaconstore.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import pl.mackan.beaconstore.R
import android.content.Intent
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_profile.*


class ProfileActivity : AppCompatActivity() {

    private lateinit var user : FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        user = intent.getParcelableExtra("user")

        if (user != null) {
            email_textView.text = user.email
        }
    }

    //region [BUTTONS]
    fun offerButtonClick(view: View) {
        val intent = Intent( this@ProfileActivity, ProductsActivity::class.java)
        startActivity(intent)
    }

    fun cartButtonClick(view: View) {
        val intent = Intent( this@ProfileActivity, CartActivity::class.java)
        startActivity(intent)
    }

    //endregion
}