package com.chinaece.gaia.util;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.chinaece.gaia.R;

public class NotificationCenter {
	
	private static NotificationManager mNotificationManager ;
	public static int NOTIFICATION_ID = 1;
	public static int PENDING_NOTIFICATION_ID = 65536;
	public static int ONGOING_NOTIFICATION_ID = 65535;
	
	private static void sendNotification(Intent intent, Context context, String tip, String title, String content, int ID, int flags){
		if(mNotificationManager == null)
			mNotificationManager =  (NotificationManager)context.getSystemService(Activity.NOTIFICATION_SERVICE);
		Notification mNotification = new Notification(R.drawable.appicon, tip, System.currentTimeMillis());
		mNotification.flags = flags;
		if(intent != null){
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		}
		PendingIntent mContentIntent = PendingIntent.getActivity(context,0, intent, 0);;
		mNotification.setLatestEventInfo(context, title, content, mContentIntent);  
        mNotificationManager.notify(ID, mNotification); 
	}                                                               
	
	public static void sendNormalNotification(Intent intent, Context context, String tip, String title, String content){
		sendNotification(intent, context, tip, title, content, NOTIFICATION_ID++, Notification.FLAG_AUTO_CANCEL);
	}
	
	public static void sendPendingsNotification(Intent intent, Context context, String tip, String title, String content){
		sendNotification(intent, context, tip, title, content, PENDING_NOTIFICATION_ID, Notification.FLAG_AUTO_CANCEL);
	}
	
	public static void sendOngoingNotification(Intent intent, Context context, String tip, String title, String content)
	{
		sendNotification(intent, context, tip, title, content,ONGOING_NOTIFICATION_ID, Notification.FLAG_ONGOING_EVENT|Notification.FLAG_NO_CLEAR);
	}
	
	public static void sendTimerNotification(Intent intent, Context context, String tip, String title, String content, int time){
		final int nid = NOTIFICATION_ID;
		sendNormalNotification(intent, context, tip, title, content);
		TimerTask cancelTask = new TimerTask() {
			
			@Override
			public void run() {
				NotificationCenter.clearNotification(nid);
			}
		};
		Timer timer = new Timer(true);
		timer.schedule(cancelTask, time * 1000);
	}
	
	public static void clearNotification(int id){
		mNotificationManager.cancel(id);
	}
	
}
