package com.chinaece.gaia.types.documentitem;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class RadioField extends ItemType {
	private LinearLayout linearlayout;
	private RadioButton radio;

	public RadioField(JSONObject obj) throws JSONException {
		super(obj);
		if (obj.getString("type").equals("RadioField"))
			type = "RadioField";
		else
			throw new IllegalStateException("bad init RadioField");
	}

	@Override
	public View getMappingInstance(Context context) throws JSONException {
		if (linearlayout != null)
			return linearlayout;
		linearlayout = new LinearLayout(context);
		if (display == 1 || display == 3 || display == 4) {
			radio = new RadioButton(context);
			radio.setText(displayValue);
			radio.setTag(dataValue);
			radio.setClickable(false);
			radio.setFocusable(false);
			linearlayout.addView(radio);
			if (display == 3) {
				radio.setVisibility(View.INVISIBLE);
			}
		} else if (display == 2) {
			RadioGroup radioGroup = new RadioGroup(context);
			for (int i = 0; i < list_value.length(); i++) {
				radio = new RadioButton(context);
				radio.setText(list_value.getJSONObject(i).getString(
						"displayValue"));
				radio.setTag(list_value.getJSONObject(i).getString("dataValue"));
				radioGroup.addView(radio);
			}
			linearlayout.addView(radioGroup);
		}
		return linearlayout;
	}

	@Override
	public String getInstanceValue() {
		return radio.getTag().toString();
	}

}
