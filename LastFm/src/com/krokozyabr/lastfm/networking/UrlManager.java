package com.krokozyabr.lastfm.networking;

import android.content.SharedPreferences;

public class UrlManager {

	public static final String USERNAME_PREF = "username_pref";
	
	public static final String API_KEY = "bd689eb89f9d259122f3f03acf0c164a";
	
	public static final String BASE_URL = "http://ws.audioscrobbler.com/2.0/";
	
	public static final String GET_TOP_ARTISTS = "user.gettopartists";
	public static final String GET_EVENTS = "artist.getevents";
	
	
	public static String getTopArtisisUrl(String name){
		return BASE_URL
		+ "?method=" + GET_TOP_ARTISTS
		+ "&user=" + name
		+ "&api_key=" + API_KEY + "&format=json";
	}
	
	public static String getEventsUrl(SharedPreferences prefs, String name){
		return BASE_URL
				+ "?method=" + GET_EVENTS
				+ "&artist=" + name.replaceAll(" ", "%20").replaceAll("&", "%26")
				+ "&api_key=" + API_KEY + "&format=json";
	}
	
	public static String getUserUrl(SharedPreferences prefs, String method){
		return BASE_URL
				+ "?method=" + method
				+ "&user=" + prefs.getString(USERNAME_PREF, "")
				+ "&api_key=" + API_KEY + "&format=json";
	}
}
