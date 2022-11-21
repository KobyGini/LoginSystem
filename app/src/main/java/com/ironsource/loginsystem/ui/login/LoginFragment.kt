package com.ironsource.loginsystem.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ironsource.loginsystem.databinding.FragmentLoginBinding
import com.ironsource.loginsystem.util.ext.collectLatestSafeLifecycle
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    lateinit var loginViewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        setListeners()
        collectLoginStateFlow()
    }

    private fun collectLoginStateFlow() {

        collectLatestSafeLifecycle(
            loginViewModel.loginSharedFlow
        ) {
            when (it) {
                is LoginViewModel.LoginShardFlow.Error -> {
                    Toast.makeText(requireContext(), it.massage, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setListeners() {
        binding.apply {
            loginButton.setOnClickListener {
                binding.loginProgressBar.visibility = View.VISIBLE
                val email = binding.emailEditText.text.toString()
                val password = binding.passwordEditText.text.toString()

                loginViewModel.login(
                    email,
                    password
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}