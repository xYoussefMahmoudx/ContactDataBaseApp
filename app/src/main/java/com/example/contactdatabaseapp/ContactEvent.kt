package com.example.contactdatabaseapp

sealed interface ContactEvent{
    object SaveContact :ContactEvent
    data class SetFName(val fName:String):ContactEvent
    data class SetLName(val lName:String):ContactEvent
    data class SetPNumber(val pNumber: String):ContactEvent

    object ShowDialog:ContactEvent
    object HideDialog:ContactEvent
    data class SortContacts(val sortType:SortType):ContactEvent
    data class DeleteContact(val contact: Contact):ContactEvent
}