package com.eofel.wp.utils;

import android.content.Context;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;
import org.json.JSONException;
import org.json.JSONArray;

public class SelfHelper {
	
	private Context context;
	private ArrayList<String> mArrayList;
	
	public SelfHelper(Context context) {
		this.context = context;
	}
	
	public List<MySuggestion> GetSwap(String text, String from) {
		List<MySuggestion> mList = new ArrayList<MySuggestion>();
		JsonLoader mJosnLoader = new JsonLoader(context);
		try {
			JSONObject mJsonObject = new JSONObject(mJosnLoader.LoadJsonFromAssets());
			JSONArray mJsonArray = mJsonObject.getJSONArray(from);
			
			mArrayList = new ArrayList<>();
			
			for (int i = 0; i < mJsonArray.length(); i++) {
				JSONObject mJson = mJsonArray.getJSONObject(i);
				mArrayList.add(mJson.getString("name"));
			}
			
			for (int j = 0; j < mArrayList.size(); j++) {
				if (mArrayList.get(j).toLowerCase().indexOf(text.toLowerCase()) != -1) {
					mList.add(new MySuggestion(mArrayList.get(j)));
				}
			}
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
		
		return mList;
	}
}
