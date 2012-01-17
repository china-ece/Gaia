package com.chinaece.gaia.parsers;

import java.util.ArrayList;
import java.util.Collection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.chinaece.gaia.types.documentitem.CheckboxField;
import com.chinaece.gaia.types.documentitem.DateField;
import com.chinaece.gaia.types.documentitem.DepartmentField;
import com.chinaece.gaia.types.documentitem.InputField;
import com.chinaece.gaia.types.documentitem.ItemType;
import com.chinaece.gaia.types.documentitem.RadioField;
import com.chinaece.gaia.types.documentitem.SelectField;
import com.chinaece.gaia.types.documentitem.StringField;
import com.chinaece.gaia.types.documentitem.TextareaField;
import com.chinaece.gaia.types.documentitem.UserField;

public class ItemParser extends AbstractJSONParser<ItemType> {


	@Override
	public ItemType parser(JSONObject jsonObj) {
		 throw new UnsupportedOperationException("do not call");
	}

	@Override
	public Collection<ItemType> parser(JSONArray jsonArray) {
		ArrayList<ItemType> itemlist = new ArrayList<ItemType>();
		try {
			for(int i = 0;i<jsonArray.length();i++){
				JSONObject jsonitem = jsonArray.getJSONObject(i);
				if(jsonitem.length() == 0)
					itemlist.add(null);
				else if(jsonitem.getString("type").equals("SelectField")){
					itemlist.add(new SelectField(jsonitem));
					}
				else if(jsonitem.getString("type").equals("DateField")){
					itemlist.add(new DateField(jsonitem));
					}
				else if(jsonitem.getString("type").equals("DepartmentField")){
					itemlist.add(new DepartmentField(jsonitem));
					}
				else if(jsonitem.getString("type").equals("InputField")){
					itemlist.add(new InputField(jsonitem));
					}
				else if(jsonitem.getString("type").equals("RadioField")){
					itemlist.add(new RadioField(jsonitem));
					}
				else if(jsonitem.getString("type").equals("String")){
					itemlist.add(new StringField(jsonitem));
					}
				else if(jsonitem.getString("type").equals("TextareaField")){
					itemlist.add(new TextareaField(jsonitem));
					}
				else if(jsonitem.getString("type").equals("UserField")){
					itemlist.add(new UserField(jsonitem));
					}
				else if(jsonitem.getString("type").equals("CheckboxField")){
					itemlist.add(new CheckboxField(jsonitem));
					}
				}
			return itemlist;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
}