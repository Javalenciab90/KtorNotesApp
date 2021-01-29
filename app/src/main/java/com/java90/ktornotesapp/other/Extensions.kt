package com.java90.ktornotesapp.other

import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

fun Fragment.showSnackBar(message: String) {
    activity?.let {
        val view: View = it.findViewById(android.R.id.content)
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
    }
}