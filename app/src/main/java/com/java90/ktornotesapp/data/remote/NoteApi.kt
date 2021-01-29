package com.java90.ktornotesapp.data.remote

import com.java90.ktornotesapp.data.local.entities.Note
import com.java90.ktornotesapp.data.remote.requests.AccountRequest
import com.java90.ktornotesapp.data.remote.requests.AddOwnerRequest
import com.java90.ktornotesapp.data.remote.requests.DeleteNoteRequest
import com.java90.ktornotesapp.data.remote.responses.SimpleResponse
import com.java90.ktornotesapp.other.Constants.URL_PATH_ADD_NOTE
import com.java90.ktornotesapp.other.Constants.URL_PATH_ADD_OWNER_NOTE
import com.java90.ktornotesapp.other.Constants.URL_PATH_DELETE_NOTE
import com.java90.ktornotesapp.other.Constants.URL_PATH_GET_NOTES
import com.java90.ktornotesapp.other.Constants.URL_PATH_LOGIN
import com.java90.ktornotesapp.other.Constants.URL_PATH_REGISTER
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * We don't use @HEADER annotation because we using
 * an INTERCEPTOR that changes those Headers for us
 * each time that we need authentication.
 */

interface NoteApi {

    @POST(URL_PATH_REGISTER)
    suspend fun register(
        @Body registerRequest: AccountRequest
    ): Response<SimpleResponse>

    @POST(URL_PATH_LOGIN)
    suspend fun login(
        @Body loginRequest: AccountRequest
    ): Response<SimpleResponse>

    @POST(URL_PATH_ADD_NOTE)
    suspend fun addNote(
        @Body addRequest: Note
    ): Response<ResponseBody>

    @POST(URL_PATH_DELETE_NOTE)
    suspend fun deleteNote(
        @Body deleteRequest: DeleteNoteRequest
    ): Response<SimpleResponse>

    @POST(URL_PATH_ADD_OWNER_NOTE)
    suspend fun addOwnerToNote(
        @Body addOwnerRequest: AddOwnerRequest
    ): Response<SimpleResponse>

    @GET(URL_PATH_GET_NOTES)
    suspend fun getNotes() : Response<List<Note>>
}