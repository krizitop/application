package com.example.myapplication

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.icu.text.CaseMap.Title
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices

fun MainActivity.initElements() {

    fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

    binding?.tvLocation?.setOnClickListener{
        showAlert(successClicked = {
            locationPermissionRequest.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
        }, negativeClicked = {
            Toast.makeText(this, "No obtuviste el permiso", Toast.LENGTH_LONG).show()
        })

    }

}

fun MainActivity.checkPermission() {
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
        binding?.tvMessage?.text = "Tienes el permiso"
        startListening()
    }
    run {
      showAlert(successClicked = {
          locationPermissionRequest.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
      }, negativeClicked = {
          Toast.makeText(this, "No obtuviste el permiso", Toast.LENGTH_LONG).show()
      })
    }

}

@SuppressLint("MissingPermission")
fun MainActivity.startListening() {
    fusedLocationProviderClient?.lastLocation?.addOnSuccessListener(this) { location ->
        if (location != null) {
            val exactitude = location.accuracy.toString()
            val latitude = location.longitude.toString()
            val longitude = location.latitude.toString()
            Log.d("localizacion", exactitude)
            Log.d("localizacion", latitude)
            Log.d("localizacion", longitude)
            binding?.tvLocation?.text = "$exactitude $latitude, $longitude"
        }
    }
}
fun MainActivity.showAlert(title: String="Alerta", okButton: String="Aceptar", negativeButton: String="No",successClicked:()->Unit,negativeClicked:()->Unit){

    AlertDialog.Builder(this).setTitle(title).setMessage("Aceptas el permiso")
            .setNegativeButton(negativeButton) { dialog, which ->
       negativeClicked()
    }.setPositiveButton(okButton) { dialog, which ->
                successClicked()
    }.create().show()

}
