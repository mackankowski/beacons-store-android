package pl.mackan.beaconstore.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import pl.mackan.beaconstore.R
import com.google.firebase.firestore.FirebaseFirestore
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_products.*
import com.google.firebase.firestore.auth.User
import pl.mackan.beaconstore.Product
import pl.mackan.beaconstore.ProductAdapter


class ProductsActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var arrayOfProducts: ArrayList<Product>
    private lateinit var adapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products)

        db = FirebaseFirestore.getInstance()

        arrayOfProducts = ArrayList<Product>()
        adapter = ProductAdapter(this, arrayOfProducts)
        products_listView.adapter = adapter
    }

    override fun onResume() {
        super.onResume()

        db.collection("products")
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        for (document in task.result!!) {

//                            Log.d(FragmentActivity.TAG, document.id + " => " + document.get("name"))
                            if(document.get("state").toString() == "active") {
                                var newProduct: Product = Product(document.id, document.get("name").toString(), document.get("count") as Long)
                                adapter.add(newProduct)
                            }
                        }
                    } else {
                        //TODO: ERROR handling
                    }
                }
    }
}