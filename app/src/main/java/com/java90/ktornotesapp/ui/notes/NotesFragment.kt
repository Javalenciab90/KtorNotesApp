package com.java90.ktornotesapp.ui.notes

import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.java90.ktornotesapp.R
import com.java90.ktornotesapp.databinding.FragmentNotesBinding
import com.java90.ktornotesapp.utils.Constants.KEY_LOGGED_IN_EMAIL
import com.java90.ktornotesapp.utils.Constants.KEY_LOGGED_IN_PASSWORD
import com.java90.ktornotesapp.utils.Constants.NO_EMAIL
import com.java90.ktornotesapp.utils.Constants.NO_PASSWORD
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NotesFragment : Fragment() {

    @Inject
    lateinit var sharedPref: SharedPreferences

    private lateinit var binding: FragmentNotesBinding

    override fun onCreateView(inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentNotesBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fabAddNote.setOnClickListener {
            findNavController().navigate(NotesFragmentDirections.actionNotesFragmentToAddEditNoteFragment(""))
        }
    }

    private fun logOut() {
        sharedPref.edit().apply{
            putString(KEY_LOGGED_IN_EMAIL, NO_EMAIL)
            putString(KEY_LOGGED_IN_PASSWORD, NO_PASSWORD)
        }.apply()
        val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.notesFragment, true)
                .build()
        findNavController().navigate(
                NotesFragmentDirections.actionNotesFragmentToAuthFragment(),
                navOptions
        )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_notes, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.miLogout -> logOut()
        }
        return super.onOptionsItemSelected(item)
    }

}