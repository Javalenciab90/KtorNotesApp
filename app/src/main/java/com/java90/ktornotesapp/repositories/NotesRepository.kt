package com.java90.ktornotesapp.repositories

import android.app.Application
import com.java90.ktornotesapp.data.local.NoteDao
import com.java90.ktornotesapp.data.local.entities.LocallyDeletedNoteID
import com.java90.ktornotesapp.data.local.entities.Note
import com.java90.ktornotesapp.data.remote.NoteApi
import com.java90.ktornotesapp.data.remote.requests.AccountRequest
import com.java90.ktornotesapp.data.remote.requests.AddOwnerRequest
import com.java90.ktornotesapp.data.remote.requests.DeleteNoteRequest
import com.java90.ktornotesapp.utils.Resource
import com.java90.ktornotesapp.utils.checkForInternetConnection
import com.java90.ktornotesapp.utils.networkBoundResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import retrofit2.Response
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

    fun observeNoteByID(noteID: String) = noteDao.observeNoteById(noteID)

    suspend fun deleteLocallyDeletedNoteID(deletedNoteID: String) {
        noteDao.deleteLocallyDeletedNotedID(deletedNoteID)
    }

    suspend fun getNoteById(noteID: String) = noteDao.getNoteById(noteID)

    private var curNotesResponse: Response<List<Note>>? = null
    private suspend fun syncNotes() {

        val unSyncedNotes = noteDao.getAllUnsyncedNotes()
        unSyncedNotes.forEach { note -> insertNote(note) }

        val locallyDeletedNoteIDs = noteDao.getAllLocallyDeletedNoteIDs()
        locallyDeletedNoteIDs.forEach { id -> deleteNote(id.deletedNoteID) }

        curNotesResponse = noteApi.getNotes()
        curNotesResponse?.body()?.let { notes ->
            noteDao.deleteAllNotes()
            insertNotes(notes.onEach { note -> note.isSynced = true } )
        }
    }

    fun getAllNotes() : Flow<Resource<List<Note>>> {
        return networkBoundResource(
                query = {
                    noteDao.getAllNotes()
                },
                fetch = {
                    syncNotes()
                    curNotesResponse
                },
                saveFetchResult = { response ->
                    response?.body()?.let {
                        insertNotes(it.onEach { note -> note.isSynced = true })
                    }
                },
                shouldFetch = {
                    checkForInternetConnection(context)
                }
        )
    }

    suspend fun addOwnerToNote(owner: String, noteID: String) = withContext(Dispatchers.IO) {
        try {
            val response = noteApi.addOwnerToNote(AddOwnerRequest(owner, noteID))
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