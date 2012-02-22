package com.chinaece.gaia.types.documentitem;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.EditText;

public class TextareaField extends ItemType {
	private EditText edittext = null;
	public TextareaField(JSONObject obj) throws JSONException {
		super(obj);
		if(obj.getString("type").equals("TextareaField"))
			type = "TextareaField";
		else
			throw new IllegalStateException("bad init TextareaField");
	}

	@Override
	public View getMappingInstance(Context context) throws JSONException {
		
		if(edittext != null)
			return edittext;
		edittext = new EditText(context);
		if(display == 1 || display == 3 || display == 4)
		{
			edittext.setText(displayValue);
			edittext.setTag(dataValue);
			edittext.setTextColor(Color.BLACK);
			edittext.setClickable(false);
			edittext.setFocusable(false);
			if (display == 3)
				edittext.setVisibility(View.INVISIBLE);
		}
		else if (display == 2){
			edittext.setText(displayValue);
			edittext.setTag(dataValue);
			edittext.setTextColor(Color.BLACK);
		}
		return edittext;
	}

	@Override
	public String getInstanceValue() {
		return edittext.getText().toString();
	}
}
