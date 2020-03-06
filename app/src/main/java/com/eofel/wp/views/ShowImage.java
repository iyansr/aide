package com.eofel.wp.views;

import android.widget.*;
import com.squareup.picasso.*;

import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.eofel.wp.R;
import com.eofel.wp.views.ShowImage;
import java.io.IOException;
import com.google.android.gms.ads.doubleclick.PublisherInterstitialAd;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;

public class ShowImage extends AppCompatActivity {

	private PublisherInterstitialAd mInterstitialAd;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_show);

		ImageView imageView = (ImageView) findViewById(R.id.resultwl);
		Button button = (Button) findViewById(R.id.setwall);
		
		showAds();

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		final String url = bundle.getString("url");
		Picasso.with(this)
			.load(url)
			.into(imageView);
		button.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View p1) {
					Picasso.with(getApplicationContext()).load(url).into(new Target() {

							@Override
							public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
								WallpaperManager manager = WallpaperManager.getInstance(ShowImage.this);
								try {
									manager.setBitmap(bitmap);
								}
								catch (IOException e) {
									e.printStackTrace();
								}
								Toast.makeText(ShowImage.this, "Wallpaper Has changed", Toast.LENGTH_SHORT).show();
							}

							@Override
							public void onBitmapFailed(Drawable p1) {
								Toast.makeText(ShowImage.this, "Error", Toast.LENGTH_SHORT).show();

							}

							@Override
							public void onPrepareLoad(Drawable p1) {
								// TODO: Implement this method
							}

						});
				}
			});
    }

	private void showAds() {
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

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus) {
			hideSystemUI();
		}
	}

	private void hideSystemUI() {
		View decorView = getWindow().getDecorView();
		decorView.setSystemUiVisibility(
			View.SYSTEM_UI_FLAG_LAYOUT_STABLE
			| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            // Hide the nav bar and status bar
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_FULLSCREEN);
	}
}
