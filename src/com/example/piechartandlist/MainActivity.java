package com.example.piechartandlist;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.graphics.Color;

import com.example.piechartandlist.FetchDataTaskPie.AsyncResponseListener;
import com.shinobicontrols.charts.ChartFragment;
import com.shinobicontrols.charts.DataAdapter;
import com.shinobicontrols.charts.DataPoint;
import com.shinobicontrols.charts.PieDonutSeries;
import com.shinobicontrols.charts.PieSeries;
import com.shinobicontrols.charts.PieSeriesStyle;
import com.shinobicontrols.charts.Series;
import com.shinobicontrols.charts.ShinobiChart;
import com.shinobicontrols.charts.SimpleDataAdapter;

public class MainActivity extends Activity implements AsyncResponseListener{
	private DataAdapter<String, Double> dataAdapter;
	private PieSeries series;
	private ProgressBar bar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Create new pie chart only first time
		initialPieChart();
		
		bar = (ProgressBar) findViewById(R.id.progressBar);
        Button getList = (Button) findViewById(R.id.my_list);
        
        // Get color data info
 		FetchDataTaskPie dataTask = new FetchDataTaskPie(this);
 		dataTask.execute();
        
        getList.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(MainActivity.this, ShowList.class);
				startActivity(i);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_refreshing) {
			bar.setVisibility(View.VISIBLE);
			// Update color data info
    		FetchDataTaskPie dataTask = new FetchDataTaskPie(this);
    		dataTask.execute();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void initialPieChart(){
		ChartFragment pieChartFragment =
                (ChartFragment) getFragmentManager().findFragmentById(R.id.pie_chart);

        // Get the a reference to the ShinobiChart from the ChartFragment
        ShinobiChart shinobiChart = pieChartFragment.getShinobiChart();

        // Set trial license key
        shinobiChart.setLicenseKey("UONigH/ewqTlGfoMjAxNTAzMjh3dXdlaTExMDdAZ21haWwuY29tcmOO/9ehRYw49tpfd+k4Wz/vGSVoFNI7J8QXZe2ajp+Q0tQ4WEdAa69XC9uwCtr1+avEWUouirJs8fOBMv3Zc2HKnkWsZqpD3hQ0G3Vykk2akXjEOHycCSUmh98aRhtMifqSPx6HRsDPFMwDggeHIZH9w97w=BQxSUisl3BaWf/7myRmmlIjRnMU2cA7q+/03ZX9wdj30RzapYANf51ee3Pi8m2rVW6aD7t6Hi4Qy5vv9xpaQYXF5T7XzsafhzS3hbBokp36BoJZg8IrceBj742nQajYyV7trx5GIw9jy/V6r0bvctKYwTim7Kzq+YPWGMtqtQoU=PFJTQUtleVZhbHVlPjxNb2R1bHVzPnh6YlRrc2dYWWJvQUh5VGR6dkNzQXUrUVAxQnM5b2VrZUxxZVdacnRFbUx3OHZlWStBK3pteXg4NGpJbFkzT2hGdlNYbHZDSjlKVGZQTTF4S2ZweWZBVXBGeXgxRnVBMThOcDNETUxXR1JJbTJ6WXA3a1YyMEdYZGU3RnJyTHZjdGhIbW1BZ21PTTdwMFBsNWlSKzNVMDg5M1N4b2hCZlJ5RHdEeE9vdDNlMD08L01vZHVsdXM+PEV4cG9uZW50PkFRQUI8L0V4cG9uZW50PjwvUlNBS2V5VmFsdWU+");

        // Create our DataAdapter and data
        dataAdapter = new SimpleDataAdapter<String, Double>();

        // Create a PieSeries and give it the data adapter
        series = new PieSeries();
        series.setSelectionMode(Series.SelectionMode.POINT_SINGLE);
        // The value represents the angle in radians in an anti-clockwise direction
        series.setSelectedPosition(50.0f);
        series.setDataAdapter(dataAdapter);
        shinobiChart.addSeries(series);

        // Apply styling to the Pie Series
        PieSeriesStyle style = series.getStyle();
        style.setFlavorColors(new int[]{
                Color.argb(255, 103, 169, 66), // green
                Color.argb(255, 248, 184, 60), // yellow
                Color.argb(255, 255, 51, 51)   // red
        });
        style.setRadialEffect(PieDonutSeries.RadialEffect.DEFAULT);
        style.setCrustShown(false);
        style.setLabelTextSize(16.0f);

        // apply style to selected slices
        PieSeriesStyle selectedSeriesStyle = series.getSelectedStyle();
        selectedSeriesStyle.setFlavorColors(new int[]{
                Color.argb(255, 103, 169, 66), // green
                Color.argb(255, 248, 184, 60), // yellow
                Color.argb(255, 255, 51, 51)   // red
        });
        selectedSeriesStyle.setCrustThickness(10f);
        selectedSeriesStyle.setCrustColors(new int[]{
                Color.argb(255, 0, 0, 0), // black
        });
        series.setSelectedStyle(selectedSeriesStyle);
	}

	@Override
	public void fetchDataPieFinish(String[] result) {
		dataAdapter.clear();
		dataAdapter.add(new DataPoint<String, Double>(result[0], Double.parseDouble(result[0])));
        dataAdapter.add(new DataPoint<String, Double>(result[1], Double.parseDouble(result[1])));
        dataAdapter.add(new DataPoint<String, Double>(result[2], Double.parseDouble(result[2])));
        bar.setVisibility(View.GONE);
	}
	
}
