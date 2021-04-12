package com.java90.ktornotesapp.ui.notedetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.java90.ktornotesapp.data.local.entities.Note
import com.java90.ktornotesapp.databinding.FragmentNoteDetailBinding
import com.java90.ktornotesapp.utils.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import io.noties.markwon.Markwon

@AndroidEntryPoint
class NoteDetailFragment : Fragment() {

    private val viewModel: NoteDetailViewModel by viewModels()

    private var _binding: FragmentNoteDetailBinding? = null
    private val binding get() = _binding!!

    private val args: NoteDetailFragmentArgs by navArgs()

    private var curNote : Note? = null

    override fun onCreateView(inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentNoteDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpObservables()

        binding.fabEditNote.setOnClickListener {
            findNavController().navigate(NoteDetailFragmentDirections.actionNoteDetailFragmentToAddEditNoteFragment(args.id))
        }
    }

    private fun setMarkdownText(text: String) {
        val markWon = Markwon.create(requireContext())
        val markDown = markWon.toMarkdown(text)
        markWon.setParsedMarkdown(binding.tvNoteContent, markDown)
    }

    private fun setUpObservables() {
        viewModel.observeNoteByID(args.id).observe(viewLifecycleOwner, Observer {
            it?.let { note ->
                with(binding) {
                    tvNoteTitle.text = note.title
                    setMarkdownText(note.content)
                    curNote = note
                }
            } ?: showSnackBar("Note not found")
        })
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}