package com.overdevx.mystoryapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.overdevx.mystoryapp.auth.LoginActivity
import com.overdevx.mystoryapp.data.MainViewModel
import com.overdevx.mystoryapp.data.MainViewModelFactory
import com.overdevx.mystoryapp.data.adapter.StoryAdapter
import com.overdevx.mystoryapp.databinding.FragmentHomeBinding
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var userViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
//        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { view, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            view.updatePadding(bottom = systemBars.bottom)
//            insets
//        }
        val mainViewModelFactory = MainViewModelFactory(requireContext())
        userViewModel = ViewModelProvider(this, mainViewModelFactory).get(MainViewModel::class.java)
        userViewModel.fetchStories(1, 10)
        observeData()
        observeLoading()
        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { v, actionId, event ->
                    searchBar.setText(searchView.text)
                    searchView.hide()
                    false
                }
        }

        binding.recyclerItem.layoutManager = LinearLayoutManager(requireContext())

        binding.textView.setOnClickListener {
            userViewModel.logout()
            startActivity(Intent(requireContext(),LoginActivity::class.java))
            requireActivity().finish()
        }
        return root
    }

    private fun observeData() {
        userViewModel.stories.observe(viewLifecycleOwner, Observer { data ->
            binding.recyclerItem.adapter = StoryAdapter(data)
        })
    }

    private fun observeLoading() {
        userViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressindicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}