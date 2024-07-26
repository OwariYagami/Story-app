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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.overdevx.mystoryapp.R
import com.overdevx.mystoryapp.auth.LoginActivity
import com.overdevx.mystoryapp.data.adapter.StoryAdapter
import com.overdevx.mystoryapp.data.bottomsheet.ProfileModalBottomSheet
import com.overdevx.mystoryapp.databinding.FragmentHomeBinding

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
        mainViewModel = ViewModelProvider(this, mainViewModelFactory).get(MainViewModel::class.java)
        mainViewModel.getName()
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

        binding.appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (Math.abs(verticalOffset) - appBarLayout.totalScrollRange == 0) {
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
        })
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            requireActivity().finish() // Close the activity, effectively exiting the app
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
        binding.recyclerItem.adapter = adapter
        mainViewModel.stories.observe(viewLifecycleOwner) { data ->
            adapter.submitData(lifecycle,data)
//            if (data.) {
//                binding.emptyLayout.root.visibility = View.VISIBLE
//            }
        }

        mainViewModel.userName.observe(viewLifecycleOwner, Observer { data ->
            binding.toolbar.toolbarUsername.text = data.toString()
        })
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