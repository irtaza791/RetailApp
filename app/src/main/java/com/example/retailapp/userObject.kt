package com.example.retailapp

class userObject {
    var barcode: Int = 0
    var name: String = ""
    var quantity: Int = 0

    constructor(name:String,quantity:Int,barcode:Int){
        this.barcode = barcode
        this.name = name
        this.quantity = quantity

    }
}