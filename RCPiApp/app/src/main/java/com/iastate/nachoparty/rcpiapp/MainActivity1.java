package com.iastate.nachoparty.rcpiapp;

import android.app.Activity;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.nfc.Tag;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.List;
import java.util.UUID;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import com.iastate.nachoparty.rcpiapp.R;

public class MainActivity1 extends Activity {

    protected static final String label = "bluetooth1";
    Button buttonOn, buttonOff;
    protected static BluetoothAdapter bluetoothAdapter = null;
    protected static BluetoothSocket bluetoothSocket = null;
    protected static OutputStream outstream = null;
    private BluetoothDevice device;
    private ArrayAdapter<String> bluetoothItems;
    // SPP UUID service

    //private static final UUID MY_UUID = UUID.fromString("00001101-0000-8000-00805F9B34FB");
    //MAC address of bluetooth
    protected static String address = "00:02:72:cd:9f:60 ";//to be updated

    /* Called when the activity is first created */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);
        buttonOn = (Button) findViewById(R.id.button_go);
        buttonOff = (Button) findViewById(R.id.button_stop);
        final Button back=(Button) findViewById(R.id.button_back);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        //checkBTState();

        ListView bluetoothList=(ListView) findViewById(R.id.listView_bluetoothItems);


        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ControllerHomeScreen.class));
            }
        });
        /*
        buttonOn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                   sendData("1");
                Toast.makeText(getBaseContext(), "Turn on LED", Toast.LENGTH_SHORT).show();
            }
        });
        buttonOff.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                 sendData("0");
                Toast.makeText(getBaseContext(), "Turn off LED", Toast.LENGTH_SHORT).show();
            }
        });

        */
    }

    private BluetoothLeScanner scanner;
    private List<ScanResult> bList;
    //private ScanCallback call;
    private BluetoothAdapter.LeScanCallback call;
    protected void searching()
    {
        //scanner.startScan(call);
        bluetoothAdapter.startLeScan(call);
        //call.onBatchScanResults(bList);

        for(ScanResult r:bList)
        {
            bluetoothItems.add(r.getDevice().getName());
        }
    }

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        if (Build.VERSION.SDK_INT >= 10) {
            try {
                final Method m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord");
      //          return (BluetoothSocket) m.invoke(device, MY_UUID);
            } catch (Exception e) {
                Log.e(label, "Could not create Insecure RFComm Connection", e);

            }
        }
        //return device.createRfcommSocketToServiceRecord(MY_UUID);
        return null;
    }

    public void onResume() {
        super.onResume();
        Log.d(label, "....onResume-try connect.....");
        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);
        try {
            bluetoothSocket = createBluetoothSocket(device);
        } catch (IOException e1) {
            errorExit("Fatal Error","In onResume() and socket create failed:"+e1.getMessage());
        }
        bluetoothAdapter.cancelDiscovery();
        Log.d(label, "...Connecting.....");


        try {
            bluetoothSocket.connect();
            Log.d(label, "...Connection ok...");
        } catch (IOException e) {
            try {
                bluetoothSocket.close();
            } catch (IOException e2) {
               errorExit("Fatal Error", "In onResume() and unable to close socket during connection failure" + e2.getMessage() + ".");
            }

        }
        Log.d(label, "...Create Socket...");

        try {
            outstream = bluetoothSocket.getOutputStream();
        } catch (IOException e) {
            errorExit("Fatal Error", "In onResume() and output stream creation failed:" + e.getMessage() + ".");
        }
    }
    public void onPause() {
        super.onPause();

        Log.d(label, "...In onPause()...");

        if (outstream != null) {
            try {
                outstream.flush();
            } catch (IOException e) {
                errorExit("Fatal Error", "In onPause() and failed to flush output stream: " + e.getMessage() + ".");
            }
        }

        try     {
            bluetoothSocket.close();
        } catch (IOException e2) {
           errorExit("Fatal Error", "In onPause() and failed to close socket." + e2.getMessage() + ".");
        }
    }
    private void checkBTState() {
        // Check for Bluetooth support and then check to make sure it is turned on
        // Emulator doesn't support Bluetooth and will return null
        if(bluetoothAdapter==null) {
            errorExit("Fatal Error", "Bluetooth not support");
        } else {
            if (bluetoothAdapter.isEnabled()) {
                Log.d(label, "...Bluetooth ON...");
            } else {
                //Prompt user to turn on Bluetooth
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }
    }
    protected void errorExit(String title, String message){
        Toast.makeText(getBaseContext(), title + " - " + message, Toast.LENGTH_LONG).show();
        finish();
    }
    protected void sendData(String message) {
        byte[] msgBuffer = message.getBytes();

        Log.d(label, "...Send data: " + message + "...");

        try {
            outstream.write(msgBuffer);
        } catch (IOException e) {
            String msg = "In onResume() and an exception occurred during write: " + e.getMessage();
            if (address.equals("00:00:00:00:00:00"))
                msg = msg + ".\n\nUpdate your server address from 00:00:00:00:00:00 to the correct address on line 35 in the java code";
            //msg = msg +  ".\n\nCheck that the SPP UUID: " + MY_UUID.toString() + " exists on server.\n\n";

            errorExit("Fatal Error", msg);
        }
    }

}








