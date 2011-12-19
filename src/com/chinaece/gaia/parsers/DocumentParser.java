package com.chinaece.gaia.parsers;

import java.util.ArrayList;
import java.util.Collection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.chinaece.gaia.types.DocumentType;

public class DocumentParser extends AbstractJSONParser<DocumentType> {

	@Override
	public DocumentType parser(JSONObject jsonObj) {
		DocumentType document = new DocumentType();
		try {
			document.setDesc(jsonObj.getString("desc"));
			document.setDisplay(jsonObj.getString("display"));
			document.setValue(jsonObj.getString("value"));
			document.setName(jsonObj.getString("name"));
			return document;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Collection<DocumentType> parser(JSONArray jsonArray) {
		try {
		ArrayList<DocumentType> documentlist = new ArrayList<DocumentType>();
		for(int i = 0;i<jsonArray.length();i++){
				documentlist.add(parser((JSONObject) jsonArray.get(i)));
				return documentlist;
			}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
		return null;
	}

}
