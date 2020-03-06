package com.eofel.wp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.content.Intent;
import com.eofel.wp.views.MainViews;
import android.net.ConnectivityManager;
import android.content.Context;
import android.net.NetworkInfo;
import com.eofel.wp.views.ErrorNetwork;

public class MainActivity extends AppCompatActivity {
	
	private Handler mHandler;
	private boolean isConnected = false;
	private ConnectivityManager mConnection;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		mConnection = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		mHandler = new Handler();
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				if(mConnection.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED || 
				   mConnection.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
					isConnected = true;
					startActivity(new Intent(getApplicationContext(), MainViews.class));
					finish();
				} else {
					isConnected = false;
					startActivity(new Intent(getApplicationContext(), ErrorNetwork.class));
					finish();
				}
				
			}
				
		}, 2000L);
    }
}
