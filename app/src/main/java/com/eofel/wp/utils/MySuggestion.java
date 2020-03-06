package com.eofel.wp.utils;

import com.arlib.floatingsearchview.suggestions.model.*;
import android.os.*;

public class MySuggestion implements SearchSuggestion {

	private String mBody;

	public MySuggestion(String suggestion) {
		this.mBody = suggestion.toLowerCase();
	}

	@Override
	public String getBody() {
		return mBody;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mBody);
	}
}
