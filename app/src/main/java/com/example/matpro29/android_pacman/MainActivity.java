package com.example.matpro29.android_pacman;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    TextView tv1;
    TextView tv2;
    TextView tv3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);

        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), 0, null);


        Panel panel = new Panel(this);
        setContentView(panel);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        tv1.setText("event.values[0] = " + Float.toString(event.values[0]));
        tv2.setText("event.values[1] = " + Float.toString(event.values[1]));
        tv3.setText("event.values[2] = " + Float.toString(event.values[2]));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
