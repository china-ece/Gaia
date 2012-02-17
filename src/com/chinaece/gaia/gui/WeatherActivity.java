package com.chinaece.gaia.gui;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinaece.gaia.R;
import com.chinaece.gaia.db.DataStorage;
import com.chinaece.gaia.http.OAHttpApi;
import com.chinaece.gaia.types.WeatherType;

public class WeatherActivity extends Activity{
	private URL formatUrl;
	
	/** Called when the activity is first created. */
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DataStorage.load(WeatherActivity.this);
		try {
			formatUrl = new URL(DataStorage.properties.getProperty("url"));
			ApiTask task = new ApiTask();
			task.execute(formatUrl.toString());
		} catch (MalformedURLException e) {
		}		
	}
	
    class ApiTask extends AsyncTask<String, Integer, Collection<WeatherType>> {
		private ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(WeatherActivity.this, "请稍等...",
					"正在打开...");
		}

		@Override
		protected Collection<WeatherType> doInBackground(String... params) {
			OAHttpApi OaApi = new OAHttpApi(params[0]);
			Collection<WeatherType> weather = OaApi.getWeather();
			return weather;
		}

		@Override
		protected void onPostExecute(Collection<WeatherType> weather) {
			dialog.dismiss();
			if(weather!=null){
			    TextView tv1=new TextView(WeatherActivity.this);
			    tv1.setText(weather.iterator().next().getCity());
			    tv1.setTextSize(25);	
			    tv1.setTextColor(Color.YELLOW);
			    EditText et1= new EditText(WeatherActivity.this);
			    et1.setTextColor(Color.rgb(25, 25, 112));
			    et1.setBackgroundColor(Color.rgb(64, 224, 208));
			    et1.setEnabled(false);
			    et1.setText("今日天气:\n" + weather.iterator().next().getTodayTemp()+"\n"
	        		    +weather.iterator().next().getTodayWeather()+"\n"
	        		    +weather.iterator().next().getTodayWind());
		        EditText et2= new EditText(WeatherActivity.this);
				et2.setTextColor(Color.rgb(25, 25, 112));
				et2.setBackgroundColor(Color.rgb(64, 224, 208));
				et2.setEnabled(false);
		        et2.setText("明日天气:\n" +weather.iterator().next().getTomorrowTemp()+"\n"
		        		   +weather.iterator().next().getTomorrowWeather());
		        
		        EditText et3= new EditText(WeatherActivity.this);
		        et3.setText("小提示:\n" +weather.iterator().next().getTips());
		        et3.setBackgroundColor(Color.rgb(135, 206, 235));
		        et3.setTextColor(Color.rgb(8, 46, 84));
		        et3.setEnabled(false);	        
		        LinearLayout weatherlayout = new LinearLayout(WeatherActivity.this);
		        weatherlayout.setOrientation(LinearLayout.VERTICAL);
		        LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,  
		                LinearLayout.LayoutParams.WRAP_CONTENT);       
		        param1.setMargins(20, 0, 20, 10);
		        LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,  
		                LinearLayout.LayoutParams.WRAP_CONTENT); 
		        param2.setMargins(20, 0, 20, 10);
		        weatherlayout.setBackgroundResource(R.drawable.login);
		        weatherlayout.addView(tv1,param1);
		        weatherlayout.addView(et1,param1);
		        weatherlayout.addView(et2,param1);
		        weatherlayout.addView(et3,param2);
		        setContentView(weatherlayout);				
			}
			else{
				LinearLayout weatherlayout = new LinearLayout(WeatherActivity.this);
				weatherlayout.setBackgroundResource(R.drawable.login);
				setContentView(weatherlayout);
			}
				
		}
	}
    
}

