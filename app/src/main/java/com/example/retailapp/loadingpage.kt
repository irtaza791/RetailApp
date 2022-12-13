package com.example.retailapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth

import com.google.firebase.auth.ktx.auth

import com.google.firebase.ktx.Firebase



class loadingpage : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    var scannerbtn: Button? = null
    var productsBtn: Button? = null
    var signoutbtn:Button? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loadingpage)
        auth = Firebase.auth

        /*
        The buttons include a "scanner" button, a "products" button, and a "sign out" button.
        When the "scanner" button is clicked, the user is redirected to the app's main activity.
        When the "products" button is clicked, the user is redirected to a "products view" activity.
        When the "sign out" button is clicked, the user is signed out of their account and redirected to the app's home activity.
         */

        signoutbtn = findViewById(R.id.signoutbtn)
        productsBtn = findViewById(R.id.productsBtn)
        scannerbtn = findViewById(R.id.homeButton)


        signoutbtn!!.setOnClickListener{
            auth.signOut()
            val intent = Intent(this,HomeAct ::class.java)

            startActivity(intent)
            finish()
        }
        scannerbtn!!.setOnClickListener{
            val intent = Intent(this,MainActivity ::class.java)
            startActivity(intent)
            finish()

        }
        productsBtn!!.setOnClickListener{

            val intent= Intent(this,ProductsView ::class.java)
            startActivity(intent)
            finish()

        }



    }


}