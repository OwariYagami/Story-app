package com.overdevx.mystoryapp.data.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.overdevx.mystoryapp.R
import com.overdevx.mystoryapp.databinding.ProfileBottomSheetLayoutBinding

class ProfileModalBottomSheet() :
    BottomSheetDialogFragment() {
    lateinit var binding: ProfileBottomSheetLayoutBinding
    var profileOptionListener: ProfileOptionListener? = null
    interface ProfileOptionListener {
        fun onProfileSelected()
        fun onLogoutSelected()
        fun onLanguageSelected()
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
        binding.cvLang.setOnClickListener{
            profileOptionListener?.onLanguageSelected()
            dismiss()
        }
        binding.cvLogout.setOnClickListener {
            profileOptionListener?.onLogoutSelected()
            dismiss()
        }
        return binding.root

    }

}