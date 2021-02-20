package com.java90.ktornotesapp.utils

object Constants {

    const val DATABASE_NAME = "notes_db"
    // 192.168.0.9
    const val BASE_URL = "http://10.0.2.2:8080"
    const val URL_PATH_REGISTER = "/register"
    const val URL_PATH_LOGIN = "/login"
    const val URL_PATH_DELETE_NOTE = "/deleteNote"
    const val URL_PATH_ADD_NOTE = "/addNote"
    const val URL_PATH_ADD_OWNER_NOTE = "/addOwnerToNote"
    const val URL_PATH_GET_NOTES = "/getNotes"

    // Interceptor
    val IGNORE_AUTH_URLS = listOf("/login", "/register")

    const val KEY_LOGGED_IN_EMAIL = "KEY_LOGGED_IN_EMAIL"
    const val KEY_LOGGED_IN_PASSWORD = "KEY_LOGGED_IN_PASSWORD"
    const val NO_EMAIL = "NO_EMAIL"
    const val NO_PASSWORD = "NO_PASSWORD"
    const val ENCRYPTED_SHARED_PREF_NAME = "enc_shared_pref"

    const val DEFAULT_NOTE_COLOR = "FFA500"
}