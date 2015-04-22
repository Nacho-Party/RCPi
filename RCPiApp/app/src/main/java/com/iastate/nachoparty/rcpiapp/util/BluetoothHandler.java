package com.iastate.nachoparty.rcpiapp.util;

import android.app.Activity;
import android.bluetooth.BluetoothServerSocket;
import android.content.Context;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import com.iastate.nachoparty.rcpiapp.ControllerHomeScreen;
import com.iastate.nachoparty.rcpiapp.R;

public class BluetoothHandler extends Activity implements OnItemClickListener {

    protected static BluetoothAdapter bluetoothAdapter;
    protected static BluetoothSocket bluetoothSocket;
    private ArrayList<String> bluetoothDevices;
    private ArrayList<BluetoothDevice> devices;
    private ArrayAdapter<String> list;
    private ListView listView;
    protected IntentFilter filter;
    protected BroadcastReceiver receiver;
    protected static final int SUCCESSFUL=0;
    protected static final String label = "bluetooth1";
    protected static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    /*
    protected static Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message m)
        {
            super.handleMessage(m);
            switch(m.what)
            {
                case SUCCESSFUL:
                    ConnectedThread connectedThread = new ConnectedThread((BluetoothSocket)m.obj);

            }
        }
    };
    */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_handler);
        Button back = (Button) findViewById(R.id.button_back);
        listView = (ListView) findViewById(R.id.listView_bluetoothItems);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        checkBTState();
        search();


        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ControllerHomeScreen.class));
            }
        });
    }

    public void onResume()
    {
        super.onResume();
        search();
    }

    public void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    public void onDestroy()
    {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    public void search() {
        listView.setOnItemClickListener(this);
        list = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(list);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothDevices = new ArrayList<String>();
        filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        devices = new ArrayList<BluetoothDevice>();
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();

                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    String name = "";

                    devices.add(device);
                    list.add(device.getName() + "\n" + device.getAddress());
                } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                    Toast.makeText(getApplicationContext(), "Started searching for devices", Toast.LENGTH_LONG).show();
                } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                    Toast.makeText(getApplicationContext(), "Finished searching", Toast.LENGTH_SHORT).show();
                } else if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                    if (bluetoothAdapter.getState() == BluetoothAdapter.STATE_OFF) {
                        checkBTState();
                    }
                }

            }
        };
        registerReceiver(receiver, filter);
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        registerReceiver(receiver, filter);
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(receiver, filter);
        filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(receiver, filter);

        discover();
    }

    public void discover() {
        bluetoothAdapter.cancelDiscovery();
        bluetoothAdapter.startDiscovery();
    }

    private void checkBTState() { //DONE
        // Check for Bluetooth support and then check to make sure it is turned on
        // Emulator doesn't support Bluetooth and will return null
        if (bluetoothAdapter == null) {
            errorExit("Fatal Error", "Bluetooth not supported");
        } else {
            if (!bluetoothAdapter.isEnabled()) {
                //Prompt user to turn on Bluetooth
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }
    }

    protected void errorExit(String title, String message) { //DONE
        Toast.makeText(getBaseContext(), title + " - " + message, Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }
        if (list.getItem(position).contains("Paired")) {
            BluetoothDevice newDevice = devices.get(position);
            ConnectThread connection = new ConnectThread(newDevice);
        }
    }
}
