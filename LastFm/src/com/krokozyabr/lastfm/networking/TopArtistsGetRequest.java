package com.krokozyabr.lastfm.networking;

import java.io.InputStream;

import android.util.Log;


public class TopArtistsGetRequest extends GetSpecHttpsRequest<TopArtistsGetResponse>{
	
	private String name;

	public TopArtistsGetRequest(String name) {
		super(TopArtistsGetResponse.class);
		this.name = name;
	}

	@Override
	String composeUrl() {
		return UrlManager.getTopArtisisUrl(name);
	}

	@Override
	TopArtistsGetResponse parse(InputStream is) throws Exception {
		String body = readBody(is);
		Log.d("response", body);
		TopArtistsGetResponse response = new TopArtistsGetResponse();
		response.parseSuccess(body);
		return response;
	}

}
