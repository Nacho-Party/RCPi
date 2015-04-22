package com.iastate.nachoparty.rcpiapp.util;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Patrick on 4/22/2015.
 */
public class ConnectedThread
{
    private BluetoothSocket socket;
    private OutputStream stream;

    public ConnectedThread(BluetoothSocket s)
    {
        socket=s;
        OutputStream tempOut=null;

        try
        {
            tempOut=socket.getOutputStream();
        }
        catch (IOException e)
        {
            Log.i(BluetoothHandler.label,"Output stream connection failed.");
        }

        stream=tempOut;
    }

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
    }

}
