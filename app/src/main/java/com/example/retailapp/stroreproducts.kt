package com.example.retailapp


import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase


class stroreproducts : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    val database = FirebaseDatabase.getInstance("https://retailapp-c7112-default-rtdb.europe-west1.firebasedatabase.app").reference
    val currentUser = FirebaseAuth.getInstance().currentUser?.uid
    var productCode: TextView? = null
    var btnStore: Button? = null
    var nameOfProduct: EditText? = null
    var quantity:EditText? = null
    var scanAgain:Button? = null
    var manageProducts:Button? = null
    var returnHome:Button? = null
    var signoutbtn:Button? = null

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stroreproducts)
        signoutbtn = findViewById(R.id.signoutbtn)
        manageProducts = findViewById(R.id.manageProducts)
        scanAgain = findViewById(R.id.ScanAgain)
        returnHome = findViewById(R.id.returnHome)
        btnStore = findViewById(R.id.store)
        auth = Firebase.auth
        nameOfProduct = findViewById<EditText?>(R.id.nameOfProduct)
        val barcode  = intent.getStringExtra("BARCODE")
        productCode = findViewById<TextView>(R.id.productCode).apply {
              text = barcode
          }  // not main
        quantity = findViewById(R.id.quantity)  // not main


        signoutbtn!!.setOnClickListener{
            auth.signOut()
            val intent: Intent = Intent(this,HomeAct ::class.java)
            startActivity(intent)
            finish()
        }

        scanAgain!!.setOnClickListener{
            val intent: Intent = Intent(this,MainActivity ::class.java)
                startActivity(intent)
            finish()
        }
        manageProducts!!.setOnClickListener{
            val intent: Intent = Intent(this,ProductsView ::class.java)
            startActivity(intent)
            finish()
        }
        returnHome!!.setOnClickListener{
            val intent: Intent = Intent(this,loadingpage ::class.java)
            startActivity(intent)
            finish()
        }

        btnStore!!.setOnClickListener{
            database.child(currentUser.toString()).child("All Products").child(productCode!!.text.toString()).setValue(products(productCode!!.text.toString(),nameOfProduct!!.text.toString(),quantity!!.text.toString()))
        }

    }
}