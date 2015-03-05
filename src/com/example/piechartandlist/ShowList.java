package com.example.piechartandlist;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.piechartandlist.FetchDataTaskList.AsyncResponseListener;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class ShowList extends Activity implements AsyncResponseListener{
	private ListView list;
	private MyAdapter adapter;
	private ProgressBar bar;
	private List<List<String>> value;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_list);
		
		list = (ListView) findViewById(R.id.result_list);
		bar = (ProgressBar) findViewById(R.id.progressBar);
		value = new ArrayList<List<String>>();
		adapter = new MyAdapter(getApplicationContext(), value);
		list.setAdapter(adapter);
		
		FetchDataTaskList dataTask = new FetchDataTaskList(this);
		dataTask.execute();
		
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				List<String> listitem = (List<String>) adapter.getItem(position);
				Toast.makeText(getApplicationContext(), listitem.get(0), Toast.LENGTH_SHORT).show();
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.show_list, menu);
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
			// Update data info
			FetchDataTaskList dataTask = new FetchDataTaskList(this);
			dataTask.execute();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void fetchDataPieFinish(List<List<String>> result) {
		value.clear();
		value.addAll(result);
		adapter.notifyDataSetChanged();
		bar.setVisibility(View.GONE);
	}
}
