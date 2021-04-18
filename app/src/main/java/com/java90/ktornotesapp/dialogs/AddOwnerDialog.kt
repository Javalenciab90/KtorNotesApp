package com.java90.ktornotesapp.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.java90.ktornotesapp.R
import com.java90.ktornotesapp.databinding.EditTextEmailBinding
import com.skydoves.colorpickerview.ColorEnvelope
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener

class AddOwnerDialog : DialogFragment() {

    private var positiveListener: ((String) -> Unit)? = null
    fun setPositiveListener(listener: (String) -> Unit) {
        positiveListener = listener
    }

    private var _binding: EditTextEmailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = LayoutInflater.from(requireContext())
        _binding = EditTextEmailBinding.inflate(inflater, null, false)

        return MaterialAlertDialogBuilder(requireContext())
                .setView(binding.root)
                .setIcon(R.drawable.ic_add_person)
                .setTitle("Add owner to Note")
                .setMessage("ENter an E-mail of a person you want to share to note with.")
                .setPositiveButton("Add") { _, _ ->
                    val email = binding.etAddOwnerEmail.text.toString()
                    positiveListener?.let { yes ->
                        yes(email)
                    }
                }
                .setNegativeButton("Cancel") { dialoInterface, _ ->
                    dialoInterface.cancel()
                }
                .create()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}