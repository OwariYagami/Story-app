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
import com.overdevx.mystoryapp.ui.dashboard.CameraActivity
import java.io.File

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

        binding.cvTake.setOnClickListener {
            uploadOptionListener?.onCameraSelected()
            dismiss()
        }
        binding.cvGal.setOnClickListener {
            uploadOptionListener?.onGallerySelected()
            dismiss()
        }

        return binding.root

    }

}