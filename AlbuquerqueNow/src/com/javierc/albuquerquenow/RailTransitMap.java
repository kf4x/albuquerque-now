package com.javierc.albuquerquenow;

//import java.io.IOException;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//import com.example.albuquerquenow.util.KmlParse;
//import com.google.android.gms.maps.model.BitmapDescriptorFactory;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.MarkerOptions;
//import com.google.android.gms.maps.model.PolylineOptions;
//import android.graphics.Color;
//import android.os.AsyncTask;

import android.os.Bundle;
import android.view.MenuItem;
import com.javierc.albuquerquenow.R;

public class RailTransitMap extends MapActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		useOfflineRoutes = false;
		// TODO Auto-generated method stub
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayShowTitleEnabled(false);
		getActionBar().setIcon(R.drawable.home);
		
		//new AddMapObjsTask(this).execute("https://maps.google.com/maps/ms?ie=UTF8&t=m&source=embed&msa=0&output=kml&msid=201876092879256242075.0004c7b592eaf1c192359");
		//new AddMapObjsTask(this).execute("http://data.cabq.gov/community/bikepaths/BikePaths.kmz");

//		new AddMapObjsTask(this).execute("http://coagisweb.cabq.gov/arcgis/rest/services/public/InteragencyTrails/MapServer/4/query?where=OBJECTID_1+%3E1&text=&objectIds=&time=&geometry=&geometryType=esriGeometryEnvelope&inSR=&spatialRel=esriSpatialRelIntersects&relationParam=&outFields=&returnGeometry=true&maxAllowableOffset=&geometryPrecision=&outSR=&returnIdsOnly=false&returnCountOnly=false&orderByFields=&groupByFieldsForStatistics=&outStatistics=&returnZ=false&returnM=false&gdbVersion=&f=kmz");
		new KmlTask(this).execute("https://maps.google.com/maps/ms?ie=UTF8&t=m&source=embed&msa=0&output=kml&msid=201876092879256242075.0004c7b592eaf1c192359");
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
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		googlemap.clear();
		super.onPause();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		new KmlTask(this).execute("https://maps.google.com/maps/ms?ie=UTF8&t=m&source=embed&msa=0&output=kml&msid=201876092879256242075.0004c7b592eaf1c192359");
		super.onResume();
	}

}
