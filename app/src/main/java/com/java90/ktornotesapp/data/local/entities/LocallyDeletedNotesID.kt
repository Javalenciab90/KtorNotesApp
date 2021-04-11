package com.java90.ktornotesapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "locally_deleted_note_ids")
class LocallyDeletedNoteID (
    @PrimaryKey(autoGenerate = false)
    val deletedNoteID: String
)