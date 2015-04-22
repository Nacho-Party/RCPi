package com.iastate.nachoparty.rcpiapp;

import android.app.Activity;
import android.bluetooth.BluetoothServerSocket;
import android.content.Context;
import android.R.layout;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
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
import java.util.jar.Attributes;
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
    protected static BluetoothAdapter bluetoothAdapter;
    protected static BluetoothSocket bluetoothSocket;
    protected static BluetoothServerSocket serverSocket;
    //protected static OutputStream outputStream;
    private BluetoothDevice device;
    private ArrayAdapter<String> bluetoothItems;
    protected static OutputStream outputStream;
    private BroadcastReceiver receiver;
    // SPP UUID service

    protected static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    //MAC address of bluetooth
    protected static String address = "00:02:72:cd:9f:60 ";//to be updated

    /* Called when the activity is first created */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_handler);
        final Button back=(Button) findViewById(R.id.button_back);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        checkBTState();

        try {
            serverSocket=bluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(label, MY_UUID);
        } catch (IOException e){}

        try{
            bluetoothSocket=serverSocket.accept();
        }catch  (IOException e) {}

        try{
            outputStream=MainActivity1.bluetoothSocket.getOutputStream();
        }catch (IOException e){
            Log.d(MainActivity1.label,"Output stream connection failed.");
        }

        receiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                String a=intent.getAction();

                if(BluetoothDevice.ACTION_FOUND.equals(a))
                {
                    BluetoothDevice device=intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                    bluetoothItems.add(device.getName());
                }
            }
        };
        IntentFilter filter=new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver, filter);

        //searching();

        ListView bluetoothList=(ListView) findViewById(R.id.listView_bluetoothItems);

        bluetoothList.setAdapter(new ArrayAdapter<String>(this,R.layout.abc_list_menu_item_layout));

        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ControllerHomeScreen.class));
            }
        });
    }

    private BluetoothLeScanner scanner;
    private List<ScanResult> bList;
    private ScanCallback call;
    //private BluetoothAdapter.LeScanCallback call;
    private Intent intent;
    public void registerReceiver(View view)
    {
        this.registerReceiver(receiver, new IntentFilter("android.intent.action.TIME_TICK"));
        Toast.makeText(this,"Registered broadcast receiver", Toast.LENGTH_SHORT).show();
    }

    public void unregisterReceiver(View view)
    {
        this.unregisterReceiver(receiver);
        Toast.makeText(this,"broadcast receiver unregistered",Toast.LENGTH_SHORT);
    }

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        if (Build.VERSION.SDK_INT >= 10) {
            try {
                final Method m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord");
                return (BluetoothSocket) m.invoke(device, MY_UUID);
            } catch (Exception exception) {
                Log.e(label, "Could not create Insecure RFComm Connection", exception);

            }
        }
        return device.createRfcommSocketToServiceRecord(MY_UUID);
    }
    public void onResume() {
        super.onResume();
        checkBTState();
        /*
        Log.d(label, "....onResume-try connect.....");
        // FIX ME BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);
        try {
            bluetoothSocket = createBluetoothSocket(device);
        } catch (IOException e1) {
            errorExit("Fatal Error","In onResume() and socket create failed:"+e1.getMessage());
        }
        //FIX ME
        // bluetoothAdapter.cancelDiscovery();
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
        */
    }
    public void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
        /*
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
       */
    }
    public void onDestroy()
    {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    private void checkBTState() { //DONE
        // Check for Bluetooth support and then check to make sure it is turned on
        // Emulator doesn't support Bluetooth and will return null
        if(bluetoothAdapter==null) {
            errorExit("Fatal Error", "Bluetooth not supported");
        } else {
            if (!bluetoothAdapter.isEnabled()) {
                //Prompt user to turn on Bluetooth
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }
    }
    protected void errorExit(String title, String message){ //DONE
        Toast.makeText(getBaseContext(), title + " - " + message, Toast.LENGTH_LONG).show();
    }
    /*
    protected void sendData(String message) {
        byte[] msgBuffer = message.getBytes();

        Log.d(label, "...Send data: " + message + "...");

        try {
            outputStream.write(msgBuffer);
        } catch (IOException e) {
            String msg = "In onResume() and an exception occurred during write: " + e.getMessage();
            if (address.equals("00:00:00:00:00:00"))
                msg = msg + ".\n\nUpdate your server address from 00:00:00:00:00:00 to the correct address on line 35 in the java code";
            //msg = msg +  ".\n\nCheck that the SPP UUID: " + MY_UUID.toString() + " exists on server.\n\n";

            errorExit("Fatal Error", msg);
        }
    }
    */
}








