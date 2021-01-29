package com.java90.ktornotesapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import java.util.*

/**
 * @synced: value to know if is synchronize with the server
 * but we can't send it with the whole data, so we Expose
 * to ignore that field.
 */

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = false)
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val content: String,
    val date: Long,
    val owners: List<String>,
    val color: String,
    @Expose(deserialize = false, serialize = false)
    val isSynced: Boolean = false
)