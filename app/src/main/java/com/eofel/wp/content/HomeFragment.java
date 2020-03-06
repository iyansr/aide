package com.eofel.wp.content;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import com.arlib.floatingsearchview.FloatingSearchView;
import com.eofel.wp.R;
import com.eofel.wp.utils.ViewController;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import org.json.JSONObject;
import com.eofel.wp.utils.JsonLoader;
import org.json.JSONException;
import org.json.JSONArray;
import java.util.ArrayList;
import com.eofel.wp.utils.ItemContent;
import android.support.v7.widget.LinearLayoutManager;
import com.eofel.wp.bind.MobileLegends;
import android.widget.Toast;
import com.eofel.wp.bind.PubgMobile;
import com.eofel.wp.bind.FreeFire;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.AdRequest;
import android.widget.Button;
import android.content.Intent;
import com.eofel.wp.views.ShowImage;
import com.eofel.wp.utils.DataHelper;
import java.util.List;
import com.eofel.wp.utils.MySuggestion;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import android.view.MenuItem;
import com.eofel.wp.views.About;

public class HomeFragment extends ViewController implements AppBarLayout.OnOffsetChangedListener, View.OnClickListener {
	
	private static final String TAG = "HomeFragment";
	private FloatingSearchView mSearchView;
	private AppBarLayout mAppBarLayout;
	private JsonLoader mJsonLoader;
	private RecyclerView mMobileLegend;
	private RecyclerView mPubg;
	private RecyclerView mFreeFire;
	private Button mButtonML, mButtonPubg, mButtonFF;
	
	private AdView mAdView;
	private AdView mAdView2;
	
	public HomeFragment() {
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_main, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState){
		super.onViewCreated(view, savedInstanceState);
		mSearchView = view.findViewById(R.id.floating_search_view);
		mAppBarLayout = view.findViewById(R.id.appbar);
		
		mButtonML = view.findViewById(R.id.showAllMl);
		mButtonPubg = view.findViewById(R.id.showAllPubg);
		mButtonFF = view.findViewById(R.id.ff_btn);
		mButtonML.setOnClickListener(this);
		mButtonPubg.setOnClickListener(this);
		mButtonFF.setOnClickListener(this);
		
		mJsonLoader = new JsonLoader(getContext());
		
		mMobileLegend = view.findViewById(R.id.cardView);
		mPubg = view.findViewById(R.id.pubg);
		mFreeFire = view.findViewById(R.id.ff);
		
		mAdView = view.findViewById(R.id.adView);
		mAdView2 = view.findViewById(R.id.adView2);
		
		mAppBarLayout.addOnOffsetChangedListener(this);
		setupDrawer();
		setupMobileLegends();
		setupAds();
		setupSearch();
	}

	private void setupSearch() {
		mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {

				@Override
				public void onSearchTextChanged(String oldQuery, String newQuery) {
					if (!oldQuery.equals("") && newQuery.equals("")) {
						mSearchView.clearSuggestions();
					} else {
						mSearchView.showProgress();
						DataHelper helper = new DataHelper(getActivity());
						List<MySuggestion> mList = helper.GetSwap(newQuery);
						mSearchView.swapSuggestions(mList);
						mSearchView.hideProgress();

					}
				}
			});
		
		mSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {

				@Override
				public void onSuggestionClicked(SearchSuggestion suggestion) {
					Log.d("status", suggestion.getBody().toString());
					QueryResult fragment = new QueryResult();

					Bundle bundle = new Bundle();
					bundle.putString("with", suggestion.getBody().toString());
					fragment.setArguments(bundle);
					getActivity().getSupportFragmentManager()
						.beginTransaction()
						.replace(R.id.fragment_container, fragment, TAG)
						.addToBackStack(null)
						.commit();
				}

				@Override
				public void onSearchAction(String query) {
					QueryResult fragment = new QueryResult();
					Bundle bundle = new Bundle();
					bundle.putString("with", query);
					fragment.setArguments(bundle);
					getActivity().getSupportFragmentManager()
						.beginTransaction()
						.replace(R.id.fragment_container, fragment, TAG)
						.addToBackStack(null)
						.commit();
				}
		});
		
		mSearchView.setOnMenuItemClickListener(new FloatingSearchView.OnMenuItemClickListener() {

				@Override
				public void onActionMenuItemSelected(MenuItem item) {
					switch (item.getItemId()) {
						case R.id.action_about:
							startActivity(new Intent(getActivity(), About.class));
							break;
					}
				}
			
		});
	}
	
	private void setupAds() {
		AdRequest mAdRequest = new AdRequest.Builder().build();
		mAdView.loadAd(mAdRequest);
		
		AdRequest.Builder mAdRequest2 = new AdRequest.Builder();
		mAdView2.loadAd(mAdRequest2.build());
	}
	

	private void setupMobileLegends() {
		try {
			mMobileLegend.setHasFixedSize(true);
			JSONObject mJsonObject = new JSONObject(mJsonLoader.LoadJsonFromAssets());
			JSONArray mJsonArray = mJsonObject.getJSONArray("mobileLegend");
			ArrayList<ItemContent> item = new ArrayList<ItemContent>();
			
			for (int i = 0; i < mJsonArray.length(); i++) {
				JSONObject mJson = mJsonArray.getJSONObject(i);
				item.add(new ItemContent(
							mJson.getString("name"),
							mJson.getString("url")
						));
				Log.d("Status", mJson.get("url").toString());
			}
			LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
			mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
			if (item.size() > 0 & mMobileLegend != null) {
				mMobileLegend.setAdapter(new MobileLegends(item, new MobileLegends.OnItemClicked() {
					@Override
					public void itemClicked(ItemContent content) {
						Bundle bundle = new Bundle();
						bundle.putString("name", content.getName());
						bundle.putString("url", content.getUrl());
						Intent intent = new Intent(getActivity(), ShowImage.class);
						intent.putExtras(bundle);
						startActivity(intent);
					}
				}));
			}
			mMobileLegend.setLayoutManager(mLayoutManager);
			setupPubg();
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void setupPubg() {
		try {
			mPubg.setHasFixedSize(true);
			JSONObject mJsonObject = new JSONObject(mJsonLoader.LoadJsonFromAssets());
			JSONArray mJsonArray = mJsonObject.getJSONArray("pubgMobile");
			ArrayList<ItemContent> item = new ArrayList<ItemContent>();

			for (int i = 0; i < mJsonArray.length(); i++) {
				JSONObject mJson = mJsonArray.getJSONObject(i);
				item.add(new ItemContent(
							 mJson.getString("name"),
							 mJson.getString("url")
						 ));
			}
			LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
			mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
			if (item.size() > 0 & mMobileLegend != null) {
				mPubg.setAdapter(new PubgMobile(item, new PubgMobile.OnItemClicked() {
					@Override
					public void itemClicked(ItemContent content) {
						Bundle bundle = new Bundle();
						bundle.putString("name", content.getName());
						bundle.putString("url", content.getUrl());
						Intent intent = new Intent(getActivity(), ShowImage.class);
						intent.putExtras(bundle);
						startActivity(intent);
					}
				}));
			}
			mPubg.setLayoutManager(mLayoutManager);
			setupFf();
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void setupFf() {
		try {
			mFreeFire.setHasFixedSize(true);
			JSONObject mJsonObject = new JSONObject(mJsonLoader.LoadJsonFromAssets());
			JSONArray mJsonArray = mJsonObject.getJSONArray("freeFire");
			ArrayList<ItemContent> item = new ArrayList<ItemContent>();

			for (int i = 0; i < mJsonArray.length(); i++) {
				JSONObject mJson = mJsonArray.getJSONObject(i);
				item.add(new ItemContent(
							 mJson.getString("name"),
							 mJson.getString("url")
						 ));
			}
			LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
			mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
			if (item.size() > 0 & mMobileLegend != null) {
				mFreeFire.setAdapter(new FreeFire(item, new FreeFire.OnItemClicked() {
					@Override
					public void itemClicked(ItemContent content) {
						Bundle bundle = new Bundle();
						bundle.putString("name", content.getName());
						bundle.putString("url", content.getUrl());
						Intent intent = new Intent(getActivity(), ShowImage.class);
						intent.putExtras(bundle);
						startActivity(intent);
					}
				}));
			}
			mFreeFire.setLayoutManager(mLayoutManager);
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	private void setupDrawer() {
		AttachSearchViewToActivity(mSearchView);
	}
	
	@Override
	public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset){
		mSearchView.setTranslationY(verticalOffset);
	}
	
	@Override
	public void onClick(View v) {
		if (v == mButtonML) {
			ListFragment fragment = new ListFragment();

			Bundle bundle = new Bundle();
			bundle.putString("with", "mobileLegend");
			fragment.setArguments(bundle);
			getActivity().getSupportFragmentManager()
			.beginTransaction()
				.replace(R.id.fragment_container, fragment, "HomeFragment")
				.addToBackStack(null)
				.commit();
		} else if (v == mButtonPubg) {
			ListFragment fragment = new ListFragment();

			Bundle bundle = new Bundle();
			bundle.putString("with", "pubgMobile");
			fragment.setArguments(bundle);
			getActivity().getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.fragment_container, fragment, "HomeFragment")
				.addToBackStack(null)
				.commit();
		} else if (v == mButtonFF) {
			ListFragment fragment = new ListFragment();

			Bundle bundle = new Bundle();
			bundle.putString("with", "freeFire");
			fragment.setArguments(bundle);
			getActivity().getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.fragment_container, fragment, "HomeFragment")
				.addToBackStack(null)
				.commit();
		}
	}
	
	@Override
	public boolean onActivityBackPressed() {
		if (!mSearchView.setSearchFocused(false)) {
			return false;
		}
		return true;
	}
	
}
