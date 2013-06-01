package com.javierc.albuquerquenow;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.javierc.albuquerquenow.util.WifiParseCSV;
import com.javierc.albuquerquenow.util.WifiParseCSV.WifiSpot;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

public class WifiMap extends MapActivity{

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayShowTitleEnabled(false);
		getActionBar().setIcon(R.drawable.arrow_left);
		new AddLocationsTask(this).execute("http://documents.cabq.gov/GIS/itsd/wifi/wifiaccesspoints.csv");
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub		
		if (item.getItemId() == android.R.id.home) {
			this.finish();
		} 
		return super.onOptionsItemSelected(item);
	}
	
	class AddLocationsTask extends AsyncTask<String, Void, List<MarkerOptions>>{
		
	    private Activity activity;
	    
	    public AddLocationsTask(Activity a) {
			// TODO Auto-generated constructor stub
	    	activity = a;
		}

		@Override
		protected List<MarkerOptions> doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			List<WifiSpot> spots = new ArrayList<WifiSpot>();
			List<MarkerOptions> markerOptsList = new ArrayList<MarkerOptions>();
			try {
				spots = new WifiParseCSV().parseFromUrl(new URL(params[0]));
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				//System.out.println(spots.size());
				for (int i = 0; i < spots.size(); i++) {
					 MarkerOptions markOptions = new MarkerOptions();
					 double lat = ((WifiSpot)spots.get(i)).get_ll()[0];
					// System.out.println(lat);
					 double lng = ((WifiSpot)spots.get(i)).get_ll()[1];
					 //System.out.println(lng);
					 //System.out.println(((WifiSpot)spots.get(i)).get_ssid());
					 
					 //markOptions.title(((WifiSpot)spots.get(i)).get_ssid());
					 //markOptions.snippet("");
					 markOptions.position(new LatLng(lat, lng));
					 
					 
					 markerOptsList.add(markOptions);
				}
			} catch (Exception e) {
				// TODO: handle exception
			}

			
			
			return markerOptsList;
		}
		
		
		@Override
		protected void onPostExecute(List<MarkerOptions> result) {
			// TODO Auto-generated method stub
			if(result == null){
				Toast.makeText(getBaseContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
			} else{
				for (int i = 0; i < result.size(); i++) {
					((MapActivity)activity).googlemap.addMarker((MarkerOptions) result.get(i));
				}
			}
			
			super.onPostExecute(result);
		}
		
		
	}
}
