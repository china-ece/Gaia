package com.chinaece.gaia.gui;

import com.chinaece.gaia.R;
import com.chinaece.gaia.db.DataStorage;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

public class newDataActivity extends Activity{
	private DatePicker datepicker = null;	
	private TimePicker timePicker = null;

    //声明一个独一无二的标识，来作为要显示DatePicker的Dialog的ID：   
    static final int DATE_DIALOG_ID = 0; 
@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	DataStorage.load(newDataActivity.this);
	setContentView(R.layout.newdate);
	//获取日期选择框中的时间
	datepicker=(DatePicker)findViewById(R.id.datePicker1);	
	timePicker=(TimePicker)findViewById(R.id.timePicker1);
	timePicker.setIs24HourView(true);//设置时间格式为24小时制
	//确定Button事件
	Button button=(Button)findViewById(R.id.button1);
	button.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();  
            intent.putExtra("year",datepicker.getYear()+"");  
            intent.putExtra("month",datepicker.getMonth()+"");  
            intent.putExtra("day",datepicker.getDayOfMonth()+"");  
            intent.putExtra("h",timePicker.getCurrentHour()+""); 
            intent.putExtra("m",timePicker.getCurrentMinute()+""); 
            newDataActivity.this.setResult(1000, intent);  
            finish();//必须手动finish   
	
		}
	});
}
}

