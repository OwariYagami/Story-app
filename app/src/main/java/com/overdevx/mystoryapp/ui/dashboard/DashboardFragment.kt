package com.overdevx.mystoryapp.ui.dashboard

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.overdevx.mystoryapp.data.bottomsheet.UploadModalBottomSheet
import com.overdevx.mystoryapp.data.getImageUri
import com.overdevx.mystoryapp.databinding.FragmentDashboardBinding
import com.overdevx.mystoryapp.ui.dashboard.CameraActivity.Companion.CAMERAX_RESULT
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class DashboardFragment : Fragment(), UploadModalBottomSheet.UploadDialogListener  {

    private var _binding: FragmentDashboardBinding? = null
    var uriImage: MultipartBody.Part? = null
    private var currentImageUri: Uri? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    lateinit var dialog: UploadModalBottomSheet
    lateinit var listener2: UploadModalBottomSheet.UploadDialogListener
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.cardChoose.setOnClickListener {
//            dialog = UploadModalBottomSheet(listener2)
//            dialog.show(
//                requireActivity().supportFragmentManager,
//                UploadModalBottomSheet.TAG
//            )
            startCameraX()

        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onImageSelected(imageUri: File) {
        Glide.with(requireContext())
            .load(imageUri)
            .into(binding.ivPlaceholder)
        val requestFile: RequestBody =
            RequestBody.create("image/*".toMediaTypeOrNull(), imageUri)

        val imagePart: MultipartBody.Part =
            MultipartBody.Part.createFormData("photo", imageUri.name, requestFile)

        uriImage = imagePart
        dialog.dismiss()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listener2 = this
    }

    private fun startCameraX() {
        val intent = Intent(requireContext(), CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }
    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERAX_RESULT) {
            currentImageUri = it.data?.getStringExtra(CameraActivity.EXTRA_CAMERAX_IMAGE)?.toUri()
            showImage()
        }
    }
    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.ivPlaceholder.setImageURI(it)
        }
    }
}