package com.nerdinand.glogger;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class CapturingActivity extends Activity implements SensorEventListener, OnClickListener {

	static private ArrayList<SensorEvent> eventLog;
	static private ArrayList<Long> nanoTimes;

	private SensorManager sensorManager;
	private Sensor accelerometer;

	private TextView loggerText;
	private Button startCapturingButton;
	private Button pauseCapturingButton;
	private Button stopCapturingButton;

	private boolean capturing = false;
	private Button resetButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_capturing);

		loggerText = (TextView) findViewById(R.id.logger_text);

		startCapturingButton = (Button) findViewById(R.id.start_capturing_button);
		pauseCapturingButton = (Button) findViewById(R.id.pause_capturing_button);
		stopCapturingButton = (Button) findViewById(R.id.stop_capturing_button);
		resetButton = (Button) findViewById(R.id.reset_button);

		startCapturingButton.setOnClickListener(this);
		pauseCapturingButton.setOnClickListener(this);
		stopCapturingButton.setOnClickListener(this);
		resetButton.setOnClickListener(this);

		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

		eventLog = new ArrayList<SensorEvent>(10000);
		nanoTimes = new ArrayList<Long>(10000);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (capturing) {
			getNanoTimes().add(System.nanoTime());
			getEventLog().add(event);
		}

		updateLabel();
	}

	private void updateLabel() {
		loggerText.setText("Events logged: " + getEventLog().size());
	}

	@Override
	public void onClick(View view) {
		if (view == startCapturingButton) {
			sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);

			capturing = true;

		} else if (view == pauseCapturingButton) {
			capturing = false;

		} else if (view == stopCapturingButton) {
			capturing = false;

			sensorManager.unregisterListener(this);

			Intent intent = new Intent(this, LogViewerActivity.class);
			this.startActivity(intent);
			
		} else if (view == resetButton) {
			getNanoTimes().clear();
			getEventLog().clear();
			
			updateLabel();
		}
	}

	public static ArrayList<Long> getNanoTimes() {
		return nanoTimes;
	}

	public static ArrayList<SensorEvent> getEventLog() {
		return eventLog;
	}
}
