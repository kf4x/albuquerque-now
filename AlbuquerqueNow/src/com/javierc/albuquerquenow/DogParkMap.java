package com.javierc.albuquerquenow;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.javierc.albuquerquenow.util.ParkParseCSV;
import com.javierc.albuquerquenow.util.ParkParseCSV.DogPark;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

public class DogParkMap extends MapActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayShowTitleEnabled(false);
		getActionBar().setIcon(R.drawable.arrow_left);
		new AddParksTask(this).execute("http://www.cabq.gov/parksandrecreation/documents/Dog%20Park%20Map%20Plone.csv");
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
	class AddParksTask extends AsyncTask<String, Void, List<MarkerOptions>>{
		
	    private Activity activity;
	    
	    public AddParksTask(Activity a) {
			// TODO Auto-generated constructor stub
	    	activity = a;
		}

		@Override
		protected List<MarkerOptions> doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			List<DogPark> parks = new ArrayList<ParkParseCSV.DogPark>();
			List<MarkerOptions> markerOptsList = new ArrayList<MarkerOptions>();
			try {
				 parks = new ParkParseCSV().parseFromUrl(new URL(params[0]));
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				for (int i = 0; i < parks.size(); i++) {
					 MarkerOptions markOptions = new MarkerOptions();
					 double lat = ((DogPark)parks.get(i)).get_ll()[0];
					 double lng = ((DogPark)parks.get(i)).get_ll()[1];
					 markOptions.title(((DogPark)parks.get(i)).get_name());
					 markOptions.snippet(((DogPark)parks.get(i)).toString());
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
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		new AddParksTask(this).execute("http://www.cabq.gov/parksandrecreation/documents/Dog%20Park%20Map%20Plone.csv");
		super.onResume();
	}
}
