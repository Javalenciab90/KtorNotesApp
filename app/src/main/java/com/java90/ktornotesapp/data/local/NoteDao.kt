package com.java90.ktornotesapp.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.java90.ktornotesapp.data.local.entities.Note
import kotlinx.coroutines.flow.Flow

/**
 * Functions to define the access to the Database.
 */

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Query("DELETE FROM notes WHERE id = :noteID")
    suspend fun deleteNote(noteID: String)

    @Query("DELETE FROM notes WHERE isSynced = 1")
    suspend fun deleteAllSyncedNotes()

    @Query("SELECT * FROM notes WHERE id = :noteID")
    fun observeNoteById(noteID: String): LiveData<Note>

    @Query("SELECT * FROM notes WHERE id = :noteID")
    suspend fun getNoteById(noteID: String): Note?

    @Query("SELECT * FROM notes ORDER BY date DESC")
    fun getAllNotes() : Flow<List<Note>>

    @Query("SELECT * FROM notes WHERE isSynced = 0")
    suspend fun getAllUnSyncedNotes() : List<Note>
}