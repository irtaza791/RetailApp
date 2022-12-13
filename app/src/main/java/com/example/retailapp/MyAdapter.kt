package com.example.retailapp

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView


/*
This file defines a custom adapter MyAdapter for a RecyclerView.
The adapter takes a list of products and uses it to create a View for each item in the list.
The View contains the product's name, barcode, quantity, and an image.
The View also has an on-click listener that shows a toast message with the product's barcode when clicked.

 */

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
        val decodedImg = currentItem.encodedimg?.let { decodingImg(it) }
        holder.imgDisplay.setImageBitmap(decodedImg)

        holder.itemView.setOnLongClickListener {

            true
        }
    }
    private fun decodingImg(imgString : String): Bitmap {
        val imageBytes = Base64.decode(imgString, Base64.NO_WRAP)
        val imageBitmap = BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.size)
        return imageBitmap

    }
    override fun getItemCount(): Int {
        return productList.size
    }







    inner class MyViewHolder(productView : View ) : RecyclerView.ViewHolder(productView){

        val productName : TextView = productView.findViewById(R.id.productName)
        val productBarcode : TextView = productView.findViewById(R.id.productBarcode)
        val productQuantity : TextView = productView.findViewById(R.id.productQuantity)
        val imgDisplay : ImageView = productView.findViewById(R.id.imgDisplay)

        init {
            productView.setOnClickListener{
                val position: Int = adapterPosition
                val currentItem = productList[position]

                // Launch the new activity here
                val intent = Intent(productView.context, productDetailsActivity::class.java)
                intent.putExtra("barcode", currentItem.barcode)
                intent.putExtra("name", currentItem.name)
                intent.putExtra("quantity", currentItem.quantity)
                intent.putExtra("encodedimg", currentItem.encodedimg)
                startActivity(productView.context, intent, null)

                true
            }
        }


       }
    }

