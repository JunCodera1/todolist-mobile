package com.example.todolist

import android.content.DialogInterface
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    var itemList = ArrayList<String>()
    var fileHelper = FileHelper()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        itemList = fileHelper.readData(this)
        var arrayAdapter = ArrayAdapter(this,
            android.R.layout.simple_list_item_1,
            android.R.id.text1,
            itemList)
        val lvCongViec = findViewById<ListView>(R.id.lvCongViec)
        val edtInput = findViewById<EditText>(R.id.edtInput)
        val btnAdd = findViewById<Button>(R.id.btnAdd)
        lvCongViec.adapter = arrayAdapter
        btnAdd.setOnClickListener {
            var itemName = edtInput.text.toString()
            itemList.add(itemName)
            edtInput.setText("")
            fileHelper.writeData(itemList, applicationContext)
            arrayAdapter.notifyDataSetChanged()
        }

        // xóa item công viê sau khi hoàn thành
        lvCongViec.setOnItemClickListener { parent, view, position, id ->
            val alert = AlertDialog.Builder(this)
            alert.setTitle("Delete")
            alert.setMessage("Bạn đã hoàn thành công việc ? nhấn yes để xóa !")
            alert.setCancelable(true)
            alert.setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, i ->
                itemList.removeAt(position)
                arrayAdapter.notifyDataSetChanged()
                // ghi danh sách mới vào tệp trên máy
                fileHelper.writeData(itemList, applicationContext)
            })
            alert.setNegativeButton("No", DialogInterface.OnClickListener { dialog, i ->
                dialog.cancel()
            })
            alert.create()
            alert.show()
        }

    }
}