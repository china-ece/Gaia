package com.chinaece.gaia.parsers;

import java.util.ArrayList;
import java.util.Collection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.chinaece.gaia.types.documentitem.DateField;
import com.chinaece.gaia.types.documentitem.DepartmentField;
import com.chinaece.gaia.types.documentitem.InputField;
import com.chinaece.gaia.types.documentitem.ItemType;
import com.chinaece.gaia.types.documentitem.SelectField;
import com.chinaece.gaia.types.documentitem.StringField;
import com.chinaece.gaia.types.documentitem.UserField;

public class DocumentParser extends AbstractJSONParser<ItemType> {

	@Override
	public ItemType parser(JSONObject jsonObj) {
		throw new UnsupportedOperationException("do not call");
	}

	@Override
	public Collection<ItemType> parser(JSONArray jsonArray) {
		ArrayList<ItemType> itemlist = new ArrayList<ItemType>();
		try {
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonItem = jsonArray.getJSONObject(i);
				if(jsonItem.length() == 0){
					itemlist.add(null);
				}
				else if(jsonItem.getString("type").equals("SelectField")){
					itemlist.add(new SelectField(jsonItem));
				}
				else if(jsonItem.getString("type").equals("String")){
					itemlist.add(new StringField(jsonItem));
				}
				else if(jsonItem.getString("type").equals("DepartmentField")){
					itemlist.add(new DepartmentField(jsonItem));
				}
				else if(jsonItem.getString("type").equals("UserField")){
					itemlist.add(new UserField(jsonItem));
				}
				else if(jsonItem.getString("type").equals("DateField")){
					itemlist.add(new DateField(jsonItem));
				}
				else if(jsonItem.getString("type").equals("InputField")){
					itemlist.add(new InputField(jsonItem));
				}
			}
			return itemlist;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

}
