package com.example.albuquerquenow;

import java.util.List;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class WeatherArrayAdapter extends ArrayAdapter<String>{
	
	
	
	private Activity activity;
	private List<AtmData> data;

	public WeatherArrayAdapter(Activity activity, List<AtmData> data, String[] namesString) {
		
		super(activity, R.layout.rowlayout, namesString);

		this.activity = activity;
		this.data = data;
	}
	
	static class ViewHolder{
		public TextView number;
		public TextView text;
		public TextView type;
		public AtmData dataObj;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		
		//if the row has not been made http://www.youtube.com/watch?v=N6YdwzAvwOA
		if(convertView == null)
		{
			LayoutInflater inflater = activity.getLayoutInflater();
			//set up the row with custom layout
			convertView = inflater.inflate(R.layout.rowlayout, null);
			
			
			//create the new instance of view holder
			holder = new ViewHolder();
			// set the inner object
			holder.text = (TextView) convertView.findViewById(R.id.label); // layout set in the constructor with super
			holder.number = (TextView) convertView.findViewById(R.id.importance_lbl);
			holder.type = (TextView) convertView.findViewById(R.id.type);
			
			convertView.setTag(holder);
			//convertView.setId((int) data.get(position).getId());
		}
		else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.dataObj = data.get(position);
		

		
		holder.text.setText(holder.dataObj.getLocation());
		holder.type.setText(holder.dataObj.getReadingType());
		if (holder.dataObj.isReliable()) {
			holder.number.setText(holder.dataObj.getData());
		} else{
			holder.number.setText("!");
		}

		
		
		return convertView;
	}
}
