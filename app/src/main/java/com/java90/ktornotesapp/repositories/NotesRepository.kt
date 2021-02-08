package com.java90.ktornotesapp.repositories

import com.java90.ktornotesapp.data.local.NoteDao
import com.java90.ktornotesapp.data.remote.NoteApi
import com.java90.ktornotesapp.data.remote.requests.AccountRequest
import com.java90.ktornotesapp.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NotesRepository @Inject constructor(
    private val noteDao: NoteDao,
    private val noteApi: NoteApi) {

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