package com.example.retailapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.widget.Button
import android.widget.Toast

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.*
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase



class loadingpage : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    var scannerbtn: Button? = null
    var productsBtn: Button? = null
    var signoutbtn:Button? = null
    val database = FirebaseDatabase.getInstance("https://retailapp-c7112-default-rtdb.europe-west1.firebasedatabase.app").reference
    val currentUser = getInstance().currentUser?.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loadingpage)
        auth = Firebase.auth



        signoutbtn = findViewById(R.id.signoutbtn)
        productsBtn = findViewById(R.id.productsBtn)
        scannerbtn = findViewById(R.id.homeButton)


        signoutbtn!!.setOnClickListener{
            auth.signOut()
            val intent: Intent = Intent(this,HomeAct ::class.java)

            startActivity(intent)
            finish()
        }
        scannerbtn!!.setOnClickListener{
            val intent: Intent = Intent(this,MainActivity ::class.java)
            startActivity(intent)
            finish()

        }
        productsBtn!!.setOnClickListener{

            val intent: Intent = Intent(this,ProductsView ::class.java)
            startActivity(intent)
            finish()

        }



    }


}