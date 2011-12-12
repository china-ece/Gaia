package com.chinaece.gaia.parsers;

import java.util.Collection;

import org.json.JSONArray;
import org.json.JSONObject;

import com.chinaece.gaia.types.GaiaType;

public interface Parser<T extends GaiaType> {
	abstract public T parser(JSONObject jsonObj);
	
	abstract public Collection<T> parser(JSONArray jsonArray);
}
