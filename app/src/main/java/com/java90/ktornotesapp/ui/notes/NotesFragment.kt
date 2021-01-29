package com.java90.ktornotesapp.ui.notes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.java90.ktornotesapp.databinding.FragmentNotesBinding

class NotesFragment : Fragment() {

    private lateinit var binding: FragmentNotesBinding

    override fun onCreateView(inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentNotesBinding.inflate(inflater, container, false)
        return binding.root
    }
}