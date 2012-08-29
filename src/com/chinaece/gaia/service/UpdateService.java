package com.chinaece.gaia.service;

import com.chinaece.gaia.R;
import com.chinaece.gaia.gui.MainActivity;
import com.chinaece.gaia.gui.PendingsActivity;
import com.chinaece.gaia.gui.quick.WidgetProvider;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.IBinder;
import android.text.format.Time;
import android.widget.RemoteViews;

public class UpdateService extends Service {
	
	private Bitmap contactCountIcon;
	private NotificationManager nm;
	private Bitmap icon;
	private Bitmap quickicon;
	int contacyCount;
	
	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		UpdateWidget(this);
	}

	private void UpdateWidget(Context context) {
		quickicon=quickmission();
		Time time = new Time();
		time.setToNow();
		RemoteViews updateView = new RemoteViews(context.getPackageName(),
				R.xml.quick);
		updateView.setImageViewBitmap(R.id.quickcoin,quickicon );
		updateView.setTextViewText(R.id.textView, "华东有色OA");

		if(contacyCount==0){
			Intent intent = new Intent(UpdateService.this, MainActivity.class);			
			PendingIntent pendingIntent = PendingIntent.getActivity(UpdateService.this, 0,intent, 0);
			updateView.setOnClickPendingIntent(R.id.quickcoin, pendingIntent);
			AppWidgetManager awg = AppWidgetManager.getInstance(context);
			awg.updateAppWidget(new ComponentName(context, WidgetProvider.class),
					updateView);			
		}else{
			Intent intent = new Intent(UpdateService.this, PendingsActivity.class);			
			PendingIntent pendingIntent = PendingIntent.getActivity(UpdateService.this, 0,intent, 0);
			updateView.setOnClickPendingIntent(R.id.quickcoin, pendingIntent);
			AppWidgetManager awg = AppWidgetManager.getInstance(context);
			awg.updateAppWidget(new ComponentName(context, WidgetProvider.class),
					updateView);	
		}
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	public Bitmap quickmission(){
		Bitmap icon;
		PendingService pendingService=new PendingService();	 	
	 	contacyCount=pendingService.getListsize();
	 	if(contacyCount==0){
			icon=getResIcon(getResources(), R.drawable.appicon);
			contactCountIcon=generatorContactCountIcon(icon);
			nm=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
			showNotifacation(contactCountIcon);	 		
	 	}else {
			icon=getResIcon(getResources(), R.drawable.appicon1);
			contactCountIcon=generatorContactCountIcon(icon);
			nm=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
			showNotifacation(contactCountIcon);			
		}

		return contactCountIcon;
	}
	
	/**
	    * 根据id获取一个图片
	    * @param res
	    * @param resId
	    * @return
	    */ 

private Bitmap getResIcon(Resources res,int resId){
	Drawable icon=res.getDrawable(resId);
	if(icon instanceof BitmapDrawable){
		BitmapDrawable bd=(BitmapDrawable)icon;
		return bd.getBitmap();
	}else{
		return null;
	}
}

/**
 * 在给定的图片的右上角加上联系人数量。数量用红色表示
 * @param icon 给定的图片
 * @return 带联系人数量的图片
 */ 
private Bitmap generatorContactCountIcon(Bitmap icon){
	//初始化画布
	int iconSize=(int)getResources().getDimension(android.R.dimen.app_icon_size);

	Bitmap contactIcon=Bitmap.createBitmap(iconSize, iconSize, Config.ARGB_8888);
	Canvas canvas=new Canvas(contactIcon);
	
	//拷贝图片
	Paint iconPaint=new Paint();
	iconPaint.setDither(true);//防抖动 
 	iconPaint.setFilterBitmap(true);//用来对Bitmap进行滤波处理，这样，当你选择Drawable时，会有抗锯齿的效果 
 	Rect src=new Rect(0, 0, icon.getWidth(), icon.getHeight());
	Rect dst=new Rect(0, 0, iconSize, iconSize);
	canvas.drawBitmap(icon, src, dst, iconPaint);
	
	//在图片上创建一个覆盖的联系人个数
 	if(contacyCount==0){
 	//启用抗锯齿和使用设备的文本字距
    	Paint countPaint=new Paint(Paint.ANTI_ALIAS_FLAG|Paint.DEV_KERN_TEXT_FLAG);
 	    countPaint.setColor(Color.WHITE);
 	    countPaint.setTextSize(15);
 	    Typeface font = Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD);//黑体，加粗
 	    countPaint.setTypeface(font); 	
 	    canvas.drawText(String.valueOf(""), 60, 16, countPaint);	
 	}else{
 	 	Paint countPaint=new Paint(Paint.ANTI_ALIAS_FLAG|Paint.DEV_KERN_TEXT_FLAG);
 	 	countPaint.setColor(Color.WHITE);
 	 	countPaint.setTextSize(15);
 	 	Typeface font = Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD);//黑体，加粗
 	 	countPaint.setTypeface(font); 
 	    canvas.drawText(String.valueOf(contacyCount), 62, 17, countPaint);
 	}
 	return contactIcon;
}


/**
 * 显示状态栏通知
 * @param icon 通知内容图标
 */ 

private void showNotifacation(Bitmap icon){
	Notification notification=new  Notification(R.drawable.appicon,"代办事项数量",System.currentTimeMillis());
	//使用RemoteView自定义通知视图
//	RemoteViews contentView=new RemoteViews(getPackageName(), R.xml.notification);
//	contentView.setImageViewBitmap(R.id.image, icon);
//	contentView.setTextViewText(R.id.text, "图标上的红色数字表示手机中联系人的数量");
//	notification.contentView=contentView;
//	Intent notificationIntent=new Intent(this, QuicklymissionActivity.class);
//	PendingIntent contentIntent=PendingIntent.getActivity(this, 0, notificationIntent, 0);
//	notification.contentIntent=contentIntent;
//	nm.notify(NOTIFICATION_CONTACT_ID, notification);
}
}