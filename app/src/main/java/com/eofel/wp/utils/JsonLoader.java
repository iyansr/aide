package com.eofel.wp.utils;

import android.content.*;
import java.io.*;

public class JsonLoader {

	private Context context;

	public JsonLoader(Context context) {
		this.context = context;
	}

	public String LoadJsonFromAssets() {
		String json = null;
		try {
			InputStream in = context.getApplicationContext().getAssets().open("source.json");
			int size = in.available();
			byte[] buffer = new byte[size];
			in.read(buffer);
			in.close();
			json = new String(buffer, "UTF-8");
		}
		catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		return json;
	}
}
