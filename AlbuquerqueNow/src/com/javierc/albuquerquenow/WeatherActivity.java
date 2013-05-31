package com.javierc.albuquerquenow;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import com.javierc.albuquerquenow.WeatherArrayAdapter.ViewHolder;
import com.javierc.albuquerquenow.fetch.AtmDataFetch;
import com.javierc.albuquerquenow.hlperobj.AtmData;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.app.ListActivity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader.TileMode;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class WeatherActivity extends ListActivity {
	ImageView liveCam;
	LinearLayout progressLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weather);
		
		
		
		progressLayout = (LinearLayout) findViewById(R.id.progress_indic_layout);

		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayShowTitleEnabled(false);
		getActionBar().setIcon(R.drawable.home);

		
		
		ListView lv = getListView();
		LayoutInflater inflater = getLayoutInflater();
		ViewGroup header = (ViewGroup)inflater.inflate(R.layout.header, lv, false);
		lv.addHeaderView(header, null, false);
		

//		liveCam.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				//new DownloadImageTask().execute(liveCam);
//			}
//		});
		
		lv.setOnItemClickListener(itemClick());
		
		liveCam = (ImageView)  findViewById(R.id.camera);
		liveCam.setTag("http://wwc.instacam.com/instacamimg/KOBTV/KOBTV_s.jpg");


	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == android.R.id.home) {
			this.finish();
		}
		return super.onOptionsItemSelected(item);
	}
	

	private OnItemClickListener itemClick() {
		// TODO Auto-generated method stub
		return new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				 AtmData d =  (AtmData)((ViewHolder)arg1.getTag()).dataObj;
			     final Dialog dialog = new Dialog(WeatherActivity.this);
			     dialog.setTitle("More Information");
			     dialog.setContentView(R.layout.air_qual_popup);
			     dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
			     //Button btnDismiss = (Button)dialog.getWindow().findViewById(R.id.dismiss);
			     TextView tv = (TextView)dialog.findViewById(R.id.pollut);
				 tv.setText("Responsible Pollutant: "+d.getReadingType());
				((TextView)dialog.findViewById(R.id.code)).setBackgroundColor(d.getColor());
				((TextView)dialog.findViewById(R.id.category)).setText("Category: "+ d.getCategory());
				((TextView)dialog.findViewById(R.id.pollut_level)).setText("Reading: "+ d.getData() + " " + d.getUnits());
				if (!d.isReliable()) {
					((TextView)dialog.findViewById(R.id.warn)).setVisibility(View.VISIBLE);
				}
			     dialog.show();
			    }	
				
		};
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.weather, menu);
		return true;
	}
	private class FetchDataAsync extends AsyncTask<Void, Void, List<AtmData>>{
		String filename;
		Activity act;
		
		public FetchDataAsync(Activity act) {
			// TODO Auto-generated constructor stub
			this.act = act;
		}
		
		
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			progressLayout.setVisibility(View.VISIBLE);
			Time now = new Time();
			now.setToNow();
			int hour = now.hour;
			int day = now.monthDay;
			if(now.minute < 15){
				hour--;
			} else if(now.hour < 2){
				--day;
				hour = 24 - now.hour;
			}
			filename = String.valueOf(now.year).substring(2) + String.format("%02d", now.month+1) + String.format("%02d",day) + String.format("%02d",hour-1) + ".NM2";
			Log.d("file", filename);
			super.onPreExecute();
		}

		@Override
		protected List<AtmData> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			List<AtmData> l = null;
			
			AtmDataFetch fetch = new AtmDataFetch();
			
			try {
				l = fetch.getDataFromURL(new URL("http://data.cabq.gov/airquality/aqindex/daily/"+filename));
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			return l;
			

		}
		
		@Override
		protected void onPostExecute(List<AtmData> result) {
			// TODO Auto-generated method stub
			if (result == null) {
				Toast.makeText(getBaseContext(), "Error getting data.", Toast.LENGTH_LONG).show();			
			} else if(result.size() == 0){
				//((TextView)findViewById(R.id.alert)).setVisibility(View.VISIBLE);
				Toast.makeText(getBaseContext(), "No available data.", Toast.LENGTH_LONG).show();
			}
			else{
				//((TextView)findViewById(R.id.alert)).setVisibility(View.INVISIBLE);
				setListAdapter(new WeatherArrayAdapter(act , result, new String[result.size()]));
			}
			progressLayout.setVisibility(View.GONE);
			super.onPostExecute(result);
		}
		
	}
	
	private class DownloadImageTask extends AsyncTask<ImageView, Void, Bitmap> {

		ImageView imageView = null;

		@Override
		protected Bitmap doInBackground(ImageView... imageViews) {
		    this.imageView = imageViews[0];
		    return download_Image((String)imageView.getTag());

		}

		@Override
		protected void onPostExecute(Bitmap result) {
			//Bitmap bitmap = result;
			if(result == null){
				result = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
				imageView.setImageBitmap(result);
			}
			else{
				Bitmap circleBitmap = Bitmap.createBitmap(result.getWidth(), result.getHeight(), Bitmap.Config.ARGB_8888);
	
				BitmapShader shader = new BitmapShader (result,  TileMode.CLAMP, TileMode.CLAMP);
				Paint paint = new Paint();
				paint.setAntiAlias(true);
		        paint.setShader(shader);
		        
//		        int center = circleBitmap.getWidth()/2;
//		        imageView.getDrawable().getBounds().width()/2
//		        imageView.getDrawable().getBounds().height()/2
		        Canvas c = new Canvas(circleBitmap);

			    imageView.setImageBitmap(circleBitmap);
			    c.drawCircle(imageView.getDrawable().getBounds().width()/2, imageView.getDrawable().getBounds().height()/2, imageView.getDrawable().getBounds().height()/2, paint);

			} 

		}


		private Bitmap download_Image(String url) {
		  Bitmap bm = null;
		    try {
		        URL aURL = new URL(url);
		        URLConnection conn = aURL.openConnection();
		        conn.connect();
		        InputStream is = conn.getInputStream();
		        BufferedInputStream bis = new BufferedInputStream(is);
		        bm = BitmapFactory.decodeStream(bis);
		        bis.close();
		        is.close();
		    } catch (IOException e) {

		        Log.e("ERRRR","IO: " + e.getMessage().toString());
		    } catch (Exception e) {
				// TODO: handle exception
			}
		    return bm;  
		}
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		this.finish();
		super.onPause();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		new FetchDataAsync(this).execute();
		
		new DownloadImageTask().execute(liveCam); //must be called after fetching data
		
		super.onResume();
	}
}
