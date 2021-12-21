package com.blesspearl.jobz4me.Fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.blesspearl.jobz4me.R
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class Location_Fragment : Fragment() {
    private lateinit var supportMapFragment: SupportMapFragment
    private lateinit var client: FusedLocationProviderClient
    private var googleApiClient: GoogleApiClient? = null
    val REQUEST_LOCATION = 199
    private val args: Location_FragmentArgs by navArgs()
    private lateinit var lat: String
    private lateinit var lon: String
    private lateinit var location: Location
    private lateinit var linLayout: LinearLayout
    private lateinit var disInfo: TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_location, container, false)
        supportMapFragment =
            childFragmentManager.findFragmentById(R.id.google_maps) as SupportMapFragment
        client = LocationServices.getFusedLocationProviderClient(requireActivity())
        linLayout = view.findViewById(R.id.lin_layout)
        disInfo = view.findViewById(R.id.distanceMessage)

        val manager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(requireActivity())) {
            if (ActivityCompat
                    .checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
            ) {
                getCurrentLocation()
            } else {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    44
                )
            }
        }
        if (!hasGPSDevice(requireActivity())) {
            Log.d("Map_Fragment", "Device cannot support Location tracking ")
        }
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(requireActivity())) {
            Log.e("Map_Fragment", "Gps already not enabled")
            enableLoc()
        } else {
            Log.e("Map_Fragment", "Gps already enabled")
        }

        val location1 = args.location
        val split = location1.split(",")
        lat = split[0]
        lon = split[1]
        location =
            Location(LocationManager.NETWORK_PROVIDER) // OR GPS_PROVIDER based on the requirement
        location.latitude = lat.toDouble()
        location.longitude = lon.toDouble()
        supportMapFragment.getMapAsync { googleMap ->
            val latLng = LatLng(location.latitude, location.longitude)
            val option = MarkerOptions().position(latLng)
                .title(args.commonName)
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5F))
            googleMap.addMarker(option)
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
                    } catch (e: IntentSender.SendIntentException) {
                    }
                }
            }
        }
    }

    @SuppressLint("MissingPermission", "SetTextI18n")
    private fun getCurrentLocation() {
        val task = this.client.lastLocation
        task.addOnSuccessListener {
            supportMapFragment.getMapAsync {
                val task = this.client.lastLocation
                task.addOnSuccessListener { loc ->
                    supportMapFragment.getMapAsync { googleMap ->
                        val latLng = LatLng(loc.latitude, loc.longitude)
                        val option = MarkerOptions().position(latLng)
                            .title("Here You are")
                        linLayout.visibility = VISIBLE
                        disInfo.text =
                            "You are ${(loc.distanceTo(location)) / 1000}Km  to the Job destination  "
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5F))
                        googleMap.addMarker(option)
                    }
                }
            }
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