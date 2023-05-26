package com.example.wheeloffortune

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.wheeloffortune.databinding.FragmentMainMenuBinding

class MainMenuFragment: Fragment(R.layout.fragment_main_menu) {

    private lateinit var binding: FragmentMainMenuBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentMainMenuBinding.bind(requireView())

        binding.btnPlay.setOnClickListener {
            findNavController().navigate(R.id.action_mainMenuFragment_to_gameFragment)
        }

        binding.btnWords.setOnClickListener {
            findNavController().navigate(R.id.action_mainMenuFragment_to_wordsFragment)
        }
    }
}