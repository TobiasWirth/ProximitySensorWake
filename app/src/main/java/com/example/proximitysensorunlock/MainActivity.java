package com.example.proximitysensorunlock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.TriggerEventListener;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private float previousProxValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Sensor prox;

        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        if(sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY) != null){
            prox = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
            sensorManager.registerListener(this, prox, SensorManager.SENSOR_DELAY_NORMAL);

            previousProxValue = 5.0f;
        }else{
            //TODO: Error Message
        }




    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor.getType() == Sensor.TYPE_PROXIMITY){

            proximitySensorMethod(sensorEvent);
        }
    }

    private void proximitySensorMethod(SensorEvent event) {
        Log.d("debug", "Proximity Activity " + event.values[0]);

        Log.d("debug", "previous Value: " + previousProxValue);

        PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);



        if(pm.isDeviceIdleMode()){
        if(event.values[0] == 0.0 && previousProxValue != 0.0) {
            PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "app:ScreenWake");
            wakeLock.acquire();
            Log.d("debug", "wakeLock acquired");
            wakeLock.release();
        }
        }

        previousProxValue = event.values[0];

        Log.d("debug", "new previous Value: " + previousProxValue);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
