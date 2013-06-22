package com.javierc.albuquerquenow;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONObject;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.darvds.ribbonmenu.RibbonMenuView;
import com.darvds.ribbonmenu.iRibbonMenuCallback;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

public class MainActivity extends Activity implements iRibbonMenuCallback {

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
		
			
	}
	


	private OnClickListener loadListener() {
		// TODO Auto-generated method stub
		return new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				LinearLayout l = (LinearLayout)findViewById(R.id.listLayout);
				l.setVisibility(View.VISIBLE);
				loadLL.setVisibility(View.GONE);
				loadTweets();
			}


		};
	}


	private void loadTweets() {
		// TODO Auto-generated method stub
		String url = "https://search.twitter.com/search.json?q=%23abq&rpp=5&include_entities=false&result_type=mixed";
        aq.progress(R.id.progress_lv).ajax(url, JSONObject.class,this, "renderNews");
        
	}
	
	
	private void addItems(JSONArray ja, List<JSONObject> items){
		for(int i = 0 ; i < ja.length(); i++){
			JSONObject jo = ja.optJSONObject(i);
			if(jo.has("profile_image_url")){
				items.add(jo);
			}
		}
	}
	
	
	public void renderNews(String url, JSONObject json, AjaxStatus status) {
		
		if(json == null) return;
		
		JSONArray ja = json.optJSONArray("results");
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
				aq.id(R.id.name).text(jo.optString("text", "No Title"));
				aq.id(R.id.meta).text("@" + jo.optString("from_user", ""));
				
				String tb = jo.optString("profile_image_url");

				aq.id(R.id.tb).progress(R.id.progress).image(tb, true, true, 0, 0, null, AQuery.FADE_IN_NETWORK, 1.0f);
				
				
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
	            String user = jo.optString("from_user");
	            String statID = jo.optString("id_str");

	            String url = "https://twitter.com/" + user + "/status/" + statID;
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

	public static void trimCache(Context context) {
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


	public static boolean deleteDir(File dir) {
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
}
