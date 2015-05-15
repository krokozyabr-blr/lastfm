package com.krokozyabr.lastfm;

import java.util.ArrayList;

import com.krokozyabr.lastfm.model.Event;
import com.krokozyabr.lastfm.networking.EventsGetRequest;
import com.krokozyabr.lastfm.networking.EventsGetResponse;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class DetailsActivity extends BaseActivity{
	
	private ListView listView;
	EventsAdapter adapter;	
	ArrayList<Event> eventsList;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.details_activity);
		
		eventsList = new ArrayList<Event>();
		
		listView = (ListView) findViewById(R.id.list);
		adapter = new EventsAdapter(this);
		listView.setAdapter(adapter);
		
		String name = getIntent().getExtras().getString("name");
		
		if (name != null)
			spiceManager.execute(new EventsGetRequest(this, name), new RequestListener<EventsGetResponse>() {
				
				@Override
				public void onRequestSuccess(EventsGetResponse response) {

					if (Utils.isEmpty(response.getErrorInfo().getErrorMessage())){
						eventsList = response.getEvents();
						adapter.notifyDataSetChanged();
					} else {
						showError(response.getErrorInfo().getErrorMessage());
					}
				}
				
				@Override
				public void onRequestFailure(SpiceException exc) {
					showError(exc.getMessage());
				}
			});
	};
	
	class EventsAdapter extends BaseAdapter{
		
		private LayoutInflater layoutInflater;
			
		public EventsAdapter(Context context) {
			layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
	
		@Override
		public int getCount() {
			return eventsList.size();
		}
	
		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return eventsList.get(position);
		}
	
		@Override
		public long getItemId(int id) {
			return id;
		}
	
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			View view = convertView;
		    if (view == null) {
		    	view = layoutInflater.inflate(android.R.layout.simple_list_item_1, parent, false);
		    }
		    
		    TextView title = (TextView) view.findViewById(android.R.id.text1);
		        title.setText(eventsList.get(position).getTitle());
		    
			return view;
		}
	}
	
}
