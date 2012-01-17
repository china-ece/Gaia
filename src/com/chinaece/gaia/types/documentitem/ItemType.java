package com.chinaece.gaia.types.documentitem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.chinaece.gaia.types.GaiaType;

import android.content.Context;
import android.view.View;

abstract public class ItemType implements GaiaType{
	protected String type;
	
	protected String name;
	
	protected String displayValue;
	
	protected String dataValue;
	
	protected int display;
	
	protected JSONArray list_value;
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayValue() {
		return displayValue;
	}

	public void setDisplayValue(String displayValue) {
		this.displayValue = displayValue;
	}

	public String getDataValue() {
		return dataValue;
	}

	public void setDataValue(String dataValue) {
		this.dataValue = dataValue;
	}

	public int getDisplay() {
		return display;
	}

	public void setDisplay(int display) {
		this.display = display;
	}

	public JSONArray getList_value() {
		return list_value;
	}

	public void setList_value(JSONArray list_value) {
		this.list_value = list_value;
	}
	
	abstract public View getMappingInstance(Context context) throws JSONException;
	
	abstract public String getInstanceValue();
	
	final public boolean isChanged(){
		return !type.equals("String") && display == 2 && !getInstanceValue().equals(dataValue);
	}
	
	public ItemType(JSONObject obj) throws JSONException {
		displayValue = obj.getString("displayValue");
		name = obj.optString("name", null);
		display = obj.optInt("display", 0);
		dataValue = obj.optString("dataValue", null);
		list_value = obj.optJSONArray("list-value");
	}
}
