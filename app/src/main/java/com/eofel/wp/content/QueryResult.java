package com.eofel.wp.content;

import android.view.*;

import android.os.Bundle;
import com.eofel.wp.R;
import com.arlib.floatingsearchview.FloatingSearchView;
import android.widget.Toast;
import android.util.Log;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import java.util.ArrayList;
import org.json.JSONObject;
import org.json.JSONArray;
import com.eofel.wp.utils.JsonLoader;
import org.json.JSONException;
import com.eofel.wp.utils.ItemContent;
import com.eofel.wp.bind.ResultImage;
import com.eofel.wp.bind.SpaceLayout;
import android.support.v4.view.ViewCompat;
import android.content.Intent;
import com.eofel.wp.views.ShowImage;
import com.eofel.wp.utils.DataHelper;
import java.util.List;
import com.eofel.wp.utils.MySuggestion;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.eofel.wp.utils.ViewController;

public class QueryResult extends ViewController {
	
	private FloatingSearchView mSearchView;
	private RecyclerView mRecyclerView;
	
	
	private ArrayList<ItemContent> mMobileLegends;
	private ArrayList<ItemContent> mPubgMobile;
	private ArrayList<ItemContent> mFreeFire;
	private ArrayList<ItemContent> mList;
	private JsonLoader mJosnLoader;
	private String value = "";
	
	public QueryResult() {
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.result, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mSearchView = view.findViewById(R.id.searchView);
		mRecyclerView = view.findViewById(R.id.search_results_list);
		mJosnLoader = new JsonLoader(getActivity());
		setupViews();
		setuopSearch();
	}

	private void setuopSearch() {
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
						.replace(R.id.fragment_container, fragment, "HomeFragment")
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
						.replace(R.id.fragment_container, fragment, "HomeFragment")
						.addToBackStack(null)
						.commit();
				}
			});
	}

	private void setupViews() {
		Bundle bundle = this.getArguments();
		value = bundle.getString("with");
		mSearchView.setSearchText(value);
		mSearchView.setOnHomeActionClickListener(new FloatingSearchView.OnHomeActionClickListener() {
				@Override
				public void onHomeClicked() {
					HomeFragment fragment = new HomeFragment();
					getActivity().getSupportFragmentManager()
						.beginTransaction()
						.replace(R.id.fragment_container, fragment)
						.commit();
				}
		});
		setupResult();
	}
	
	private void setupResult() {
		try {
			JSONObject mJsonObject = new JSONObject(mJosnLoader.LoadJsonFromAssets());
			JSONArray mJsonArray = mJsonObject.getJSONArray("mobileLegend");
			JSONArray mPubgArray = mJsonObject.getJSONArray("pubgMobile");
			JSONArray mFFArray = mJsonObject.getJSONArray("freeFire");

			mMobileLegends = new ArrayList<>();
			mPubgMobile = new ArrayList<>();
			mFreeFire = new ArrayList<>();
			mList = new ArrayList<>();

			for (int i = 0; i < mJsonArray.length(); i++) {
				JSONObject mJson = mJsonArray.getJSONObject(i);
				mMobileLegends.add(new ItemContent(
									   mJson.getString("name"), mJson.getString("url")
								   ));
			}

			for (int i = 0; i < mPubgArray.length(); i++) {
				JSONObject mJson = mPubgArray.getJSONObject(i);
				mPubgMobile.add(new ItemContent(
									mJson.getString("name"), mJson.getString("url")
								));
			}

			for (int i = 0; i < mFFArray.length(); i++) {
				JSONObject mJson = mFFArray.getJSONObject(i);
				mFreeFire.add(new ItemContent(
								  mJson.getString("name"), mJson.getString("url")
							  ));
			}
			mMobileLegends.addAll(mPubgMobile);
			mMobileLegends.addAll(mFreeFire);

			for (int j = 0; j < mMobileLegends.size(); j++) {
				if (mMobileLegends.get(j).getName().toLowerCase().indexOf(value.toLowerCase()) != -1) {
					mList.add(new ItemContent(
								  mMobileLegends.get(j).getName(), mMobileLegends.get(j).getUrl()
							  ));
				}
			}

			setupList();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void setupList() {
		mRecyclerView.setAdapter(new ResultImage(mList, new ResultImage.OnItemClicked() {

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
		
		SpaceLayout mSpace = new SpaceLayout(getActivity(), 200);
		mRecyclerView.setLayoutManager(mSpace);
		ViewCompat.setNestedScrollingEnabled(mRecyclerView, false);
	}
	
	@Override
	public boolean onActivityBackPressed() {
		if (!mSearchView.setSearchFocused(false)) {
			return false;
		}
		return true;
	}
}
