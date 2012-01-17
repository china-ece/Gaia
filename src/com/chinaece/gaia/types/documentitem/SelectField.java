package com.chinaece.gaia.types.documentitem;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class SelectField extends ItemType{
	
	private Spinner spinner = null;
	
	public SelectField(JSONObject obj) throws JSONException{
		super(obj);
			if(obj.getString("type").equals("SelectField"))
				type = "SelectField";
			else
				throw new IllegalStateException("bad init SelectField");
		}
	
	@Override
	public View getMappingInstance(Context context) throws JSONException {
		if(spinner != null)
			return spinner;
		int pos = -1;
		spinner = new Spinner(context);
		ArrayList<JSONObject> data = new ArrayList<JSONObject>();
		if(display == 1 || display == 3 || display == 4){
			JSONObject original = new JSONObject();
			original.put("displayValue", displayValue);
			original.put("dataValue", dataValue);
			data.add(original);
			spinner.setClickable(false);
			if(display == 3)
				spinner.setVisibility(View.INVISIBLE);
		}
		else if(display == 2){
			for(int i = 0; i<list_value.length();i++){
				data.add(list_value.getJSONObject(i));
				if(dataValue != null && list_value.getJSONObject(i).getString("dataValue").equals(dataValue))
					pos = i;
			}
		}
		KVAdapter<JSONObject> adapter = new KVAdapter<JSONObject>(context, android.R.layout.simple_spinner_item, data);
		spinner.setAdapter(adapter);
		spinner.setSelection(pos == -1?0:pos);
		return spinner;
	}
	
	@Override
	public String getInstanceValue() {
		try {
			return ((JSONObject)spinner.getSelectedItem()).getString("dataValue");
		} catch (JSONException e) {
			return dataValue;
		}
	}
	
	class KVAdapter<T> extends ArrayAdapter<JSONObject>{
		
		public KVAdapter(Context context, int textViewResourceId,
				List<JSONObject> objects) {
			super(context, textViewResourceId, objects);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v;
			if(convertView == null){
				LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = inflater.inflate(android.R.layout.simple_spinner_item, null);
			}else
				v = convertView;
			TextView lable = (TextView) v.findViewById(android.R.id.text1);
			try {
				lable.setText(getItem(position).getString("displayValue"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return v;
		}
		
		@Override
		public View getDropDownView(int position, View convertView,
				ViewGroup parent) {
			View v;
			if(convertView == null){
				LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, null);
			}else
				v = convertView;
			TextView lable = (TextView) v.findViewById(android.R.id.text1);
			try {
				lable.setText(getItem(position).getString("displayValue"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return v;
		}
		
	}

}
