package com.overdevx.mystoryapp.ui.dashboard

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.overdevx.mystoryapp.R
import com.overdevx.mystoryapp.data.bottomsheet.UploadModalBottomSheet
import com.overdevx.mystoryapp.data.utils.reduceFileImage
import com.overdevx.mystoryapp.data.utils.uriToFile
import com.overdevx.mystoryapp.databinding.FragmentDashboardBinding
import com.overdevx.mystoryapp.ui.dashboard.CameraActivity.Companion.CAMERAX_RESULT
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private var currentImageUri: Uri? = null
    private val binding get() = _binding!!
    private lateinit var dashboardViewModel: DashboardViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private  var latitude:Double?=null
    private  var longitude:Double?=null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewModelFactory = DashboardViewModelFactory(requireContext())
        dashboardViewModel =
            ViewModelProvider(this, viewModelFactory).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        observeUpload()


        validateInputs()
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeUpload() {
        dashboardViewModel.uploadResult.observe(viewLifecycleOwner) { response ->
            if (response != null) {
                if (response.error == false) {
                    showSuccessDialog()
                } else {
                    Toast.makeText(requireContext(), "Upload Failed :${response.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun uploadImage() {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, requireContext()).reduceFileImage()
            Log.d("Image File", "showImage: ${imageFile.path}")
            val description = binding.etDesc.text.toString()

            val desc = description.toRequestBody("text/plain".toMediaType())
            val lat: RequestBody? = latitude?.toFloat()?.toString()?.toRequestBody("text/plain".toMediaType())
            val lon: RequestBody? = longitude?.toFloat()?.toString()?.toRequestBody("text/plain".toMediaType())
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())

            val multipartBody = MultipartBody.Part.createFormData(
                "photo",
                imageFile.name,
                requestImageFile
            )
            dashboardViewModel.uploadImage(multipartBody, desc,lat,lon)
        } ?: run {
            Toast.makeText(requireContext(), "Please select an image first", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun validateInputs() {
        val isDescriptionFilled = binding.etDesc.text.toString().isNotEmpty()
        val isImageSelected = currentImageUri != null
        binding.btnUpload.isEnabled = isDescriptionFilled && isImageSelected
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressindicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun observeLoading() {
        dashboardViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
    }
    private fun showSuccessDialog() {

        val dialogView: View =
            LayoutInflater.from(requireContext()).inflate(R.layout.success_dialog_layout, null)


        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setView(dialogView)
            .setCancelable(false)
            .create()
        val submitButton: Button = dialogView.findViewById(R.id.btn_oke)
        val desc: TextView = dialogView.findViewById(R.id.tv_desc)
        desc.text = getString(R.string.your_story_has_been_successfully_uploaded)
        submitButton.setOnClickListener {
            binding.etDesc.setText("")
            binding.ivPlaceholder.setImageResource(R.drawable.ic_add_image)
            dashboardViewModel.uploadResult.value = null
            binding.switchLocation.isChecked=false
            dialog.dismiss()
            navigateToHome()

        }
        dialog.show()

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLoading()

        binding.cardChoose.setOnClickListener {
            val uploadModalBottomSheet = UploadModalBottomSheet.newInstance()
            uploadModalBottomSheet.uploadOptionListener =
                object : UploadModalBottomSheet.UploadOptionListener {
                    override fun onCameraSelected() {
                        startCameraX()
                    }

                    override fun onGallerySelected() {
                        startGallery()
                    }
                }
            uploadModalBottomSheet.show(childFragmentManager, "UploadModalBottomSheet")
        }

        binding.etDesc.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validateInputs()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        binding.btnUpload.setOnClickListener {
            uploadImage()
        }

        binding.switchLocation.setOnCheckedChangeListener{buttonView,isChecked ->
            if(isChecked){
                getMyLocation()
            }
        }

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

            Glide.with(requireContext())
                .load(currentImageUri)
                .into(binding.ivPlaceholder)
            validateInputs()
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    override fun onResume() {
        super.onResume()
    }

    private fun navigateToHome() {
        findNavController().navigate(R.id.action_dashboardFragment_to_homeFragment)
    }
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            } else {
                // Handle permission denial
            }
        }
    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                requireActivity().applicationContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {

            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    location?.let {
                         latitude = it.latitude
                         longitude = it.longitude
                        Log.d("LOCATION","Lat :$latitude | Lon:$longitude")
                    }
                }
        } else {
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }
}