package com.eofel.wp.views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.eofel.wp.R;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;
import com.eofel.wp.utils.ViewController;
import com.arlib.floatingsearchview.FloatingSearchView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import com.eofel.wp.content.HomeFragment;
import com.google.android.gms.ads.InterstitialAd;
import android.widget.Toast;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.doubleclick.*;
import java.util.List;
import android.support.v4.view.GravityCompat;
import com.eofel.wp.content.ListFragment;

public class MainViews extends AppCompatActivity implements ViewController.ControllerCallback, NavigationView.OnNavigationItemSelectedListener {
	

	private static final String TAG = "MainViews";

	private DrawerLayout mDrawerLayout;
	private NavigationView mNavigatiomnView;
	
	private PublisherInterstitialAd mInterstitialAd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mNavigatiomnView = (NavigationView) findViewById(R.id.nav_view);
		
		mNavigatiomnView.setNavigationItemSelectedListener(this);
		loadInterstialsAds();
		switchFragment(new HomeFragment());
	}
	
	@Override
	public void attachToDrawer(FloatingSearchView searchView) {
		searchView.attachNavigationDrawerToMenuButton(mDrawerLayout);
	}
	
	@Override
	public boolean onNavigationItemSelected(MenuItem item) {
		mDrawerLayout.closeDrawer(GravityCompat.START);
		switch (item.getItemId()) {
			case R.id.home:
				switchFragment(new HomeFragment());
				return true;
			case R.id.mls:
				showFragment(new ListFragment(), "mobileLegend");
				return true;
			case R.id.pubgs:
				showFragment(new ListFragment(), "pubgMobile");
				return true;
			case R.id.ffs:
				showFragment(new ListFragment(), "freeFire");
				return true;
			default:
				return true;
		}
	}
	
	private void loadInterstialsAds() {
		mInterstitialAd = new PublisherInterstitialAd(this);
		mInterstitialAd.setAdUnitId(getString(R.string.interstitial_id));
		mInterstitialAd.setAdListener(new AdListener() {

				@Override
				public void onAdLoaded() {
					super.onAdLoaded();
					if (mInterstitialAd.isLoaded()) {
						mInterstitialAd.show();
					}
				}

				@Override
				public void onAdFailedToLoad(int i) {
					super.onAdFailedToLoad(i);
				}
			});
		
		PublisherAdRequest adRequest = new PublisherAdRequest.Builder().build();
        mInterstitialAd.loadAd(adRequest);

	}
	
	private void switchFragment(Fragment fragment) {
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.fragment_container, fragment)
				.commit();
	}
	
	private void showFragment(Fragment fragment, String idle) {
		Bundle bundle = new Bundle();
		bundle.putString("with", idle);
		fragment.setArguments(bundle);
		getSupportFragmentManager()
			.beginTransaction()
			.replace(R.id.fragment_container, fragment, "HomeFragment")
			.addToBackStack(null)
			.commit();
	}

	@Override
	public void onBackPressed() {
		List fragment = getSupportFragmentManager().getFragments();
		ViewController controller = (ViewController) fragment.get(fragment.size() - 1);
		
		if (!controller.onActivityBackPressed()) {
			super.onBackPressed();
		}
	}
	
	
}
