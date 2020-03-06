package com.eofel.wp.utils;

import android.content.Context;
import java.util.List;
import java.util.ArrayList;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.Set;
import java.util.HashSet;

public class DataHelper {

	private Context context;
	private ArrayList<String> mMobileLegends;
	private ArrayList<String> mPubgMobile;
	private ArrayList<String> mFreeFire;

	public DataHelper(Context context) {
		this.context = context;
	}

	public List<MySuggestion> GetSwap(String text) {
		List<MySuggestion> mList = new ArrayList<MySuggestion>();
		JsonLoader mJosnLoader = new JsonLoader(context);
		try {
			JSONObject mJsonObject = new JSONObject(mJosnLoader.LoadJsonFromAssets());
			JSONArray mJsonArray = mJsonObject.getJSONArray("mobileLegend");
			JSONArray mPubgArray = mJsonObject.getJSONArray("pubgMobile");
			JSONArray mFFArray = mJsonObject.getJSONArray("freeFire");
			
			mMobileLegends = new ArrayList<>();
			mPubgMobile = new ArrayList<>();
			mFreeFire = new ArrayList<>();

			for (int i = 0; i < mJsonArray.length(); i++) {
				JSONObject mJson = mJsonArray.getJSONObject(i);
				mMobileLegends.add(mJson.getString("name"));
			}
			
			for (int i = 0; i < mPubgArray.length(); i++) {
				JSONObject mJson = mPubgArray.getJSONObject(i);
				mPubgMobile.add(mJson.getString("name"));
			}
			
			for (int i = 0; i < mFFArray.length(); i++) {
				JSONObject mJson = mFFArray.getJSONObject(i);
				mFreeFire.add(mJson.getString("name"));
			}
			mMobileLegends.addAll(mPubgMobile);
			mMobileLegends.addAll(mFreeFire);
			
			Set<String> set = new HashSet<>(mMobileLegends);
			mMobileLegends.clear();
			mMobileLegends.addAll(set);
			
			for (int j = 0; j < mMobileLegends.size(); j++) {
				if (mMobileLegends.get(j).toLowerCase().indexOf(text.toLowerCase()) != -1) {
					mList.add(new MySuggestion(mMobileLegends.get(j)));
				}
			}
			
		}
		catch (JSONException e) {
			e.printStackTrace();
		}

		return mList;
	}
}
