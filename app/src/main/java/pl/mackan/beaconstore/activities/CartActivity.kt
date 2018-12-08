package pl.mackan.beaconstore.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_cart.*
import pl.mackan.beaconstore.Product
import pl.mackan.beaconstore.ProductAdapter
import pl.mackan.beaconstore.R

class CartActivity : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var CartArray: ArrayList<Product>
    private lateinit var adapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()


        CartArray = ArrayList<Product>()
        adapter = ProductAdapter(this, CartArray)
        cart_listView.adapter = adapter
    }

    override fun onResume() {
        super.onResume()

        var userId = null;



        db.collection("user_orders")
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        for (document in task.result!!) {
//                            Log.i(Beacon.logTag, auth.currentUser.toString());

                                //var newProduct: Product = Product(document.id, document.loadFromDatabase("name").toString(), document.loadFromDatabase("count") as Long)
                                //adapter.add(newProduct)
                        }
                    } else {
                        //TODO: ERROR handling
                    }
                }
    }
}