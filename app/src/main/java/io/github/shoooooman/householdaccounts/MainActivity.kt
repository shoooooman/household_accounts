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
    private var addDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val inflater: LayoutInflater = LayoutInflater.from(this)
        val dialogLayout: View = inflater.inflate (R.layout.dialog, null)

        val listView: ListView = findViewById(R.id.item_list)
        itemAdapter = ItemAdapter(this@MainActivity, list)
        itemAdapter!!.add(Item(0, "合計", list.sumBy { it.price }))
        itemAdapter!!.notifyDataSetChanged()
        listView.adapter = itemAdapter
        listView.onItemClickListener =
                AdapterView.OnItemClickListener { parent, view, pos, id ->
                    AlertDialog.Builder(this)
                        .setMessage("Menu")
                        .setPositiveButton("編集") { _, _ ->
                            val editDialogLayout: View = inflater.inflate (R.layout.dialog, null)
                            val editName: EditText = editDialogLayout.findViewById(R.id.edit_name)
                            val editPrice: EditText = editDialogLayout.findViewById(R.id.edit_price)
                            editName!!.setText(list[pos].name)
                            editPrice!!.setText(list[pos].price.toString())
                            editPrice!!.inputType = TYPE_CLASS_NUMBER
                            editName!!.requestFocus()
                            val editDialog = AlertDialog.Builder(this)
                                .setView(editDialogLayout)
                                .setTitle("アイテム編集")
                                .setPositiveButton("決定") { _, _ ->
                                    if (editName!!.text.isNotEmpty() && editPrice!!.text.isNotEmpty()) {
                                        val tmp = list[pos].price
                                        list[pos].name = editName!!.text.toString()
                                        list[pos].price = editPrice!!.text.toString().toInt()
                                        list[0].price += list[pos].price - tmp
                                        itemAdapter!!.notifyDataSetChanged()
                                    }
                                }.create()
                            editDialog!!.show()
                        }
                        .setNegativeButton("削除") { _, _ ->
                            list[0].price -= list[pos].price
                            list.removeAt(pos)
                            itemAdapter!!.notifyDataSetChanged()
                        }
                        .setNeutralButton("キャンセル", null)
                        .show();
                }


        val addButton = findViewById<Button>(R.id.add_button)
        addButton.setOnClickListener {
            val editName: EditText = dialogLayout.findViewById(R.id.edit_name)
            val editPrice: EditText = dialogLayout.findViewById(R.id.edit_price)
            editName!!.text = null
            editPrice!!.text = null
            editName!!.requestFocus()
            if (addDialog == null) {
                editPrice!!.inputType = TYPE_CLASS_NUMBER
                addDialog = AlertDialog.Builder(this)
                    .setView(dialogLayout)
                    .setTitle("アイテム追加")
                    .setPositiveButton("決定") { _, _ ->
                        if (editName!!.text.isNotEmpty() && editPrice!!.text.isNotEmpty()) {
                            val newItem = Item(list.size.toLong(),
                                editName!!.text.toString(), editPrice!!.text.toString().toInt())
                            itemAdapter!!.add(newItem)
                            list[0].price += newItem.price
                            itemAdapter!!.notifyDataSetChanged()
                        }
                    }.create()
            }
            addDialog!!.show()
        }
    }
}
