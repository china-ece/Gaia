package com.chinaece.gaia.types.documentitem;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class CheckboxField extends ItemType{
	private LinearLayout linearlayout;
    private TextView textview;
    private String checkboxsdataValue = "";
    private Mydialog dialog;
	public CheckboxField(JSONObject obj) throws JSONException {
		super(obj);
		if(obj.getString("type").equals("CheckboxField")){
			type = "CheckboxField";
			checkboxsdataValue = dataValue;
		}
		else 
			throw new IllegalStateException("bad init CheckboxField");
	}

	@Override
	public View getMappingInstance(final Context context) throws JSONException {
		if(linearlayout != null)
			return linearlayout;
		linearlayout = new LinearLayout(context);
		linearlayout.setOrientation(LinearLayout.VERTICAL);
		textview = new TextView(context);
		textview.setText(displayValue);
		textview.setHint("请点击选择");
		textview.setTextColor(Color.RED);
		linearlayout.addView(textview);
		Button button = new Button(context);
		button.setText("更改选项");
		//linearlayout.addView(button, pa);
		if(display == 4||display == 3){
			button.setClickable(false);
			button.setFocusable(false);
			if(display == 3)
				textview.setVisibility(View.INVISIBLE);
				button.setVisibility(View.VISIBLE);
		}
		else if(display == 2 || display== 1){
			textview.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(dialog == null)
						dialog = new Mydialog(context);
						dialog.setDisplay(context);
				}
			});
		}
		return linearlayout;
	}

	@Override
	public String getInstanceValue() {
		return checkboxsdataValue;
	}
	
	class Mydialog extends Dialog implements android.view.View.OnClickListener{
		private ArrayList<CheckBox> checkboxs = new ArrayList<CheckBox>();
		
		public Mydialog(Context context) {
			super(context);
			setTitle("请选择");
		}
		
		public void setDisplay(Context context){
			try {
				if(checkboxs.size()!=0){
					show();
					return;
				}
				Button button  = new Button(context);
				ScrollView scrollView = new ScrollView(context);
				TableLayout dialogLayout = new TableLayout(context);
				dialogLayout.setOrientation(LinearLayout.VERTICAL);
				button.setText("确定");
				button.setOnClickListener(this);
				dialogLayout.addView(button);
				for(int i = 0;i<list_value.length();i=i+2){
					TableRow row = new TableRow(context);
					row.setPadding(10, 0, 10, 0);
					CheckBox checkbox = new CheckBox(context);
					checkbox.setText(list_value.getJSONObject(i).getString("displayValue"));
					checkbox.setTag(list_value.getJSONObject(i).getString("dataValue"));
					checkboxs.add(checkbox);
					checkbox.setChecked(false);
					row.addView(checkbox);
					if(i+1 < list_value.length()){
						CheckBox checkbox1 = new CheckBox(context);
						checkbox1.setText(list_value.getJSONObject(i+1).getString("displayValue"));
						checkbox1.setTag(list_value.getJSONObject(i+1).getString("dataValue"));
						checkboxs.add(checkbox1);
						checkbox1.setChecked(false);
						row.addView(checkbox1);
					}
					dialogLayout.addView(row);
				}
				scrollView.addView(dialogLayout);
				setContentView(scrollView);
				show();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		@Override
		public void onClick(View v) {
		 	String checkboxsdisplayValue = "";
		 	checkboxsdataValue = "";
			for(int i = 0;i<checkboxs.size();i++){
				if(checkboxs.get(i).isChecked()){
					checkboxsdisplayValue +=(checkboxs.get(i).getText()+";");
					checkboxsdataValue +=(checkboxs.get(i).getTag()+";");
				}
			}
			textview.setText(checkboxsdisplayValue.equals("")?"":checkboxsdisplayValue.substring(0,checkboxsdisplayValue.length()-1));
			checkboxsdataValue = checkboxsdataValue.equals("")?"":checkboxsdataValue.substring(0,checkboxsdataValue.length()-1);
			dismiss();
			
		}
	}
}
