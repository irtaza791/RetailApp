package com.example.retailapp

import android.content.ContentValues
import android.content.Intent
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

    var ScanAgain:Button? = null
    var manageProducts:Button? = null
    var returnHome:Button? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_stroreproducts)
        manageProducts = findViewById(R.id.manageProducts)
        ScanAgain = findViewById(R.id.ScanAgain)
        returnHome = findViewById(R.id.returnHome)

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
        var rs = db.rawQuery("SELECT * FROM ITEMS",null)



        ScanAgain!!.setOnClickListener{
            var intent: Intent = Intent(this,MainActivity ::class.java)
                startActivity(intent)
        }
        manageProducts!!.setOnClickListener{
            var intent: Intent = Intent(this,ProductsStored ::class.java)

            startActivity(intent)
        }
        returnHome!!.setOnClickListener{
            var intent: Intent = Intent(this,HomeAct ::class.java)
            startActivity(intent)
        }
        btnStore!!.setOnClickListener{
            var cv  = ContentValues()
            cv.put("NAME",nameOfProduct!!.text.toString())
            cv.put("QUANTITY",quantity!!.text.toString())
            cv.put("BARCODE",productCode!!.text.toString())
            db.insert("ITEMS",null,cv)

            nameOfProduct!!.setText("")
            quantity!!.setText("")


        }

    }
}