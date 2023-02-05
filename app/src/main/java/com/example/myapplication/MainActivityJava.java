package com.example.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class MainActivityJava extends AppCompatActivity {

    private TextView tvMessage;
    private TextView tvLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;

    @SuppressLint("MissingPermission")
    ActivityResultLauncher<String[]> locationPermissionRequest =
            registerForActivityResult(new ActivityResultContracts
                            .RequestMultiplePermissions(), result -> {
                        Boolean fineLocationGranted = result.getOrDefault(
                                Manifest.permission.ACCESS_FINE_LOCATION, false);
                        Boolean coarseLocationGranted = result.getOrDefault(
                                Manifest.permission.ACCESS_COARSE_LOCATION, false);
                        if (fineLocationGranted != null && fineLocationGranted) {
                            // Precise location access granted.
                            tvMessage.setText(R.string.main_location_permission);
                            startListening();
                        } else if (coarseLocationGranted != null && coarseLocationGranted) {
                            // Only approximate location access granted.
                            tvMessage.setText(R.string.main_location_permission);
                            startListening();
                        } else {
                            // No location access granted.
                            tvMessage.setText(R.string.main_location_permission_denied);
                        }
                    }
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvMessage = findViewById(R.id.tv_message);
        tvLocation = findViewById(R.id.tv_location);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        tvMessage.setText("hola");

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            tvMessage.setText("Tienes el permiso");
            startListening();
        }{
            new AlertDialog.Builder(this).setTitle("Alerta").setMessage("Aceptas el permiso").setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(MainActivityJava.this, "No obtuviste el permiso", Toast.LENGTH_LONG).show();
                }
            }).setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    locationPermissionRequest.launch(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION});

                }
            }).create().show();
        }


//        startListening();

    }

    private void startListening() {
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null){
                    String exactitude = String.valueOf(location.getAccuracy());
                    String latitude = String.valueOf(location.getLongitude());
                    String longitude = String.valueOf(location.getLatitude());
                    Log.d("localizacion", exactitude);
                    Log.d("localizacion", latitude);
                    Log.d("localizacion", longitude);
                    tvLocation.setText(exactitude + " " + latitude + ", "+ longitude );


                }

            }
        });


    }

}