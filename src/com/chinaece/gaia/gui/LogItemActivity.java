package com.chinaece.gaia.gui;

import java.util.Calendar;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.chinaece.gaia.R;
import com.chinaece.gaia.db.DataStorage;
import com.chinaece.gaia.types.CalendarType;

public class LogItemActivity extends Activity{
	private CalendarType calendar;
	
@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	DataStorage.load(LogItemActivity.this);
	setContentView(R.layout.logrecord1);
	Calendar cal = Calendar.getInstance();
	final int mYear = cal.get(Calendar.YEAR);
	final int mMonth = cal.get(Calendar.MONTH);
	final int mDay = cal.get(Calendar.DATE);
	int dayofweek = cal.get(Calendar.DAY_OF_WEEK)-1;
	TextView tvdate = (TextView)findViewById(R.id.txt_date);
	tvdate.setText(mYear + "年" + (mMonth+1) + "月" + mDay + "日");
	TextView tvweek = (TextView)findViewById(R.id.txt_week);
	tvweek.setText("星期" + dayofweek);
	if(getIntent().getExtras().getSerializable("calendar")!=null){
		calendar = (CalendarType) getIntent().getExtras().getSerializable("calendar");
	}
	EditText ed1 = (EditText)findViewById(R.id.editText1);
	ed1.setFocusable(false);
	ed1.setText(calendar.getAffair());
	
	EditText ed2 = (EditText)findViewById(R.id.editText2);
	ed2.setFocusable(false);
	ed2.setText(calendar.getContent1());
	
	TextView setdate = (TextView)findViewById(R.id.txt_setdate);
	setdate.setClickable(false);
	setdate.setVisibility(View.INVISIBLE);
		
		
		TextView setStartTime = (TextView)findViewById(R.id.txt_startTime);
		setStartTime.setFocusable(false);
		setStartTime.setText("开始时间："+calendar.getStarttime());
		
		TextView setEndTime = (TextView)findViewById(R.id.txt_endTime);
		setEndTime.setFocusable(false);
		setEndTime.setText("结束时间："+calendar.getEndtime());
	}
}

