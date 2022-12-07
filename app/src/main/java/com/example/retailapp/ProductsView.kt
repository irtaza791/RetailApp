package com.example.retailapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products_view)

        productRecycler = findViewById(R.id.productList)

        productRecycler.layoutManager = LinearLayoutManager(this)
        productRecycler.setHasFixedSize(true)

        productArrayList = arrayListOf<products>()

        getProductsData()

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

                            Toast.makeText(applicationContext,"Item Removed Successfully",
                                Toast
                                .LENGTH_LONG).show()
                            productRecycler.adapter?.notifyItemRemoved(viewHolder.adapterPosition)
                        }else{

                        }
                    }

                }
            }
        }

    }
}