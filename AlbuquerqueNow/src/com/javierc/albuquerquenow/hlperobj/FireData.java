package com.javierc.albuquerquenow.hlperobj;

public class FireData {
	private String _fireName;
	private long _acres;
	private double _lat;
	private double _lng;
	
	public FireData() {
		// TODO Auto-generated constructor stub
	}
	
	public FireData(String n, long a, double lat, double lng){
		_fireName = n;
		_acres = a;
		_lat = lat;
		_lng = lng;
	}
	
	
	public String get_fireName() {
		return _fireName;
	}
	public void set_fireName(String _fireName) {
		this._fireName = _fireName;
	}
	public long get_acres() {
		return _acres;
	}
	public void set_acres(long _acres) {
		this._acres = _acres;
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
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this._fireName;
	}
}
