package com.chinaece.gaia.parsers;

import java.util.ArrayList;
import java.util.Collection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.chinaece.gaia.types.ContactType;

public class ContactParser extends AbstractJSONParser<ContactType> {

	@Override
	public ContactType parser(JSONObject jsonObj) {
		ContactType contact = new ContactType();
		try {
			contact.setName(jsonObj.getString("name"));
			contact.setEmail(jsonObj.getString("email"));
			contact.setTelephone(jsonObj.getString("telephone"));
			return contact;
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public Collection<ContactType> parser(JSONArray jsonArray) {
		ArrayList<ContactType> contactlist = new ArrayList<ContactType>();
		try {
			for (int i = 0; i < jsonArray.length(); i++) {
				contactlist.add(parser((JSONObject) jsonArray.get(i)));
			}
			return contactlist;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

}
