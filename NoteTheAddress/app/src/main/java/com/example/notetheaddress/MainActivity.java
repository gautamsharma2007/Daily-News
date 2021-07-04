package com.example.notetheaddress;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;

    static  ArrayList<String> places=new ArrayList<String>();
    static ArrayList<LatLng> latlngofplaces=new ArrayList<LatLng>();
    ArrayList<String> lats=new ArrayList<>();
    ArrayList<String> longs=new ArrayList<>();
    static ArrayAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        places.clear();
        lats.clear();
        longs.clear();
        latlngofplaces.clear();


        try {
            SharedPreferences sharedPreferences=this.getSharedPreferences("com.example.notetheaddress",Context.MODE_PRIVATE);
            places=(ArrayList<String>)Serializeit.deserialize(sharedPreferences.getString("places",Serializeit.serialize(new ArrayList<String>())));
            longs=(ArrayList<String>)Serializeit.deserialize(sharedPreferences.getString("longs",Serializeit.serialize(new ArrayList<String>())));
            lats=(ArrayList<String>)Serializeit.deserialize(sharedPreferences.getString("lats",Serializeit.serialize(new ArrayList<String>())));

            int i=0;




        }catch (Exception e){
            e.printStackTrace();
        }
        int i;
        if(places.size()>0 && lats.size()>0 && longs.size()>0){
            Log.i("half","done");
            if (places.size()==lats.size() && places.size()==longs.size()){
                Log.i("quarter","left");
                for (i=0;i<lats.size();i++){
                    latlngofplaces.add(new LatLng(Double.parseDouble(lats.get(i)),Double.parseDouble(longs.get(i))));
                    Log.i("Done",latlngofplaces.toString());
                }
            }
        }else {
            places.add("Add a place");
            latlngofplaces.add(new LatLng(0,0));
        }



        listView=(ListView) findViewById(R.id.listviewi);
        adapter=new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1,places);
        listView.setAdapter(adapter);
        listView.setBackgroundColor(Color.WHITE);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Intent intent = new Intent(getApplicationContext(),Map.class);
                    intent.putExtra("position",position);


                    startActivity(intent);

            }
        });

    }
}