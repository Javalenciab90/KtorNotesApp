package com.java90.ktornotesapp.repositories

import android.app.Application
import com.java90.ktornotesapp.data.local.NoteDao
import com.java90.ktornotesapp.data.local.entities.LocallyDeletedNoteID
import com.java90.ktornotesapp.data.local.entities.Note
import com.java90.ktornotesapp.data.remote.NoteApi
import com.java90.ktornotesapp.data.remote.requests.AccountRequest
import com.java90.ktornotesapp.data.remote.requests.DeleteNoteRequest
import com.java90.ktornotesapp.utils.Resource
import com.java90.ktornotesapp.utils.checkForInternetConnection
import com.java90.ktornotesapp.utils.networkBoundResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NotesRepository @Inject constructor(
    private val noteDao: NoteDao,
    private val noteApi: NoteApi,
    private val context: Application
) {

    suspend fun insertNote(note: Note) {
        val response = try {
            noteApi.addNote(note)
        } catch (e: Exception) { null }
        if (response != null && response.isSuccessful)  {
            noteDao.insertNote(note.apply { isSynced = true })
        } else {
            noteDao.insertNote(note)
        }
    }

    suspend fun deleteNote(noteID: String) {
        val response = try {
            noteApi.deleteNote(DeleteNoteRequest(noteID))
        } catch (e: java.lang.Exception) {
            null
        }
        noteDao.deleteNoteById(noteID)
        if (response == null || !response.isSuccessful) {
           noteDao.insertLocallyDeletedNoteID(LocallyDeletedNoteID(noteID))
        } else {
            deleteLocallyDeletedNoteID(noteID)
        }
    }

    suspend fun insertNotes(notes: List<Note>) {
        notes.forEach { insertNote(it) }
    }

    suspend fun deleteLocallyDeletedNoteID(deletedNoteID: String) {
        noteDao.deleteLocallyDeletedNotedID(deletedNoteID)
    }

    suspend fun getNoteById(noteID: String) = noteDao.getNoteById(noteID)

    fun getAllNotes() : Flow<Resource<List<Note>>> {
        return networkBoundResource(
                query = {
                    noteDao.getAllNotes()
                },
                fetch = {
                    noteApi.getNotes()
                },
                saveFetchResult = { response ->
                    response.body()?.let {
                        insertNotes(it)
                    }
                },
                shouldFetch = {
                    checkForInternetConnection(context)
                }
        )
    }

    suspend fun login(email: String, password: String) = withContext(Dispatchers.IO) {
        try {
            val response = noteApi.login(AccountRequest(email, password))
            if (response.isSuccessful) {
                response.body()?.let {
                    if (it.successful) Resource.success(it.message)
                    else Resource.error(it.message, null)
                }
            }else {
                Resource.error(response.message(), null)
            }
        }catch (e: Exception) {
            Resource.error("Couldn't connect to the servers. Check internet connection", null)
        }
    }

    suspend fun register(email: String, password: String) = withContext(Dispatchers.IO) {
        try {
            val response = noteApi.register(AccountRequest(email, password))
            if (response.isSuccessful) {
                response.body()?.let {
                    if (it.successful) Resource.success(it.message)
                    else Resource.error(it.message, null)
                }
            }else {
                Resource.error(response.message(), null)
            }
        }catch (e: Exception) {
            Resource.error("Couldn't connect to the servers. Check internet connection", null)
        }
    }
}