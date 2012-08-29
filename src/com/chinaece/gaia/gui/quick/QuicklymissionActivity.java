//package com.chinaece.gaia.gui.quick;
//
//import android.app.Activity;
//import android.app.Notification;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.appwidget.AppWidgetManager;
//import android.content.Context;
//import android.content.Intent;
//import android.content.res.Resources;
//import android.graphics.Bitmap;
//import android.graphics.Bitmap.Config;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.Paint;
//import android.graphics.Rect;
//import android.graphics.Typeface;
//import android.graphics.drawable.BitmapDrawable;
//import android.graphics.drawable.Drawable;
//import android.os.Bundle;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.ImageButton;
//import android.widget.RemoteViews;
//import android.widget.TextView;
//
//import com.chinaece.gaia.R;
//import com.chinaece.gaia.gui.MainActivity;
//import com.chinaece.gaia.gui.PendingsActivity;
//import com.chinaece.gaia.service.PendingService;
//
//public class QuicklymissionActivity extends Activity {
//
//	int mAppWidgetId;
//	ImageButton button1;
//	TextView textview;
//	private Bitmap contactCountIcon;
//	private NotificationManager nm;
//	private final static int NOTIFICATION_CONTACT_ID=1;
//	final String mPerfName = "com.chinaece.gaia.gui.quick.QuicklymissionActivity";
//	int contacyCount;
//	
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.xml.quicklymission);			
//		setResult(RESULT_CANCELED);
//		Intent intent = getIntent();
//		Bundle extras = intent.getExtras();
//		if (extras != null) {
//			mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,AppWidgetManager.INVALID_APPWIDGET_ID);
//		}
//
//		if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
//			finish();
//		}
//		
//		button1=(ImageButton)findViewById(R.id.button1);
//		textview=(TextView)findViewById(R.id.textView);
//		textview.setTextColor(Color.WHITE);
//		PendingService pendingService=new PendingService();	 	
//	 	contacyCount=pendingService.getListsize();
//	 	if(contacyCount==0){
//
//			Bitmap icon;
//			icon=getResIcon(getResources(), R.drawable.appicon);
//			contactCountIcon=generatorContactCountIcon(icon);
//			button1.setImageBitmap(contactCountIcon);
//			nm=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
//			showNotifacation(contactCountIcon);
//			
//	 	}else{		
//		Bitmap icon;
//		icon=getResIcon(getResources(), R.drawable.appicon1);
//		contactCountIcon=generatorContactCountIcon(icon);
//		button1.setImageBitmap(contactCountIcon);
//		nm=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
//		showNotifacation(contactCountIcon);
//	 	}
//		
//		button1.setOnClickListener(new OnClickListener() {
//		@Override
//		public void onClick(View v) {
//			// TODO Auto-generated method stub
//			RemoteViews views = new RemoteViews(QuicklymissionActivity.this
//					.getPackageName(), R.xml.quick);
//			views.setImageViewBitmap(R.id.quickcoin,contactCountIcon );
//			views.setTextViewText(R.id.textView, "华东有色OA");
//			
//			if(contacyCount==0){
//				Intent intent = new Intent(QuicklymissionActivity.this, MainActivity.class);			
//				PendingIntent pendingIntent = PendingIntent.getActivity(QuicklymissionActivity.this, 0,intent, 0);
//				intent.setAction(mPerfName + mAppWidgetId);
//				intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,mAppWidgetId);
//				views.setOnClickPendingIntent(R.id.quickcoin, pendingIntent);
//				
//				AppWidgetManager appWidgetManager = AppWidgetManager
//						.getInstance(QuicklymissionActivity.this);
//				appWidgetManager.updateAppWidget(mAppWidgetId, views);	
//			}else{
//				Intent intent = new Intent(QuicklymissionActivity.this, PendingsActivity.class);			
//				PendingIntent pendingIntent = PendingIntent.getActivity(QuicklymissionActivity.this, 0,intent, 0);
//				intent.setAction(mPerfName + mAppWidgetId);
//				intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,mAppWidgetId);
//				views.setOnClickPendingIntent(R.id.quickcoin, pendingIntent);
//				
//				AppWidgetManager appWidgetManager = AppWidgetManager
//						.getInstance(QuicklymissionActivity.this);
//				appWidgetManager.updateAppWidget(mAppWidgetId, views);			
//			}
//							
//			Intent resultValue = new Intent();
//			resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,mAppWidgetId);
//
//			setResult(RESULT_OK, resultValue);
//			finish();
//			
//		}
//	});				
//	}
//		
//	@Override
//	protected void onDestroy() {
//		super.onDestroy();
//		nm.cancel(NOTIFICATION_CONTACT_ID);
//	}
//	
//	/**
//	    * 根据id获取一个图片
//	    * @param res
//	    * @param resId
//	    * @return
//	    */ 
// private Bitmap getResIcon(Resources res,int resId){
// 	Drawable icon=res.getDrawable(resId);
// 	if(icon instanceof BitmapDrawable){
// 		BitmapDrawable bd=(BitmapDrawable)icon;
// 		return bd.getBitmap();
// 	}else{
// 		return null;
// 	}
// }
// 
// /**
//  * 在给定的图片的右上角加上联系人数量。数量用红色表示
//  * @param icon 给定的图片
//  * @return 带联系人数量的图片
//  */ 
// private Bitmap generatorContactCountIcon(Bitmap icon){
// 	//初始化画布
// 	int iconSize=(int)getResources().getDimension(android.R.dimen.app_icon_size);
//
// 	Bitmap contactIcon=Bitmap.createBitmap(iconSize, iconSize, Config.ARGB_8888);
// 	Canvas canvas=new Canvas(contactIcon);
// 	
// 	//拷贝图片
// 	Paint iconPaint=new Paint();
// 	iconPaint.setDither(true);//防抖动 
//  	iconPaint.setFilterBitmap(true);//用来对Bitmap进行滤波处理，这样，当你选择Drawable时，会有抗锯齿的效果 
//  	Rect src=new Rect(0, 0, icon.getWidth(), icon.getHeight());
// 	Rect dst=new Rect(0, 0, iconSize, iconSize);
// 	canvas.drawBitmap(icon, src, dst, iconPaint);
// 	 	
// 	//在图片上创建一个覆盖的联系人个数
// 	if(contacyCount==0){
// 	//启用抗锯齿和使用设备的文本字距
//    	Paint countPaint=new Paint(Paint.ANTI_ALIAS_FLAG|Paint.DEV_KERN_TEXT_FLAG);
// 	    countPaint.setColor(Color.WHITE);
// 	    countPaint.setTextSize(15);
// 	    Typeface font = Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD);//黑体，加粗
// 	    countPaint.setTypeface(font); 	
// 	    canvas.drawText(String.valueOf(""), 60, 16, countPaint);	
// 	}else{
// 	 	Paint countPaint=new Paint(Paint.ANTI_ALIAS_FLAG|Paint.DEV_KERN_TEXT_FLAG);
// 	 	countPaint.setColor(Color.WHITE);
// 	 	countPaint.setTextSize(16);
// 	 	Typeface font = Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD);//黑体，加粗
// 	 	countPaint.setTypeface(font); 
// 	    canvas.drawText(String.valueOf(contacyCount), 60, 16, countPaint);
// 	}
// 	return contactIcon;
// }
// 
// /**
//  * 显示状态栏通知
//  * @param icon 通知内容图标
//  */ 
//
// private void showNotifacation(Bitmap icon){
// 	Notification notification=new  Notification(R.drawable.appicon,"代办事项数量",System.currentTimeMillis());
// 	//使用RemoteView自定义通知视图
//// 	RemoteViews contentView=new RemoteViews(getPackageName(), R.xml.notification);
//// 	contentView.setImageViewBitmap(R.id.image, icon);
//// 	contentView.setTextViewText(R.id.text, "图标上的红色数字表示手机中联系人的数量");
//// 	notification.contentView=contentView;
//// 	Intent notificationIntent=new Intent(this, QuicklymissionActivity.class);
//// 	PendingIntent contentIntent=PendingIntent.getActivity(this, 0, notificationIntent, 0);
//// 	notification.contentIntent=contentIntent;
//// 	nm.notify(NOTIFICATION_CONTACT_ID, notification);
// }
//}