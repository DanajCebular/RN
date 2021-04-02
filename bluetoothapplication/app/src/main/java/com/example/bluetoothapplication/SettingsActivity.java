package com.example.bluetoothapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.Console;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class SettingsActivity extends AppCompatActivity {

    BluetoothAdapter bAdapter = BluetoothAdapter.getDefaultAdapter();
    String address1;
    SharedPreferences myPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // SETS UP SHAREDPREFERENCES
        myPrefs = getSharedPreferences("prefID", Context.MODE_PRIVATE);

        String addressSave = myPrefs.getString("addressKey","");
        TextView label = findViewById(R.id.textView7);
        label.setText("MAC Address: " + addressSave);
        address1 = addressSave;



        // GETS A LIST OF BLUETOOTH DEVICES
        final ListView list = (ListView) findViewById(R.id.list);
        String[] devices = new String[]{
        };

        final List<String> devices_list = new ArrayList<String>(Arrays.asList(devices));
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, devices_list);
        list.setAdapter(arrayAdapter);

        Set<BluetoothDevice> pairedDevices = bAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                //String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
                devices_list.add(/*deviceName + " " + */deviceHardwareAddress);
            }
        }

    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            //TextView name = findViewById(R.id.textView6);
            TextView macAddress = findViewById(R.id.textView7);

            String selectedFromList = (list.getItemAtPosition(position).toString());
            //name.setText("Name: " + selectedFromList);
            macAddress.setText(("MAC Address: " + selectedFromList));
            address1 = selectedFromList;

            myPrefs = getSharedPreferences("prefID", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = myPrefs.edit();
            editor.putString("addressKey", selectedFromList);
            editor.apply();
        }
    });
    }

    public void connect (View v){

        // Put the String to pass back into an Intent and close this activity
        Intent intent = new Intent();
        intent.putExtra("ADDRESS", address1);
        setResult(RESULT_OK, intent);
        finish();
    }
}
