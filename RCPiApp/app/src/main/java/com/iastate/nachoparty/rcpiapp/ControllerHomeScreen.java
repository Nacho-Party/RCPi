package com.iastate.nachoparty.rcpiapp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.Toast;

import java.io.IOException;


public class ControllerHomeScreen extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);

        final Button bluetooth=(Button) findViewById(R.id.button_bluetooth);
        final Button go=(Button) findViewById(R.id.button_go);
        //final Button stop=(Button) findViewById(R.id.button_stop);
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
                String message="0";
                BluetoothHandler.connected.sendData(message.getBytes());
            }
        });

        go.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                String message="0";
                BluetoothHandler.connected.sendData(message.getBytes());

                return true;
            }
        });

        go.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_UP)
                {
                    String message="1";
                    BluetoothHandler.connected.sendData(message.getBytes());
                }

                return false;
            }
        });
        /*
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = "1";
                BluetoothHandler.connected.sendData(message.getBytes());
            }
        });

        stop.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String message="4";
                BluetoothHandler.connected.sendData(message.getBytes());

                return true;
            }
        });
        */
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message="2";
                BluetoothHandler.connected.sendData(message.getBytes());
            }
        });

        left.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                String message="2";
                BluetoothHandler.connected.sendData(message.getBytes());

                return true;
            }
        });
        left.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_UP)
                {
                    String message="-1";
                    BluetoothHandler.connected.sendData(message.getBytes());
                }

                return false;
            }
        });

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message="3";
                BluetoothHandler.connected.sendData(message.getBytes());
            }
        });

        right.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                String message="3";
                BluetoothHandler.connected.sendData(message.getBytes());

                return true;
            }
        });

        right.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_UP)
                {
                    String message="-1";
                    BluetoothHandler.connected.sendData(message.getBytes());
                }

                return false;
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
    }

    public void onDestroy()
    {
        super.onDestroy();
    }

    public void errorExit(String title, String message){
        Toast.makeText(getBaseContext(), title + " - " + message, Toast.LENGTH_LONG).show();
        finish();
    }

}
