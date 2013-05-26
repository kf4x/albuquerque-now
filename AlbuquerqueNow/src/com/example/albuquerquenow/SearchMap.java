package com.example.albuquerquenow;


import java.util.ArrayList;
import java.util.HashMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Toast;

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
    protected void addToMap(ArrayList<HashMap<String, String>> venueMap) {
        googlemap.clear();

        LatLng pos = null;
            for (int i = 0; i < venueMap.size(); i++) {
                String lat = venueMap.get(i).get("lat");
                String lng = venueMap.get(i).get("lng");
                try {
                    pos = new LatLng(Double.valueOf(lat), Double.valueOf(lng));
                } catch (Exception e) {
                }
                googlemap.addMarker(new MarkerOptions()
                        .title(venueMap.get(i).get("name").toString())
                        .snippet(venueMap.get(i).get("here").toString())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                        .position(pos)
                        );
            }
    }
    private class SearchFoursquare extends AsyncTask<String, Void, Void>{
        private JSONObject json;
        private JSONObject response;
        private JSONArray venues;
        private ArrayList<HashMap<String, String>> venueMap = new ArrayList<HashMap<String, String>>();
        @Override
        protected Void doInBackground(String... params) {
            if (params[2].contains(" ")) {
                params[2] = params[2].replace(" ", "+");
            }
            try {
                json = new JSONFetch().getJSONfromUrl("https://api.foursquare.com/v2/venues/search?ll="+ params[0] +","+ params[1] + "&query='"+params[2]+"'&oauth_token=QHWKC5NBUGVO2XSHGD5LVCKV5QXWK20GTAUSQHDG51PC32AA&v=20130212");

                response = json.getJSONObject("response");
                venues = response.getJSONArray("venues");
                venueMap.clear();
                for (int i = 0; i < venues.length(); i++) {
                    JSONObject v = venues.getJSONObject(i);

                    String name = v.getString("name");

                    JSONObject loc = v.getJSONObject("location");
                    String lat = loc.getString("lat");
                    String lng = loc.getString("lng");

                    JSONObject here = v.getJSONObject("hereNow");
                    String count = here.getString("count");

                    HashMap<String, String> map = new HashMap<String, String>();

                    map.put("name", name);
                    map.put("lat", lat);
                    map.put("lng", lng);
                    map.put("here", count);

                    venueMap.add(map);

                }
            } catch (JSONException ex) {
                Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception e) {
				// TODO: handle exception
			}
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
        	if (venueMap.size() > 0) {
                addToMap(venueMap);
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
