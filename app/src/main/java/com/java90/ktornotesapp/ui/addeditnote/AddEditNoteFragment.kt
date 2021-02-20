package com.java90.ktornotesapp.ui.addeditnote

import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.lifecycle.Observer
import com.java90.ktornotesapp.R
import com.java90.ktornotesapp.data.local.entities.Note
import com.java90.ktornotesapp.databinding.FragmentAddEditNoteBinding
import com.java90.ktornotesapp.dialogs.ColorPickerDialogFragment
import com.java90.ktornotesapp.utils.Constants.DEFAULT_NOTE_COLOR
import com.java90.ktornotesapp.utils.Constants.KEY_LOGGED_IN_EMAIL
import com.java90.ktornotesapp.utils.Constants.NO_EMAIL
import com.java90.ktornotesapp.utils.Status
import com.java90.ktornotesapp.utils.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

const val FRAGMENT_TAG = "AddEditNoteFragment"

@AndroidEntryPoint
class AddEditNoteFragment : Fragment() {

    private val viewModel: AddEditNoteViewModel by viewModels()
    private val args: AddEditNoteFragmentArgs by navArgs()

    @Inject
    lateinit var sharePref: SharedPreferences


    private lateinit var binding: FragmentAddEditNoteBinding

    private var curNote: Note? = null
    private var curNoteColor = DEFAULT_NOTE_COLOR

    override fun onCreateView(inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAddEditNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (args.id.isNotEmpty()) {
            viewModel.getNoteById(args.id)
        }
        setUpObservers()
        keepStateOfDialogWithRotation(savedInstanceState)

        binding.viewNoteColor.setOnClickListener {
            ColorPickerDialogFragment().apply {
                setPositiveListener {
                    changeViewNoteColor(it)
                }
            }.show(parentFragmentManager, FRAGMENT_TAG)
        }
    }

    private fun keepStateOfDialogWithRotation(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            val colorPickerDialog = parentFragmentManager.findFragmentByTag(FRAGMENT_TAG) as ColorPickerDialogFragment?
            colorPickerDialog?.setPositiveListener {
                changeViewNoteColor(it)
            }
        }
    }

    private fun changeViewNoteColor(colorString: String) {
        val drawable = ResourcesCompat.getDrawable(resources, R.drawable.circle_shape, null)
        drawable?.let {
            val drawableWrapper = DrawableCompat.wrap(it)
            val color = Color.parseColor("#${colorString}")
            DrawableCompat.setTint(drawableWrapper, color)
            binding.viewNoteColor.background = drawableWrapper
            curNoteColor = colorString
        }
    }

    private fun setUpObservers() {
        viewModel.note.observe(viewLifecycleOwner, Observer {
            it?.getContentIfNotHandled()?.let { result ->
                when(result.status) {
                    Status.LOADING -> { /* NO-OP */ }
                    Status.SUCCESS -> {
                        binding.apply {
                            result.data?.let { note ->
                                curNote = note
                                etNoteTitle.setText(note.title)
                                etNoteContent.setText(note.content)
                                changeViewNoteColor(note.color)
                            }
                        }
                    }
                    Status.ERROR -> { showSnackBar(result.message ?: "Not not found") }
                }
            }
        })
    }

    override fun onPause() {
        super.onPause()
        saveNote()
    }

    private fun saveNote() {
        val authEmail = sharePref.getString(KEY_LOGGED_IN_EMAIL, NO_EMAIL) ?: NO_EMAIL

        binding.apply {
            val title = etNoteTitle.text.toString()
            val content = etNoteContent.text.toString()
            if (title.isEmpty() || content.isEmpty()) { return }

            val date = System.currentTimeMillis()
            val color = curNoteColor
            val id = curNote?.id ?: UUID.randomUUID().toString()
            val owners = curNote?.owners ?: listOf(authEmail)
            val note = Note(id = id, title, content, date, owners, color)
            viewModel.insertNote(note)
        }

    }
}