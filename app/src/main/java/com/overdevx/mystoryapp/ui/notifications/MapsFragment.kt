package com.overdevx.mystoryapp.ui.notifications

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.overdevx.mystoryapp.R
import com.overdevx.mystoryapp.databinding.FragmentHomeBinding
import com.overdevx.mystoryapp.databinding.FragmentMapsBinding
import com.overdevx.mystoryapp.ui.home.MainViewModel
import com.overdevx.mystoryapp.ui.home.MainViewModelFactory

class MapsFragment : Fragment() {
    private lateinit var mMap: GoogleMap
    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!
    private lateinit var mapsFragmentViewModel: MapsFragmentViewModel
    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        mMap = googleMap
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        val mapsViewModelFactory = MapsFragmentViewModelFactory(requireContext())
        mapsFragmentViewModel =
            ViewModelProvider(this, mapsViewModelFactory).get(MapsFragmentViewModel::class.java)
        mapsFragmentViewModel.fetchStoriesWithLocation()
        observeData()

    }

    private fun observeData() {
        mapsFragmentViewModel.stories.observe(viewLifecycleOwner) { data ->
            data.forEach { data ->
                val latLng = data.lat?.let { data.lon?.let { it1 -> LatLng(it, it1) } }
                latLng?.let {
                    MarkerOptions()
                        .position(it)
                        .title(data.name)
                        .snippet(data.description)
                }?.let {
                    mMap.addMarker(
                        it
                    )
                }
            }
        }
    }

//    private fun showLoading(isLoading: Boolean) {
//        binding.progressindicator.isVisible = isLoading
//    }


}