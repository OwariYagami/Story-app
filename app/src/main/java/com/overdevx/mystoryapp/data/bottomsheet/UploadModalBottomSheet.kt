package com.overdevx.mystoryapp.data.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.overdevx.mystoryapp.R
import com.overdevx.mystoryapp.databinding.ChooseBottomSheetLayoutBinding

class UploadModalBottomSheet() :
    BottomSheetDialogFragment() {
    lateinit var binding: ChooseBottomSheetLayoutBinding
    var uploadOptionListener: UploadOptionListener? = null
    interface UploadOptionListener {
        fun onCameraSelected()
        fun onGallerySelected()
    }

    companion object {
        fun newInstance(): UploadModalBottomSheet {
            return UploadModalBottomSheet()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ChooseBottomSheetLayoutBinding.bind(
            inflater.inflate(
                R.layout.choose_bottom_sheet_layout,
                container
            )
        )

        binding.materialCardView.setOnClickListener {
            uploadOptionListener?.onCameraSelected()
            dismiss()
        }
        binding.materialCardView2.setOnClickListener {
            uploadOptionListener?.onGallerySelected()
            dismiss()
        }
        return binding.root

    }

}