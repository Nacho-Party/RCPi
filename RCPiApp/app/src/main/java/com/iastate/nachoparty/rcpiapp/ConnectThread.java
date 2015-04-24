package com.iastate.nachoparty.rcpiapp;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;

/**
 * Created by Patrick on 4/22/2015.
 */
public class ConnectThread extends Thread
{
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

        BluetoothHandler.bluetoothSocket=temp;
    }

    public void konnekt()
    {
        BluetoothHandler.bluetoothAdapter.cancelDiscovery();
        try
        {
            BluetoothHandler.bluetoothSocket.connect();
            Log.i(BluetoothHandler.label,"Connected -- ConnectThread");
        }
        catch (IOException e)
        {
            Log.i(BluetoothHandler.label,"Connection failed. ConnectThread");
        }
        try
        {
            BluetoothHandler.bluetoothSocket.close();
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
            BluetoothHandler.bluetoothSocket.close();
        }
        catch (IOException e)
        {
            Log.i(BluetoothHandler.label,"Socket close failed.");
        }
    }


}


