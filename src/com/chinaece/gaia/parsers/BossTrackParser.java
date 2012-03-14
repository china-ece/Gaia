package com.chinaece.gaia.parsers;

import java.util.ArrayList;
import java.util.Collection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.chinaece.gaia.types.BossTrackingType;

public class BossTrackParser  extends AbstractJSONParser<BossTrackingType>{

	@Override
	public BossTrackingType parser(JSONObject jsonObj) {
		try {
			BossTrackingType bosstrack = new BossTrackingType();
			bosstrack.setId(jsonObj.getString("id"));
			bosstrack.setDate(jsonObj.getString("date"));
			bosstrack.setDatedone(jsonObj.getString("datedone"));
			bosstrack.setDepart(jsonObj.getString("depart"));
			bosstrack.setDescribe(jsonObj.getString("describe"));
			bosstrack.setFeedbackdepart(jsonObj.getString("feedbackdepart"));
			bosstrack.setIdentify(jsonObj.getString("identify"));
			bosstrack.setIsdo(jsonObj.getString("isdo"));
			bosstrack.setItem(jsonObj.getString("item"));
			bosstrack.setLeadview(jsonObj.getString("leadview"));
			bosstrack.setModality(jsonObj.getString("modality"));
			bosstrack.setNextor(jsonObj.getString("nextor"));
			bosstrack.setNum(jsonObj.getString("num"));
			bosstrack.setPerson(jsonObj.getString("person"));
			bosstrack.setSecretary(jsonObj.getString("secretary"));
			bosstrack.setTitle(jsonObj.getString("title"));
			if(jsonObj.has("childs")){
				for(int i = 0;i<jsonObj.getJSONArray("childs").length();i++){
					JSONObject child = jsonObj.getJSONArray("childs").getJSONObject(i);
					bosstrack.addChild(child.getString("id"), child.getString("follow"));
				}
			}
			return bosstrack;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Collection<BossTrackingType> parser(JSONArray jsonArray) {
		try {
			ArrayList<BossTrackingType> bosstrackList = new ArrayList<BossTrackingType>();
			for(int i = 0;i<jsonArray.length();i++){
				bosstrackList.add(parser((JSONObject) jsonArray.get(i)));
			}
			return bosstrackList;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
}
