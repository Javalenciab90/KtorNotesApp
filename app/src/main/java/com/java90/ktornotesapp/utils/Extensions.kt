package com.java90.ktornotesapp.utils

import android.view.View
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import java.util.regex.Pattern

fun Fragment.showSnackBar(message: String) {
    activity?.let {
        val view: View = it.findViewById(android.R.id.content)
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
    }
}

fun String.isEmailValid() : Boolean {
    val expression = "^[\\w.-]+@([\\w\\-]+\\.)+[A-Z]{2,8}$"
    val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE).matcher(this)
    return this.isNotEmpty() && pattern.matches()
}

fun ProgressBar.showProgressBar() {
    this.visibility = View.VISIBLE
}

fun ProgressBar.hideProgressBar() {
    this.visibility = View.GONE
}
