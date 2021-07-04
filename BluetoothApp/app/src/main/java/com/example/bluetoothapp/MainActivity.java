package com.example.bluetoothapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.security.Permission;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    ListView listView;
    TextView status;
    Button search;
    BluetoothAdapter bluetoothAdapter;
    ArrayList<String> names;
    ArrayAdapter adapter;


    public void clicksearch(View view){
        names.clear();
        adapter.notifyDataSetChanged();
        bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        status.setText("Searching...");
        search.setEnabled(false);
        bluetoothAdapter.startDiscovery();

    }
    private final BroadcastReceiver receiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            Log.i("actionnnn",action);
            if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
                status.setText("Finished");
                search.setEnabled(true);
            }else if(BluetoothDevice.ACTION_FOUND.equals(action)){
                BluetoothDevice device= intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String name=device.getName();
                String address=device.getAddress();
                String rssi=Integer.toString(intent.getShortExtra(BluetoothDevice.EXTRA_RSSI,Short.MIN_VALUE));
                names.add("Name:"+name +"  address:"+address);
                adapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView= findViewById(R.id.listview);
        status= findViewById(R.id.textView);
        search= findViewById(R.id.button);
        bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        names=new ArrayList<String>();

        adapter=new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1,names);
        listView.setAdapter(adapter);

        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        registerReceiver(receiver,intentFilter);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001);

    }
}