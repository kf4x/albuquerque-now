package com.example.albuquerquenow;

import android.graphics.Color;

public class AtmData {
	private String readingType;
	private String units;
	private String location;
	private String data;
	private boolean isReliable;
	private String category;

	public String getReadingType() {
		return readingType;
	}
	
	public AtmData() {
		// TODO Auto-generated constructor stub
	}
	
	public AtmData(String r, String u, String l, String d){
		this.readingType = r;
		this.units = u;
		this.location = l;
		this.data = d;
	}

	public void setReadingType(String readingType) {
		this.readingType = readingType;
	}

	public String getUnits() {
		return units;
	}

	public void setUnits(String units) {
		this.units = units;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public boolean isReliable() {
		return isReliable;
	}

	public void setReliable(boolean isReliable) {
		this.isReliable = isReliable;
	}
	
	public int getColor(){
		if(readingType.contains("OZONE")){
			if(Double.valueOf(data) <= 59){
				category = "Good";
				return Color.rgb(46, 204, 113);
			}else if(Double.valueOf(data) <= 75){
				category = "Moderate";
				return Color.rgb(241, 196, 15);
			} else if(Double.valueOf(data) <= 95){
				category = "Unhealthy for sensitive groups";
				return Color.rgb(230, 126, 34);
			} else if(Double.valueOf(data) <= 115){
				category = "Unhealthy";
				return Color.rgb(192, 57, 43);
			} else if(Double.valueOf(data) <= 374){
				category = "Very Unhealthy";
				return Color.rgb(142, 68, 173);
			} else if(Double.valueOf(data) > 374){
				category = "Hazardous";
				return Color.rgb(123, 17, 19);
			}
			 
		} else if(readingType.contains("PM10")){
			if(Double.valueOf(data) <= 54){
				category = "Good";
				return Color.rgb(46, 204, 113);
			}else if(Double.valueOf(data) <= 154){
				category = "Moderate";
				return Color.rgb(241, 196, 15);
			} else if(Double.valueOf(data) <= 254){
				category = "Unhealthy for sensitive groups";
				return Color.rgb(230, 126, 34);
			} else if(Double.valueOf(data) <= 354){
				category = "Unhealthy";
				return Color.rgb(192, 57, 43);
			}else if(Double.valueOf(data) <= 424){
				category = "Very Unhealthy";
				return Color.rgb(142, 68, 173);
			}  else if(Double.valueOf(data) <= 504){
				category = "Hazardous";
				return Color.rgb(123, 17, 19);
			}
		} else if(readingType.contains("PM2.5")){
			if(Double.valueOf(data) <= 15.4){
				category = "Good";
				return Color.rgb(46, 204, 113);
			}else if(Double.valueOf(data) <= 40.4){
				category = "Moderate";
				return Color.rgb(241, 196, 15);
			} else if(Double.valueOf(data) <= 65.4){
				category = "Unhealthy for sensitive groups";
				return Color.rgb(230, 126, 34);
			} else if(Double.valueOf(data) <= 150.4){
				category = "Unhealthy";
				return Color.rgb(192, 57, 43);
			} else if(Double.valueOf(data) <= 250.4){
				category = "Very Unhealthy";
				return Color.rgb(142, 68, 173);
			}  else if(Double.valueOf(data) < 350.4){
				category = "Hazardous";
				return Color.rgb(123, 17, 19);
			}
			
			
		
		} else if(readingType.contains("PSI")){
			if(Double.valueOf(data) <= 50){
				category = "Good";
				return Color.rgb(46, 204, 113);
			}else if(Double.valueOf(data) <= 100){
				category = "Moderate";
				return Color.rgb(241, 196, 15);
			} else if(Double.valueOf(data) <= 150){
				category = "Unhealthy for sensitive groups";
				return Color.rgb(230, 126, 34);
			} else if(Double.valueOf(data) <= 200){
				category = "Unhealthy";
				return Color.rgb(192, 57, 43);
			} else if(Double.valueOf(data) <= 300){
				category = "Very Unhealthy";
				return Color.rgb(142, 68, 173);
			} else if(Double.valueOf(data) > 300){
				category = "Hazardous";
				return Color.rgb(123, 17, 19);
			}
		} else if(readingType.contains("NO2")){
			if(Double.valueOf(data) < .65){
				category = "Good";
				return Color.rgb(46, 204, 113);
			
			}else if(Double.valueOf(data) <= 1.24){
				category = "Very Unhealthy";
				return Color.rgb(142, 68, 173);
			} else if(Double.valueOf(data) > 1.25){
				category = "Hazardous";
				return Color.rgb(123, 17, 19);
			}
	
		} else if(readingType.contains("SO2")){
			if(Double.valueOf(data) <= .034){
				category = "Good";
				return Color.rgb(46, 204, 113);
			}else if(Double.valueOf(data) <= .144){
				category = "Moderate";
				return Color.rgb(241, 196, 15);
			} else if(Double.valueOf(data) <= .224){
				category = "Unhealthy for sensitive groups";
				return Color.rgb(230, 126, 34);
			} else if(Double.valueOf(data) <= .304){
				category = "Unhealthy";
				return Color.rgb(192, 57, 43);
			} else if(Double.valueOf(data) <= .604){
				category = "Very Unhealthy";
				return Color.rgb(142, 68, 173);
			} else if(Double.valueOf(data) <= 1.004){
				category = "Hazardous";
				return Color.rgb(123, 17, 19);
			}
		}
		return -1;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
	
	
}
