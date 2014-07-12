package com.example.clientexample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.TextView;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
@SuppressLint("NewApi")
public class ParseJSON extends Activity {
	
  
/** Called when the activity is first created. */
	
  @TargetApi(Build.VERSION_CODES.GINGERBREAD)
@SuppressLint("NewApi")
@Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    /*# Just for testing, allow network access in the main thread
    # NEVER use this is productive code */
    StrictMode.ThreadPolicy policy = new StrictMode.
    ThreadPolicy.Builder().permitAll().build();
    StrictMode.setThreadPolicy(policy);
   
    setContentView(R.layout.main);
    TextView textViewResult = (TextView) findViewById(R.id.textView1);
    
    //----------------------------//
    JSONArray contacts = new JSONArray();
    String readTwitterFeed = readTwitterFeed();
    try {
      JSONObject jsonObject = new JSONObject(readTwitterFeed);
      Log.i(ParseJSON.class.getName(),
          "Number of entries " + jsonObject.length());
      contacts = jsonObject.getJSONArray("contacts");
      Log.d("jsonObject", " " +jsonObject.getJSONArray("contacts"));
      for (int i = 0; i < contacts.length(); i++) {
        Log.d("LENGTH", " "+contacts.length());
    	JSONObject c = contacts.getJSONObject(i);
    	Log.d("c.name", c.getString("name"));
    	textViewResult.append(c.getString("name")+"\n");
        Log.i(ParseJSON.class.getName(), c.getString("name"));
      }
    }catch (Exception e) {
    	Log.d("ERRRRORRR","ERRORRR");
    	e.printStackTrace();
    }
  }

  public String readTwitterFeed() {
    StringBuilder builder = new StringBuilder();
    HttpClient client = new DefaultHttpClient();
    HttpGet httpGet = new HttpGet("http://api.androidhive.info/contacts/");
    try {
      HttpResponse response = client.execute(httpGet);
      StatusLine statusLine = response.getStatusLine();
      int statusCode = statusLine.getStatusCode();
      if (statusCode == 200) {
        HttpEntity entity = response.getEntity();
        InputStream content = entity.getContent();
        BufferedReader reader = new BufferedReader(new InputStreamReader(content));
        
        String line;
        while ((line = reader.readLine()) != null) {
          builder.append(line);
        }
      } else {
        Log.e(ParseJSON.class.toString(), "Failed to download file");
      }
    } catch (ClientProtocolException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return builder.toString();
    
  }
} 