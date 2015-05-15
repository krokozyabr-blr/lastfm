package com.krokozyabr.lastfm.model;

import com.google.android.gms.maps.model.LatLng;

public class Location {

	private LatLng geopoint;
	private String city;
	private String country;
	
	public Location(){
		geopoint = new LatLng(0, 0);
	}

	public LatLng getGeopoint() {
		return geopoint;
	}

	public void setGeopoint(LatLng geopoint) {
		this.geopoint = geopoint;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
}
