package com.chinaece.gaia.gui;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.chinaece.gaia.R;
import com.chinaece.gaia.db.DataStorage;
import com.chinaece.gaia.http.OAHttpApi;

public class newLogItemActivity extends Activity{
    //用来保存年月日：   
    private int mYear;  
    private int mMonth;  
    private int mDay; 
    private int h;
    private int m;   
	private String formatUrl,appid, token, formid,url;    
    private TextView editText3=null;
    private TextView endeditText=null;
    private EditText nameeditText=null;
    private EditText editText=null;    
    private static final int STEP1 = 1000;   
    private static final int STEP2 = 1001;    
    
@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);  
	
	DataStorage.load(newLogItemActivity.this);
	setContentView(R.layout.logitem);
	Calendar cal = Calendar.getInstance();
	
	final int mYear = cal.get(Calendar.YEAR);
	final int mMonth = cal.get(Calendar.MONTH);
	final int mDay = cal.get(Calendar.DATE);
	int dayofweek = cal.get(Calendar.DAY_OF_WEEK)-1;

	TextView tvdate = (TextView)findViewById(R.id.txt_date);
	tvdate.setText(mYear + "年" + (mMonth+1) + "月" + mDay + "日");
	TextView tvweek = (TextView)findViewById(R.id.txt_week);
    if(dayofweek==4){
    	tvweek.setText("星期四");
    }else if(dayofweek==1){
    	tvweek.setText("星期一");
	}else if(dayofweek==2){
    	tvweek.setText("星期二");
	}else if(dayofweek==3){
    	tvweek.setText("星期三");
	}else if(dayofweek==5){
    	tvweek.setText("星期五");
	}else if(dayofweek==6){
    	tvweek.setText("星期六");
	}else if(dayofweek==7){
    	tvweek.setText("星期日");
	}
	
	editText3=(TextView)findViewById(R.id.TextView3);
	nameeditText=(EditText)findViewById(R.id.editText1);
	editText=(EditText)findViewById(R.id.editText2);
	
	//开始日程时间设置
	Button button3=(Button)findViewById(R.id.button3); 
	button3.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent calIntent = new Intent(newLogItemActivity.this,newDataActivity.class);
			newLogItemActivity.this.startActivityForResult(calIntent, STEP1);//数字随意 
		}
	});
	endeditText=(TextView)findViewById(R.id.TextView4);
	//结束日程时间设置
	Button button4=(Button)findViewById(R.id.button4); 
	button4.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent calIntent = new Intent(newLogItemActivity.this,endNewDataActivity.class);
			newLogItemActivity.this.startActivityForResult(calIntent, STEP2);//数字随意 	
		}
	});
	
	//返回Button,实现返回日历原来界面
	Button button=(Button)findViewById(R.id.button1);
	button.setOnClickListener(new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent calIntent = new Intent(newLogItemActivity.this,CalendarActivity.class);
			startActivity(calIntent);
		}
		
	});
	
	//提交Button,实现重组新数据，发回数据库，并且在日历界面上展现
	Button button2=(Button)findViewById(R.id.button2);
	button2.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			String endtime=endeditText.getText().toString();
			String starttime=editText3.getText().toString();
			String nameString=nameeditText.getText().toString();
			String string=editText.getText().toString();
			//String aaa= endtime.replaceAll("[-:]+","");//去除字符串中的"-"和":"

			if("".equals(nameString)&&(nameString.equals(""))){
				Toast.makeText(getApplicationContext(), "标题不能为空", Toast.LENGTH_SHORT).show();
			}else {
				
			    if("".equals(starttime)&&(starttime.equals(""))){
			    	Toast.makeText(getApplicationContext(), "开始时间不能为空", Toast.LENGTH_SHORT).show(); 
			    }else{			
			         if("".equals(endtime)&&(endtime.equals(""))){
			             Toast.makeText(getApplicationContext(), "结束时间不能为空", Toast.LENGTH_SHORT).show(); 
			    	 }else{
			    				token = DataStorage.properties.getProperty("token");
								formatUrl =DataStorage.properties.getProperty("url");
			    				appid="11df-d5cd-a070e761-9234-c19050f7abd5";//OA系统的APPid
			    				//url="http://oa.china-ece.com:18081/client/getApps.action?";
			    				formid="11e0-0748-ff23f103-bbfc-a93bc9c46f71";
			    				
			    				JSONObject document = new JSONObject();
			    				JSONObject document1 = new JSONObject();
			    				
			    				try {

			    					try {
										document.put("affair",URLEncoder.encode(nameString,"UTF-8"));
										document.put("content1", URLEncoder.encode(string,"UTF-8"));
			    					} catch (UnsupportedEncodingException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

			    					document.put("starttime", starttime);
			    					document.put("endtime", endtime);			    					
			    					
			    				} catch (JSONException e) {
			    					e.printStackTrace();
			    				}
			    				
			    				try {
			    					document1.put("formid", formid);
			    					document1.put("appid", appid);	
			    					document1.put("fields", document);	
								} catch (Exception e) {
									// TODO: handle exception
								}
			    				 				
			    				SubmitTask task = new SubmitTask();
			    				task.execute(formatUrl.toString(), token.toString(),document1.toString());
			    	 }			     
			    }			
			}
		}
	});
	
}

