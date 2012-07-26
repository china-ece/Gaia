package com.chinaece.gaia.calendar;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.chinaece.gaia.gui.CalendarActivity;

public class DateView extends ViewGroup {
	private CalendarView cv;
	private JSONArray ja;
	
	public void setJa(JSONArray ja) {
		this.ja = ja;
	}


	public DateView(Context context) {
		super(context);
		cv = new CalendarView(context);
		addView(cv);
		
		Button bt = new Button(context);
		addView(bt);
		
	}
	//新建日程的按钮
	public DateView(final Context context,AttributeSet attrs) {
		super(context,attrs);
		CalendarView cv = new CalendarView(context);
		cv.setId(1000000);
		addView(cv);
		Button bt = new Button(context);
		bt.setText("新日程安排");
		bt.setId(235648);
		addView(bt);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		 super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		 setMeasuredDimension(CalendarActivity.width, 2000);
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int childcount = getChildCount();
		for(int a = 0;a<childcount;a++){
			View child = getChildAt(a);
			child.measure(r - l, b - t);
			if(a<1){
				child.setVisibility(View.VISIBLE);
				child.layout(0,0, child.getMeasuredWidth(), child.getMeasuredHeight());
			}
			if(a == 1){
				child.setVisibility(View.VISIBLE);
				child.layout(0,0, child.getMeasuredWidth(), child.getMeasuredHeight());
			}
			else{
				JSONObject jb;
				try {
					if(ja !=null){
						child.setVisibility(View.VISIBLE);
						jb = (JSONObject) ja.get(a-2);
						child.layout((Integer)jb.get("left"),(Integer)jb.get("top"),(Integer)jb.get("right"),(Integer) jb.get("bottom"));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
			}
		}
	}	
}
