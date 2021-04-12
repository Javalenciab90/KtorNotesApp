package com.java90.ktornotesapp.ui.notes

import android.content.SharedPreferences
import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_USER
import android.graphics.Canvas
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.java90.ktornotesapp.R
import com.java90.ktornotesapp.databinding.FragmentNotesBinding
import com.java90.ktornotesapp.ui.adapters.NoteAdapter
import com.java90.ktornotesapp.utils.Constants.KEY_LOGGED_IN_EMAIL
import com.java90.ktornotesapp.utils.Constants.KEY_LOGGED_IN_PASSWORD
import com.java90.ktornotesapp.utils.Constants.NO_EMAIL
import com.java90.ktornotesapp.utils.Constants.NO_PASSWORD
import com.java90.ktornotesapp.utils.Status
import com.java90.ktornotesapp.utils.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NotesFragment : Fragment() {

    private val viewModel: NotesViewModel by viewModels()

    @Inject
    lateinit var sharedPref: SharedPreferences

    private lateinit var binding: FragmentNotesBinding
    private lateinit var noteAdapter: NoteAdapter

    private val swipingItem = MutableLiveData(false)

    override fun onCreateView(inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentNotesBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().requestedOrientation = SCREEN_ORIENTATION_USER
        setUpRecyclerView()
        setUpObservers()
        setUpSwipeRefreshLayout()

        noteAdapter.setOnItemClickListener {
            findNavController().navigate(NotesFragmentDirections.actionNotesFragmentToNoteDetailFragment(it.id))
        }

        binding.fabAddNote.setOnClickListener {
            findNavController().navigate(NotesFragmentDirections.actionNotesFragmentToAddEditNoteFragment(""))
        }
    }

    private fun setUpObservers() {
        viewModel.allNotes.observe(viewLifecycleOwner, Observer {
            it?.let { event ->
                // peekContent is to retrieve the data of event
                val result = event.peekContent()
                when (result.status) {
                    Status.LOADING -> {
                        result.data?.let { notes -> noteAdapter.notes = notes }
                        binding.swipeRefreshLayout.isRefreshing = true
                    }
                    Status.SUCCESS -> {
                        result.data?.let { notes -> noteAdapter.notes = notes }
                        binding.swipeRefreshLayout.isRefreshing = false
                    }
                    Status.ERROR -> { // Show an Error but Still showing data from Database.
                        event.getContentIfNotHandled()?.let { errorResource ->
                            errorResource.message?.let { message ->  showSnackBar(message) }
                        }
                        result.data?.let { notes -> noteAdapter.notes = notes }
                        binding.swipeRefreshLayout.isRefreshing = false
                    }
                }
            }
        })
        swipingItem.observe(viewLifecycleOwner, Observer {
            binding.swipeRefreshLayout.isEnabled = !it
        })
    }

    private val itemToucheCallback = object : ItemTouchHelper.SimpleCallback(
        0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
    ) {
        override fun onChildDraw(
            c: Canvas,
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            dX: Float,
            dY: Float,
            actionState: Int,
            isCurrentlyActive: Boolean
        ) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                swipingItem.postValue(isCurrentlyActive)
            }
        }

        override fun onMove(recyclerView: RecyclerView,
                            viewHolder: RecyclerView.ViewHolder,
                            target: RecyclerView.ViewHolder
        ): Boolean {
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position =  viewHolder.layoutPosition
            val note = noteAdapter.notes[position]
            viewModel.deleteNote(note.id)
            Snackbar.make(requireView(), "Note was successfully deleted", Snackbar.LENGTH_SHORT).apply {
                setAction("Undo") {
                    viewModel.insertNote(note)
                    viewModel.deleteLocallyDeletedNoteID(note.id)
                }
                show()
            }
        }
    }

    private fun setUpSwipeRefreshLayout() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.syncAllNotes()
        }
    }

    private fun setUpRecyclerView() {
        binding.rvNotes.apply {
            noteAdapter = NoteAdapter()
            adapter = noteAdapter
            layoutManager = LinearLayoutManager(requireContext())
            ItemTouchHelper(itemToucheCallback).attachToRecyclerView(this)
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