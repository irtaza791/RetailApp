package com.example.retailapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class HomeAct : AppCompatActivity() {
    var scannerbtn: Button? = null
    var productsBtn: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        productsBtn = findViewById(R.id.productsBtn)
        scannerbtn = findViewById(R.id.homeButton)

        scannerbtn!!.setOnClickListener{
            var intent: Intent = Intent(this,MainActivity ::class.java)

            startActivity(intent)
        }
        productsBtn!!.setOnClickListener{

            var intent: Intent = Intent(this,ProductsStored ::class.java)

            startActivity(intent)
        }

    }



}