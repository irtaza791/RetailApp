package com.example.retailapp

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.cardview.widget.CardView

class stroreproducts : AppCompatActivity() {
    var Cardview3: CardView? = null
    var productCode: TextView? = null
    var btnStore: Button? = null
    var nameOfProduct: EditText? = null
    var quantity:EditText? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_stroreproducts)
         Cardview3= findViewById(R.id.Cardview3) // not main
          // not main
         btnStore = findViewById(R.id.store)
        nameOfProduct = findViewById<EditText?>(R.id.nameOfProduct)
        val barcode  = intent.getStringExtra("BARCODE")
        productCode = findViewById<TextView>(R.id.productCode).apply {
              text = barcode
          }  // not main
         quantity = findViewById(R.id.quantity)  // not main
        var helper = MyDbHelper(applicationContext)
        var db = helper.readableDatabase
        var rs = db.rawQuery("SELECT * FROM PRODUCTS",null)
        btnStore!!.setOnClickListener{
            var cv  = ContentValues()
            cv.put("NAME",nameOfProduct!!.text.toString())
            cv.put("QUANTITY",quantity!!.text.toString())
            cv.put("BARCODE",nameOfProduct!!.text.toString())
            db.insert("PRODUCTS",null,cv)

            nameOfProduct!!.setText("")
            quantity!!.setText("")


        }
    }
}