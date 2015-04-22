package com.iastate.nachoparty.rcpiapp;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.Toast;
import android.widget.VideoView;

import com.iastate.nachoparty.rcpiapp.util.BluetoothHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class ControllerHomeScreen extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);

        //final VideoView sight= (VideoView) findViewById(R.id.car_view);
        final Button bluetooth=(Button) findViewById(R.id.button_bluetooth);
        final Button go=(Button) findViewById(R.id.button_go);
        final Button stop=(Button) findViewById(R.id.button_stop);
        final Button left=(Button) findViewById(R.id.button_left);
        final Button right=(Button) findViewById(R.id.button_right);


        bluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(getApplicationContext(), BluetoothHandler.class));
            }
        });

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendData("0");
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendData("1");
            }
        });

        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendData("2");
            }
        });

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendData("3");
            }
        });

    }

    public void onPause()
    {
        super.onPause();
    }

    public void onResume()
    {
        super.onResume();
        try {
            MainActivity1.outputStream = MainActivity1.bluetoothSocket.getOutputStream();
        } catch (IOException e) {
            errorExit("Fatal Error", "In onResume() and output stream creation failed:" + e.getMessage() + ".");
        } catch(NullPointerException e) {
            Log.d(MainActivity1.label, "Null pointer caught");
        }
    }

    public void onDestroy()
    {
        super.onDestroy();
    }

    public void errorExit(String title, String message){
        Toast.makeText(getBaseContext(), title + " - " + message, Toast.LENGTH_LONG).show();
        finish();
    }

    protected void sendData(String message)
    {

    }
}
