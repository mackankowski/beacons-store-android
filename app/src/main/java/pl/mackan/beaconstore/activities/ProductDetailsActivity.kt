package pl.mackan.beaconstore.activities

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_product_details.*
import pl.mackan.beaconstore.Product
import pl.mackan.beaconstore.R
import pl.mackan.beaconstore.Singletons.BeaconStore_Products

class ProductDetailsActivity : AppCompatActivity() {

    private lateinit var product : Product

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_product_details)
        product = BeaconStore_Products.getProductAt(intent.getIntExtra("productIndex", 0))

        setProductInfo()
    }

    fun setProductInfo()
    {
        if(product != null)
        {
            name.text = product.name
            price.text = product.price.toString()
            description.text = product.desc

            val bitmap = BitmapFactory.decodeStream(product!!.image.inputStream())
            image.setImageBitmap(bitmap)
        }
    }

    fun continueButtonClick(view: View) {
        this.finish()
    }
}