package com.chinaece.gaia.parsers;

import java.util.ArrayList;
import java.util.Collection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.chinaece.gaia.types.PendingType;

public class PendingParser extends AbstractJSONParser<PendingType> {

	@Override
	public PendingType parser(JSONObject jsonObj) {
		try {
			PendingType pending = new PendingType();
			pending.setDocid(jsonObj.getString("docid"));
			pending.setFormid(jsonObj.getString("formid"));
			pending.setAppid(jsonObj.getString("appid"));
			pending.setDate(jsonObj.getString("date"));
			pending.setSummary(jsonObj.getString("summary"));
			return pending;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;

	}

	@Override
	public Collection<PendingType> parser(JSONArray jsonArray) {
		try {
			ArrayList<PendingType> pendinglist = new ArrayList<PendingType>();
			for (int i = 0; i < jsonArray.length(); i++) {
				pendinglist.add(parser((JSONObject) jsonArray.get(i)));
			}
			return pendinglist;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

}
