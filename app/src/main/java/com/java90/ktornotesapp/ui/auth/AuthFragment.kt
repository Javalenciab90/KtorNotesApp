package com.java90.ktornotesapp.ui.auth

import android.content.SharedPreferences
import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.java90.ktornotesapp.R
import com.java90.ktornotesapp.data.remote.BasicAuthInterceptor
import com.java90.ktornotesapp.databinding.FragmentAuthBinding
import com.java90.ktornotesapp.utils.Constants.KEY_LOGGED_IN_EMAIL
import com.java90.ktornotesapp.utils.Constants.KEY_LOGGED_IN_PASSWORD
import com.java90.ktornotesapp.utils.Constants.NO_EMAIL
import com.java90.ktornotesapp.utils.Constants.NO_PASSWORD
import com.java90.ktornotesapp.utils.Status
import com.java90.ktornotesapp.utils.hideProgressBar
import com.java90.ktornotesapp.utils.showProgressBar
import com.java90.ktornotesapp.utils.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AuthFragment : Fragment() {

    private val viewModel: AuthViewModel by viewModels()

    @Inject
    lateinit var sharedPref: SharedPreferences

    @Inject
    lateinit var basicAuthInterceptor: BasicAuthInterceptor

    private var curEmail: String? = null
    private var curPassword: String? = null

    private lateinit var binding: FragmentAuthBinding

    override fun onCreateView(inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAuthBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (isLoggedIn()) {
            authenticateApi(curEmail ?: "", curPassword ?: "")
            redirectLogin()
        }

        requireActivity().requestedOrientation = SCREEN_ORIENTATION_PORTRAIT
        setUpObserves()
        register()
        login()
    }

    private fun setSharedPreferences() {
        sharedPref.edit().apply {
            putString(KEY_LOGGED_IN_EMAIL, curEmail)
            putString(KEY_LOGGED_IN_PASSWORD, curPassword)
        }.apply()
        authenticateApi(curEmail ?: "", curPassword ?: "")
    }

    private fun authenticateApi(email: String, password: String) {
        basicAuthInterceptor.email = email
        basicAuthInterceptor.password = password
    }

    private fun setUpObserves() {

        viewModel.loggingStatus.observe(viewLifecycleOwner, { result ->
            result?.let {
                when(result.status) {
                    Status.LOADING -> {
                        binding.loginProgressBar.showProgressBar()
                    }
                    Status.SUCCESS -> {
                        binding.loginProgressBar.hideProgressBar()
                        setSharedPreferences()
                        redirectLogin()
                    }
                    Status.ERROR -> {
                        binding.loginProgressBar.showProgressBar()
                    }
                }
            }
        })

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

    private fun isLoggedIn() : Boolean {
        curEmail = sharedPref.getString(KEY_LOGGED_IN_EMAIL, NO_EMAIL) ?: NO_EMAIL
        curPassword = sharedPref.getString(KEY_LOGGED_IN_PASSWORD, NO_PASSWORD) ?: NO_PASSWORD
        return curEmail != NO_EMAIL && curPassword != NO_PASSWORD
    }

    private fun redirectLogin() {
        val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.authFragment, true)
                .build()
        findNavController().navigate(AuthFragmentDirections.actionAuthFragmentToNotesFragment(), navOptions)
    }

    private fun login() {
        binding.apply {
            btnLogin.setOnClickListener {
                curEmail = etLoginEmail.text.toString().trim()
                curPassword = etLoginPassword.text.toString().trim()
                viewModel.login(
                        etLoginEmail.text.toString().trim(),
                        etLoginPassword.text.toString().trim()
                )
            }
        }
    }

    private fun register() {
        binding.apply {
            btnRegister.setOnClickListener {
                viewModel.register(
                        etRegisterEmail.text.toString().trim(),
                        etRegisterPassword.text.toString().trim(),
                        etRegisterPasswordConfirm.text.toString().trim()
                )
            }
        }
    }
}