


package com.example.albuquerquenow;

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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.AsyncTask;

import com.example.albuquerquenow.R;
import com.example.albuquerquenow.R.drawable;
import com.example.albuquerquenow.util.KmlParse;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;


public class BusObjectsTask extends AsyncTask<String, Void, List<Object>>{
    private JSONObject json;
    private JSONArray marks;
    private Activity activity;
    private OnCompleteCB callback;
    
    public BusObjectsTask(Activity a) {
		// TODO Auto-generated constructor stub
    	activity = a;
	}
    public BusObjectsTask(Activity a, OnCompleteCB c) {
		// TODO Auto-generated constructor stub
    	activity = a;
    	callback = c;
	}   
    
    @Override
    protected List<Object> doInBackground(String... params) {
    	//KmlParse p = new KmlParse();
    	List<Object> mapObjects = new ArrayList<Object>();
    	Matrix mat = new Matrix();
    	try {
    		//if (activity instanceof MapActivity) {
    		//Log.d("testing this", String.valueOf(params.length));
    		if (((MapActivity)activity).useOfflineRoutes == true && params[2].isEmpty()) {
    			json = new KmlParse().KMLFromFile(activity.getAssets().open("routes/"+params[1]));
    			//Log.d("testing this", params[1]);
			} else if (!((MapActivity)activity).useOfflineRoutes) {
				json = new KmlParse().KMLFromURL(new URL(params[0] + params[1]));
			} else if (params[2].contains("bus")) {
				json = new KmlParse().KMLFromURL(new URL(params[0] + params[1]));
			} else if (params[2].contains("fullurl")) {
				json = new KmlParse().KMLFromURL(new URL(params[0]));
			}			
			//}

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
//            response = json.getJSONObject("placemarks");
            marks = json.getJSONArray("placemarks");
            //markerMap.clear();
            
            

            for (int i = 0; i < marks.length(); i++) {
                JSONObject v = marks.getJSONObject(i);
                String name ="";double heading=0;
                try {
                	name = v.getString("name");
                	heading = v.getDouble("heading");
				} catch (Exception e) {
					// TODO: handle exception
				}
               
                
                String desc = v.getString("description");

                JSONArray loc = v.getJSONArray("geometry");

                //JSONArray coord = v.getJSONArray("coordinates");
                JSONObject coordinates = loc.getJSONObject(0);
                
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
                    
                    mat.preRotate((float) heading);///in degree
                    Bitmap mBitmap = BitmapFactory.decodeResource(activity.getResources(), R.drawable.arrows);
                    mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), mat, true);
                    
                    
                    markOptions.snippet(desc);
                    markOptions.title(name);
                    markOptions.icon(BitmapDescriptorFactory.fromBitmap(mBitmap));
                    mapObjects.add(markOptions);
                    mBitmap.recycle();
                    mat.reset();
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
    	List<Marker> l = new ArrayList<Marker>();
    	if(result.size() > 0){
			for (int i = 0; i < result.size(); i++) {
				//googlemap.addPolyline(result.get(i));
				if (result.get(i) instanceof PolylineOptions && activity instanceof MapActivity) {
					((MapActivity)activity).googlemap.addPolyline((PolylineOptions) result.get(i));
				} else if (result.get(i) instanceof MarkerOptions) {
					Marker marker = ((MapActivity)activity).googlemap.addMarker((MarkerOptions) result.get(i));
					l.add(marker);
				}
//				(PolylineOptions) result.get(i))
			}

    	}
    	
    	if (callback != null) {
    		callback.OnCompleteCB(l);
		}
        super.onPostExecute(result);
    }

}
