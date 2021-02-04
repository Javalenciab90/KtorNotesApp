package com.java90.ktornotesapp.ui.auth

import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.java90.ktornotesapp.databinding.FragmentAuthBinding
import com.java90.ktornotesapp.utils.Status
import com.java90.ktornotesapp.utils.hideProgressBar
import com.java90.ktornotesapp.utils.showProgressBar
import com.java90.ktornotesapp.utils.showSnackBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthFragment : Fragment() {

    private val viewModel: AuthViewModel by viewModels()

    private lateinit var binding: FragmentAuthBinding

    override fun onCreateView(inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAuthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().requestedOrientation = SCREEN_ORIENTATION_PORTRAIT
        setUpObserves()
        register()
    }

    private fun register() {
        binding.apply {
            btnRegister.setOnClickListener {
                viewModel.register(
                        etLoginEmail.text.toString().trim(),
                        etRegisterPassword.text.toString().trim(),
                        etRegisterPasswordConfirm.text.toString().trim()
                )
            }
        }
    }

    private fun setUpObserves() {
        viewModel.registerStatus.observe(viewLifecycleOwner, { result ->
            result?.let {
                when(result.status) {
                    Status.LOADING -> {
                        binding.registerProgressBar.showProgressBar()
                    }
                    Status.SUCCESS -> {
                        binding.registerProgressBar.hideProgressBar()
                        showSnackBar(result.data ?: "Successfuly register an account")
                    }
                    Status.ERROR -> {
                        binding.registerProgressBar.hideProgressBar()
                        showSnackBar(result.data ?: "An unknown error occurred")
                    }
                }
            }
        })
    }
}