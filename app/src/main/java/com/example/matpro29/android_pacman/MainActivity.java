package com.example.matpro29.android_pacman;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    Panel panel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), sensorManager.SENSOR_DELAY_NORMAL, null);

        panel = new Panel(this);
        setContentView(panel);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (panel.getStart()) {
            if ((int) event.values[1] > 5) {
                panel.zmnienKierunekPacman(0); //góra
            }
            if ((int) event.values[1] < -5) {
                panel.zmnienKierunekPacman(2); //dół
            }
            if ((int) event.values[2] > 5) {
                panel.zmnienKierunekPacman(3); //lewo
            }
            if ((int) event.values[2] < -5) {
                panel.zmnienKierunekPacman(1); //prawo
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
