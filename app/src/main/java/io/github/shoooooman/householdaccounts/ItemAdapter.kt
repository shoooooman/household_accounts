package io.github.shoooooman.householdaccounts

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class ItemAdapter(context: Context, list: List<Item>) : ArrayAdapter<Item>(context, 0, list) {
    private val layoutInflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?) : View {
        var view = convertView
        if (view == null) {
            view = layoutInflater.inflate(R.layout.itemrow, parent, false)
        }

        val item = getItem(position) as Item
        val textName = view?.findViewById<TextView>(R.id.text_name)
        textName?.text = item.name
        val textPrice = view?.findViewById<TextView>(R.id.text_price)
        textPrice?.text = context.getString(R.string.price_digit, item.price)

        return view!!
    }

}