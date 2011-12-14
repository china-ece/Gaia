package com.chinaece.gaia.parsers;

import java.util.Collection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.chinaece.gaia.types.UserType;

public class UserParser implements GaiaParser<UserType>{

	@Override
	public UserType parser(JSONObject jsonObj) {
		try {
			if(jsonObj.getBoolean("status") == true){
				UserType user = new UserType();
				user.setName(jsonObj.getString("name"));
				user.setToken(jsonObj.getString("token"));
				return user;
			}else
				return null;
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
