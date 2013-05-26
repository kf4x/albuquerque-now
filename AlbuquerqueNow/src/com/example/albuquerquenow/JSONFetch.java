package com.example.albuquerquenow;

import java.io.InputStream;

import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

public class JSONFetch {
    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = ""; 
    
    public JSONObject getJSONfromUrl(String url){
        //make connection to url and try to connect
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
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
            }
            else {
            Log.e(JSONFetch.class.toString(), "Failed to download file");
          }
        } catch (ClientProtocolException e) {
          e.printStackTrace();
        } catch (IOException e) {
          e.printStackTrace();
        }
        
        try {
            json = builder.toString();
        } catch (Exception e) {
        }
        
        try {
            jObj = new JSONObject(json);
        } catch (Exception e) {
        }
        
        
        return jObj;
    }
}
