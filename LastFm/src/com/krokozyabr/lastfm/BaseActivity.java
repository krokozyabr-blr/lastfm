package com.krokozyabr.lastfm;

import com.krokozyabr.lastfm.networking.NetworkingService;
import com.octo.android.robospice.SpiceManager;

import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

public class BaseActivity extends FragmentActivity {
	
	protected SpiceManager spiceManager = new SpiceManager(NetworkingService.class);

	@Override
	protected void onStart() {
		if (!spiceManager.isStarted()) {
			spiceManager.start(this);
		}
		super.onStart();
	}

	@Override
	protected void onDestroy() {
		if (spiceManager.isStarted()) {
			spiceManager.shouldStop();
		}
		super.onDestroy();
	}
	
	protected void showError(String message){
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}
}
