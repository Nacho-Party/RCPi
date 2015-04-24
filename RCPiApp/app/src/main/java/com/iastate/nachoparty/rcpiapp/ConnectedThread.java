package com.iastate.nachoparty.rcpiapp;

import android.app.Activity;
import android.bluetooth.BluetoothSocket;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Patrick on 4/22/2015.
 */
public class ConnectedThread extends Activity
{
    private OutputStream stream;
    private InputStream inputStream;

    public ConnectedThread(BluetoothSocket s)
    {
        BluetoothHandler.bluetoothSocket=s;
        OutputStream tempOut=null;

        try
        {
            tempOut=BluetoothHandler.bluetoothSocket.getOutputStream();
        }
        catch (IOException e)
        {
            Log.i(BluetoothHandler.label,"Output stream connection failed.");
        }

        stream=tempOut;
    }
    /*
    public void run()
    {
        byte[] buffer;  // buffer store for the stream
        int bytes; // bytes returned from read()

        // Keep listening to the InputStream until an exception occurs
        while (true) {
            try {
                // Read from the InputStream
                buffer = new byte[1024];
                bytes = inputStream.read(buffer);

            } catch (IOException e) {
                break;
            }
        }
    }
    */
    public void sendData(byte[] b)
    {
        try
        {
            stream.write(b);
        }
        catch (IOException e)
        {
            Log.i(BluetoothHandler.label,"Data sending failed.");
        }
        catch (NullPointerException e)
        {
            Toast.makeText(getApplicationContext(),"Y'all need to connect to a device",Toast.LENGTH_SHORT).show();
        }
    }

}
