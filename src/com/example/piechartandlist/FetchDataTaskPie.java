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

import com.example.piechartandlist.PLContract.PieEntry;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

public class FetchDataTaskPie extends AsyncTask<Void, Void, String[]>{
	private final String LOG_TAG = FetchDataTaskPie.class.getSimpleName();
	private AsyncResponseListener listener;
	private PLDbHelper myDbHelper;
	private Context context;
	
	public interface AsyncResponseListener{
		void fetchDataPieFinish(String[] result);
	}
	
	public FetchDataTaskPie(Context context, AsyncResponseListener listener){
		this.listener = listener;
		this.context = context;
	}
	
	private void addColorToDb(String[] colorResult){
		myDbHelper = new PLDbHelper(context);
		myDbHelper.deleteAllPie();
		myDbHelper.addColorInfo(colorResult);
		
		myDbHelper.getPieTableInfo();
		
	}

	private String[] getDataFromJson(String jsonResult) throws JSONException{
    	String[] colorResult = new String[3];//order green yellow red
    	JSONObject object = new JSONObject(jsonResult);
    	JSONObject redObject = object.getJSONObject("hits").getJSONObject("health").getJSONObject("red");
    	JSONObject greenObject = object.getJSONObject("hits").getJSONObject("health").getJSONObject("green");
    	JSONObject yellowObject = object.getJSONObject("hits").getJSONObject("health").getJSONObject("yellow");
    	colorResult[2] = redObject.getString("total_hits");
    	colorResult[0] = greenObject.getString("total_hits");
    	colorResult[1] = yellowObject.getString("total_hits");
    	
    	addColorToDb(colorResult);
    	return colorResult;
    }
    

	@Override
	protected String[] doInBackground(Void... params) {
		android.os.Debug.waitForDebugger();
		InputStream inputStream = null;
        String jsonResult = "";
        // create HttpClient
        HttpClient httpClient = new DefaultHttpClient();
        String uri = "https://appem.totango.com/api/v1/search/accounts/health_dist";
        HttpPost httpPost = new HttpPost(uri);
        String body = "{\"terms\":[{\"type\":\"totango_user_scope\",\"is_one_of\":[\"mobile+testme@totango.com\"]}],\"group_fields\":[{\"type\":\"health\"}]}";
        
        // Encoding POST data
        try{
        	List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
        	nameValuePair.add(new BasicNameValuePair("query", body));
        	httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
        	httpPost.setHeader("app-token", "1a1c626e8cdca0a80ae61b73ee0a1909941ab3d7mobile+testme@totango.com");
        	httpPost.setHeader("Accept", "application/json, text/javascript, *//*; q=0.01");
        	httpPost.setHeader("X-Requested-With", "XMLHttpRequest");
		}  catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        
        // Make an HTTP POST request
        try {
            // make GET request to the given URL
            HttpResponse httpResponse = httpClient.execute(httpPost);
            
            // Transfer the response body JSON file to string
            HttpEntity responseEntity = httpResponse.getEntity();
            if(responseEntity!=null) {
                jsonResult = EntityUtils.toString(responseEntity);
                Log.e(LOG_TAG, jsonResult);
            }
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
	protected void onPostExecute(String[] result) {
		listener.fetchDataPieFinish(result);
	}

}
