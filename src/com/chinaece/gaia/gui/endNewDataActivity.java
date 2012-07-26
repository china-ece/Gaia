package com.chinaece.gaia.gui;

import org.w3c.dom.Text;

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

public class endNewDataActivity extends Activity{
	private DatePicker datepicker = null;
	private TimePicker timePicker = null;
    String month;
    //声明一个独一无二的标识，来作为要显示DatePicker的Dialog的ID：   
    static final int DATE_DIALOG_ID = 1; 
@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	DataStorage.load(endNewDataActivity.this);
	setContentView(R.layout.endnewdate);
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
			if(datepicker.getMonth()<10){
				month="0"+datepicker.getMonth();
			}
            intent.putExtra("year",datepicker.getYear()+"");  
            intent.putExtra("month",month+"");  
            intent.putExtra("day",datepicker.getDayOfMonth()+""); 
            intent.putExtra("h",timePicker.getCurrentHour()+""); 
            intent.putExtra("m",timePicker.getCurrentMinute()+"");            
            endNewDataActivity.this.setResult(1001, intent);  
            finish();//必须手动finish   
	
		}
	});
}
}

