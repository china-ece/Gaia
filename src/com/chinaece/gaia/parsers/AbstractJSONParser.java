package com.chinaece.gaia.parsers;

import org.json.JSONArray;
import org.json.JSONObject;

abstract public class AbstractJSONParser<T> implements GaiaParser<T>{

	public Object parser(Object obj){
		if(obj instanceof JSONObject)
			return parser((JSONObject)obj);
		else if(obj instanceof JSONArray)
			return parser((JSONArray)obj);
		return null;
	}
	
}
