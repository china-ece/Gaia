package com.chinaece.gaia.types.documentitem;

import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

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
		Button  button = new Button(context);
		button.setText("改变日期");
		text.setText(displayValue);
		text.setTextColor(Color.BLACK);
		linearLayout.addView(text);
		linearLayout.addView(button);
		button.setOnClickListener(new OnClickListener() {
			
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
		return linearLayout;
	}

	@Override
	public String getInstanceValue() {
		return null;
	}
   
}
