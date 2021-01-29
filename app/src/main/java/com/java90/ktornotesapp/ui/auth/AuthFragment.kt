package com.java90.ktornotesapp.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.java90.ktornotesapp.databinding.FragmentAuthBinding

class AuthFragment : Fragment() {

    private lateinit var binding: FragmentAuthBinding

    override fun onCreateView(inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAuthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {
            findNavController().navigate(AuthFragmentDirections.actionAuthFragmentToNotesFragment())
        }
    }
}