package com.example.instantaddress;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;
    TextView textView1;
    TextView textView2;
    TextView textView3;
    TextView textView4;
    TextView textView5;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,1000,locationListener);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView1=(TextView) findViewById(R.id.textView2);
        textView2=(TextView) findViewById(R.id.textView3);
        textView3=(TextView) findViewById(R.id.textView4);
        textView4=(TextView) findViewById(R.id.textView5);
        textView5=(TextView) findViewById(R.id.textView6);
        locationManager=(LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        locationListener=new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {



                Geocoder geocoder=new Geocoder(getApplicationContext(), Locale.getDefault());

                try {
                    List<Address> list=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),3);
                    String address="";
                    if(list.size()>0 && list!=null){
                        if(list.get(0).hasLatitude()==true){
                            textView1.setText("Latitude : "+Double.toString(list.get(0).getLatitude()));
                        }
                        if(list.get(0).hasLongitude()==true){
                            textView2.setText("Longitude : "+Double.toString(list.get(0).getLongitude()));
                        }
                        if(location.hasAccuracy()==true){
                            textView3.setText("Accuracy : "+Double.toString(location.getAccuracy()));
                        }
                        if(location.hasAltitude()==true){
                            textView4.setText("Latitude : "+Double.toString(location.getAltitude()));
                        }
                        if(list.get(0).getThoroughfare()!=null){
                            address+=list.get(0).getThoroughfare()+" ";
                        }
                        if(list.get(0).getLocality()!=null){
                            address+=list.get(0).getLocality()+" ";
                        }
                        if(list.get(0).getSubAdminArea()!=null){
                            address+=list.get(0).getSubAdminArea()+" ";
                        }
                        if(list.get(0).getAdminArea()!=null){
                            address+=list.get(0).getAdminArea()+" ";
                        }
                        if(list.get(0).getPostalCode()!=null){
                            address+=list.get(0).getPostalCode()+" ";
                        }
                        textView5.setText("Address :\n"+address);
                    }
                }catch (Exception e){
                    Log.i("Sorry","error");
                    e.printStackTrace();
                }

            }
        };
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
    }
}