//处理返回的结果：   
@Override  
protected void onActivityResult(int requestCode, int resultCode, Intent data) { 
    switch (requestCode) {   
    case STEP1:   
        mYear = Integer.parseInt(data.getStringExtra("year"));  
        mMonth = Integer.parseInt(data.getStringExtra("month"));  
        mDay = Integer.parseInt(data.getStringExtra("day"));  
        h= Integer.parseInt(data.getStringExtra("h"));
        m= Integer.parseInt(data.getStringExtra("m"));
        //设置文本的内容：   
        editText3.setText(new StringBuilder()  
                    .append(mYear).append("-")  
                    .append(mMonth + 1).append("-")//得到的月份+1，因为从0开始   
                    .append(mDay).append(" ")
                    .append(h).append(":")
                    .append(m).append(":")
                    .append("00").append(""));        
        break;   
    case STEP2:   
        mYear = Integer.parseInt(data.getStringExtra("year"));  
        mMonth = Integer.parseInt(data.getStringExtra("month"));
        mDay = Integer.parseInt(data.getStringExtra("day")); 
        h= Integer.parseInt(data.getStringExtra("h"));
        m= Integer.parseInt(data.getStringExtra("m"));        
        //设置文本的内容：   
        endeditText.setText(new StringBuilder()  
                    .append(mYear).append("-")  
                    .append(mMonth + 1).append("-")//得到的月份+1，因为从0开始   
                    .append(mDay).append("  ")
                    .append(h).append(":")
                    .append(m).append(":")
                    .append("00").append(""));        
        break; 
    }
}

class SubmitTask extends AsyncTask<String, Integer, Boolean> {
	private ProgressDialog dialog;

	@Override
	protected void onPreExecute() {
		dialog = ProgressDialog.show(newLogItemActivity.this, "请稍等...",
				"正在提交...");
	}
	
	@Override
	protected Boolean doInBackground(String... params) {
		OAHttpApi OaApi = new OAHttpApi(params[0]);
		Boolean flag = OaApi.newBbuild(params[1],
				params[2]);
		return flag;
	}
	
	@Override
	protected void onPostExecute(Boolean flag) {
		dialog.dismiss();
		if(flag){
			Toast.makeText(getApplicationContext(), "提交成功", Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(newLogItemActivity.this,CalendarActivity.class);
			startActivity(intent);
			newLogItemActivity.this.finish();
		}
		else{
			Toast.makeText(getApplicationContext(), "提交失败", Toast.LENGTH_LONG).show();
		}
	}
}

}  