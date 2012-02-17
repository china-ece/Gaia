package com.chinaece.gaia.parsers;

import java.util.ArrayList;
import java.util.Collection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.chinaece.gaia.types.DocumentType;
import com.chinaece.gaia.types.documentitem.BranchType;

public class DocumentParser extends AbstractJSONParser<DocumentType> {

	@Override
	public DocumentType parser(JSONObject jsonObj) {
		DocumentType document = new DocumentType();
		FlowPathParser flowpathparser = new FlowPathParser();
		ArrayList<BranchType> flowpath = new ArrayList<BranchType>();
		try {
			document.setEditable(jsonObj.getBoolean("editable"));
			ItemParser itemparser = new ItemParser();
			document.setItems(itemparser.parser(jsonObj.getJSONArray("items")));
			document.setSubmitable(jsonObj.getBoolean("submitable"));
			document.setCurrNodeid(jsonObj.getString("currNodeid"));
			document.setChoiceflag(jsonObj.getJSONArray("flowPath").getBoolean(0));
			for(int i = 1;i<jsonObj.getJSONArray("flowPath").length();i++){
				flowpath.add(flowpathparser.parser(jsonObj.getJSONArray("flowPath").getJSONObject(i)));
			}
			document.setFlowPath(flowpath);
			return document;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Collection<DocumentType> parser(JSONArray jsonArray) {
		 throw new UnsupportedOperationException("do not call");
	}

}
