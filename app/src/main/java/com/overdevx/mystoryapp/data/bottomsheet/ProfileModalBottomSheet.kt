package com.overdevx.mystoryapp.data.bottomsheet

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.overdevx.mystoryapp.R
import com.overdevx.mystoryapp.databinding.ChooseBottomSheetLayoutBinding
import com.overdevx.mystoryapp.databinding.ProfileBottomSheetLayoutBinding
import com.overdevx.mystoryapp.ui.dashboard.CameraActivity
import java.io.File

class ProfileModalBottomSheet() :
    BottomSheetDialogFragment() {
    lateinit var binding: ProfileBottomSheetLayoutBinding
    var profileOptionListener: ProfileOptionListener? = null
    interface ProfileOptionListener {
        fun onProfileSelected()
        fun onLogoutSelected()
    }

    companion object {
        fun newInstance(): ProfileModalBottomSheet {
            return ProfileModalBottomSheet()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ProfileBottomSheetLayoutBinding.bind(
            inflater.inflate(
                R.layout.profile_bottom_sheet_layout,
                container
            )
        )

        binding.cvProfile.setOnClickListener {
            profileOptionListener?.onProfileSelected()
            dismiss()
        }
        binding.cvLogout.setOnClickListener {
            profileOptionListener?.onLogoutSelected()
            dismiss()
        }
        return binding.root

    }

}