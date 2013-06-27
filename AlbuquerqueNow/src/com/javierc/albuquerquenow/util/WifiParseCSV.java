package com.javierc.albuquerquenow.util;

import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;




public class WifiParseCSV {
	Pattern p = Pattern.compile("^((\"(?:[^\"]|\"\")*\"|[^,]*)(,(\"(?:[^\"]|\"\")*\"|[^,]*))*)$");
	public class WifiSpot{
		private String _name;
		private String _address;
		private String _ssid;
		private String _website;
		private double[] _ll;		//cant decide if I should use Double[] or LatLng(requires google play services lib)

		
		
		public String get_name() {
			return _name;
		}


		public WifiSpot set_name(String _name) {
			this._name = _name;
			return this;
		}


		public String get_address() {
			return _address;
		}


		public WifiSpot set_address(String _address) {
			this._address = _address;
			return this;
		}

		public String get_website() {
			return _website;
		}


		public WifiSpot set_website(String _website) {
			this._website = _website;
			return this;
		}

		public double[] get_ll() {
			return _ll;
		}


		public WifiSpot set_ll(double[] _ll) {
			this._ll = _ll;
			return this;
		}




		@Override
		public String toString() {
			return get_name() + "\n" + get_address() + "\n" + get_website(); 
		}


		public String get_ssid() {
			return _ssid;
		}


		public WifiSpot set_ssid(String _ssid) {
			this._ssid = _ssid;
			return this;
		}
	}
	
	public List<WifiSpot> parseFromUrl(URL url) {
		Scanner in = null;
		List<WifiSpot> data = new ArrayList<WifiSpot>();

		try {
			URLConnection connection = url.openConnection();
	        connection.connect();
			in = new Scanner(url.openStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(in == null){
			Log.i("NULL", "URL null");
			return data;
		}
		
		
		in.nextLine();
		while(in.hasNext()){
			String  line;
			String[] row;
			double[] ll = new double[2];
			line  = in.nextLine();
			int commaLoc = 0, totalComma=0; 
			String subline ="";
			try {
				commaLoc = line.lastIndexOf(',');
//				totalComma = 
				subline = line.substring(0, commaLoc);
			} catch (Exception e) {
				// TODO: handle exception
			}
			
//			System.out.println(commaLoc);
//			List <String> list = parseLine(line);
//			Matcher m = p.matcher(subline);
//			row = subline.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
//			row = p.split(subline);
////			row = subline.
////			Log.i("SPOT", String.valueOf(row.length));
//	        Matcher matcher = p.matcher(subline);
//	        while (matcher.find()) {
//	            System.out.println(matcher.group(3));
//	        }
//			System.out.println(list);
//			for (int i = 0; i < list.size(); i++) {
//				System.out.println(list.get(i));
//				Log.i("SPOT", list.get(i));
//			}
//			try {
//				double lng = Double.parseDouble(row[3]);
//				double lat = Double.parseDouble(row[4]);
//				//this order is same order as LatLng object;
//				ll[0] = lat;
//				ll[1] = lng;
//			} catch (Exception e) {
//				// TODO: handle exception
//				e.printStackTrace();
//				ll[0] = 0.0;
//				ll[1] = 0.0;
//			}
//			data.add(new WifiSpot()
//					.set_name(row[0])
//					.set_address(row[1])
//					.set_ssid(row[2])
//					.set_ll(ll)
//					.set_website("")
//					);
			
			
		}
		
		//quick fix!!
		//data.remove(0);
//		Log.i("Removing", "Removed first element");
		
		return data;
		
	}

	private List<String> parseLine(String line) {
		// TODO Auto-generated method stub
		List <String> l = new ArrayList<String>();
		boolean isInsideQ = false;
		boolean ready = false;
		String s = "";
		for (int i = 0; i < line.length(); i++) {
			Log.i("SPOT", line.charAt(i)+"");
			if (line.charAt(i) == '"' && isInsideQ != true) {
				isInsideQ = true;
			} else if (line.charAt(i) == '"' && isInsideQ == true) {
				isInsideQ = false;
			}
			if (line.charAt(i) == ',' && isInsideQ == false) {
				ready = true;
			}
			
			if(ready){
				l.add(s);
				s = "";
			}else{
				s.concat(line.charAt(i)+ "");
			}
		}
		return l;
		
	}	
}
