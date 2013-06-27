package com.javierc.albuquerquenow;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONObject;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.darvds.ribbonmenu.RibbonMenuView;
import com.darvds.ribbonmenu.iRibbonMenuCallback;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader.TileMode;
import android.util.Log;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity implements iRibbonMenuCallback {
	ImageView liveCam;
	private RibbonMenuView rbmView;
	private LinearLayout ll;
	private LinearLayout loadLL;
	protected AQuery listAq;
	protected AQuery aq;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//getActionBar().setTitle("Menu");
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        aq = new AQuery(this);
        rbmView = (RibbonMenuView) findViewById(R.id.ribbonMenuView);
        rbmView.setMenuClickCallback(this);
        rbmView.setMenuItems(R.menu.ribbon_menu);
        rbmView.setBackgroundResource(R.color.concrete);
        rbmView.getVisibility();
        ll = (LinearLayout)findViewById(R.id.mainll_mA);
        
        loadLL = (LinearLayout)findViewById(R.id.load_ll);
		((Button)findViewById(R.id.loadTweets)).setOnClickListener(loadListener());
		
		liveCam = (ImageView)  findViewById(R.id.camera);
		liveCam.setTag("http://wwc.instacam.com/instacamimg/KOBTV/KOBTV_s.jpg");
		
	}
	


	private OnClickListener loadListener() {
		// TODO Auto-generated method stub
		return new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				LinearLayout l = (LinearLayout)findViewById(R.id.listLayout);
				l.setVisibility(View.VISIBLE);
				loadLL.setVisibility(View.GONE);
				loadNews();
				
			}


		};
	}
	private void loadNews() {
		// TODO Auto-generated method stub
		String url = "http://www.google.com/uds/GnewsSearch?q=albuquerque&v=1.0";
        aq.progress(R.id.progress_lv).ajax(url, JSONObject.class,this, "renderNews");
        
	}

	
	
	private void addItems(JSONArray ja, List<JSONObject> items){
		for(int i = 0 ; i < ja.length(); i++){
			JSONObject jo = ja.optJSONObject(i);
			if(jo.has("content")){
				items.add(jo);
			}
		}
	}
	
	
	public void renderNews(String url, JSONObject json, AjaxStatus status) {
		
		if(json == null) return;
		JSONObject jo = json.optJSONObject("responseData");
		JSONArray ja = jo.optJSONArray("results");
//		Log.i("pub", ja.toString());
		
		if(ja == null) return;
		
		List<JSONObject> items = new ArrayList<JSONObject>();
		addItems(ja, items);
		
		listAq = new AQuery(this);
		
		
		ArrayAdapter<JSONObject> aa = new ArrayAdapter<JSONObject>(this, R.layout.content_item_s, items){
			
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				
				if(convertView == null){
					convertView = getLayoutInflater().inflate(R.layout.content_item_s, null);
				}
				
				JSONObject jo = getItem(position);
				
				AQuery aq = listAq.recycle(convertView);
				aq.id(R.id.name).text(jo.optString("title", "No Title"));
				aq.id(R.id.meta).text(jo.optString("publisher", ""));
				Log.i("pub", jo.optString("publisher", ""));
				
//				String tb = jo.optString("profile_image_url");

//				aq.id(R.id.tb).progress(R.id.progress).image(tb, true, true, 0, 0, null, AQuery.FADE_IN_NETWORK, 1.0f);
				
				
				return convertView;
				
			}
		};
		
		aq.id(R.id.list).adapter(aa);
		ListView lv = (ListView) findViewById(R.id.list);
		lv.setOnItemClickListener(itemClick());
		
		
	}
	
	private OnItemClickListener itemClick() {
		// TODO Auto-generated method stub
		return new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

	            JSONObject jo = (JSONObject) arg0.getItemAtPosition(arg2);
	            String url = jo.optString("signedRedirectUrl");

	            Intent i = new Intent(Intent.ACTION_VIEW);
	            i.setData(Uri.parse(url));
	            startActivity(i);

			}
		
		};
	}



	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		int id = item.getItemId();
		if (id == android.R.id.home) {
			
			hideLayout();
			rbmView.toggleMenu();
			
			return true;
		
		} else {
			return super.onOptionsItemSelected(item);
		}

	}


	@Override
	public void RibbonMenuItemClick(int itemId) {
		// TODO Auto-generated method stub
		
		if(itemId == R.id.ribbon_menu_explore){
			startActivity(new Intent(getBaseContext(), ExploreActivity.class));
		}
		else if(itemId == R.id.ribbon_menu_transit){
			startActivity(new Intent(getBaseContext(), TransitMap.class));
		}
		else if(itemId == R.id.ribbon_menu_rrtransit){
			startActivity(new Intent(getBaseContext(), RailTransitMap.class));
		}
		else if(itemId == R.id.ribbon_menu_search){
			startActivity(new Intent(getBaseContext(), SearchMap.class));
		}
		else if(itemId == R.id.ribbon_menu_weather){
			startActivity(new Intent(getBaseContext(), WeatherActivity.class));
		}
		else if(itemId == R.id.ribbon_menu_about){
			startActivity(new Intent(getBaseContext(), AboutActivity.class));
		}
		else if(itemId == R.id.ribbon_menu_fires){
			startActivity(new Intent(getBaseContext(), FireActivity.class));
		}
		hideLayout();
	}
	@Override
	protected void onDestroy() {
	// closing Entire Application
		trimCache(this);
	    super.onDestroy();
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		
		super.onPause();
	}

	private static void trimCache(Context context) {
		Log.d("trim", "deleted cache");
	    try {
	        File dir = new File(Environment.getExternalStorageDirectory() + "/abqnowkml");
	        if (dir != null && dir.isDirectory()) {
	            deleteDir(dir);
	            
	        }
	        
	    } catch (Exception e) {
	        // TODO: handle exception
	    }
	}


	private static boolean deleteDir(File dir) {
	    if (dir != null && dir.isDirectory()) {
	        String[] children = dir.list();
	        for (int i = 0; i < children.length; i++) {
	            boolean success = deleteDir(new File(dir, children[i]));
	            if (!success) {
	                return false;
	            }
	        }
	    }

	    return dir.delete();
	}
	private void hideLayout() {
		ll.setVisibility(ll.getVisibility() == View.GONE ? View.VISIBLE :View.GONE);
		if (ll.getVisibility() == View.VISIBLE) {
			ll.startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.rbm_in_from_left));
		}else {
			ll.startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.rbm_out_to_left));
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
	protected void onResume() {
		// TODO Auto-generated method stub
		if (ll.getVisibility() == View.VISIBLE) {
			new DownloadImageTask().execute(liveCam); //must be called after fetching data		
		}
		super.onResume();
	}
}
