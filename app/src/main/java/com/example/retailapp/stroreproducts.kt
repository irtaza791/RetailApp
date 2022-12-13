package com.example.retailapp


import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import java.io.ByteArrayOutputStream


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
    var prdImage:ImageView? = null
    var captureImgBtn:Button? = null
    var encodedImg:String? = null
    var textFieldAlert:TextView? = null

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stroreproducts)
        signoutbtn = findViewById(R.id.signoutbtn)
        manageProducts = findViewById(R.id.manageProducts)
        scanAgain = findViewById(R.id.ScanAgain)
        returnHome = findViewById(R.id.returnHome)
        btnStore = findViewById(R.id.store)
        textFieldAlert = findViewById(R.id.textFieldAlert)
        prdImage = findViewById(R.id.prdImage)
        captureImgBtn = findViewById(R.id.captureImgBtn)
        auth = Firebase.auth
        nameOfProduct = findViewById<EditText?>(R.id.nameOfProduct)
        val barcode  = intent.getStringExtra("BARCODE")
        productCode = findViewById<TextView>(R.id.productCode).apply {
              text = barcode
          }  // not main
        quantity = findViewById(R.id.quantity)


        signoutbtn!!.setOnClickListener{
            // on clicked it will signout the user
            auth.signOut()
            val intent = Intent(this,HomeAct ::class.java)
            startActivity(intent)
            finish()
        }

        scanAgain!!.setOnClickListener{
            // will redirect to the main activity
            val intent = Intent(this,MainActivity ::class.java)
                startActivity(intent)
            finish()
        }
        manageProducts!!.setOnClickListener{
            // will redirect to the inventory activity
            val intent= Intent(this,ProductsView ::class.java)
            startActivity(intent)
            finish()
        }
        returnHome!!.setOnClickListener{
            // will redirect to the loading page
            val intent= Intent(this,loadingpage ::class.java)
            startActivity(intent)
            finish()
        }


        btnStore!!.setOnClickListener{
            // on click check all the fields if the all the fields are not empty store the product in the firebase database
            if (productCode!!.text.isEmpty() || nameOfProduct!!.text.isEmpty() || quantity!!.text.isEmpty() ){
                textFieldAlert!!.visibility = View.VISIBLE
            }else{
                textFieldAlert!!.setText("Product Stored Successfully")
                database.child(currentUser.toString()).child("All Products").child(productCode!!.text.toString()).setValue(products(productCode!!.text.toString(),nameOfProduct!!.text.toString(),quantity!!.text.toString(),encodedImg))
            }
        }

        captureImgBtn!!.setOnClickListener{
            // runs the camera action where user can take the picture of the products
            captureImage()
        }

    }

    val CAMERA_REQUEST_CODE = 1
    val GALLERY_REQUEST_CODE = 2
    private fun captureImage() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA_REQUEST_CODE)
    }
    private fun selectImage() {
        // fuction to select the iimage from the gallery
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // function to take the image and set it in the image view as a bitmap and to store it into
        // database we first encode the image as base 64 string
        // and store the value into encodedImg variable which is then stored in to the firebase database
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {

            val imageBitmap = data?.extras?.get("data") as Bitmap
            prdImage!!.setImageBitmap(imageBitmap)
            encodedImg = encodeImageAsBase64(imageBitmap)

        } else if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK) {
            val imageUri = data?.data
        }
    }

    fun encodeImageAsBase64(imageBitmap: Bitmap): String {
        // First, we will create a ByteArrayOutputStream to hold the image data.
        val byteArrayOutputStream = ByteArrayOutputStream()
        // Next, we will compress the image Bitmap into the ByteArrayOutputStream using the
        // JPEG format and a quality of 100 (which is the maximum quality).
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        // Then, we will convert the image data from the ByteArrayOutputStream to a byte array.
        val imageBytes = byteArrayOutputStream.toByteArray()
        // Finally, we will encode the byte array as a Base64 string and return it.
        return Base64.encodeToString(imageBytes, Base64.NO_WRAP)
    }


}