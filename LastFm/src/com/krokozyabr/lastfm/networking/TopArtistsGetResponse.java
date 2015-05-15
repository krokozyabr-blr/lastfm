package com.krokozyabr.lastfm.networking;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.krokozyabr.lastfm.model.Artist;

public class TopArtistsGetResponse extends AbstractResponse{

	ArrayList<Artist> list;
	
	TopArtistsGetResponse(){
		list = new ArrayList<Artist>();
	}
	
	@Override
	void parseSuccess(String body) throws Exception {
		
		try{
			JSONObject o = new JSONObject(body);
			JSONObject topartists = new JSONObject(o.getString("topartists"));
			JSONArray a = topartists.getJSONArray("artist");
			for (int i = 0; i<a.length(); i++){
				JSONObject obj = (JSONObject) a.get(i);
				Artist artist = new Artist();
				artist.setName(obj.getString("name"));
				artist.setCount(obj.getInt("playcount"));
				artist.setMbid(obj.getString("mbid"));
				artist.setUrl(obj.getString("url"));
				
				list.add(artist);
				
			}
		} catch(JSONException e){
			e.printStackTrace();
		}
	}
	
	public ArrayList<Artist> getArtists(){
		return list;
	}

}
