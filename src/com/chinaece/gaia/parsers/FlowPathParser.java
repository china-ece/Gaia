package com.chinaece.gaia.parsers;

import java.util.ArrayList;
import java.util.Collection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.chinaece.gaia.types.documentitem.BranchType;

public class FlowPathParser extends AbstractJSONParser<BranchType> {

	@Override
	public BranchType parser(JSONObject jsonObj) {
		BranchType branch = new BranchType();
		try {
			branch.setPathid(jsonObj.getString("pathid"));
			branch.setName(jsonObj.getString("name"));
			branch.setFlowtype(jsonObj.getString("flowtype"));
			if(jsonObj.has("list-value")){
				for(int i = 0;i<jsonObj.getJSONArray("list-value").length();i++){
					JSONObject user = jsonObj.getJSONArray("list-value").getJSONObject(i);
					branch.addUser(user.getString("displayValue"), user.getString("dataValue"));
				}
			}
			return branch;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Collection<BranchType> parser(JSONArray jsonArray) {
		return null;
	}

}
