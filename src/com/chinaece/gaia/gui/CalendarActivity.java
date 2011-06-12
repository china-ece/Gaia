package com.chinaece.gaia.gui;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.chinaece.gaia.R;
import com.chinaece.gaia.calendar.CalendarView;
import com.chinaece.gaia.calendar.DateView;
import com.chinaece.gaia.db.DataStorage;
import com.chinaece.gaia.http.OAHttpApi;
import com.chinaece.gaia.types.CalendarType;

public class CalendarActivity extends Activity implements com.chinaece.gaia.calendar.GetCalendar{
	private DateView dv;
	public static int width;
	private CalendarView cv;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	DataStorage.load(this);
        super.onCreate(savedInstanceState);
        refresh();
        Timer time = new Timer();
        time.schedule(new TimerTask() {
			
			@Override
			public void run() {
				 Looper.prepare();
				 String token = DataStorage.properties.getProperty("token");
					URL formatUrl;
					try {
						formatUrl = new URL(DataStorage.properties.getProperty("url"));
						ApiTask api = new ApiTask();
						api.execute(formatUrl.toString(),token.toString(),cv.getStartTime().toString(),cv.getEndTime().toString());
					} catch (MalformedURLException e) {
						e.printStackTrace();
					}
				Looper.loop();	
			}
		}, 500);
   }
    
	public void refresh(){
		DisplayMetrics dm=new DisplayMetrics();  
        getWindowManager().getDefaultDisplay().getMetrics(dm);  
        width = dm.widthPixels;
		 setContentView(R.layout.date);
	        cv = (CalendarView)findViewById(1000000);
	        cv.setGetCalendar(this);
	        dv = (DateView)findViewById(R.id.calview);
	}
	

	class ApiTask extends AsyncTask<String, Integer, Collection<CalendarType>>{
		private ProgressDialog dialog;
		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(CalendarActivity.this, "提示", "正在下载请稍等");
		}	

		@Override
		protected Collection<CalendarType> doInBackground(final String... params) {
			OAHttpApi api = new OAHttpApi(params[0]);
			Collection<CalendarType>	calendartype = api.getBossCalendar(params[1],params[2],params[3]);
			return calendartype;
		}
		
		@Override
		protected void onPostExecute(Collection<CalendarType> calendarList) {
			int count = dv.getChildCount();
			for(int i = 0;i<count;i++){
				if(i>1){
					dv.removeViewAt(2);
				}
			}
			JSONArray array = new JSONArray();
			dialog.dismiss();
			if(calendarList!=null){
				if(calendarList.size()>0){
					for(final CalendarType calendar:calendarList){
						SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						String sta = calendar.getStarttime();
						String end = calendar.getEndtime();
						try {
							java.util.Date da = date.parse(sta);
							Calendar cal = Calendar.getInstance();
							cal.setTime(da);
							int sweek = cal.get(cal.DAY_OF_WEEK);
							int shour = cal.get(cal.HOUR_OF_DAY);
							int sminute = cal.get(cal.MINUTE);
							java.util.Date eda = date.parse(end);
							Calendar ecal = Calendar.getInstance();
							ecal.setTime(eda);
							int eweek = ecal.get(ecal.DAY_OF_WEEK);
							int ehour = ecal.get(ecal.HOUR_OF_DAY);
							int eminute = ecal.get(ecal.MINUTE);
							if(sweek == eweek){
								JSONObject jb = new JSONObject();
								int left = (int)(width/8*(sweek)+1);
								int top = (int) (100+1900/25*(shour+1)+((float)(1900/25))/60*sminute);
								int right = (int)(width/8*(sweek+1)-1);
								int bottom = (int) (100+1900/25*(ehour+1)+((float)(1900/25))/60*eminute);
								try {
									jb.put("left", left);
									jb.put("top", top);
									jb.put("right", right);
									jb.put("bottom", bottom);
									array.put(jb);
								} catch (JSONException e) {
									e.printStackTrace();
								}
								EditText edt = new EditText(getApplicationContext());
								edt.setText(calendar.getAffair());
								edt.setFocusable(false);
								edt.setWidth(60);
								edt.setTextSize(10);
								edt.setOnClickListener(new OnClickListener() {
									
									@Override
									public void onClick(View v) {
										Intent intent = new Intent(CalendarActivity.this,LogItemActivity.class);
										Bundle bundle = new Bundle();
										bundle.putSerializable("calendar", calendar);
										intent.putExtras(bundle);
										startActivity(intent);
									}
								});
								edt.setBackgroundColor(0x80000000);
								dv.addView(edt);
							}
							if(eweek>sweek){
								JSONObject jb = new JSONObject();
								int left = (int)(width/8*(sweek)+1);
								int top = (int) (100+1900/25*(shour+1)+((float)(1900/25))/60*sminute);
								int right = (int)(width/8*(sweek+1)-1);
								int bottom = (int) (100+1900/25*(23+1)+((float)(1900/25))/60*60);
								try {
									jb.put("left", left);
									jb.put("top", top);
									jb.put("right", right);
									jb.put("bottom", bottom);
									array.put(jb);
								} catch (JSONException e) {
									e.printStackTrace();
								}
								EditText edt = new EditText(getApplicationContext());
								edt.setText(calendar.getAffair());
								edt.setFocusable(false);
								edt.setOnClickListener(new OnClickListener() {
									
									@Override
									public void onClick(View v) {
										Intent intent = new Intent(CalendarActivity.this,LogItemActivity.class);
										Bundle bundle = new Bundle();
										bundle.putSerializable("calendar", calendar);
										intent.putExtras(bundle);
										startActivity(intent);
									}
								});
								edt.setTextSize(10);
								edt.setWidth(60);
								edt.setBackgroundColor(0x80000000);
								dv.addView(edt);
								JSONObject jb1 = new JSONObject();
								int left1 = (int)(width/8*(eweek)+1);
								int top1 = (int) (100+1900/25*(0+1)+((float)(1900/25))/60*0);
								int right1 = (int)(width/8	*(eweek+1)-1);
								int bottom1 = (int) (100+1900/25*(ehour+1)+((float)(1900/25))/60*eminute);
								try {
									jb1.put("left", left1);
									jb1.put("top", top1);
									jb1.put("right", right1);
									jb1.put("bottom", bottom1);
									array.put(jb1);
								} catch (JSONException e) {
									e.printStackTrace();
								}
								EditText edt1 = new EditText(getApplicationContext());
								edt1.setText(calendar.getAffair());
								edt1.setFocusable(false);
								edt1.setTextSize(10);
								edt1.setOnClickListener(new OnClickListener() {
									
									@Override
									public void onClick(View v) {
										Intent intent = new Intent(CalendarActivity.this,LogItemActivity.class);
										Bundle bundle = new Bundle();
										bundle.putSerializable("calendar", calendar);
										intent.putExtras(bundle);
										startActivity(intent);
									}
								});
								edt1.setWidth(60);
								edt1.setBackgroundColor(0x80000000);
								dv.addView(edt1);
								if(eweek-sweek>1){
									for(int i = 1 ;i<eweek-sweek;i++){
										JSONObject jb2 = new JSONObject();
										int left2 = (int)(width/8*(sweek+i)+1);
										int top2 = (int) (100+1900/25*(0+1)+((float)(1900/25))/60*0);
										int right2 = (int)(width/8*(sweek+i+1)-1);
										int bottom2 = (int) (100+1900/25*(23+1)+((float)(1900/25))/60*60);
										try {
											jb2.put("left", left2);
											jb2.put("top", top2);
											jb2.put("right", right2);
											jb2.put("bottom", bottom2);
											array.put(jb2);
										} catch (JSONException e) {
											e.printStackTrace();
										}
										EditText edt2 = new EditText(getApplicationContext());
										edt2.setText(calendar.getAffair());
										edt2.setFocusable(false);
										edt2.setTextSize(10);
										edt2.setWidth(60);
										edt2.setOnClickListener(new OnClickListener() {
											
											@Override
											public void onClick(View v) {
												Intent intent = new Intent(CalendarActivity.this,LogItemActivity.class);
												Bundle bundle = new Bundle();
												bundle.putSerializable("calendar", calendar);
												intent.putExtras(bundle);
												startActivity(intent);
											}
										});
										edt2.setBackgroundColor(0x80000000);
										dv.addView(edt2);
									}
								}
							}
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
					System.err.println(array);
					dv.setJa(array);
				}
				else{
					Toast.makeText(getApplicationContext(), "暂无日志", Toast.LENGTH_SHORT).show();
				}
			}
			else{
				Toast.makeText(getApplicationContext(), "网络出错请稍候再试...", Toast.LENGTH_SHORT).show();
			}
			
		}
	}

	@Override
	public void getCalendar(String startTime, String endTime) {
		 String token = DataStorage.properties.getProperty("token");
			URL formatUrl;
			try {
				formatUrl = new URL(DataStorage.properties.getProperty("url"));
				ApiTask api = new ApiTask();
				api.execute(formatUrl.toString(),token.toString(),startTime.toString(),endTime.toString());
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
	}
}