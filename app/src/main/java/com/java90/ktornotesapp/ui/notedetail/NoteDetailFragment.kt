package com.java90.ktornotesapp.ui.notedetail

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.java90.ktornotesapp.R
import com.java90.ktornotesapp.data.local.entities.Note
import com.java90.ktornotesapp.databinding.FragmentNoteDetailBinding
import com.java90.ktornotesapp.dialogs.AddOwnerDialog
import com.java90.ktornotesapp.utils.Status
import com.java90.ktornotesapp.utils.hideProgressBar
import com.java90.ktornotesapp.utils.showProgressBar
import com.java90.ktornotesapp.utils.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import io.noties.markwon.Markwon

const val ADD_OWNER_DIALOG_TAG = "ADD_OWNER_DIALOG_TAG"

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
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpObservables()

        binding.fabEditNote.setOnClickListener {
            findNavController().navigate(NoteDetailFragmentDirections.actionNoteDetailFragmentToAddEditNoteFragment(args.id))
        }

        if (savedInstanceState != null) {
            val addOwnerDialog = parentFragmentManager.findFragmentByTag(ADD_OWNER_DIALOG_TAG) as AddOwnerDialog?
            addOwnerDialog?.setPositiveListener {
                addOwnerToCurNote(it)
            }
        }
    }

    private fun showAddOwnerDialog() {
        AddOwnerDialog().apply {
            setPositiveListener {
                addOwnerToCurNote(it)
            }
        }.show(parentFragmentManager, ADD_OWNER_DIALOG_TAG)
    }

    private fun addOwnerToCurNote(email: String) {
        curNote?.let { note ->
            viewModel.addOwnerToNote(email, note.id)
        }
    }

    private fun setMarkdownText(text: String) {
        val markWon = Markwon.create(requireContext())
        val markDown = markWon.toMarkdown(text)
        markWon.setParsedMarkdown(binding.tvNoteContent, markDown)
    }

    private fun setUpObservables() {
        viewModel.addOwnerStatus.observe(viewLifecycleOwner, Observer { event ->
            event?.getContentIfNotHandled()?.let { result ->
                when(result.status) {
                    Status.LOADING -> {
                        binding.addOwnerProgressBar.showProgressBar()
                    }
                    Status.SUCCESS -> {
                        binding.addOwnerProgressBar.hideProgressBar()
                        showSnackBar(result.data ?: "Successfully added owner to note")
                    }
                    Status.ERROR -> {
                        binding.addOwnerProgressBar.hideProgressBar()
                        showSnackBar(result.message ?: "An unknown error ocurred")
                    }
                }
            }
        })
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.note_detail_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.miAddOwner -> showAddOwnerDialog()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}