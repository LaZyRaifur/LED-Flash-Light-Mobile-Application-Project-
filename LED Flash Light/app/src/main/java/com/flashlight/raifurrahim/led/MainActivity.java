package com.flashlight.raifurrahim.led;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.lang.reflect.Parameter;
import java.security.Policy;

public class MainActivity extends AppCompatActivity {


    ImageButton Switch;


    private Camera camera;
    private boolean isFlashOn;
    private boolean hasFlash;
    android.hardware.Camera.Parameters params;
    MediaPlayer mp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Switch = (ImageButton)findViewById(R.id.LightOn);

        //First check if device is supporting or not

        hasFlash = getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        if (!hasFlash){
            //device doesn't support flash
            //show alert message and close the application

            AlertDialog alert = new AlertDialog.Builder(MainActivity.this)
                    .create();
            alert.setTitle("Error");
            alert.setMessage("Sorry your device doesn't support flash light!");
            alert.setButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //closing the application
                    finish();
                }
            });
            alert.show();
            return;

        }


        //get Camera
        getCamera();

        //display button image
        toggleButtonImage();

        //Switch button click event click to toggle flash on/off

        Switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFlashOn){
                    //turn off flash

                    tunrOffFlash();
                }else{
                    //turn on flash

                    turnOnFlash();
                }
            }
        });

    }


    //Turning on method
    private void turnOnFlash() {

        if (!isFlashOn){
            if (camera == null || params == null){return;
        }
        //play sound implement
            playSound();

            params = camera.getParameters();
            params.setFlashMode(Parameters.FLASH_MODE_TORCH);
            camera.setParameters(params);
            camera.startPreview();
            isFlashOn = true;

            //changing button/switch image
            toggleButtonImage();
    }}


    //playing sound .
    private void playSound() {
    if (isFlashOn){
        mp = MediaPlayer.create(MainActivity.this,R.raw.light_switch_off);

    }else {
        mp = MediaPlayer.create(MainActivity.this,R.raw.light_switch_on);
    }

    mp.setOnCompletionListener(new OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            mp.release();
        }
    });
    mp.start();

    }

    //turn off flash light

    private void tunrOffFlash() {
    if (isFlashOn){
        if (camera ==null || params == null){
            return;
        }

        //play sound
        playSound();

        params = camera.getParameters();
        params.setFlashMode(Parameters.FLASH_MODE_OFF);
        camera.setParameters(params);
        camera.stopPreview();
        isFlashOn = false;

        //changing button /switch image
        toggleButtonImage();

    }
    }

    /*Toggle  switch button images
     changing image states to on/off
         */
    private void toggleButtonImage() {
    if (isFlashOn){
        Switch.setImageResource(R.drawable.on);
    }else {
        Switch.setImageResource(R.drawable.index);
    }
    }

    //get the camera implement

    @SuppressLint("LongLogTag")
    private void getCamera() {
              if (camera == null){
                  try{
                      camera = Camera.open();
                      params = camera.getParameters();
                  }catch (RuntimeException e){
                      Log.e("Camera Error.Failed to open.Error: ",e.getMessage());
                  }
              }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        setTitle("Quiz");
        builder.setMessage("Are you sure you want to Quite! ");
        builder.setPositiveButton("OK!!!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),"Cancel was clicked",Toast.LENGTH_SHORT).show();
                    }
                });
//Create the Alert Dialog Object and return it
        builder.create().show();

    }

    @Override
    protected void onPause() {
        super.onPause();

        //on pause turn on off the flash
        tunrOffFlash();
    }

    @Override
    protected void onRestart() {
        super.onRestart();


    }

    @Override
    protected void onResume() {
        super.onResume();

        //on resume turn on the flash

        if (hasFlash)turnOnFlash();
    }

    @Override
    protected void onStart() {
        super.onStart();

        //on starting the app get the camera parameters
        getCamera();
    }

    @Override
    protected void onStop() {
        super.onStop();

        //on stop release the camera
        if (camera !=null){
            camera.release();
            camera = null;
        }
    }
}
