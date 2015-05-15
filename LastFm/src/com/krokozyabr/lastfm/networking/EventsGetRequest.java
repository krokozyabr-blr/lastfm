package com.krokozyabr.lastfm.networking;

import java.io.InputStream;

import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Log;


public class EventsGetRequest extends GetSpecHttpsRequest<EventsGetResponse>{
	
	private Context context;
	private String name;

	public EventsGetRequest(Context context, String name) {
		super(EventsGetResponse.class);
		this.context = context;
		this.name = name;
	}

	@Override
	String composeUrl() {
		return UrlManager.getEventsUrl(PreferenceManager.getDefaultSharedPreferences(context), name);
	}

	@Override
	EventsGetResponse parse(InputStream is) throws Exception {
		String body = readBody(is);
//		Log.d("response", body);
		EventsGetResponse response = new EventsGetResponse();
		response.parseSuccess(body);
		return response;
	}

}
