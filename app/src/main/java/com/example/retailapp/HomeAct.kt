package com.example.retailapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import java.util.UUID


class HomeAct : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    var signGuideText:TextView? = null
    var registerGuideText:TextView? = null
    var loginBtn:Button? = null
    var signUpBtn:Button? = null
    var email: EditText? = null
    var password: EditText? = null

    val database = FirebaseDatabase.getInstance("https://retailapp-c7112-default-rtdb.europe-west1.firebasedatabase.app").reference
    val currentUser = FirebaseAuth.getInstance().currentUser?.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        auth = Firebase.auth
        registerGuideText = findViewById(R.id.registerGuideText)
        signGuideText = findViewById(R.id.signGuideText)
        loginBtn = findViewById(R.id.LoginBtn)
        signUpBtn = findViewById(R.id.Signupbtn)
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)



        signGuideText!!.setOnClickListener{
            signUpBtn!!.visibility = View.GONE
            loginBtn!!.visibility = View.VISIBLE
            signGuideText!!.visibility = View.GONE
            registerGuideText!!.visibility = View.VISIBLE

        }
        registerGuideText!!.setOnClickListener{
            signUpBtn!!.visibility = View.VISIBLE
            loginBtn!!.visibility = View.GONE
            signGuideText!!.visibility = View.VISIBLE
            registerGuideText!!.visibility = View.GONE

        }

        signUpBtn!!.setOnClickListener{
            register()
        }
        loginBtn!!.setOnClickListener{
            login()
        }

    }
    private fun register(){
        val email = findViewById<EditText>(R.id.email)
        val password = findViewById<EditText>(R.id.password)

        loginBtn = findViewById(R.id.LoginBtn)
        signUpBtn = findViewById(R.id.Signupbtn)

        if (email.text.isEmpty() || password.text.isEmpty()){
            makeText(this,"Please fill all the fields", LENGTH_SHORT).show()
            return
        }

        val inputEmail = email.text.toString()
        val inputPassword = password.text.toString()

        auth.createUserWithEmailAndPassword(inputEmail,inputPassword)
            .addOnCompleteListener(this){ task ->
                if(task.isSuccessful){

                    val intent: Intent = Intent(this,loadingpage ::class.java)
                    startActivity(intent)
                    finish()
//                    val id = database.push().key
                    makeText(baseContext,"Authenticatication Successful.", LENGTH_SHORT).show()

                }else{
                    makeText(baseContext,"Authentication failed.", LENGTH_SHORT).show()

                }
            }
            .addOnFailureListener {
                makeText(this,"Error Occured. ${it.localizedMessage}", LENGTH_SHORT).show()

            }
    }

private fun login(){
    val email = findViewById<EditText>(R.id.email)
    val password = findViewById<EditText>(R.id.password)
    if (email.text.isEmpty() || password.text.isEmpty()){
        makeText(this,"Please fill all the fields", LENGTH_SHORT).show()
        return
    }
    val inputEmail = email.text.toString()
    val inputPassword = password.text.toString()
    auth.signInWithEmailAndPassword(inputEmail,inputPassword)
        .addOnCompleteListener(this){ task ->
            if (task.isSuccessful){
                val intent: Intent = Intent(this,loadingpage ::class.java)
                startActivity(intent)
                finish()
                makeText(baseContext,"Authenticatication Successful. ${FirebaseAuth.getInstance().currentUser?.uid}", LENGTH_SHORT).show()

            }else{
                makeText(baseContext,"Authentication failed.", LENGTH_SHORT).show()
            }
        }
        .addOnFailureListener {
            makeText(this,"Error Occured. ${it.localizedMessage}", LENGTH_SHORT).show()

        }

}

}