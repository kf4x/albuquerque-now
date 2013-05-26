package com.example.albuquerquenow;


import java.util.List;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import com.google.android.gms.maps.model.Marker;


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
			
			startActivity(new Intent(this, LegalNoticesActivity.class));
		} else if (item.getItemId() == R.id.bus_refresh) {
			//first delete all the current markers
			refreshMarkers();
			//plot new markers
			plotBus(actionBar.getSelectedNavigationIndex());
			
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
		//Toast.makeText(getBaseContext(), String.valueOf(l.size()), Toast.LENGTH_SHORT).show();
	}
	
	private void refreshMarkers(){
		for (int i = 0; i < markers.size(); i++) {
			((Marker)markers.get(i)).remove();
		}
	}
	
    private boolean plotInformatoin(int i) {

		String routeURL = "http://data.cabq.gov/transit/realtime/trace/";		
		String routeFilename = "trace" + busNumber[i]+ ".kml";
		
		new AddTransMapObjTask(this).execute(routeURL, routeFilename, "");
		//new AddLiveBus().execute("http://data.cabq.gov/community/art/publicart/PublicArt.kmz");
		return false;
	}
    
    private boolean plotBus(int i) {
		if (i == 40) {
			new AddTransMapObjTask(this,this).execute("http://data.cabq.gov/transit/UNM/shuttle.kml","","fullurl");
		} else{
	    	String busURL = "http://data.cabq.gov/transit/realtime/introute/";
	    	String busFilename = "introute" + busNumber[i]+ ".kml";
			new AddTransMapObjTask(this, this).execute(busURL, busFilename, "bus");
    	}
    	return false;
	}
}
