package com.java90.ktornotesapp.utils

object Constants {

    const val DATABASE_NAME = "notes_db"

    //******* URLS ******** 192.168.0.9 - 169.254.10.16 -10.0.2.2 - 0.0.0.0//
    const val BASE_URL = "http://192.168.0.9:8080"
    const val URL_PATH_REGISTER = "/register"
    const val URL_PATH_LOGIN = "/login"
    const val URL_PATH_DELETE_NOTE = "/deleteNote"
    const val URL_PATH_ADD_NOTE = "/addNote"
    const val URL_PATH_ADD_OWNER_NOTE = "/addOwnerToNote"
    const val URL_PATH_GET_NOTES = "/getNotes"
    // Interceptor
    val IGNORE_AUTH_URLS = listOf("/login", "/register")

    const val ENCRYPTED_SHARED_PREF_NAME = "enc_shared_pref"
}