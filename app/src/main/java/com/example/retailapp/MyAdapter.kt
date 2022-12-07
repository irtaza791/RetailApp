package com.example.retailapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(private var productList : ArrayList<products>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>(){


    private var clicked:ArrayList<products> = productList
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
       val producView = LayoutInflater.from(parent.context).inflate(R.layout.product_list,parent,false)
        return  MyViewHolder(producView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = productList[position]

        holder.productName.text = currentItem.name
        holder.productBarcode.text = currentItem.barcode
        holder.productQuantity.text = currentItem.quantity
    }

    override fun getItemCount(): Int {
        return productList.size
    }


    inner class MyViewHolder(productView : View ) : RecyclerView.ViewHolder(productView){

        val productName : TextView = productView.findViewById(R.id.productName)
        val productBarcode : TextView = productView.findViewById(R.id.productBarcode)
        val productQuantity : TextView = productView.findViewById(R.id.productQuantity)
        init {
            productView.setOnClickListener{
                val position: Int = adapterPosition



                Toast.makeText(productView.context,"You clicked on ${clicked[position].barcode}",Toast
                    .LENGTH_LONG).show()

        }


       }
    }
}