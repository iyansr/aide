package com.eofel.wp.utils;

import android.support.v4.app.Fragment;
import com.arlib.floatingsearchview.FloatingSearchView;
import android.content.Context;
import android.util.Log;

public abstract class ViewController extends Fragment {
	
	private ControllerCallback mCallback;
	
	public interface ControllerCallback {
		void attachToDrawer(FloatingSearchView searchView)
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		if (context instanceof ControllerCallback) {
			mCallback = (ControllerCallback) context;
		} else {
			Log.e("Error", "Must implement callback");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mCallback = null;
	}
	
	public void AttachSearchViewToActivity(FloatingSearchView searchView) {
		if (mCallback != null) {
			mCallback.attachToDrawer(searchView);
		}
	}
	
	public abstract boolean onActivityBackPressed();
	
}
