package com.example.contactdatabaseapp

import androidx.room.Database

@Database(
    entities = [Contact::class],
    version = 1
)

abstract class ContactDataBase() {

    abstract val dao :ContactDao
}