package com.javierc.albuquerquenow.hlperobj;

public class VenueData {
	private String _name;
	private String _website;
	private double _lat;
	private double _lng;
	private int _here;
	private long _total;
	
	public VenueData(String n, String w, double l, double ln, int h, long t){
		_name = n;
		 _website = w;
		 _lat = l;
		 _lng = ln;
		 _here = h;
		 _total = t;
	}
	
	public VenueData(){
		
	}
	
	public String get_name() {
		return _name;
	}
	public void set_name(String _name) {
		this._name = _name;
	}
	public String get_website() {
		return _website;
	}
	public void set_website(String _website) {
		this._website = _website;
	}
	public double get_lat() {
		return _lat;
	}
	public void set_lat(double _lat) {
		this._lat = _lat;
	}
	public double get_lng() {
		return _lng;
	}
	public void set_lng(double _lng) {
		this._lng = _lng;
	}
	public int get_here() {
		return _here;
	}
	public void set_here(int _here) {
		this._here = _here;
	}
	public long get_total() {
		return _total;
	}
	public void set_total(long _total) {
		this._total = _total;
	}
}
