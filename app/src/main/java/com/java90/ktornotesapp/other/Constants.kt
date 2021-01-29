package com.java90.ktornotesapp.other

object Constants {

    //******* URLS ********//
    const val URL_PATH_REGISTER = "/register"
    const val URL_PATH_LOGIN = "/login"
    const val URL_PATH_DELETE_NOTE = "/deleteNote"
    const val URL_PATH_ADD_NOTE = "/addNote"
    const val URL_PATH_ADD_OWNER_NOTE = "/addOwnerToNote"
    const val URL_PATH_GET_NOTES = "/getNotes"

    // Interceptor
    val IGNORE_AUTH_URLS = listOf("/login", "/register")


}