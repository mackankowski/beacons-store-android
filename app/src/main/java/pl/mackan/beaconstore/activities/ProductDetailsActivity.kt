package pl.mackan.beaconstore.activities

import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_product_details.*
import pl.mackan.beaconstore.Product
import pl.mackan.beaconstore.R

class ProductDetailsActivity : AppCompatActivity() {

    private lateinit var product : Product

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)
        product = intent.getSerializableExtra("product") as Product

        if(product != null)
        {
            name.text = product.name
            price.text = product.price.toString()
            description.text = product.desc

            val bitmap = BitmapFactory.decodeStream(product!!.image.inputStream())
            image.setImageBitmap(bitmap)
        }
        else
        {
            this.finish()
        }
    }
}