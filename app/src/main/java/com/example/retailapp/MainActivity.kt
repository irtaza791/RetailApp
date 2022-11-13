package com.example.retailapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.CaptureActivity
import org.json.JSONException
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.util.jar.Manifest

class MainActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks,EasyPermissions.RationaleCallbacks {
    var Cardview1:CardView? = null
    var Cardview2:CardView? = null
    var Cardview3:CardView? = null
    var scanbtn:Button? = null
    var codebtn:Button? = null
    var btnEnter:Button? = null
    var edtCode:EditText? = null
    var productCode:TextView? = null
    var ptext:TextView? = null
    var hide:Animation? = null
    var reveal:Animation? = null
    var btnStore:Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Cardview1 = findViewById(R.id.Cardview1)
        Cardview2= findViewById(R.id.Cardview2)
        Cardview3= findViewById(R.id.Cardview3)
        scanbtn = findViewById(R.id.scanbtn)
        codebtn = findViewById(R.id.entercodebtn)
        btnEnter = findViewById(R.id.sbmtcode)
        edtCode = findViewById(R.id.codeText)
        ptext = findViewById(R.id.text)
        productCode = findViewById(R.id.productCode)
        btnStore = findViewById(R.id.store)

        hide = AnimationUtils.loadAnimation(this,android.R.anim.fade_out)
        reveal = AnimationUtils.loadAnimation(this,android.R.anim.fade_in)

        ptext!!.startAnimation(reveal)
        Cardview2!!.startAnimation(reveal)

        ptext!!.setText("Scan QR Code")
        Cardview2!!.visibility = View.VISIBLE

        btnEnter!!.setOnClickListener{
            if(edtCode!!.text.toString().isNullOrEmpty()){
                Toast.makeText(this,"Please enter the code",Toast.LENGTH_SHORT).show()
            }else{
                var value =  edtCode!!.text.toString()
                Toast.makeText(this,value,Toast.LENGTH_SHORT).show()
            }

        }

        scanbtn!!.setOnClickListener{
            ptext!!.startAnimation(reveal)
            Cardview1!!.startAnimation(hide)
            Cardview2!!.startAnimation(reveal)

            Cardview2!!.visibility = View.VISIBLE
            Cardview1!!.visibility = View.GONE
            ptext!!.setText("Scan QR Code")
            cameraTask()

        }
        Cardview2!!.setOnClickListener{
            cameraTask()
        }
        codebtn!!.setOnClickListener{
            ptext!!.startAnimation(reveal)
            Cardview1!!.startAnimation(reveal)
            Cardview2!!.startAnimation(hide)
            Cardview3!!.startAnimation(hide)
            Cardview3!!.visibility = View.GONE
            Cardview2!!.visibility = View.GONE
            Cardview1!!.visibility = View.VISIBLE
            ptext!!.setText("Enter QR Code")
        }
        val context = this
        btnStore!!.setOnClickListener{
            val name: String = "Hello"
            val quantity:Int = 1
            val barcode:Int = 1230
            var user = userObject(name,quantity,barcode)
            var db = handler(context)
            db.insertData(user)
        }

    }

    private  fun cameraAccess():Boolean
    {
       return EasyPermissions.hasPermissions(this,android.Manifest.permission.CAMERA)
    }
    private fun cameraTask(){
        if(cameraAccess()){
                var scanner = IntentIntegrator(this)
            scanner.setPrompt("scan a qr code")
            scanner.setCameraId(0)
            scanner.setOrientationLocked(true)
            scanner.setBeepEnabled(true)
            scanner.captureActivity = CaptureActivity::class.java
            scanner.initiateScan()
        }else{
            EasyPermissions.requestPermissions(
                this,
                "Please Allow the app to use your camera.",
                123,
                android.Manifest.permission.CAMERA
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data)
        if(result != null){
            if(result.contents == null){
                Toast.makeText(this,"Result not found",Toast.LENGTH_SHORT).show()
                edtCode!!.setText("")

            }else{
                try {
                    Cardview1!!.startAnimation(reveal)
                    Cardview2!!.startAnimation(hide)
                    Cardview3!!.visibility = View.VISIBLE
                    productCode!!.setText(result.contents.toString())
                    Cardview2!!.visibility = View.GONE
                    edtCode!!.setText(result.contents.toString())

                }catch (exception:JSONException){
                    Toast.makeText(this,exception.localizedMessage,Toast.LENGTH_SHORT).show()

                    edtCode!!.setText("")

                }
            }
        }else{

        }
        if(requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE){
            Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT).show()
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this)
    }
    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
     if(EasyPermissions.somePermissionPermanentlyDenied(this,perms)){
            AppSettingsDialog.Builder(this).build().show()
     }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {

    }

    override fun onRationaleAccepted(requestCode: Int) {

    }

    override fun onRationaleDenied(requestCode: Int) {

    }
}