package com.eofel.wp;

import android.app.Application;
import com.google.android.gms.ads.MobileAds;

public class Ads extends Application {

	@Override
	public void onCreate() {
		
		super.onCreate();
		MobileAds.initialize(this, getResources().getString(R.string.app_id));
	}
	
	
}
