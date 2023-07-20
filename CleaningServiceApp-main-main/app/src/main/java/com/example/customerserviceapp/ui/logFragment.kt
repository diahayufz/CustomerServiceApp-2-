package com.example.customerserviceapp.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.customerserviceapp.R

class logFragment : Fragment() {
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_log, container, false)

        usernameEditText = view.findViewById(R.id.usernameEditText)
        passwordEditText = view.findViewById(R.id.passwordEditText)
        loginButton = view.findViewById(R.id.loginButton)

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Perform login authentication
            val isAuthenticated = authenticateUser(username, password)

            if (isAuthenticated) {
                // Login success, perform necessary actions
                Toast.makeText(activity, "login Successful", Toast.LENGTH_SHORT).show()
                val navController = Navigation.findNavController(
                    requireActivity(),
                    R.id.nav_host_fragment_content_main
                )
                navController.navigate(R.id.action_logFragment_to_HomeFragment)
            } else {
                // Login failed, show error message
                Toast.makeText(activity, "Invalid credentials", Toast.LENGTH_SHORT).show()
            }
        }
        return view
    }

    private fun authenticateUser(username: String, password: String): Boolean {
        // Implement your authentication logic here
        // This is just a sample implementation
        return username == "syafrizul" && password == "151201"
    }
}