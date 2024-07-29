package com.overdevx.mystoryapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.overdevx.mystoryapp.R
import com.overdevx.mystoryapp.auth.LoginActivity
import com.overdevx.mystoryapp.data.adapter.LoadingStateAdapter
import com.overdevx.mystoryapp.data.adapter.StoryAdapter
import com.overdevx.mystoryapp.data.bottomsheet.ProfileModalBottomSheet
import com.overdevx.mystoryapp.databinding.FragmentHomeBinding
import kotlin.math.abs

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val mainViewModelFactory = MainViewModelFactory(requireContext())
        mainViewModel = ViewModelProvider(this, mainViewModelFactory)[MainViewModel::class.java]
        mainViewModel.getName()
        observeData()
        observeLoading()
        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { _, _, _ ->
                    searchBar.setText(searchView.text)
                    searchView.hide()
                    false
                }
        }

        binding.recyclerItem.layoutManager = LinearLayoutManager(requireContext())

        binding.ivPp.setOnClickListener {
            val profilModalBottomSheet = ProfileModalBottomSheet.newInstance()
            profilModalBottomSheet.profileOptionListener =
                object : ProfileModalBottomSheet.ProfileOptionListener {
                    override fun onProfileSelected() {
                        Toast.makeText(requireContext(), "Profile clicked", Toast.LENGTH_SHORT)
                            .show()
                    }

                    override fun onLogoutSelected() {
                        showLogoutDialog()
                    }

                    override fun onLanguageSelected() {
                        setupAction()
                    }

                }
            profilModalBottomSheet.show(childFragmentManager, "UploadModalBottomSheet")

        }

        binding.appbar.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (abs(verticalOffset) - appBarLayout.totalScrollRange == 0) {
                // Ketika collapsed
                binding.toolbar.toolbarTitle.visibility = View.VISIBLE
                binding.toolbar.toolbarUsername.visibility = View.VISIBLE
                binding.toolbar.view.visibility = View.VISIBLE
            } else {
                // Ketika expanded
                binding.toolbar.toolbarTitle.visibility = View.GONE
                binding.toolbar.toolbarUsername.visibility = View.GONE
                binding.toolbar.view.visibility = View.GONE
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            requireActivity().finish()
        }
        return root
    }

    private fun showLogoutDialog() {
        val dialogView: View =
            LayoutInflater.from(requireContext()).inflate(R.layout.logout_dialog_layout, null)

        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setView(dialogView)
            .setCancelable(false)
            .create()
        val yes: Button = dialogView.findViewById(R.id.btn_yes)
        val no: Button = dialogView.findViewById(R.id.btn_no)
        yes.setOnClickListener {
            mainViewModel.logout()
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            dialog.dismiss()
            requireActivity().finish()
        }
        no.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun observeData() {
        val adapter = StoryAdapter()
        binding.recyclerItem.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        mainViewModel.stories.observe(viewLifecycleOwner) { data ->
            adapter.submitData(lifecycle, data)
            adapter.addLoadStateListener { loadState ->
                val isEmpty = loadState.source.refresh is LoadState.NotLoading && adapter.itemCount == 0
                binding.emptyLayout.root.visibility = if (isEmpty) View.VISIBLE else View.GONE
            }
        }

        mainViewModel.userName.observe(viewLifecycleOwner) { data ->
            binding.toolbar.toolbarUsername.text = data.toString()
        }
    }

    private fun observeLoading() {
        mainViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressindicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setupAction() {
        startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}