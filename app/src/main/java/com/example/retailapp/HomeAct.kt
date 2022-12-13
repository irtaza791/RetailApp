package com.example.retailapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase



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

    /*
    register() is a function that allows a user to register for a new account in the app.
    It takes the user's email address and password and sends them to Firebase for authentication.
    If the authentication is successful, the user is redirected to a "loading page" activity, and a message is displayed to the user indicating that the authentication was successful.
     If the authentication fails, a message is displayed to the user indicating the failure.
     */
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
                    makeText(baseContext,"Registration Successful.", LENGTH_SHORT).show()

                }else{
                    makeText(baseContext,"Authentication failed.", LENGTH_SHORT).show()

                }
            }
            .addOnFailureListener {
                makeText(this,"Error Occured. ${it.localizedMessage}", LENGTH_SHORT).show()

            }
    }



    /*
    login() is a function that allows a user to log in to their account in the app.
    It takes the user's email address and password and sends them to Firebase for authentication.
    If the authentication is successful, the user is redirected to a "loading page" activity, and a message is displayed to the user indicating that the login was successful.
    If the authentication fails, a message is displayed to the user indicating the failure.
     */
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
                makeText(baseContext,"Login Successful.", LENGTH_SHORT).show()

            }else{
                makeText(baseContext,"Authentication failed.", LENGTH_SHORT).show()
            }
        }
        .addOnFailureListener {
            makeText(this,"Error Occured. ${it.localizedMessage}", LENGTH_SHORT).show()

        }

}

}