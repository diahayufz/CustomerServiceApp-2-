package com.example.customerserviceapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.customerserviceapp.R

class HomeFragment : Fragment() {
    private lateinit var titleTextView: TextView
    private lateinit var serviceImageView: ImageView
    private lateinit var descriptionTextView: TextView
    private lateinit var otherServicesTextView: TextView
    private lateinit var service1ImageView: ImageView
    private lateinit var service1TextView: TextView
    private lateinit var service2ImageView: ImageView
    private lateinit var service2TextView: TextView
    private lateinit var addOrderButton: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        titleTextView = view.findViewById(R.id.titleTextView)
        serviceImageView = view.findViewById(R.id.serviceImageView)
        descriptionTextView = view.findViewById(R.id.descriptionTextView)
        otherServicesTextView = view.findViewById(R.id.otherServicesTextView)
        service1ImageView = view.findViewById(R.id.service1ImageView)
        service1TextView = view.findViewById(R.id.service1TextView)
        service2ImageView = view.findViewById(R.id.service2ImageView)
        service2TextView = view.findViewById(R.id.service2TextView)
        addOrderButton = view.findViewById(R.id.addOrderButton)

        addOrderButton.setOnClickListener {
            addOrder()
        }

        return view
    }

    private fun addOrder() {
        // TODO: Implement logic to add order
        Toast.makeText(activity, "Add Order clicked!", Toast.LENGTH_SHORT).show()
        val navController = Navigation.findNavController(
            requireActivity(),
            R.id.nav_host_fragment_content_main
        )
        navController.navigate(R.id.action_HomeFragment_to_FirstFragment)
    }
}