package com.javierc.albuquerquenow.fetch;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.javierc.albuquerquenow.hlperobj.AtmData;


public class AtmDataFetch {
	private String sense;
	private String reading;
	
	private List<AtmData> formatted;
	
	public AtmDataFetch() {
		// TODO Auto-generated constructor stub
	}
	
	public AtmDataFetch(String s, String r){
		this.sense=s;
		this.reading=r;
		
	}
	
	
	
	public List<AtmData> getDataFromURL(URL url){
		Scanner in = null;
		List<AtmData> data = new ArrayList<AtmData>();
		

		try {
			URLConnection connection = url.openConnection();
	        connection.connect();
			in = new Scanner(url.openStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(in == null){
			return data;
		}
		while(in.hasNext()){
			String  r, u, line;
			line  = in.nextLine();

			if(line.contains("BEGIN_GROUP")){
				AtmData da = new AtmData();
				
				while(in.hasNext()){
					
					if(line.contains("VARIABLE")){
						int i = line.toString().indexOf(",");
						r = line.toString().substring(++i);
						da.setReadingType(r);
					}
					else if(line.contains("UNITS")){
						int i = line.toString().indexOf(",");
						u = line.toString().substring(++i);
						da.setUnits(u);
					}
					else if(line.contains("BEGIN_DATA")){
						String curentData="";
						
						while(in.hasNext()){
							line = in.nextLine();
							if(line.contains("END_DATA")){
								break;
							}
							curentData += line + "\n";
						}
						da.setData(curentData);
					}
					else if(line.contains("END_GROUP")){
						data.add(da);
						break;
					}
					line = in.nextLine();
				}
			}
		}
		formatted = data;
		return getFormatted();
		
	}
	
	private List<AtmData> getFormatted(){
		List<AtmData> result =new ArrayList<AtmData>();
		
		for (int i = 0; i < formatted.size(); i++) {
			String s = formatted.get(i).getData() , line= "";
			Scanner scanner = new Scanner(s);
			while(scanner.hasNext()){
				line = scanner.nextLine();
				int locationEnd = line.indexOf(",");
				String loc = tile(line.substring(0, locationEnd).trim());
				int lastComma = line.lastIndexOf(",");
				String reading = line.substring(lastComma+1);
				result.add(new AtmData(formatted.get(i).getReadingType().toString(), formatted.get(i).getUnits().toString() , loc, reading));
			}
		}
		
		return fixList(result);
	}
	
	//
	private List<AtmData> fixList(List<AtmData> l){

		for (int i = 0; i < l.size(); i++) {
			if (l.get(i).getData().contains("G") || l.get(i).getData().contains("B") || l.get(i).getData().contains("M")) {
				boolean b = l.get(i).getData().contains("G");
				l.get(i-1).setReliable(b);
				l.remove(i);
			}
		}
		
		return l;
	}
	
	private String tile(String string){
	    final String ACTIONABLE_DELIMITERS = " '-/"; // these cause the character following to be capitalized

		StringBuilder sb = new StringBuilder();
		boolean capNext = true;
		
		for (char c : string.toCharArray()) {
			c = (capNext) ? Character.toUpperCase(c) : Character.toLowerCase(c);
			sb.append(c);
			capNext = (ACTIONABLE_DELIMITERS.indexOf((int) c) >= 0); 
		}
		return sb.toString();
    }


	public String getSense() {
		return sense;
	}



	public void setSense(String sense) {
		this.sense = sense;
	}



	public String getReading() {
		return reading;
	}



	public void setReading(String reading) {
		this.reading = reading;
	}
}
