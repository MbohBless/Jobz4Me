package com.blesspearl.jobz4me.Fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.IntentSender.SendIntentException
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.blesspearl.jobz4me.R
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class Map_Fragment : Fragment() {
    private lateinit var supportMapFragment: SupportMapFragment
    private lateinit var client: FusedLocationProviderClient
    private lateinit var searchText: EditText
    private lateinit var btnSearch: ImageButton
    private var location: String? = null
    private lateinit var confirmButton: ImageButton
    private var googleApiClient: GoogleApiClient? = null
    val REQUEST_LOCATION = 199
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_map_, container, false)
        supportMapFragment = childFragmentManager.findFragmentById(R.id.google_maps) as SupportMapFragment
        client = LocationServices.getFusedLocationProviderClient(requireActivity())
        val locateMe: ImageButton = view.findViewById(R.id.im_btn_location)
        confirmButton = view.findViewById(R.id.confirm_loc)

        searchText = view.findViewById(R.id.search_edt)
        btnSearch = view.findViewById(R.id.search)

        btnSearch.setOnClickListener { searchLocation() }

        if (location != null) confirmButton.visibility = VISIBLE
        confirmButton.setOnClickListener {
            val navHostFragment =
                activity?.supportFragmentManager?.findFragmentById(R.id.nav_host_frag) as NavHostFragment
            val navController = navHostFragment.navController
            val action = if (location != null) {
                Map_FragmentDirections.actionMapFragmentToFormFragment(location!!)
            }
            else { Map_FragmentDirections.actionMapFragmentToFormFragment() }

            navController.navigate(action)
        }
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            val navHostFragment =
                activity?.supportFragmentManager?.findFragmentById(R.id.nav_host_frag) as NavHostFragment
            val navController = navHostFragment.navController
            val action = if (location != null) {
                Map_FragmentDirections.actionMapFragmentToFormFragment(location!!)
            } else {
                Map_FragmentDirections.actionMapFragmentToFormFragment()
            }
            navController.navigate(action)
        }
        val manager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(requireActivity())) {
        }
        if (!hasGPSDevice(requireActivity())) {
            Log.d("Map_Fragment", "Device cannot support Location tracking ")
        }
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(requireActivity())) {
            Log.e("Map_Fragment", "Gps already not enabled")
            enableLoc()
        } else {
            Log.e("Map_Fragment", "Gps already enabled") }
        locateMe.setOnClickListener {
            if (ActivityCompat
                    .checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED){
                getCurrentLocation()
            }
            else{
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),44)
            }
        }
        return view
    }

    private fun hasGPSDevice(context: Context): Boolean {
        val mgr = context
            .getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val providers = mgr.allProviders
        return providers.contains(LocationManager.GPS_PROVIDER)
    }

    private fun enableLoc() {
        if (googleApiClient == null) {
            googleApiClient = GoogleApiClient.Builder(requireContext())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(object : GoogleApiClient.ConnectionCallbacks {
                    override fun onConnected(bundle: Bundle?) {}
                    override fun onConnectionSuspended(i: Int) {
                        googleApiClient!!.connect()
                    }
                })
                .addOnConnectionFailedListener { connectionResult ->
                    Log.d("Location error", "Location error " + connectionResult.errorCode)
                }.build()
            googleApiClient!!.connect()
            val locationRequest = LocationRequest.create()
            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            locationRequest.interval = 30 * 1000
            locationRequest.fastestInterval = 5 * 1000
            val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
            builder.setAlwaysShow(true)
            val result: PendingResult<LocationSettingsResult> =
                LocationServices.SettingsApi.checkLocationSettings(
                    googleApiClient!!,
                    builder.build()
                )
            result.setResultCallback { result ->
                val status: Status = result.status
                when (status.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                        status.startResolutionForResult(requireActivity(), REQUEST_LOCATION)
                    } catch (e: SendIntentException) {

                    }
                }
            }
        }
    }



    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
      val task =  this.client.lastLocation
        task.addOnSuccessListener {
            if (it!=null){
                supportMapFragment.getMapAsync{
                    location = null
                        var task = this.client.lastLocation
                        task.addOnSuccessListener { loc ->
                            supportMapFragment.getMapAsync { googleMap ->
                                var latLng = LatLng(loc.latitude, loc.longitude)
                                location = "${loc.latitude},${loc.longitude}"
                                val option = MarkerOptions().position(latLng)
                                    .title("Here You are")
                                confirmButton.visibility = VISIBLE
                                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10F))
                                googleMap.addMarker(option)
                            }
                        }
                }
            }
        }
    }

    private fun searchLocation() {
        location = null
        val locSearch = searchText.text.toString()
        var addressList: MutableList<Address>? = null
        if (locSearch.isNotBlank()) {
            searchText.text.clear()
            val geocoder: Geocoder = Geocoder(requireContext())
            try {
                addressList = geocoder.getFromLocationName(locSearch, 1)
                if (addressList.isNotEmpty()) {
                    for (i in 0 until addressList!!.size) {
                        val userAddress = addressList[i]
                        val latLng = LatLng(userAddress.latitude, userAddress.longitude)
                        location = "${userAddress.latitude},${userAddress.longitude}"
                        confirmButton.visibility = VISIBLE

                        val option = MarkerOptions().position(latLng)
                            .title(locSearch)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                        supportMapFragment.getMapAsync { googleMap ->

                            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
                            googleMap.animateCamera(CameraUpdateFactory.zoomTo(10f))
                            googleMap.addMarker(option)
                        }
                    }
                } else {
                    Toast.makeText(context, "Location Not found", Toast.LENGTH_SHORT).show()
                }
            } catch (e: java.lang.Exception) {
                print(e.stackTrace)
            }

        } else {
            Toast.makeText(context, "please enter a location to search", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 44) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation()
            }
        }
    }
}