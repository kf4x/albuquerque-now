package com.javierc.albuquerquenow;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.javierc.albuquerquenow.util.KmlParse;

//import com.javier.opendata.KMLParse;

public class KmlTask extends AsyncTask<String, Void, List<Object>>{
    private Activity activity;

    
    public KmlTask(Activity a) {
		// TODO Auto-generated constructor stub
    	activity = a;
	}

    
    @Override
    protected void onPreExecute() {
    	// TODO Auto-generated method stub
    	
    	super.onPreExecute();
    }

    @Override
    protected List<Object> doInBackground(String... params) {
        JSONArray marks;
        JSONObject json = null;
    	//KmlParse p = new KmlParse();
    	List<Object> mapObjects = new ArrayList<Object>();
    	try {
    		
//    		if (((MapActivity)activity).useOfflineRoutes == true) {
//    			//json = new KmlToJSON().KMLFromFile(activity.getAssets().open(params[0]));
//    			
//    			if (params.length < 2) {
//    				json = new KmlParse().KMLFromFile(activity.getAssets().open(params[0]));
//    				
//				} else if (params[1] == "kmz"){
//					
//					json = new KmlParse().KMZFromFile(new File(params[0]),Environment.getExternalStorageDirectory() + "/New Folder");
//				}
//			} else if (!((MapActivity)activity).useOfflineRoutes) {
//				Log.d("=====", params[0] +"");
//    			if (params.length < 2) {
//    				Log.d("=====", params[0] +"");
//    				json = new KmlParse().KMLFromURL(new URL(params[0]));
//    				
//				} else if (params[1] == "kmz"){
//					Log.d("=====", params[0] +"");
//					
//				}
//				
////				json = new KmlParse().KMZFromURL(new URL(params[0]), Environment.getExternalStorageDirectory() + "/New Folder");
//
//			}			
//    		json = new KmlParse().KMZFromURL(new URL(params[0]), Environment.getExternalStorageDirectory() + "/New Folder");
    		json = new KmlParse().KMLFromURL(new URL(params[0]));
    		
//			json = new KmlParse().KMZFromFile(new File(params[0]),Environment.getExternalStorageDirectory() + "/New Folder");

		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
            marks = json.getJSONArray("placemarks");
         
            Log.d("=====", "json" + marks.length());
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

                //JSONArray coord = v.getJSONArray("coordinates");
                JSONObject coordinates = loc.getJSONObject(0);			//<<=====
                
                if (coordinates.getString("type") == "linestring") {
                    JSONArray coord =  coordinates.getJSONArray("coordinates");
                    PolylineOptions rectOptions = new PolylineOptions();
                    for (int j = 0; j < coord.length(); j++) {
						JSONObject ll = coord.getJSONObject(j);
 
						double lt = ll.getDouble("lat");
						double lg = ll.getDouble("lng");
//						Log.d("lat stuff", lt + " " + lg);
                    	rectOptions.add(new LatLng(lt,lg));
					}
                	rectOptions.color(Color.RED);
                	rectOptions.width(6);
                	mapObjects.add(rectOptions);	
				} else if (coordinates.getString("type") == "point") {
                    JSONArray coord =  coordinates.getJSONArray("coordinates");
                    MarkerOptions markOptions = new MarkerOptions();
                    for (int j = 0; j < coord.length(); j++) {
						JSONObject ll = coord.getJSONObject(j);
 
						double lt = ll.getDouble("lat");
						double lg = ll.getDouble("lng");
//						Log.d("lat stuff", lt + " " + lg);
						markOptions.position(new LatLng(lt, lg));
					}
                    
                    markOptions.snippet(desc);
                    markOptions.title(name);
                    markOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                    mapObjects.add(markOptions);
                   
				}

            }
        } catch (JSONException ex) {
            Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception e) {
			// TODO: handle exception
		}
        
        return mapObjects;
    }

    @Override
    protected void onPostExecute(List<Object> result) {
    	Log.d("=====", "done");
    	if(result.size() > 0){
			for (int i = 0; i < result.size(); i++) {
				//googlemap.addPolyline(result.get(i));
				if (result.get(i) instanceof PolylineOptions && activity instanceof MapActivity) {
					((MapActivity)activity).googlemap.addPolyline((PolylineOptions) result.get(i));
				} else if (result.get(i) instanceof MarkerOptions && activity instanceof MapActivity) {
					((MapActivity)activity).googlemap.addMarker((MarkerOptions) result.get(i));
				}
			}

    	}
    	
    	if ((activity) instanceof ExploreMap) {
    		((ExploreMap)activity).pdialog.dismiss();
		}

        super.onPostExecute(result);
    }

}