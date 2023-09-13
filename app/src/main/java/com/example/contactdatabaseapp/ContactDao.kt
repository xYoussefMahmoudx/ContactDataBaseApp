package com.example.contactdatabaseapp

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {
    @Upsert
    suspend fun upsertContact(contact:Contact)
    @Delete
    suspend fun deleteContact(contact:Contact)
    @Query("SELECT * FROM contact ORDER By fName ASC")
    fun getContactsByFirstName(): Flow<List<Contact>>

    @Query("SELECT * FROM contact ORDER By lName ASC")
    fun getContactsByLastName(): Flow<List<Contact>>
    @Query("SELECT * FROM contact ORDER By pNumber ASC")
    fun getContactsByPhoneNumber(): Flow<List<Contact>>
}