package com.javierc.albuquerquenow;


import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.javierc.albuquerquenow.fetch.JSONFetch;
import com.javierc.albuquerquenow.hlperobj.VenueData;

public class SearchMap extends MapActivity{
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search_map, menu);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayShowTitleEnabled(false);
		getActionBar().setIcon(R.drawable.home);
		
		SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();

		searchView.setOnQueryTextListener(n());
		

		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == android.R.id.home) {
			this.finish();
		} else if (item.getItemId() == R.id.action_legalnotices) {
			String LicenseInfo = GooglePlayServicesUtil.getOpenSourceSoftwareLicenseInfo(
					getApplicationContext());
			AlertDialog.Builder LicenseDialog = new AlertDialog.Builder(SearchMap.this);
			LicenseDialog.setTitle("Legal Notices");
			LicenseDialog.setMessage(LicenseInfo);
			LicenseDialog.show();
			
		}
		return super.onOptionsItemSelected(item);
	}

	private OnQueryTextListener n() {
		// TODO Auto-generated method stub
		return new OnQueryTextListener() {
			
			@Override
			public boolean onQueryTextSubmit(String arg0) {
				// TODO Auto-generated method stub
	            InputMethodManager inputMethodManager = (InputMethodManager)  getSystemService(INPUT_METHOD_SERVICE);
	            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
				//Toast.makeText(getBaseContext(), arg0, Toast.LENGTH_SHORT).show();
	            try {
	                new SearchFoursquare().execute(
	                        String.valueOf(googlemap.getMyLocation().getLatitude()),
	                        String.valueOf(googlemap.getMyLocation().getLongitude()),
	                        arg0                        
	                            );
				} catch (Exception e) {
					// TODO: handle exception
				}

                

				return false;
			}
			
			@Override
			public boolean onQueryTextChange(String arg0) {
				// TODO Auto-generated method stub
				googlemap.clear();
				return false;
			}
			
			
		};
	}

    private class SearchFoursquare extends AsyncTask<String, Void, List <VenueData>>{
        private JSONObject json;
        private JSONObject response;
        private JSONArray venues;
       
        @Override
        protected List <VenueData> doInBackground(String... params) {
        	List <VenueData> data = new ArrayList<VenueData>();
            if (params[2].contains(" ")) {
                params[2] = params[2].replace(" ", "+");
            }
            try {
                json = new JSONFetch().getJSONfromUrl("https://api.foursquare.com/v2/venues/search?ll="+ params[0] +","+ params[1] + "&query='"+params[2]+"'&oauth_token=QHWKC5NBUGVO2XSHGD5LVCKV5QXWK20GTAUSQHDG51PC32AA&v=20130212");

                response = json.getJSONObject("response");
                venues = response.getJSONArray("venues");

                for (int i = 0; i < venues.length(); i++) {
                    JSONObject v = venues.getJSONObject(i);

                    String name = v.optString("name");

                    JSONObject loc = v.getJSONObject("location");
                    String lat = loc.getString("lat");
                    String lng = loc.getString("lng");

                    JSONObject here = v.getJSONObject("hereNow");
                    JSONObject stats = v.getJSONObject("stats");
                    String statsCount = stats.optString("checkinsCount");
                    String website = v.optString("url","");
                    String count = here.optString("count");

                    
                    data.add(new VenueData(name, website, Double.parseDouble(lat), Double.parseDouble(lng), Integer.parseInt(count), Long.parseLong(statsCount)));

                }
            } catch (JSONException ex) {
                Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception e) {
				// TODO: handle exception
			}
            return data;
        }

        @Override
        protected void onPostExecute(List <VenueData> result) {
        	googlemap.clear();
        	if (result.size() > 0) {
                for (int i = 0; i < result.size(); i++) {
                	VenueData v = result.get(i);
                	
                    googlemap.addMarker(new MarkerOptions()
	                    .title(v.get_name())
	                    .snippet( "<html><body><b>Name: </b>"+ v.get_name()
								 +"<br><b>Currently checked in: </b>"+ v.get_here()
								 +"<br><b>Total checkins: </b>"+ v.get_total()
								 +"<br><b>Website: </b>"+ "<a href=\"" + v.get_website() + "\">Visit</a>"
								 +"</body></html>")
	                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
	                    .position(new LatLng(v.get_lat(), v.get_lng()))
	                    );
				}
			} else {
				Toast.makeText(getBaseContext(), "Sorry nothing matched", Toast.LENGTH_SHORT).show();
			}
            super.onPostExecute(result);
        }

    }
    
    @Override
    protected void onPause() {
    	// TODO Auto-generated method stub
    	googlemap.clear();
    	super.onPause();
    }
}
