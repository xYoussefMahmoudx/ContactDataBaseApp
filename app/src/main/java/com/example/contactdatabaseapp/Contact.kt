package com.example.contactdatabaseapp

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity
data class Contact(
    val fName:String,
    val lName:String,
    val pNumber:String,
    @PrimaryKey(autoGenerate = true)
    val id:Int =0

    )