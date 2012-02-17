package com.chinaece.gaia.util;

import com.chinaece.gaia.R;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class NotificationCenter {
	
	private static NotificationManager mNotificationManager ;
	private static int NOTIFICATION_ID = 1;
	
	public static void sendNotification(Intent intent, Context context, String tip, String title, String content){
		if(mNotificationManager == null)
			mNotificationManager =  (NotificationManager)context.getSystemService(Activity.NOTIFICATION_SERVICE);
		Notification mNotification = new Notification(R.drawable.appicon, tip, System.currentTimeMillis());
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
		mNotification.flags |= Notification.FLAG_AUTO_CANCEL;
        PendingIntent mContentIntent =PendingIntent.getActivity(context,0, intent, 0);  
        mNotification.setLatestEventInfo(context, title, content, mContentIntent);  
        mNotificationManager.notify(NOTIFICATION_ID++, mNotification); 
	}
}
