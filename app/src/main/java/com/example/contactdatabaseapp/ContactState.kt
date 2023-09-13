package com.example.contactdatabaseapp

data class ContactState (
    val contacts:List<Contact> = emptyList(),
    val fName:String ="",
    val lName:String ="",
    val pNumber:String ="",
    val isAddingContact:Boolean =false,
    val sortType: SortType =SortType.First_Name
)