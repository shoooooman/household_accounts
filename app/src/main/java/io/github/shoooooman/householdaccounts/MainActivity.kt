package io.github.shoooooman.householdaccounts

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.text.InputType.TYPE_CLASS_NUMBER
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*

class MainActivity : AppCompatActivity() {

    private val tag = "MainActivity"
    private val list: MutableList<Item> = mutableListOf()
    private var itemAdapter: ItemAdapter? = null
    private var dialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val listView = findViewById<ListView>(R.id.item_list)
        itemAdapter = ItemAdapter(this@MainActivity, list)
        listView.adapter = itemAdapter
        listView.onItemClickListener =
                AdapterView.OnItemClickListener { parent, view, pos, id ->
                    Log.d(tag, id.toString())
                    AlertDialog.Builder(this)
                        .setMessage("削除してもいいですか？")
                        .setPositiveButton("OK") { _, _ ->
                            list.removeAt(pos)
                            itemAdapter!!.notifyDataSetChanged()
                        }.setNegativeButton("キャンセル", null)
                        .show();
                }

        val inflater: LayoutInflater = LayoutInflater.from(this)
        val layout: View = inflater.inflate (R.layout.dialog, null)

        val addButton = findViewById<Button>(R.id.add_button)
        addButton.setOnClickListener {
            val editName: EditText = layout.findViewById(R.id.edit_name)
            val editPrice: EditText = layout.findViewById(R.id.edit_price)
            editName!!.text = null
            editPrice!!.text = null
            editName!!.requestFocus()
            if (dialog == null) {
                editPrice!!.inputType = TYPE_CLASS_NUMBER
                dialog = AlertDialog.Builder(this)
                    .setView(layout)
                    .setTitle("アイテム追加")
                    .setPositiveButton("決定") { _, _ ->
                        if (editName!!.text != null && editPrice!!.text != null) {
                            // TODO: fix id
                            val newItem = Item(list.size.toLong(),
                                editName!!.text.toString(), editPrice!!.text.toString().toInt())
                            itemAdapter!!.add(newItem)
                            itemAdapter!!.notifyDataSetChanged()
                        }
                    }.create()
            }
            dialog!!.show()
        }
    }
}
