package com.nerdinand.glogger;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.hardware.SensorEvent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.LinearLayout;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.GraphViewSeries.GraphViewStyle;
import com.jjoe64.graphview.LineGraphView;

public class LogViewerActivity extends Activity {

	public static final float GRAVITY = 9.81f;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_log_viewer);

		ArrayList<SensorEvent> eventLog = CapturingActivity.getEventLog();
		ArrayList<Long> nanoTimes = CapturingActivity.getNanoTimes();

		Long startTime = nanoTimes.get(0);

		GraphViewData[] dataX = new GraphViewData[eventLog.size()];
		GraphViewData[] dataY = new GraphViewData[eventLog.size()];
		GraphViewData[] dataZ = new GraphViewData[eventLog.size()];

		int i = 0;
		for (SensorEvent sensorEvent : eventLog) {
			float xValue = (nanoTimes.get(i) - startTime) / 1000000000f;

			dataX[i] = new GraphViewData(xValue, sensorEvent.values[0] / GRAVITY);
			dataY[i] = new GraphViewData(xValue, sensorEvent.values[1] / GRAVITY);
			dataZ[i] = new GraphViewData(xValue, sensorEvent.values[2] / GRAVITY);

			i++;
		}

		GraphViewSeries seriesX = new GraphViewSeries("X", new GraphViewStyle(Color.rgb(200, 50, 00), 3), dataX);
		GraphViewSeries seriesY = new GraphViewSeries("Y", new GraphViewStyle(Color.rgb(90, 250, 00), 3), dataY);
		GraphViewSeries seriesZ = new GraphViewSeries("Z", null, dataZ);

		GraphView graphView = new LineGraphView(this, "Accelerometer log data");
		graphView.addSeries(seriesX);
		graphView.addSeries(seriesY);
		graphView.addSeries(seriesZ);

		graphView.setScalable(true);
		graphView.setScrollable(true);
		graphView.setShowLegend(true);

		LinearLayout layout = (LinearLayout) findViewById(R.id.linear_layout);
		layout.addView(graphView);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.log_viewer, menu);
		return true;
	}

}
