package com.example.albuquerquenow;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;


public class MapActivity extends Activity implements LocationListener {
	protected GoogleMap googlemap;
	protected boolean useOfflineRoutes = true;
	Boolean isSatOn = false;
	View v;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		v = (View) findViewById(R.id.l_map);
		if(isGooglePlay()){
			setContentView(R.layout.activity_map);		
			setUpMap();	
		}
	
	}

	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == R.id.action_legalnotices) {
			startActivity(new Intent(this, LegalNoticesActivity.class));
		}
		return super.onOptionsItemSelected(item);
	}
	
	private boolean isGooglePlay() {
		
		int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		
		if(status == ConnectionResult.SUCCESS){
			return(true);
		}
		else{
			((Dialog) GooglePlayServicesUtil.getErrorDialog(status, this, 10)).show();
			//Toast.makeText(this, "Google Play is not available", Toast.LENGTH_SHORT).show();
		}
		return(false);
	}
	

	private void setUpMap() {
		
		if(googlemap == null){
			googlemap = ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();
			
			if( googlemap != null){
								
				googlemap.setMyLocationEnabled(true);
				
				//move camera to albuqueruque
				googlemap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(35.08449090, -106.65113670)));
				googlemap.animateCamera(CameraUpdateFactory.zoomTo(10));
				
				
				googlemap.setOnMapLongClickListener(onLongClickMapSettins());
				googlemap.setOnInfoWindowClickListener(infoClickListener());
				
				//info window
				googlemap.setInfoWindowAdapter(new InfoWindowAdapter() {
					
					@Override
					public View getInfoWindow(Marker arg0) {
						// TODO Auto-generated method stub
						return null;
					}
					
					@Override
					public View getInfoContents(Marker arg0) {
						// TODO Auto-generated method stub
			            // Getting view from the layout file info_window_layout
			            View v = getLayoutInflater().inflate(R.layout.cust_marker_info, null);
			            TextView t = (TextView) v.findViewById(R.id.marker_title);

			            t.setText(arg0.getTitle());

			            // Returning the view containing InfoWindow contents
			            return v;
					}
				});
			}
		}
	}
	
	private OnInfoWindowClickListener infoClickListener() {
		// TODO Auto-generated method stub
		return new OnInfoWindowClickListener() {
			
			@Override
			public void onInfoWindowClick(Marker arg0) {
				// TODO Auto-generated method stub

				Dialog dialog = new Dialog(MapActivity.this);
	            LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            View vi = inflater.inflate(R.layout.webviewdialog, null);
	            dialog.setContentView(vi);
	            dialog.setTitle("About");
	            dialog.setCancelable(true);
	            WebView wb = (WebView) vi.findViewById(R.id.webview);
//	            wb.getSettings().setJavaScriptEnabled(true);
	            String summary = arg0.getSnippet();
	            wb.loadData(summary, "text/html", null);
//	            System.out.println("..loading url..");
	            dialog.show();
			}
		};
	}  


  

    
    
    
	private OnMapLongClickListener onLongClickMapSettins() {
		// TODO Auto-generated method stub
		return new OnMapLongClickListener() {
			
			@Override
			public void onMapLongClick(LatLng arg0) {
				// TODO Auto-generated method stub
				toggleTraffic().show();
				Log.i(arg0.toString(), "User long clicked");
			}
		};
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());
		
		googlemap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
		googlemap.animateCamera(CameraUpdateFactory.zoomTo(10));
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	private  AlertDialog.Builder toggleTraffic() {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle("Map View");

        adb.setPositiveButton("Turn sattellite View" + (isSatOn?"\nOFF":"\nON"), new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int id)
			{
				if(!isSatOn){
					googlemap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
					isSatOn = true;
				}
				else{
					googlemap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
					isSatOn = false;
				}
			}
		});
        adb.setNegativeButton("Turn traffic view" + (googlemap.isTrafficEnabled()?"\nOFF":"\nON"), new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
            	if(googlemap.isTrafficEnabled()){
            		googlemap.setTrafficEnabled(false);
            	}
            	else{
            		googlemap.setTrafficEnabled(true);
            	}
            }
        });
        adb.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.cancel();
			}
		});
		return adb;
	
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		googlemap.clear();
		super.onPause();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map, menu);
		return true;
	}

}
