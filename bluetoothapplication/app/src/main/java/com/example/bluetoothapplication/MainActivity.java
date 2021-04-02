package com.example.bluetoothapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    EditText valor1;
    String myEditValue;
    public static int drop1;
    public static int drop2;
    public static int drop2_delay;
    public static int flash_delay;

    static final UUID mUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
    int connection_check = 0;
    BluetoothDevice hc05;
    BluetoothSocket btSocket = null;

    private static final int SECOND_ACTIVITY_REQUEST_CODE = 0;
    String address;
    SharedPreferences myPrefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Home");

        myPrefs = getSharedPreferences("prefID", Context.MODE_PRIVATE);
        String addressSave = myPrefs.getString("addressKey","");
        TextView label = findViewById(R.id.textView8);
        label.setText(addressSave);
        address = addressSave;

        // TURNS ON BLUETOOTH
        if(!btAdapter.isEnabled())
        {
            Intent eintent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            int intVal = 1;
            startActivityForResult(eintent, intVal);
        }
    }

    public void launchSettings(View v){

        //LAUNCHES SETTINGS

        Intent i = new Intent(this, SettingsActivity.class);
        startActivityForResult(i, SECOND_ACTIVITY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // DOBIMO MAC ADDRESS IZBRANE NAPRAVE
        if (requestCode == SECOND_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                // Get String data from Intent
                String returnString = data.getStringExtra("ADDRESS");

                // Set text view with string
                TextView textView = (TextView) findViewById(R.id.textView8);
                textView.setText(returnString);
                address= returnString;

                myPrefs = getSharedPreferences("prefID", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = myPrefs.edit();
                editor.putString("addressKey", address);
                editor.apply();
            }
        }
    }

    public void send (View v){

        //SENDS DATA TO ARDUINO

        valor1 = findViewById (R.id.editTextNumber4);
        myEditValue = valor1.getText().toString();
        drop1 = Integer.parseInt(myEditValue);

        valor1 = findViewById (R.id.editTextNumber3);
        myEditValue = valor1.getText().toString();
        drop2 = Integer.parseInt(myEditValue);

        valor1 = findViewById (R.id.editTextNumber2);
        myEditValue = valor1.getText().toString();
        drop2_delay = Integer.parseInt(myEditValue);

        valor1 = findViewById (R.id.editTextNumber);
        myEditValue = valor1.getText().toString();
        flash_delay = Integer.parseInt(myEditValue);

        TextView test = findViewById(R.id.textView8);


        //DELA PRAVILNO, A SAMO ENKRAT
        hc05 = btAdapter.getRemoteDevice(address);

        try {
            btSocket = hc05.createRfcommSocketToServiceRecord(mUUID);
            btSocket.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }

/*
          // NE DELA VEČ
        if (connection_check == 0) {

            hc05 = btAdapter.getRemoteDevice(address);

            int counter = 0;
            do {
                try {
                    btSocket = hc05.createRfcommSocketToServiceRecord(mUUID);
                    btSocket.connect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                counter++;
            } while (!btSocket.isConnected() && counter < 3);
            connection_check++;
        }
 */
        test.setText(address + " test");

        OutputStream outputStream = null;
        try {
            outputStream = btSocket.getOutputStream();

            outputStream.write(drop1);
            outputStream.write(drop2);
            outputStream.write(drop2_delay);
            outputStream.write(flash_delay);

        } catch (IOException e) {
            e.printStackTrace();

        }

/*      //ZAPRE CONNECTION
        try {
            btSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


 */



    }
}