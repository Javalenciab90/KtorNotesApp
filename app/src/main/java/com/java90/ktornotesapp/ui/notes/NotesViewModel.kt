package com.java90.ktornotesapp.ui.notes

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.java90.ktornotesapp.data.local.entities.Note
import com.java90.ktornotesapp.repositories.NotesRepository
import com.java90.ktornotesapp.utils.Event
import com.java90.ktornotesapp.utils.Resource

class NotesViewModel @ViewModelInject constructor(
        private val repository: NotesRepository
) : ViewModel() {

    private val _forceUpdate = MutableLiveData(false)

    private val _allNotes = MutableLiveData<Event<Resource<List<Note>>>>()
    val allNotes : LiveData<Event<Resource<List<Note>>>> = _allNotes

    init {
        getDataFromDatabaseOnce()
    }

    fun syncAllNotes() = _forceUpdate.postValue(true)

    /**
     * Map() is conceptually identical to the use in RXJava, basically you are changing a parameter of LiveData in another one.
     *
     * SwitchMap() instead you are going to substitute the LiveData itself with another one! Typical case is when you retrieve
     * some data from a Repository for instance and to "eliminate" the previous LiveData (to garbage collect, to make it more
     * efficient the memory usually) you pass a new LiveData that execute the same action( getting a query for instance).
     *
     */
    private fun getDataFromDatabaseOnce() {
        _forceUpdate.switchMap {
            repository.getAllNotes().asLiveData(viewModelScope.coroutineContext)
        }.switchMap {
            MutableLiveData(Event(it))
        }
    }
}