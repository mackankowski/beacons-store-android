package pl.mackan.beaconstore.Singletons

import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat.startActivity
import android.widget.AdapterView
import android.widget.ListView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_products.*
import pl.mackan.beaconstore.Product
import pl.mackan.beaconstore.ProductAdapter
import pl.mackan.beaconstore.activities.ProductDetailsActivity

class BeaconStore_Products {

    companion object {
        var INSTANCE: BeaconStore_Products? = null
        lateinit var array: ArrayList<Product>
        lateinit var adapter: ProductAdapter

        fun getInstance(context: Context) {
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: init(context).also { INSTANCE }
            }
        }

        fun init(context: Context) {
            array = ArrayList<Product>()
            adapter = ProductAdapter(context, array)
        }

        fun loadFromDatabase(db: FirebaseFirestore, storage: FirebaseStorage)
        {
            db.collection("products")
                    .get()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            for (document in task.result!!) {

//                            Log.d(FragmentActivity.TAG, document.id + " => " + document.loadFromDatabase("name"))
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

        fun getProductAt(index: Int): Product
        {
            return adapter.getItem(index)
        }
    }
}