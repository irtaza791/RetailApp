package com.example.retailapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast


val DATABASE_NAME = "MyDB"
val TABLE_NAME = "PRODUCTS"
val COL_NAME = "name"
val COL_QT = "quantity"
val COL_BARCODE = "barcode"
class handler( var context:Context): SQLiteOpenHelper(context, DATABASE_NAME,null,1){
    override fun onCreate(db: SQLiteDatabase?) {
    val createTable = "CREATE TABLE " + TABLE_NAME +" (" +
            COL_BARCODE +" INTEGER," +
            COL_NAME + " VARCHAR(256)," +
            COL_QT + " INTEGER )"
        db?.execSQL(createTable)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

    }
    fun insertData(user: userObject){
        val db = this.writableDatabase
        var cv = ContentValues()
        cv.put(COL_NAME,user.name)
        cv.put(COL_QT,user.quantity)
        cv.put(COL_BARCODE,user.barcode)
        var result = db.insert(TABLE_NAME,null,cv)
        if(result == -1.toLong() ){
            Toast.makeText(context,"Failed",Toast.LENGTH_SHORT).show()

        }else{
           Toast.makeText(context,"Success",Toast.LENGTH_SHORT).show()

        }

    }

}