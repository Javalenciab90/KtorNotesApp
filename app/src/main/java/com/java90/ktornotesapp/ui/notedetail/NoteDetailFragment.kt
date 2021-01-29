package com.java90.ktornotesapp.ui.notedetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.java90.ktornotesapp.databinding.FragmentNoteDetailBinding

class NoteDetailFragment : Fragment() {

    private lateinit var binding: FragmentNoteDetailBinding

    override fun onCreateView(inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentNoteDetailBinding.inflate(inflater, container, false)
        return binding.root
    }
}