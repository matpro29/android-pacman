package com.example.matpro29.android_pacman;

import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SharedPreferences sharedPreferences;
    private PanelView panelView;
    private int gyroscope[] = new int[2];
    private String gyroscopeNames[] = new String[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        gyroscopeNames = getResources().getStringArray(R.array.gyroscope);
        for (int i=0; i<gyroscopeNames.length; i++) {
            gyroscope[i] = sharedPreferences.getInt(gyroscopeNames[i], 0);
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), sensorManager.SENSOR_DELAY_NORMAL, null);

        panelView = new PanelView(this);
        setContentView(panelView);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (panelView.getStart()) {
            if ((int) event.values[1] > gyroscope[0]+5) {
                panelView.zmnienKierunekPacman(0);//góra
            }
            if ((int) event.values[1] < gyroscope[0]-5) {
                panelView.zmnienKierunekPacman(2);//dół
            }
            if ((int) event.values[2] > gyroscope[1]+5) {
                panelView.zmnienKierunekPacman(3);//lewo
            }
            if ((int) event.values[2] < gyroscope[1]-5) {
                panelView.zmnienKierunekPacman(1);//prawo
            }
        }
        if (panelView.getGyroscopeConfig()) {
            gyroscope[0] = (int)event.values[1];
            gyroscope[1] = (int)event.values[2];
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(gyroscopeNames[0], gyroscope[0]);
            editor.putInt(gyroscopeNames[1], gyroscope[1]);
            editor.apply();

            panelView.setGyroscopeConfig(false);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
}
