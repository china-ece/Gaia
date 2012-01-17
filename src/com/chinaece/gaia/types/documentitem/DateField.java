package com.chinaece.gaia.types.documentitem;

import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DateField extends ItemType{
	private TextView text;
	private LinearLayout linearLayout;

	public DateField(JSONObject obj) throws JSONException {
		super(obj);
			if(obj.getString("type").equals("DateField"))
				type = "DateField";
			else
				throw new IllegalStateException("bad init DateField");
		}

	@Override
	public View getMappingInstance(final Context context) {
		linearLayout = new LinearLayout(context);
		final Calendar c = Calendar.getInstance();
		text = new TextView(context);
		text.setText(displayValue);
		text.setTextColor(Color.RED);
		linearLayout.addView(text);
		if(display == 1 || display == 3 || display == 4){
			text.setClickable(false);
			text.setFocusable(false);
			if (display == 3)
				text.setVisibility(View.INVISIBLE);
		}
		else if( display == 2){
			text.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
						
						@Override
						public void onDateSet(DatePicker view, int year, int monthOfYear,
								int dayOfMonth) {
							text.setText(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
						}
					}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
				}
			});
		}
		return linearLayout;
	}

	@Override
	public String getInstanceValue() {
		return text.getText().toString();
	}
   
}
