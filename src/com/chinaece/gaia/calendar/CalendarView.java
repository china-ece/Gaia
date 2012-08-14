package com.chinaece.gaia.calendar;

import java.util.Calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.chinaece.gaia.gui.CalendarActivity;



public class CalendarView extends View {
	
	private GetCalendar getCalendar;
	public static final int INVALID_POSITION = -1;
	private int leftPadding = 0;
	private int topPadding = 100;
	private int rowPadding;
	private int colPadding;

	private int year;
	private int month;
	private int day;
	private int hour;
	private int minite;
	private int second;
	private String startTime,endTime;
	
	public String getStartTime() {
		return startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	private Calendar cal = Calendar.getInstance();
	
	public Calendar getCal() {
		return cal;
	}

	private Calendar today = (Calendar) cal.clone();


	private GestureDetector mGestureDetector;
	
	Calendar calendar1 = Calendar.getInstance();
	int hour1 = calendar1.get(Calendar.HOUR_OF_DAY);
	int  minute1 = calendar1.get(Calendar.MINUTE);
	int week = calendar1.get(Calendar.DAY_OF_WEEK);

	
	public CalendarView(Context context) {
		super(context);
		setFocusable(true);
		setFocusableInTouchMode(true);
		mGestureDetector = new GestureDetector(
				new GestureDetector.SimpleOnGestureListener() {

					@Override
					public boolean onFling(MotionEvent e1, MotionEvent e2,
							float velocityX, float velocityY) {

						if (e1.getX() > e2.getX()) {
							cal.add(Calendar.WEEK_OF_YEAR, 1);
							int fyear = cal.get(Calendar.YEAR);
							int fmonth = cal.get(Calendar.MONTH);
							int fday = cal.get(Calendar.DATE);
							int monthcount = getMonthDays(fyear,fmonth);
							int amonthcount = agetMonthDays(fyear, fmonth-1);
							int start = 0,end = 0;
							int mmonth = fmonth,myear = fyear;
							int dayofweek = cal.get(Calendar.DAY_OF_WEEK); 
							start = fday+1-dayofweek;
							if(start > monthcount){
								mmonth = mmonth+1;
								start = start-monthcount;
							}
							if(start <= 0){
								mmonth = mmonth-1;
								if(mmonth-1 == 0){
									myear = myear -1 ;
								}
								start = start + amonthcount;
							}
							end = fday+7-dayofweek;
							if(end > monthcount){
								mmonth = mmonth+1;
								end = start-monthcount;
							}
							if(end <= 0){
								mmonth = mmonth-1;
								if(mmonth-1 == 0){
									myear = myear -1 ;
								}
								end = end + amonthcount;
							}
							
							String mstartTime  = myear+"-"+(mmonth+1)+"-"+start+" "+"00:00:00";
							String mendTime  = myear+"-"+(mmonth+1)+"-"+end+" "+"23:59:59";
							CalendarView.this.invalidate();
							getCalendar.getCalendar(mstartTime, mendTime);
						}

						if (e1.getX() < e2.getX()) {
							cal.add(Calendar.WEEK_OF_YEAR, -1);
							int fyear = cal.get(Calendar.YEAR);
							int fmonth = cal.get(Calendar.MONTH);
							int fday = cal.get(Calendar.DATE);
							int monthcount = getMonthDays(fyear,fmonth);
							int amonthcount = agetMonthDays(fyear, fmonth-1);
							int start = 0,end = 0;
							int mmonth = fmonth,myear = fyear;
							int dayofweek = cal.get(Calendar.DAY_OF_WEEK); 
							start = fday+1-dayofweek;
							if(start > monthcount){
								mmonth = mmonth+1;
								start = start-monthcount;
							}
							if(start <= 0){
								mmonth = mmonth-1;
								if(mmonth-1 == 0){
									myear = myear -1 ;
								}
								start = start + amonthcount;
							}
							end = fday+7-dayofweek;
							if(end > monthcount){
								mmonth = mmonth+1;
								end = start-monthcount;
							}
							if(end <= 0){
								mmonth = mmonth-1;
								if(mmonth-1 == 0){
									myear = myear -1 ;
								}
								end = end + amonthcount;
							}
							
							String estartTime  = myear+"-"+(mmonth+1)+"-"+start+" "+"00:00:00";
							String eendTime  = myear+"-"+(mmonth+1)+"-"+end+" "+"23:59:59";
							CalendarView.this.invalidate();
							getCalendar.getCalendar(estartTime, eendTime);
						}
						
						return true;
					}
				});
	}
	
	public CalendarView(Context context,AttributeSet attrs) {
		super(context,attrs);

		
	}
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		colPadding = (w -leftPadding)/ 8;
		rowPadding = (h - topPadding) /25;
	}
	@Override
	protected void onDraw(Canvas canvas) {
		Paint cPaint;
		Paint tPaint;
		Paint bPaint;
		Paint yPaint;
		Paint lPaint;
		cPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		cPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		cPaint.setColor(Color.RED);
		cPaint.setTextSize(18);

		tPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		tPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		tPaint.setColor(Color.BLACK);
		tPaint.setTextSize(20);

		bPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		bPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		bPaint.setColor(Color.WHITE);
		bPaint.setTextSize(20f);
		
		yPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		yPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		yPaint.setColor(Color.BLACK);
		yPaint.setTextSize(25f);
		
		lPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		lPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		lPaint.setColor(Color.BLACK);
		lPaint.setStrokeWidth(2);

		year = cal.get(Calendar.YEAR);
		month = cal.get(Calendar.MONTH);
		day = cal.get(Calendar.DATE);

		hour = cal.get(Calendar.HOUR);
		minite = cal.get(Calendar.MINUTE);
		second = cal.get(Calendar.SECOND);
		
		canvas.drawColor(Color.WHITE);
		
		float right = canvas.getWidth() - leftPadding;
		float bottom = canvas.getHeight() - topPadding;

		String months = String.valueOf(month+1);
		String yearMonth = year + "年" + months + "月";
		canvas.drawText(yearMonth, (colPadding * 7) / 2 - 35, 50, yPaint);

		String[] weekStr = new String[] { "周日","周一", "周二", "周三", "周四", "周五", "周六" };

		for (int i = 0; i < weekStr.length; i++) {
			canvas.drawText(weekStr[i], (float) (leftPadding + ((float)i+1.1) * colPadding),
					(float) (topPadding + rowPadding*0.4), tPaint);
		}

		canvas.drawLine(leftPadding, topPadding,right, topPadding,lPaint);
		
		for (int i = 0; i < 24; i++) {
		   String str = String.valueOf(i)+":00";
			canvas.drawText(str, leftPadding+2,
					(float) (topPadding+ (i+1.6) * rowPadding), tPaint);
			canvas.drawLine(leftPadding, topPadding + (i+1) * rowPadding, 
					right, topPadding + (i+1) * rowPadding,lPaint);
		}
		for (int i=0; i<8;i++){
			canvas.drawLine((float) (leftPadding + (i+1) * colPadding), topPadding, 
					leftPadding + (i+1) * colPadding, 2000, lPaint);
		}
		
		 Paint dpaint = new Paint();
		 dpaint.setColor(Color.RED);
		 dpaint.setAlpha(50);
		 if(cal.equals(today)){
			 canvas.drawRect(canvas.getWidth()/8*(week), 2000/25+topPadding-5, canvas.getWidth()/8*(week+1),((float)1900/25*(hour1+1))+((float)1900/25/60)*minute1+topPadding, dpaint);
		 }
		int monthcount = getMonthDays(year,month);
		int amonthcount = agetMonthDays(year, month-1);
		int week = cal.get(Calendar.WEEK_OF_MONTH);
		int dateinfo = day;
		int start = 0,end = 0;
		int mmonth = month,myear = year;
		int dayofweek = cal.get(Calendar.DAY_OF_WEEK); 
				
		start = day+1-dayofweek;
		if(start > monthcount){
			mmonth = mmonth+1;
			start = start-monthcount;
		}
		if(start <= 0){
			mmonth = mmonth-1;
			if(mmonth-1 == 0){
				myear = myear -1 ;
			}
			start = start + amonthcount;
		}
		end = day+7-dayofweek;
		if(end > monthcount){
			mmonth = mmonth+1;
			end = start-monthcount;
		}
		if(end <= 0){
			mmonth = mmonth-1;
			if(mmonth-1 == 0){
				myear = myear -1 ;
			}
			end = end + amonthcount;
		}
		
		startTime  = myear+"-"+(mmonth+1)+"-"+start+" "+"00:00:00";
		if(end<start){
			endTime  = myear+"-"+(mmonth+2)+"-"+end+" "+"23:59:59";	
		}else{
		endTime  = myear+"-"+(mmonth+1)+"-"+end+" "+"23:59:59";
		}
		for(int i = 1; i<8;i++)
		{
			dateinfo = day + i - dayofweek;
			if (dateinfo > monthcount){
				mmonth = mmonth+1;
				dateinfo = dateinfo - monthcount;
			}
			if (dateinfo <= 0){
				mmonth = mmonth-1;
				if(mmonth-1 == 0){
					myear = myear -1 ;
				}
				dateinfo = dateinfo + amonthcount;
			}
			double dx = leftPadding + (i+0.2) * colPadding;
			double dy = topPadding +  rowPadding*0.9;
			if (cal.equals(today)) {
				canvas.drawText(dateinfo + "", (float) dx, (float) dy, cPaint);
			} else {
				canvas.drawText(dateinfo + "", (float) dx, (float) dy, tPaint);
			}
		}
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(CalendarActivity.width, 2000);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		 mGestureDetector.onTouchEvent(event);
		 return true;
	}

	private int getMonthDays(int year, int mon)
	{
	mon++;
	switch (mon)
	{
	case 1:
	case 3:
	case 5:
	case 7:
	case 8:
	case 10:
	case 12:
	{
	return 31;
	}
	case 4:
	case 6:
	case 9:
	case 11:
	{
	return 30;
	}
	case 2:
	{
	if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0))
	return 29;
	else
	return 28;
	}}
	return 0;
	}

	public void setGetCalendar(GetCalendar getCalendar) {
		this.getCalendar = getCalendar;
	}
	
	
	private int agetMonthDays(int year, int mon)
	{
		mon++;
		if(mon == 0){
			mon = 12;
		}
	switch (mon)
	{
	case 1:
	case 3:
	case 5:
	case 7:
	case 8:
	case 10:
	case 12:
	{
	return 31;
	}
	case 4:
	case 6:
	case 9:
	case 11:
	{
	return 30;
	}
	case 2:
	{
	if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0))
	return 29;
	else
	return 28;
	}}
	return 0;
	}

}
