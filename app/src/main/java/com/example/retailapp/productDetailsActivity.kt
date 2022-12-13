package com.example.retailapp

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class productDetailsActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)


        val database = FirebaseDatabase.getInstance("https://retailapp-c7112-default-rtdb.europe-west1.firebasedatabase.app").reference
        val currentUser = FirebaseAuth.getInstance().currentUser?.uid
        val productName = intent.getStringExtra("name")
        val productBarcode = intent.getStringExtra("barcode")
        val productQuantity = intent.getStringExtra("quantity")
        val productImg = intent.getStringExtra("encodedimg")


        val name = findViewById<EditText>(R.id.name)
        val barcode = findViewById<TextView>(R.id.barcode)
        val quantity = findViewById<EditText>(R.id.quantity)
        val img = findViewById<ImageView>(R.id.appCompatImageView)

        name.setText(productName)
        barcode.setText(productBarcode)
        quantity.setText(productQuantity)
        val imageBytes = Base64.decode(productImg, Base64.NO_WRAP)
        val imageBitmap = BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.size)
        img.setImageBitmap(imageBitmap)

        val cancelbtn = findViewById<Button>(R.id.cancelbtn)
        val updatebtn = findViewById<Button>(R.id.updateRecord)
        val deletebtn = findViewById<Button>(R.id.deleteRecord)
        updatebtn.setOnClickListener{
            database.child(currentUser.toString()).child("All Products").child(barcode.text.toString()).setValue(products(barcode.text.toString(),name.text.toString(),quantity.text.toString(),productImg.toString()))
            val intent: Intent = Intent(this,ProductsView ::class.java)
            startActivity(intent)
            finish()

        }
       deletebtn.setOnClickListener{
         val  dataRef = FirebaseDatabase.getInstance("https://retailapp-c7112-default-rtdb.europe-west1.firebasedatabase.app").getReference("${currentUser}/All Products").child(barcode.text.toString())
           dataRef.removeValue()
           val intent: Intent = Intent(this,ProductsView ::class.java)
           startActivity(intent)
           finish()

        }
        cancelbtn.setOnClickListener{
            val intent: Intent = Intent(this,ProductsView ::class.java)
            startActivity(intent)
            finish()
        }

    }
}