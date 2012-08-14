package com.chinaece.gaia.parsers;

import java.util.ArrayList;
import java.util.Collection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;

import com.chinaece.gaia.types.CalendarType;

public class CalendarParsers extends AbstractJSONParser<CalendarType>{


	@Override
	public CalendarType parser(JSONObject jsonObj) {
		CalendarType calendartype = new CalendarType();
		try {
			jsonObj.getInt("version");
			calendartype.setAffair(jsonObj.getString("affair"));
			calendartype.setContent1(jsonObj.getString("content1"));
			calendartype.setEndtime(jsonObj.getString("endtime"));
			calendartype.setStarttime(jsonObj.getString("starttime"));
			calendartype.setId(jsonObj.getString("id"));
			calendartype.setVersion(jsonObj.getInt("version"));
			return calendartype;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Collection<CalendarType> parser(JSONArray jsonArray) {
		ArrayList<CalendarType> calendarlist = new ArrayList<CalendarType>();
		try {
			for(int i = 0;i<jsonArray.length();i++){
				calendarlist.add(parser((JSONObject) jsonArray.get(i)));
			}
			return calendarlist;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
}
