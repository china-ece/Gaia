package com.chinaece.gaia.parsers;

import java.util.Collection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.chinaece.gaia.types.UserType;

public class UserParser extends AbstractJSONParser<UserType>{

	@Override
	public UserType parser(JSONObject jsonObj) {
		try {
				UserType user = new UserType();
				user.setName(jsonObj.getString("name"));
				user.setToken(jsonObj.getString("token"));
				return user;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public Collection<UserType> parser(JSONArray jsonArray) {
	   throw new UnsupportedOperationException("do not call");
	}
}
