package com.example.retailapp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MyDbHelper(context: Context) : SQLiteOpenHelper(context, "USERDP", null, 1){
    override fun onCreate(db: SQLiteDatabase?) {
       db?.execSQL("CREATE TABLE PRODUCTS(ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, BARCODE INT, QUANTITY INT) ")
        db?.execSQL("INSERT INTO PRODUCTS(NAME, BARCODE, QUANTITY) VALUES('IRTAZA',12345,4)")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

    }

}