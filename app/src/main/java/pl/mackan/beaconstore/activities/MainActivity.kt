package pl.mackan.beaconstore.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import pl.mackan.beaconstore.R


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        if(checkIfLogged()) {
            //TODO: open profile activity with user
            val intent = Intent( this@MainActivity, ProfileActivity::class.java)
            startActivity(intent)
        }
        else {
            val intent = Intent( this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }

//TODO: check if user is logged
    fun checkIfLogged():Boolean {
        return false
    }
}