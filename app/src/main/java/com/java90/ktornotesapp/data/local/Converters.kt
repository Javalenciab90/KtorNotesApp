package com.java90.ktornotesapp.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * @function: To convert from JSON in something you need.
 * inline fun <reified T> fromJson(json: String): T {
        return Gson().fromJson(json, object: TypeToken<T>(){}.type)
   }
 * we can call it like:
 * val typeT = fromJson<List<T>>()
 */

class Converters {
    @TypeConverter
    fun fromList(list: List<String>) : String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun toList(string: String) : List<String> {
        return Gson().fromJson(string, object : TypeToken<List<String>>() {}.type)
    }
}