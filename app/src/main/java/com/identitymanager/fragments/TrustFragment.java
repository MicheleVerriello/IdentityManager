package com.identitymanager.fragments;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.identitymanager.R;

public class TrustFragment extends Fragment {

    private static final int REQUEST_ENABLE_BT = 0;
    private static final int REQUEST_DISCOVER_BT = 1;
    private static final int RESULT_OK = -1;
    BluetoothAdapter bluetoothAdapter = null;

    public TrustFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trust, container, false);
    }

    public void trust(View view) {

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter != null) {

            if (!bluetoothAdapter.isEnabled()) {//cheking bluetooth status

                if ((ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.BLUETOOTH_CONNECT ) != PackageManager.PERMISSION_GRANTED)
                        && (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.BLUETOOTH ) != PackageManager.PERMISSION_GRANTED)
                        && (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.BLUETOOTH_ADMIN ) != PackageManager.PERMISSION_GRANTED)) { //cheking permission
                    Toast.makeText(this.getContext(), "Bluetooth permissions denied", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    bluetoothAdapter.enable();
                }
            }

            //start searching for devices
        } else {
            Toast.makeText(this.getContext(), "Bluetooth not available", Toast.LENGTH_SHORT).show();
        }
    }

    public void getTrusted(View view) {

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter != null) {

            if (!bluetoothAdapter.isEnabled()) {//cheking bluetooth status

                if ((ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.BLUETOOTH_CONNECT ) != PackageManager.PERMISSION_GRANTED)
                        && (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.BLUETOOTH ) != PackageManager.PERMISSION_GRANTED)
                        && (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.BLUETOOTH_ADMIN ) != PackageManager.PERMISSION_GRANTED)) { //cheking permission
                    Toast.makeText(this.getContext(), "Bluetooth permissions denied", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Toast.makeText(this.getContext(), "Enabling bluetooth", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(bluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(intent, REQUEST_ENABLE_BT);
                }
            }

            //bluetooth discoverability
            if(!bluetoothAdapter.isDiscovering()) {
                Toast.makeText(this.getContext(), "Making your device discoverable", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(bluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                startActivityForResult(intent, REQUEST_DISCOVER_BT);
            }
        } else {
            Toast.makeText(this.getContext(), "Bluetooth not available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                if(resultCode == RESULT_OK) {
                    //bluetooth is on
                    Toast.makeText(this.getContext(), "Bluetooth enabled", Toast.LENGTH_SHORT).show();
                } else {
                    //user denied bluetooth permission
                    Toast.makeText(this.getContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
            case REQUEST_DISCOVER_BT:
                if(resultCode == RESULT_OK) {
                    Toast.makeText(this.getContext(), "Device is discoverable", Toast.LENGTH_SHORT).show();
                } else {
                    //user denied bluetooth permission
                    Toast.makeText(this.getContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}