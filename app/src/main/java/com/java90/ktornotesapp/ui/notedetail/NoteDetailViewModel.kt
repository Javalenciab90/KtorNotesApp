package com.java90.ktornotesapp.ui.notedetail

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.java90.ktornotesapp.repositories.NotesRepository

class NoteDetailViewModel @ViewModelInject constructor(
    private val repository: NotesRepository
) : ViewModel() {

    fun observeNoteByID(noteID: String) = repository.observeNoteByID(noteID)
}