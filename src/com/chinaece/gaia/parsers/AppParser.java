package com.chinaece.gaia.parsers;

import java.util.ArrayList;
import java.util.Collection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.chinaece.gaia.types.AppType;

public class AppParser extends AbstractJSONParser<AppType> {

	@Override
	public AppType parser(JSONObject jsonObj) {
		try {
			AppType app = new AppType();
			String appid = jsonObj.getString("appid");
			String name = jsonObj.getString("name");
			app.setAppid(appid);
			app.setName(name);
			return app;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Collection<AppType> parser(JSONArray jsonArray) {
		if (jsonArray == null || jsonArray.length() < 1) {
			return null;
		}
		try {
			ArrayList<AppType> list = new ArrayList<AppType>();
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject app = (JSONObject) jsonArray.get(i);
				list.add(parser(app));
			}
			return list;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
}
