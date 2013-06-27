package com.javierc.albuquerquenow;


import java.io.IOException;
import java.io.InputStream;
import java.security.Provider;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class TransitMap extends MapActivity implements ActionBar.OnNavigationListener, OnCompleteCB{
	private String[] busNumber = {
			"1","2","5","6","7","8","10","11","12","13","1618","31","34",
			"36","40","50","51","53","54", "66","92","93","94","96","97",
			"98","140","141","155","157","162","198","217","222",
			"250","251","551","766","777","790"};
	
	ActionBar actionBar;
	private List<Marker> markers;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Set up the action bar to show a dropdown list.
		actionBar = getActionBar();
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		final String[] dropdownValues = getResources().getStringArray(
				R.array.dropdown);

		// Specify a SpinnerAdapter to populate the dropdown list.
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				actionBar.getThemedContext(),
				android.R.layout.simple_spinner_item, android.R.id.text1,
				dropdownValues);

		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// Set up the dropdown list navigation in the action bar.
		actionBar.setListNavigationCallbacks(adapter, this);
		
		googlemap.getUiSettings().setRotateGesturesEnabled(false);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.transit_map, menu);
		menu.findItem(R.id.offline_routes).setChecked(useOfflineRoutes);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if(item.getItemId() == R.id.offline_routes){
			
			if(item.isChecked()){ item.setChecked(false); useOfflineRoutes = false;}
			else{ item.setChecked(true); useOfflineRoutes = true;}
			
		} else if (item.getItemId() == R.id.action_legalnotices) {
			String LicenseInfo = GooglePlayServicesUtil.getOpenSourceSoftwareLicenseInfo(
					getApplicationContext());
			AlertDialog.Builder LicenseDialog = new AlertDialog.Builder(TransitMap.this);
			LicenseDialog.setTitle("Legal Notices");
			LicenseDialog.setMessage(LicenseInfo);
			LicenseDialog.show();
					
				
		} else if (item.getItemId() == R.id.bus_refresh) {
			//first delete all the current markers
			refreshMarkers();
			//plot new markers
			plotBus(actionBar.getSelectedNavigationIndex());
			
		} else if (item.getItemId() == R.id.bus_stops){
			new PlotStopsTask(this).execute();
		}
		return super.onOptionsItemSelected(item);
	}
	

	@Override
	public boolean onNavigationItemSelected(int arg0, long arg1) {
		// TODO Auto-generated method stub
		googlemap.clear();
		if (arg0 > 39) { plotBus(arg0); return false; }
		plotInformatoin(arg0);
		plotBus(arg0);
		return false;
	}
	

    @Override
    protected void onPause() {
    	// TODO Auto-generated method stub
    	googlemap.clear();
    	super.onPause();
    }
    
    @Override
    protected void onResume() {
    	// TODO Auto-generated method stub
//    	actionBar.getSelectedNavigationIndex();
    	int i = actionBar.getSelectedNavigationIndex();
    	if (i > 39) {
    		plotBus(i);
		}
    	else{
	    	plotInformatoin(i);
	    	plotBus(i);
    	}
    	super.onResume();
    }

	@Override
	public void OnCompleteCB(List<Marker> l) {
		// TODO Auto-generated method stub
		if (markers != null) {
			refreshMarkers();
		}
		markers = l;
	}
	
	private void refreshMarkers(){
		for (int i = 0; i < markers.size(); i++) {
			((Marker)markers.get(i)).remove();
		}
	}
	
    private boolean plotInformatoin(int i) {

		String routeURL = "http://data.cabq.gov/transit/realtime/trace/";		
		String routeFilename = "trace" + busNumber[i]+ ".kml";
		
		new BusObjectsTask(this).execute(routeURL, routeFilename, "");
		//new AddLiveBus().execute("http://data.cabq.gov/community/art/publicart/PublicArt.kmz");
		return false;
	}
    
    private boolean plotBus(int i) {
		if (i == 40) {
			new BusObjectsTask(this,this).execute("http://data.cabq.gov/transit/UNM/shuttle.kml","","fullurl");
		} else{
	    	String busURL = "http://data.cabq.gov/transit/realtime/introute/";
	    	String busFilename = "introute" + busNumber[i]+ ".kml";
			new BusObjectsTask(this, this).execute(busURL, busFilename, "bus");
    	}
    	return false;
	}
    

    
    class PlotStopsTask extends AsyncTask<Void, Void, List<MarkerOptions>>{

    	JSONObject json = null;
    	JSONArray marks;
    	private Activity activity;
    	public PlotStopsTask(Activity a) {
    		activity = a;
		}
    	
		@Override
		protected List<MarkerOptions> doInBackground(Void... params) {
			List<MarkerOptions> markerOptions = new ArrayList<MarkerOptions>();
			// TODO Auto-generated method stub
			try {
				InputStream file = activity.getAssets().open("busstops/bstopSM.json");
		        byte[] data = new byte[file.available()];
		        file.read(data);
		        file.close();
		        json = new JSONObject(new String(data));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			try {
				marks = json.getJSONArray("placemarks");
	            for (int i = 0; i < marks.length(); i++) {
	                JSONObject v = marks.getJSONObject(i);
	                String name ="";
	                try {
	                	name = v.getString("name");
					} catch (Exception e) {
						// TODO: handle exception
					}
	                
	                String desc = v.getString("description");

	                JSONArray loc = v.getJSONArray("geometry");
	                JSONObject coordinates = loc.getJSONObject(0);
	                
                    JSONArray coord =  coordinates.getJSONArray("coordinates");
                    MarkerOptions markOptions = new MarkerOptions();
                    double lt = 0.0;
					double lg = 0.0;
                    for (int j = 0; j < coord.length(); j++) {
						JSONObject ll = coord.getJSONObject(j);
 
						lt = ll.getDouble("lat");
						lg = ll.getDouble("lng");
						
//						Log.d("lat stuff", lt + " " + lg);
						markOptions.position(new LatLng(lt, lg));
					}
                    

                    if (isInsideArea(myLoc, new LatLng(lt, lg), .35)) {
                        markOptions.snippet(desc);
                        markOptions.title(name);
                        markerOptions.add(markOptions);
					}
                    
                    

	            }
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			return markerOptions;
		}
		
	    private double distance(LatLng c, LatLng o){
	        final int R = 6371;

	        Double latDistance = Math.toRadians(o.latitude-c.latitude);
	        Double lonDistance = Math.toRadians(o.longitude-c.longitude);
	        Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
	                   Math.cos(Math.toRadians(c.latitude)) * Math.cos(Math.toRadians(o.latitude)) *
	                   Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
	        Double cv = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
	        Double distance = R * cv;
	         
	        return distance;
	    }
	    
	    private boolean isInsideArea(LatLng c, LatLng o, double r ){
	    	return distance(c, o) <= r;
	    }
	    
	    @Override
	    protected void onPostExecute(List<MarkerOptions> result) {	    	
	    	if(result.size() > 0){
				for (int i = 0; i < result.size(); i++) {
					
					googlemap.addMarker(result.get(i));
				}

	    	}
	    	super.onPostExecute(result);
	    }
    	
    }
}
