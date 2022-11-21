package com.ironsource.loginsystem.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ironsource.loginsystem.R
import com.ironsource.loginsystem.auth.UserAuthState
import com.ironsource.loginsystem.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var mainViewModel: MainViewModel

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        binding.navBottomBar.apply {
            setupWithNavController(findNavController(R.id.fragmentNavHost))
        }

        //Manage auth state
        lifecycleScope.launch {

            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.getAuthStateFlow().collectLatest { authenticatedState ->
                    when (authenticatedState) {
                        UserAuthState.Authenticated -> {
                            binding.navBottomBar.visibility = VISIBLE
                            this@MainActivity.findNavController(R.id.fragmentNavHost)
                                .navigate(R.id.action_global_homeFragment)
                        }

                        UserAuthState.UnAuthenticated -> {
                            binding.navBottomBar.visibility = GONE
                            this@MainActivity.findNavController(R.id.fragmentNavHost)
                                .navigate(R.id.action_global_loginFragment)
                        }
                    }
                }
            }
        }

        this.findNavController(R.id.fragmentNavHost)
            .addOnDestinationChangedListener { _, destination, _ ->
                supportActionBar?.title = destination.label
            }
    }
}