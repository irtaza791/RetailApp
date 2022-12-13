package com.example.retailapp

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.widget.Button

import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.Collections

class ProductsView : AppCompatActivity() {

    private  lateinit var dataRef : DatabaseReference
    private lateinit var productRecycler: RecyclerView
    private  lateinit var productArrayList : ArrayList<products>
    private lateinit var  deleted:String
    val database = FirebaseDatabase.getInstance("https://retailapp-c7112-default-rtdb.europe-west1.firebasedatabase.app").reference
    var pvANP:Button? = null
    var pvHome:Button? = null
    var search:SearchView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products_view)
        pvANP = findViewById(R.id.pvANP)
        pvHome = findViewById(R.id.pvHome)
        productRecycler = findViewById(R.id.productList)
        search = findViewById(R.id.productsSearch)
        productRecycler.layoutManager = LinearLayoutManager(this)
        productRecycler.setHasFixedSize(false)

        productArrayList = arrayListOf<products>()


        getProductsData()
        search!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
               // val filteredItems = productArrayList.filter { it.name!!.contains(newText.toString()) }
                filterList(newText)
                return true
            }

        })

        pvANP!!.setOnClickListener{
            val intent: Intent = Intent(this,MainActivity ::class.java)
            startActivity(intent)
            finish()
        }
        pvHome!!.setOnClickListener{
            val intent: Intent = Intent(this,loadingpage ::class.java)
            startActivity(intent)
            finish()
        }

    }
    // for search view to search for products
    private fun filterList(searchText : String){
        val filteredList = arrayListOf<products>()

        // loop through the original list of products and add items to the filteredList
        // if their name contains the search text
        for(product in productArrayList){
            if (product.name!!.contains(searchText) || product.barcode!!.contains(searchText)) {
                filteredList.add(product)
            }
        }

        // update the RecyclerView with the filtered list of products
        productRecycler.adapter = MyAdapter(filteredList)
    }

    private fun getProductsData() {
        val cu = FirebaseAuth.getInstance().currentUser?.uid.toString()
        dataRef = FirebaseDatabase.getInstance("https://retailapp-c7112-default-rtdb.europe-west1.firebasedatabase.app").getReference("${cu}/All Products")
        dataRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {


                        // looping through database
                        for (i in snapshot.children) {
                            // fetching all the data and storing in the list
                            val prd = i.getValue(products::class.java)
                            if (prd != null) {

                                productArrayList.add(prd)
                            }
                        }
                        // attaching the adapter
                        productRecycler.adapter = MyAdapter(productArrayList)
                        val itemTouchHelper = ItemTouchHelper(simpleCallBack)
                        itemTouchHelper.attachToRecyclerView(productRecycler)
                    }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    private var simpleCallBack = object  : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP.or(ItemTouchHelper.DOWN),ItemTouchHelper.LEFT.or(ItemTouchHelper.RIGHT)){
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            var startPosition = viewHolder.adapterPosition
            var endPosition = target.adapterPosition

            Collections.swap(productArrayList,startPosition,endPosition)

            recyclerView.adapter?.notifyItemMoved(startPosition,endPosition)
            return true


        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {


            var position = viewHolder.adapterPosition
            val cu = FirebaseAuth.getInstance().currentUser?.uid.toString()

            dataRef = FirebaseDatabase.getInstance("https://retailapp-c7112-default-rtdb.europe-west1.firebasedatabase.app").getReference("${cu}/All Products").child(productArrayList[viewHolder.adapterPosition].barcode.toString())
            when(direction){
                ItemTouchHelper.LEFT -> {
                    dataRef.removeValue().addOnCompleteListener{
                        task ->
                        if (task.isSuccessful){
                            productArrayList.removeAt(position)

                            Toast.makeText(applicationContext,"Item Removed Successfully", Toast.LENGTH_LONG).show()
                            productRecycler.adapter?.notifyItemRemoved(viewHolder.adapterPosition)
                            restartActivity()
                        }else{

                        }
                    }


                }
                ItemTouchHelper.RIGHT -> {
                var name = TextView(this@ProductsView)
                    name.setText(productArrayList[viewHolder.adapterPosition].name.toString())
                val builder = AlertDialog.Builder(this@ProductsView)
                    builder.setMessage("Are you sure you want to update the product?")
                    builder.setCancelable(true)
                    builder.setView(name)

                    builder.setNegativeButton("cancel",DialogInterface.OnClickListener{
                        dialog, which ->
                        Toast.makeText(applicationContext,"No Changes Applied",Toast.LENGTH_LONG).show()
                        restartActivity()

                    })

                    builder.setPositiveButton("confirm",DialogInterface.OnClickListener{
                        dialog, which ->
                            updateProduct(productArrayList[viewHolder.adapterPosition].barcode.toString())

                        })

                    builder.show()
                }

            }

        }


    }
    private fun updateProduct(newmessage:String){

       val message = newmessage
        var intent: Intent = Intent(this,stroreproducts ::class.java).also {
            it.putExtra("BARCODE",message)
            startActivity(it)
            finish()
        }
    }
    private fun restartActivity(){
        val intent: Intent = Intent(this,ProductsView ::class.java)
        startActivity(intent)
        finish()
    }
}