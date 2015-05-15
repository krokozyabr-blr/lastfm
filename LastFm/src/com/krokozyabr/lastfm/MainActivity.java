package com.krokozyabr.lastfm;

import java.util.ArrayList;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.LatLngBounds.Builder;
import com.krokozyabr.lastfm.EnterNameDialog.EnterNameListener;
import com.krokozyabr.lastfm.model.Artist;
import com.krokozyabr.lastfm.model.Event;
import com.krokozyabr.lastfm.networking.EventsGetRequest;
import com.krokozyabr.lastfm.networking.EventsGetResponse;
import com.krokozyabr.lastfm.networking.TopArtistsGetRequest;
import com.krokozyabr.lastfm.networking.TopArtistsGetResponse;
import com.krokozyabr.lastfm.networking.UrlManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends BaseActivity {
	
	private GoogleMap mMap;

	private SharedPreferences pref;
	
	private RequestListener<EventsGetResponse> eventsResponseListener;
	
	private ArrayList<Event> list;
	private LatLngBounds bounds;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		list = new ArrayList<Event>();
		
		pref = PreferenceManager.getDefaultSharedPreferences(this);
		
		int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(MainActivity.this);
		if (status == ConnectionResult.SUCCESS) {
			mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView)).getMap();
			setUpMap();

		}
		
		if (TextUtils.isEmpty(pref.getString(UrlManager.USERNAME_PREF, ""))){
			showNameDialog();
		} else {
			startDownload(pref.getString(UrlManager.USERNAME_PREF, ""));
		}
		
		eventsResponseListener = new RequestListener<EventsGetResponse>() {
			
			@Override
			public void onRequestSuccess(EventsGetResponse response) {
				if (Utils.isEmpty(response.getErrorInfo().getErrorMessage())){
					list.addAll(response.getEvents());
					for (Event e : response.getEvents()){
						mMap.addMarker(new MarkerOptions().title(e.getHeadliner()).snippet(e.getTitle()).position(e.getVenue().getLocation().getGeopoint()));
					}
					bounds = llbBuild();
					if (bounds != null){
						CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 50);
						mMap.animateCamera(cu);
					}
				} else {
					showError(response.getErrorInfo().getErrorMessage());
				}
			}
			
			@Override
			public void onRequestFailure(SpiceException exc) {
				showError(exc.getMessage());
			}
		};
		
	}
	
	private void startDownload(String name){
		spiceManager.execute(new TopArtistsGetRequest(name), new RequestListener<TopArtistsGetResponse>(){

			@Override
			public void onRequestFailure(SpiceException exc) {
				showError(exc.getMessage());
			}

			@Override
			public void onRequestSuccess(TopArtistsGetResponse response) {
				if (Utils.isEmpty(response.getErrorInfo().getErrorMessage())){
					for(Artist artist : response.getArtists()){
						spiceManager.execute(new EventsGetRequest(MainActivity.this, artist.getName()), eventsResponseListener);
					}
				} else {
					showError(response.getErrorInfo().getErrorMessage());
				}
			}
			
		});
	}
	
	private LatLngBounds llbBuild() {
		boolean hasPionts = false;
		final Builder builder = LatLngBounds.builder();

		for (Event e : list) {
			builder.include(e.getVenue().getLocation().getGeopoint());
			hasPionts = true;
		}
		return hasPionts ? builder.build() : null;
	}
	
	private void showNameDialog(){
		FragmentManager fm = getSupportFragmentManager(); 
		EnterNameDialog dlg = new EnterNameDialog(this);
		dlg.setOnNameEnteredListener(new EnterNameListener() {
			
			@Override
			public void nameEntered(String name) {
				if (mMap != null){
					list.clear();
					mMap.clear();
					if (mMap.getMyLocation() != null)
						mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
								new LatLng(mMap.getMyLocation().getLatitude(), mMap.getMyLocation().getLongitude()), 15));
				}
				startDownload(name);
			}
		});
		dlg.show(fm, "BoundaryNameDialog");
	}
	
	private void setUpMap() {
		if (mMap != null) {
			mMap.clear();
			System.gc();
			mMap.setMyLocationEnabled(true);
			if (mMap.getMyLocation() != null)
				mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
						new LatLng(mMap.getMyLocation().getLatitude(), mMap.getMyLocation().getLongitude()), 15));
			mMap.setOnMyLocationChangeListener(new OnMyLocationChangeListener() {
				
				@Override
				public void onMyLocationChange(Location location) {
					if (list.size() == 0)
						mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
							new LatLng(location.getLatitude(), location.getLongitude()), 15));
				}
			});
			mMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
				
				@Override
				public void onInfoWindowClick(Marker marker) {
					Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
					intent.putExtra("name", marker.getTitle());
					startActivity(intent);
					
				}
			});
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			showNameDialog();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
