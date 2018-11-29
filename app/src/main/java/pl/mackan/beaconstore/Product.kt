package pl.mackan.beaconstore

import android.R.attr.name
import android.content.Context
import android.widget.TextView
import android.view.LayoutInflater
import android.view.View
import com.google.firebase.firestore.auth.User
import android.view.ViewGroup
import android.widget.ArrayAdapter



class Product {
    var key: String
    var name: String
    var count: Long

    constructor(key: String, name: String, count: Long) {
        this.key = key
        this.name = name
        this.count = count
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
        val count = convertView!!.findViewById(R.id.count) as TextView
        // Populate the data into the template view using the data object
        name.text = product!!.name
        count.text = (product!!.count).toString()
        // Return the completed view to render on screen
        return convertView
    }
}