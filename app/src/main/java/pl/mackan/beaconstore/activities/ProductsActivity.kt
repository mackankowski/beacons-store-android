package pl.mackan.beaconstore.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import pl.mackan.beaconstore.R
import com.google.firebase.firestore.FirebaseFirestore
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_products.*
import com.google.firebase.firestore.auth.User
import pl.mackan.beaconstore.Product
import pl.mackan.beaconstore.ProductAdapter
import com.google.firebase.storage.FirebaseStorage
import android.widget.AdapterView






class ProductsActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var arrayOfProducts: ArrayList<Product>
    private lateinit var adapter: ProductAdapter
    private lateinit var storage: FirebaseStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products)

        db = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        arrayOfProducts = ArrayList<Product>()
        adapter = ProductAdapter(this, arrayOfProducts)
        products_listView.adapter = adapter

        products_listView.onItemClickListener = AdapterView.OnItemClickListener { arg0, arg1, position, arg3 ->
            val product = products_listView.getItemAtPosition(position) as Product
            val intent = Intent( this@ProductsActivity, ProductDetailsActivity::class.java)
            intent.putExtra("product", product)
            startActivity(intent)
        }
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
                                val storageRef = storage.reference
                                val pathReference = storageRef.child(document.get("imagePath").toString())

                                val ONE_MEGABYTE: Long = 1024 * 1024
                                pathReference.getBytes(ONE_MEGABYTE).addOnSuccessListener {
                                    // Data for "images/island.jpg" is returned, use this as needed
                                    var newProduct: Product = Product(document.id, document.get("name").toString(),
                                            document.get("count") as Long, document.get("price") as Number, document.get("desc").toString(), it)
                                    adapter.add(newProduct)
                                }.addOnFailureListener {
                                    // Handle any errors
                                }
                            }
                        }
                    } else {
                        //TODO: ERROR handling
                    }
                }
    }
}