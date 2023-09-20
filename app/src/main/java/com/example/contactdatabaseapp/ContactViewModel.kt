package com.example.contactdatabaseapp

import android.app.usage.UsageEvents.Event
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ContactViewModel(
    private val dao :ContactDao
):ViewModel() {
    private val _sortType = MutableStateFlow(SortType.First_Name)
    private val _contacts = _sortType
        .flatMapLatest { sortType->
            when(sortType){
               SortType.First_Name -> dao.getContactsByFirstName()
                SortType.Last_Name -> dao.getContactsByLastName()
                SortType.Phone_Number -> dao.getContactsByPhoneNumber()
            }

        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _state = MutableStateFlow(ContactState())
    val state = combine(_state,_contacts,_sortType){state,contacts,sortType->
        state.copy(
            contacts= contacts,
            sortType=sortType
        )

    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ContactState())

    fun onEvent(event:ContactEvent){
        when(event){
            is ContactEvent.DeleteContact -> {
                viewModelScope.launch {
                    dao.deleteContact(event.contact)
                }
            }
            ContactEvent.HideDialog -> {
                _state.update { it.copy(
                        isAddingContact = false
                    )
                }
            }
            ContactEvent.SaveContact -> {

                val fName= state.value.fName
                val lName=state.value.lName
                val pNumber=state.value.pNumber

                if(fName.isBlank()||lName.isBlank()||pNumber.isBlank()){
                    return
                }
                val contact =Contact(
                    fName=fName,
                    lName=lName,
                    pNumber=pNumber
                )
                viewModelScope.launch {
                    dao.upsertContact(contact)
                }
                _state.update {it.copy(
                    isAddingContact = false,
                    fName = "",
                    lName = "",
                    pNumber = "",

                )
                }

            }
            is ContactEvent.SetFName -> {
                _state.update {it.copy(
                    fName = event.fName
                )
                }
            }
            is ContactEvent.SetLName -> {
                _state.update {it.copy(
                    lName = event.lName
                )
                }
            }
            is ContactEvent.SetPNumber -> {
                _state.update {it.copy(
                    pNumber = event.pNumber
                )
                }
            }
            ContactEvent.ShowDialog -> {
                _state.update {it.copy(
                    isAddingContact = true
                )
                }
            }
            is ContactEvent.SortContacts -> {
                _sortType.value=event.sortType
            }
        }
    }

}