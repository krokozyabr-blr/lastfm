package com.krokozyabr.lastfm.networking;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.krokozyabr.lastfm.model.Event;

public class EventsGetResponse extends AbstractResponse{

	ArrayList<Event> list;
	
	EventsGetResponse(){
		list = new ArrayList<Event>();
	}
	
	
	@Override
	void parseSuccess(String body) throws Exception {
		try{
			JSONObject o = new JSONObject(body);
			JSONObject events = new JSONObject(o.getString("events"));
			if (events.has("event"))
				if (events.get("event") instanceof JSONArray){
					JSONArray event = events.getJSONArray("event");
					for (int i = 0; i< event.length(); i++){
						parseEvent((JSONObject)event.get(i));
					}
				} else {
					parseEvent(events.getJSONObject("event"));
				}
			
		} catch(JSONException e){
			e.printStackTrace();
//			Log.d("body", body);
		}		
	}
	
	private void parseEvent(JSONObject evt){
		Log.d("event", evt.toString());
		Event ev = new Event();
		try{
			ev.setHeadliner((new JSONObject(evt.getString("artists"))).getString("headliner"));
			ev.setId(evt.getLong("id"));
			ev.setTitle(evt.getString("title"));
			
			JSONObject venue = evt.getJSONObject("venue");
			ev.getVenue().setId(venue.getLong("id"));
			
			JSONObject loc = venue.getJSONObject("location");
			ev.getVenue().getLocation().setCity(loc.getString("city"));
			ev.getVenue().getLocation().setCountry(loc.getString("country"));
			
			JSONObject geopts = loc.getJSONObject("geo:point");
			ev.getVenue().getLocation().setGeopoint(
					new LatLng(geopts.getDouble("geo:lat"), geopts.getDouble("geo:long")));
			
			list.add(ev);
		} catch(JSONException e){
			e.printStackTrace();
		}
	}
	
	public ArrayList<Event> getEvents(){
		return list;
	}

}
