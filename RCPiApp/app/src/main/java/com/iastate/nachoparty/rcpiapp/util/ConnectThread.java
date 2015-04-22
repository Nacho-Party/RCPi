package com.iastate.nachoparty.rcpiapp.util;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.iastate.nachoparty.rcpiapp.ControllerHomeScreen;

import java.io.IOException;

/**
 * Created by Patrick on 4/22/2015.
 */
public class ConnectThread extends Thread
{
    private final BluetoothSocket socket;
    private final BluetoothDevice device;

    public ConnectThread(BluetoothDevice d)
    {
        BluetoothSocket temp=null;
        device=d;
        try
        {
            temp=device.createRfcommSocketToServiceRecord(BluetoothHandler.MY_UUID);
        }
        catch (IOException e)
        {
            Log.i(BluetoothHandler.label, "Get socket failed.");
        }

        socket=temp;
    }

    public void konnekt()
    {
        BluetoothHandler.bluetoothAdapter.cancelDiscovery();
        try
        {
            socket.connect();
            Log.i(BluetoothHandler.label,"Connected");
        }
        catch (IOException e)
        {
            Log.i(BluetoothHandler.label,"Connection failed.");
        }
        try
        {
            socket.close();
        }
        catch (IOException e)
        {
            return;
        }

        //BluetoothHandler.handler.obtainMessage(BluetoothHandler.SUCCESSFUL,socket);
    }

    public void cancel()
    {
        try
        {
            socket.close();
        }
        catch (IOException e)
        {
            Log.i(BluetoothHandler.label,"Socket close failed.");
        }
    }


}


