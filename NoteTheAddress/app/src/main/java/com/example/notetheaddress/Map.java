package com.example.notetheaddress;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Map extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    GoogleMap mMap;
    Marker myMarker ;
    LocationManager locationManager;
    Double longi;

    Double lati;
    SharedPreferences sharedPreferences;
    LatLng you;
    LocationListener locationListener;
    Intent intent;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,000,1000,locationListener);
            }
        }
    }
    public void locationmarker(Location location,String title){
        LatLng userlocation=new LatLng(location.getLatitude(),location.getLongitude());
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(userlocation).title(title));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userlocation,14));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);




        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLongClickListener(this);
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        intent=getIntent();
        if(intent.getIntExtra("position",0)==0 ){

            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    locationmarker(location, "You");
                }
            };
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 00, 1000, locationListener);


            }
        }else{
            Location placedlocation=new Location(LocationManager.GPS_PROVIDER);
            placedlocation.setLatitude(MainActivity.latlngofplaces.get(intent.getIntExtra("position",0)).latitude);
            placedlocation.setLongitude(MainActivity.latlngofplaces.get(intent.getIntExtra("position",0)).longitude);
            locationmarker(placedlocation,MainActivity.places.get(intent.getIntExtra("position",0)));
        }

    }

    @Override
    public void onMapLongClick(LatLng latLng) {

        String address="";

        Geocoder geocoder=new Geocoder(getApplicationContext(), Locale.getDefault());

        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);
            if (addresses!=null && addresses.size()>0){
                if(addresses.get(0).getThoroughfare()!=null){
                    address+=addresses.get(0).getThoroughfare()+" ";
                }
                if(addresses.get(0).getLocality()!=null){
                    address+=addresses.get(0).getLocality()+" ";
                }
                if(addresses.get(0).getSubAdminArea()!=null){
                    address+=addresses.get(0).getSubAdminArea()+"\n";
                }
                if (address.equals("")){
                    SimpleDateFormat sdf=new SimpleDateFormat("HH:mm MM yyyy");
                    address+=sdf.format(new Date());
                }
                MainActivity.places.add(address);
                MainActivity.latlngofplaces.add(latLng);

                
                MainActivity.adapter.notifyDataSetChanged();



                SharedPreferences sharedPreferences=this.getSharedPreferences("com.example.notetheaddress",Context.MODE_PRIVATE);
                ArrayList<String> lats=new ArrayList<>();
                ArrayList<String> longs=new ArrayList<>();
                for (LatLng coords:MainActivity.latlngofplaces){
                    lats.add(Double.toString(coords.latitude));
                    longs.add(Double.toString(coords.longitude));
                }
                sharedPreferences.edit().putString("places",Serializeit.serialize(MainActivity.places)).apply();
                sharedPreferences.edit().putString("lats",Serializeit.serialize(lats)).apply();
                sharedPreferences.edit().putString("longs",Serializeit.serialize(longs)).apply();


                Toast.makeText(getApplicationContext(),"Place Saved",Toast.LENGTH_SHORT).show();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }




        mMap.addMarker(new MarkerOptions().position(latLng).title(address));
    }
}