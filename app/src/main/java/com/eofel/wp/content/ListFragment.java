package com.eofel.wp.content;

import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.os.Bundle;
import com.eofel.wp.R;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import org.json.JSONObject;
import org.json.JSONArray;
import com.eofel.wp.utils.JsonLoader;
import org.json.JSONException;
import java.util.ArrayList;
import com.eofel.wp.utils.ItemContent;
import com.eofel.wp.bind.FlexBox;
import com.eofel.wp.bind.SpaceLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v4.view.ViewCompat;
import android.content.Intent;
import com.eofel.wp.views.ShowImage;
import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.eofel.wp.utils.SelfHelper;
import java.util.List;
import com.eofel.wp.utils.MySuggestion;
import com.eofel.wp.utils.ViewController;

public class ListFragment extends ViewController {

	private RecyclerView mRecyclerView;
	private JsonLoader jsonLoader;
	private FloatingSearchView mSearchView;
	private String value = "";
	
	public ListFragment() {
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_list, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		jsonLoader = new JsonLoader(getActivity());
		mRecyclerView = view.findViewById(R.id.viewAll);
		mRecyclerView.setHasFixedSize(true);
		mSearchView = view.findViewById(R.id.search_list);
		Bundle bundle = this.getArguments();
		value = bundle.getString("with");
		setupSearch();
		setupViews(value);
		
	}

	private void setupSearch() {
		mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {

				@Override
				public void onSearchTextChanged(String oldQuery, String newQuery) {
					if (!oldQuery.equals("") && newQuery.equals("")) {
						mSearchView.clearSuggestions();
					} else {
						mSearchView.showProgress();
						SelfHelper helper = new SelfHelper(getActivity());
						List<MySuggestion> mList = helper.GetSwap(newQuery, value);
						mSearchView.swapSuggestions(mList);
						mSearchView.hideProgress();

					}
				}
			});

		mSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {

				@Override
				public void onSuggestionClicked(SearchSuggestion suggestion) {
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

	private void setupViews(String value) {
		ArrayList<ItemContent> item = new ArrayList<ItemContent>();
		
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
		
		try {
			JSONObject obj = new JSONObject(jsonLoader.LoadJsonFromAssets());
			JSONArray mJsonArray = obj.getJSONArray(value);

			for (int i =0; i < mJsonArray.length(); i++) {
				JSONObject mJson = mJsonArray.getJSONObject(i);
				item.add(new ItemContent(
					mJson.getString("name"),
					mJson.getString("url")
				));
			}
			mRecyclerView.setAdapter(new FlexBox(item, new FlexBox.OnItemClicked() {

				@Override
				public void itemClicked(ItemContent content){
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
		} catch (JSONException e) {
			e.printStackTrace();
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
