package com.chinaece.gaia.util;

import com.chinaece.gaia.service.PendingService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ServiceBroadcastReceiver extends BroadcastReceiver {
	private static final String ACTION = "android.intent.action.BOOT_COMPLETED";

	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(ACTION)) {
			Intent mintent = new Intent(Intent.ACTION_RUN);
			mintent.setClass(context, PendingService.class);
			context.startService(mintent);
		}
	}
}
