package pl.mackan.beaconstore

import android.R.attr.name
import android.content.Context
import android.widget.TextView
import android.view.LayoutInflater
import android.view.View
import com.google.firebase.firestore.auth.User
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.widget.ImageView
import java.io.InputStream
import java.io.Serializable
import java.net.URL


class Product : Serializable {
    var key: String
    var name: String
    var count: Long
    var price : Number
    var desc: String
    var image : ByteArray


    constructor(key: String, name: String, count: Long, price: Number, desc: String, image: ByteArray) {
        this.key = key
        this.name = name
        this.count = count
        this.price = price
        this.desc = desc
        this.image = image
    }
}

class ProductAdapter(context: Context, products: ArrayList<Product>) : ArrayAdapter<Product>(context, 0, products) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        // Get the data item for this position
        val product = getItem(position)
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.product, parent, false)
        }
        // Lookup view for data population
        val name = convertView!!.findViewById(R.id.name) as TextView
        val image = convertView!!.findViewById(R.id.image) as ImageView
        // Populate the data into the template view using the data object
        name.text = product!!.name
        val bitmap = BitmapFactory.decodeStream(product!!.image.inputStream())
        image.setImageBitmap(bitmap)

        // Return the completed view to render on screen
        return convertView
    }
}