package com.example.customerserviceapp.ui

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PackageManagerCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.customerserviceapp.R
import com.example.customerserviceapp.aplication.CleaningsApp
import com.example.customerserviceapp.databinding.FragmentSecondBinding
import com.example.customerserviceapp.model.CleaningS
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlin.reflect.typeOf

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment(),OnMapReadyCallback, GoogleMap.OnMarkerDragListener {

    private var _binding: FragmentSecondBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var applicationContext: Context
    private val cleaningsViewModel: CleaningsViewModel by viewModels {
        CleaningsViewModelFactory((applicationContext as CleaningsApp).repository)
    }
    private val args : SecondFragmentArgs by navArgs()
    private var cleanings : CleaningS? = null
    private lateinit var mMaps: GoogleMap
    private var currentLatLang: LatLng? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onAttach(context: Context) {
        super.onAttach(context)
        applicationContext = requireContext().applicationContext
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cleanings = args.cleanings
        // kita cek jika null maka tampilan default nambah cleaning
        // jika cleanings tidak null tampilan sedikit berubah ada tombol hapus dan cleaning
        if (cleanings !=null){
            binding.deleteButton.visibility = View.VISIBLE
            binding.saveButton.text = "Ubah"
            binding.nameEditText.setText(cleanings?.name)
            binding.addressEditTextText2.setText(cleanings?.address)
            binding.typeEditText.setText(cleanings?.type)
        }

        //binding google maps
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        checkPermission()

        val name= binding.nameEditText.text
        val address= binding.addressEditTextText2.text
        val type = binding.nameEditText.text
        binding.saveButton.setOnClickListener {
             //kita kasih kondisi jika nama dan alamat kosong tidak bisa menyimpan
            if (name.isEmpty()) {
                Toast.makeText(context, "Nama tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }else if (address.isEmpty()) {
                Toast.makeText(context, "Alamat tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }else if (type.isEmpty()) {
                Toast.makeText(context, "Jenis Pelayanan tidak boleh kosong", Toast.LENGTH_SHORT).show()
            } else {
                // jika udah berhasil run
                // kita masukan data latutude dan longitude sebenarnya
                if ( cleanings == null){
                    val cleanings  = CleaningS(0, name.toString(), address.toString(), type.toString(), currentLatLang?.latitude, currentLatLang?.longitude)
                    cleaningsViewModel.insert(cleanings)
                }else {
                    val cleanings = CleaningS(cleanings?.id!!, name.toString(), address.toString(), type.toString(), currentLatLang?.latitude, currentLatLang?.longitude)
                    cleaningsViewModel.update(cleanings)
                }

                findNavController().popBackStack()
            }
        }

        binding.deleteButton.setOnClickListener {
            cleanings?.let { cleaningsViewModel.delete(it) }
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMapReady(googleMap : GoogleMap) {
        mMaps = googleMap
        // implement drag marker
        mMaps.setOnMarkerDragListener(this)

        val uiSettings = mMaps.uiSettings
        uiSettings.isZoomControlsEnabled = true
        mMaps.setOnMarkerDragListener(this)
    }

    override fun onMarkerDrag(p0: Marker) {
    }

    override fun onMarkerDragEnd(marker: Marker) {
        val newPosition = marker.position
        currentLatLang =  LatLng(newPosition.latitude, newPosition.longitude)
        Toast.makeText(context, currentLatLang.toString(), Toast.LENGTH_SHORT).show()
    }

    override fun onMarkerDragStart(p0: Marker) {
    }

    private fun checkPermission(){
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(applicationContext)
        if (ContextCompat.checkSelfPermission(
            applicationContext,
            android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            getCurrentLocation()
        }else{
            Toast.makeText(applicationContext, "Akses Lokasi ditolak", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getCurrentLocation (){
        // ngecek jika permission tidak disetujui maka akan berhenti di kondisi if
        if (ContextCompat.checkSelfPermission(
                applicationContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        // untuk test current location coba run didevice langsung atau build apknya terus instal dihp masin-masing
    fusedLocationClient.lastLocation
        .addOnSuccessListener { location ->
            if (location != null){
                var latLng = LatLng(location.latitude, location.longitude)
                currentLatLang = latLng
                var title = "Marker"
                // menampilkan lokasi sesuai koordinat yang sudah disimpan atau diupdate tadi

                // salah mengecek null, seharusnya cleanings yang di cek null atau tidak
                if (cleanings != null){
                    title=cleanings?.name.toString()
                    val newCurrentLocation = LatLng(cleanings?.latitude!!, cleanings?.longitude!!)
                    latLng = newCurrentLocation
                }
                val markerOptions = MarkerOptions()
                    .position(latLng)
                    .title(title)
                    .draggable(true)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.clean))

                mMaps.addMarker(markerOptions)
                mMaps.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15f))
            }
        }
    }
}