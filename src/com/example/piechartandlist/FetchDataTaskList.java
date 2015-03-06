package com.example.piechartandlist;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;


public class FetchDataTaskList extends AsyncTask<Void, Void, List<List<String>>> {
	private final String LOG_TAG = FetchDataTaskList.class.getSimpleName();
	private PLDbHelper myDbHelper;
	private AsyncResponseListener listener;
	private Context context;
		
	public interface AsyncResponseListener{
		void fetchDataPieFinish(List<List<String>> result);
	}
	
	public FetchDataTaskList(Context context, AsyncResponseListener listener){
		this.listener = listener;
		this.context = context;
	}
	
	private void addListToDb(List<List<String>> listResult){
		myDbHelper = new PLDbHelper(context);
		myDbHelper.deleteAllList();
		for(int i = 0; i < listResult.size(); i++){
			myDbHelper.addListInfo(listResult.get(i));
		}
		
		List<List<String>> test = myDbHelper.getListTableInfo();
		Log.e("addcolor", test.size() + "");
	}
     
     private List<List<String>> getDataFromJson(String jsonResult) throws JSONException{
     	List<List<String>> listResult = new ArrayList<List<String>>();
     	JSONObject object = new JSONObject(jsonResult).getJSONObject("response").getJSONObject("accounts");
     	JSONArray array = object.getJSONArray("hits");
     	for(int i = 0; i < array.length(); i++){
     		List<String> listitem = new ArrayList<String>();
     		JSONObject sub = array.getJSONObject(i);
     		JSONObject jfield = sub.getJSONArray("selected_fields").getJSONObject(0);
     		listitem.add(sub.getString("name"));//name
     		listitem.add(sub.getString("display_name"));//display_name
     		listitem.add(jfield.getString("health"));//field1
     		listitem.add(jfield.getString("health_prev"));//field2
     		listitem.add(jfield.getString("change_date"));//field3
     		listResult.add(listitem);
     		
     	}
     	addListToDb(listResult);
     	return listResult;
     }
     

		@Override
		protected List<List<String>> doInBackground(Void... params) {
			//android.os.Debug.waitForDebugger();
			InputStream inputStream = null;
	        String jsonResult = "";
	        // create HttpClient
	        HttpClient httpClient = new DefaultHttpClient();
	        String uri = "https://appem.totango.com/api/v1/search/accounts";
	        HttpPost httpPost = new HttpPost(uri);
	        String body = "{\"offset\":0,\"count\":1000,\"scope\":\"all\",\"terms\":[{\"type\":\"string\",\"term\":\"health\",\"in_list\":[\"green\",\"red\",\"yellow\"]},{\"type\":\"totango_user_scope\",\"is_one_of\":[\"mobile+testme@totango.com\"]}],\"fields\":[{\"type\":\"health_trend\",\"field_display_name\":\"Health last change\",\"desc\":true},{\"type\":\"health_reason\"},{\"type\":\"date_attribute\",\"attribute\":\"Contract Renewal Date\",\"field_display_name\":\"Contract Renewal Date\"},{\"type\":\"number\",\"term\":\"contract_value\",\"field_display_name\":\"Value\"},{\"type\":\"string\",\"term\":\"status\",\"field_display_name\":\"Status\"},{\"type\":\"number\",\"term\":\"score\",\"field_display_name\":\"Engagement\"},{\"type\":\"on_attention\",\"user_id\":\"(email)\"},{\"type\":\"named_aggregation\",\"aggregation\":\"unique_users\",\"duration\":14,\"field_display_name\":\"Active users (14d)\"},{\"type\":\"number_metric_change\",\"metric\":\"unique_users\",\"duration\":14,\"relative_to\":14,\"field_display_name\":\"Active users % change (14d)\"},{\"type\":\"last_touch\"}]}&date_term:{\"type\":\"date\",\"term\":\"date\",\"eq\":0}";
	        
	        // Encoding POST data
	        try{
	        	List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
	        	nameValuePair.add(new BasicNameValuePair("query", body));
	        	httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
	        	httpPost.setHeader("app-token", "1a1c626e8cdca0a80ae61b73ee0a1909941ab3d7mobile+testme@totango.com");
				httpPost.setHeader("Accept", "application/json, text/javascript, */*; q=0.01");
	        	httpPost.setHeader("X-Requested-With", "XMLHttpRequest");
			}  catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
	        
	        // Make an HTTP POST request
	        try {
	            // make GET request to the given URL
	            HttpResponse httpResponse = httpClient.execute(httpPost);
	            
	            // receive response as inputStream
	            inputStream = httpResponse.getEntity().getContent();
	 
	            // convert inputstream to string
	            if(inputStream == null){
	            	return null;
	            }
	            
	            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
	            StringBuffer buffer = new StringBuffer();
             String line;
             while ((line = reader.readLine()) != null) {
                 buffer.append(line + "\n");
             }

             if (buffer.length() == 0) {
                 // Stream was empty.  No point in parsing.
             	return null;
             }
             Log.e(LOG_TAG, buffer.toString());
             jsonResult = buffer.toString();
	        } catch (Exception e) {
	        	e.printStackTrace();
	        }
	        
	        try{
	        	return getDataFromJson(jsonResult);
	        } catch(JSONException e){
	        	e.printStackTrace();
	        }
	        
			return null;
		}
		
		@Override
		protected void onPostExecute(List<List<String>> result) {
			listener.fetchDataPieFinish(result);
			
		}
}
