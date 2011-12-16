package com.chinaece.gaia.parsers;

import java.util.Collection;

import org.json.JSONArray;
import org.json.JSONObject;

public interface GaiaParser<T> {
	abstract public Object parser(Object obj);
	
	abstract public T parser(JSONObject jsonObj);
	
	abstract public Collection<T> parser(JSONArray jsonArray);
}
