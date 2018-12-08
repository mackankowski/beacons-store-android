package pl.mackan.beaconstore.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import pl.mackan.beaconstore.R
import kotlinx.android.synthetic.main.activity_products.*
import pl.mackan.beaconstore.Product
import android.widget.AdapterView
import pl.mackan.beaconstore.Singletons.BeaconStore_Products

class ProductsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products)

        setProductsListView()
    }

    fun setProductsListView()
    {
        products_listView.adapter = BeaconStore_Products.adapter
        products_listView.onItemClickListener = AdapterView.OnItemClickListener { arg0, arg1, position, arg3 ->
//            val product = products_listView.getItemAtPosition(position) as Product
            val intent = Intent( this@ProductsActivity, ProductDetailsActivity::class.java)
            intent.putExtra("productIndex", position)
            startActivity(intent)
        }
    }
}