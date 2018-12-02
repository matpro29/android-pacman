package com.example.matpro29.android_pacman;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private PanelView panelView;
    private int gyroscopeX = 0;
    private int gyroscopeY = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), sensorManager.SENSOR_DELAY_NORMAL, null);

        panelView = new PanelView(this);
        setContentView(panelView);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (panelView.getStart()) {
            if ((int) event.values[1] > gyroscopeX+5) {
                panelView.zmnienKierunekPacman(0);//góra
            }
            if ((int) event.values[1] < gyroscopeX-5) {
                panelView.zmnienKierunekPacman(2);//dół
            }
            if ((int) event.values[2] > gyroscopeY+5) {
                panelView.zmnienKierunekPacman(3);//lewo
            }
            if ((int) event.values[2] < gyroscopeY-5) {
                panelView.zmnienKierunekPacman(1);//prawo
            }
        }
        if (panelView.getGyroscopeConfig()) {
            gyroscopeX = (int)event.values[1];
            gyroscopeY = (int)event.values[2];
            panelView.setGyroscopeConfig(false);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
