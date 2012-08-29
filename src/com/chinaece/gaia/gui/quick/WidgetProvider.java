package com.chinaece.gaia.gui.quick;

import com.chinaece.gaia.service.UpdateService;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager; 
import android.appwidget.AppWidgetProvider; 
import android.content.Context; 
import android.content.Intent; 
import android.text.format.Time;

public class WidgetProvider extends AppWidgetProvider { 

    // 没接收一次广播消息就调用一次，使用频繁     
    public void onReceive(Context context, Intent intent) {    	
        // TODO Auto-generated method stub   
        super.onReceive(context, intent);   

    } 
    
    // 没删除一个就调用一次 
    public void onDeleted(Context context, int[] appWidgetIds) { 
        // TODO Auto-generated method stub 
		Intent intent = new Intent(context, UpdateService.class);
	    PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);
		AlarmManager alarm = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		alarm.cancel(pendingIntent);
		context.stopService(intent);
		super.onDeleted(context, appWidgetIds);
    } 
 
	@Override
	   public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
	      
	      super.onUpdate(context, appWidgetManager, appWidgetIds);
	      Time time = new Time();
	      time.setToNow();
	      Intent intent = new Intent(context, UpdateService.class);
	      PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);
	      AlarmManager alarm = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
	      alarm.setRepeating(AlarmManager.RTC, time.toMillis(true), 5000, pendingIntent);
	   }
	
    // 当该Widget第一次添加到桌面是调用该方法，可添加多次但只第一次调用 
    public void onEnabled(Context context) { 
        // TODO Auto-generated method stub 
        System.out.println("OnEnable"); 
        super.onEnabled(context); 
    } 
  
    // 当最后一个该Widget删除是调用该方法，注意是最后一个 
    public void onDisabled(Context context) { 
        // TODO Auto-generated method stub 
        System.out.println("onDisable"); 
        super.onDisabled(context); 
    }
 
}