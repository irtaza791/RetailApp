package com.example.retailapp

import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.SimpleCursorAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.database.DatabaseReference

class ProductsStored : AppCompatActivity() {

    var nextbtn:Button? = null
    var prevbtn:Button? = null
    var updatebtn:Button? = null
    var deletebtn:Button? = null
    var ScanProductAgain:Button? = null
    var MoveToFirst:Button?  = null
    var ManageProducts:Button? = null

    var barcodeView:EditText? = null
    var nameView:EditText? = null
    var quantityView:EditText? = null
    var ViewAll:Button? = null
    var listHeadings:LinearLayout? = null
    var menuLayout:LinearLayout? = null
      var searchView:SearchView? = null
    var listView:ListView? =null
    lateinit var db : SQLiteDatabase
    lateinit var rs : Cursor
    lateinit var adapter: SimpleCursorAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)





        setContentView(R.layout.activity_products_stored)

        listHeadings = findViewById(R.id.listHeadings)
        barcodeView = findViewById(R.id.BarcodeView)
        nameView = findViewById(R.id.NameView)
        quantityView = findViewById(R.id.QuantityView)

        ManageProducts = findViewById(R.id.ManageProducts)
        MoveToFirst = findViewById(R.id.MoveToFirst)
        nextbtn = findViewById(R.id.Next)
        prevbtn = findViewById(R.id.Previous)
        updatebtn = findViewById(R.id.Update)
        deletebtn = findViewById(R.id.Delete)
        ScanProductAgain = findViewById(R.id.ScanProductAgain)
        ViewAll = findViewById(R.id.ViewAll)
        menuLayout = findViewById(R.id.menuLayout)
        listView = findViewById(R.id._dynamic)
        searchView = findViewById(R.id.searchView)

        var helper = MyDbHelper(applicationContext)
         db = helper.readableDatabase
         rs = db.rawQuery("SELECT * FROM ITEMS",null)


         adapter = SimpleCursorAdapter(applicationContext,R.layout.listview,rs,
            arrayOf("NAME","BARCODE","QUANTITY"),
            intArrayOf(R.id.text1,R.id.text2,R.id.text3), 0
        )
        listView!!.adapter = adapter

        registerForContextMenu(listView)




        MoveToFirst!!.setOnClickListener{
            if(rs.moveToFirst()){
                barcodeView!!.setText(rs.getString(2))
                nameView!!.setText(rs.getString(1))
                quantityView!!.setText(rs.getString(3))
            }else{
                Toast.makeText(applicationContext,"No Record Found",Toast.LENGTH_LONG).show()

            }
        }



        nextbtn!!.setOnClickListener{

            if(rs.moveToNext()){
                nameView!!.setText(rs.getString(1))
                barcodeView!!.setText(rs.getString(2))
                quantityView!!.setText(rs.getString(3))
            }else if(rs.moveToFirst()){
                nameView!!.setText(rs.getString(1))
                barcodeView!!.setText(rs.getString(2))
                quantityView!!.setText(rs.getString(3))
            }else{
                Toast.makeText(applicationContext,"No Record FOund",Toast.LENGTH_LONG).show()
            }

        }
        prevbtn!!.setOnClickListener{
            if(rs.moveToPrevious()){
                nameView!!.setText(rs.getString(1))

                barcodeView!!.setText(rs.getString(2))
                quantityView!!.setText(rs.getString(3))
            }


        }
        updatebtn!!.setOnClickListener{
            var cv = ContentValues()
            cv.put("NAME",nameView!!.text.toString())
            cv.put("QUANTITY",quantityView!!.text.toString())
            cv.put("BARCODE",barcodeView!!.text.toString())
            db.update("ITEMS",cv,"_id = ?", arrayOf(rs.getString(0)))
            rs.requery()
            var ad = AlertDialog.Builder(this)
            ad.setTitle("Product Update")
            ad.setMessage("Product Details Updated Successfully ..!")
            ad.setPositiveButton("OK",DialogInterface.OnClickListener{ dialogInterface, i ->
                if(rs.moveToFirst()){
                    barcodeView!!.setText(rs.getString(2))
                    nameView!!.setText(rs.getString(1))
                    quantityView!!.setText(rs.getString(3))
                }
            })
            ad.show()

        }
        ViewAll!!.setOnClickListener{
            //Cardview2!!.visibility = View.GONE
            menuLayout!!.visibility = View.GONE
            searchView!!.visibility = View.VISIBLE
            listHeadings!!.visibility = View.VISIBLE
            listView!!.visibility = View.VISIBLE
            ManageProducts!!.visibility = View.VISIBLE
            adapter.notifyDataSetChanged()
            searchView!!.isIconified = false
            searchView!!.queryHint = "Search Among ${rs.count} Record"


        }
        searchView!!.setOnQueryTextListener(object:SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
               return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                rs = db.rawQuery("SELECT * FROM ITEMS WHERE NAME LIKE '%${newText}%' OR BARCODE LIKE  '%${newText}%'",null)
                adapter.changeCursor(rs)
                return false
            }

        })
        ManageProducts!!.setOnClickListener{
            menuLayout!!.visibility = View.VISIBLE
            searchView!!.visibility = View.GONE
            listHeadings!!.visibility = View.GONE
            listView!!.visibility = View.GONE
            ManageProducts!!.visibility = View.GONE
        }
        deletebtn!!.setOnClickListener{
            db.delete("ITEMS","_id = ?", arrayOf(rs.getString(0)))
            rs.requery()
            var ad = AlertDialog.Builder(this)
            ad.setTitle("Product Delete")
            ad.setMessage("Product Deleted Successfully ..!")
            ad.setPositiveButton("OK",DialogInterface.OnClickListener{ dialogInterface, i ->
                if(rs.moveToFirst()){
                    barcodeView!!.setText(rs.getString(2))
                    nameView!!.setText(rs.getString(1))
                    quantityView!!.setText(rs.getString(3))
                }
            })
            ad.show()

        }
        ScanProductAgain!!.setOnClickListener{
            var intent: Intent = Intent(this,MainActivity ::class.java)
            startActivity(intent)
        }












    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menu!!.add(101,11,1,"DELETE")
        menu?.setHeaderTitle("Removing Product")
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        if(item.itemId == 11){
            db.delete("ITEMS","_id=?", arrayOf(rs.getString(0)))
            rs.requery()
            adapter.notifyDataSetChanged()
            searchView!!.queryHint = "Search Among ${rs.count} Record"
        }
        return super.onContextItemSelected(item)
    }


}