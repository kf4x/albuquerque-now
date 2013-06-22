package com.javierc.albuquerquenow;



import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.javierc.albuquerquenow.hlperobj.FireData;

public class FireActivity extends MapActivity implements ActionBar.OnNavigationListener{
	ActionBar actionBar;
	ProgressDialog pdialog;
	List<FireData> fireData;
	String[] fireNames;
	boolean firstTime = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		super.onCreate(savedInstanceState);
		
		pdialog=new ProgressDialog(this);
		pdialog.setCancelable(false);
		//move camera to albuqueruque
		googlemap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(34.134542, -106.051025)));
		googlemap.animateCamera(CameraUpdateFactory.zoomTo(6));


		 runTask(); //sets adatper
		
		// Set up the action bar to show a dropdown list.
		actionBar = getActionBar();
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);



	}

	@Override
	public boolean onNavigationItemSelected(int arg0, long arg1) {
		// TODO Auto-generated method stub
		
        if (firstTime) {
        	firstTime = false;

            return true;
        }
		
		FireData sFire = fireData.get(arg0);
//		googlemap.clear();
		new PlotFireTask().execute(fireNames[arg0]);

		googlemap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(sFire.get_lat(), sFire.get_lng())));
		googlemap.animateCamera(CameraUpdateFactory.zoomTo(10));
		return false;
	}
	private void runTask() {
		pdialog.setMessage("Downloading fire list....");
		pdialog.show();
		
		new GetFireListTask().execute();
		
	}
	
	//this is similar to a callback in that after Task this function will be called and set the adapter
	protected void setAdapt(){
		final String[] dropdownValues = fireNames;

		// Specify a SpinnerAdapter to populate the dropdown list.
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				actionBar.getThemedContext(),
				android.R.layout.simple_spinner_item, android.R.id.text1,
				dropdownValues);

		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// Set up the dropdown list navigation in the action bar.
		actionBar.setListNavigationCallbacks(adapter, this);
	}
	
	
	class GetFireListTask extends AsyncTask<Void, Void, List<FireData>> {
        private JSONObject json;
        private JSONArray fires;
		@Override
		protected List<FireData> doInBackground(Void...voids) {
			List <FireData> data = new ArrayList<FireData>();
			
			// TODO Auto-generated method stub
            try {
				json = new JSONParser().getJSONfromUrl("http://rmgsc.cr.usgs.gov/ArcGIS/rest/services/geomac_dyn/MapServer/0/query?where=UPPER%28STATE%29%3DUPPER%28%27nm%27%29&returnGeometry=false&outSR=4326&outFields=fire_name%2C+acres%2C+latitude%2C+longitude&f=pjson");
				fires = json.getJSONArray("features");
//				Log.i("Post", fires.toString());
            } catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
//            Log.i("Post", json.toString());

            
            for (int i = 0; i < fires.length(); i++) {
            	JSONObject fire = null; 
            	try {
					fire = ((JSONObject)fires.get(i)).getJSONObject("attributes");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	data.add(new FireData(fire.optString("fire_name"), fire.optLong("acres"), fire.optDouble("latitude"), fire.optDouble("longitude")));
				
			}
			return data;
		}
		
		@Override
		protected void onPostExecute(List<FireData> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			int s = result.size();
			fireData = result;
			fireNames = new String[s];
			for (int i = 0; i < s; i++) {
				FireData f = result.get(i);
				fireNames[i] = f.toString();
//				Log.i("Post", fireNames[i]);
				googlemap.addMarker(
						new MarkerOptions()
						.snippet( "<html><body><b>Name: </b>"+ f.get_fireName() 
								 +"<br><b>Acres burned: </b>"+ f.get_acres()
								 +"</body></html>")
						.title(f.get_fireName())
						.icon(BitmapDescriptorFactory.fromResource(R.drawable.fire_icon))
						.position(new LatLng(f.get_lat(), f.get_lng())));
			}
			setAdapt();
			// Set up the dropdown list navigation in the action bar.
			pdialog.dismiss();
			
			
		}
		
	}
	
	class PlotFireTask extends AsyncTask<String, Void, List<PolylineOptions>>{

        private JSONObject json;
        private JSONArray fireD;
		@Override
		protected List<PolylineOptions> doInBackground(String... params) {
			List<PolylineOptions> latlng = new ArrayList<PolylineOptions>();
			// TODO Auto-generated method stub
			if (params[0].contains(" ")) {
                params[0] = params[0].replace(" ", "%20");
            }
            json = new JSONParser().getJSONfromUrl("http://rmgsc.cr.usgs.gov/ArcGIS/rest/services/geomac_dyn/MapServer/1/query?text=" + params[0] + "&outSR=4326&f=pjson");
            try {
				fireD = json.getJSONArray("features");
				JSONObject ob = fireD.getJSONObject(0);
				JSONObject geo = ob.getJSONObject("geometry");
				JSONArray co = geo.getJSONArray("rings");
				
				for (int i = 0; i < co.length(); i++) {
					JSONArray currRing = co.getJSONArray(i);
					
					PolylineOptions rectOptions = new PolylineOptions();
					for (int j = 0; j < currRing.length(); j++) {
//						double s = currRing[j][1];
//						Log.i("Point", );
						rectOptions.add(new LatLng(((JSONArray)currRing.get(j)).optDouble(1),((JSONArray)currRing.get(j)).optDouble(0)));
						
					}
                	rectOptions.color(Color.RED);
                	rectOptions.width(3);
					latlng.add(rectOptions);
				}
				
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
			return latlng;
		}
		@Override
		protected void onPostExecute(List<PolylineOptions> result) {
			// TODO Auto-generated method stub
			
			for (int j = 0; j < result.size(); j++) {
				googlemap.addPolyline(result.get(j));				
			}
			super.onPostExecute(result);
		}
	}
	
}
