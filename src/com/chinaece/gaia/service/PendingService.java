package com.chinaece.gaia.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

import org.json.JSONArray;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.chinaece.gaia.constant.Gaia;
import com.chinaece.gaia.db.DataStorage;
import com.chinaece.gaia.gui.PendingsActivity;
import com.chinaece.gaia.http.OAHttpApi;
import com.chinaece.gaia.types.AppType;
import com.chinaece.gaia.types.PendingType;
import com.chinaece.gaia.util.NotificationCenter;

public class PendingService extends Service {
	public static final String EXTRA_UPDATE_RATE = "update-rate";
	private String tip = "待办事项提醒";
	private String title = "通知";
	private String content = "有新待办事项";
	private static URL formatUrl;
	private static String token;
	private Collection<PendingType> Lastpendinglist;
	private String strAppids;

	public int onStartCommand(final Intent intent, int flags, final int startId) {
		new Thread(null, new Runnable() {
			@Override
			public void run() {
				doServiceStart(intent, startId);
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, "Background").start();
		return Service.START_REDELIVER_INTENT;
	}

	public void doServiceStart(Intent intent, int startId) {
		while (true) {
			doRequestService();
			try {
				Thread.sleep(60000 * 5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void doRequestService() {
		try {
			DataStorage.load(getApplicationContext());
			if (DataStorage.properties.size() == 0) {
				stopService(new Intent(PendingService.this,
						PendingService.class));
				return;
			} else if (token == null || formatUrl == null) {
				token = DataStorage.properties.getProperty("token");
				formatUrl = new URL(DataStorage.properties.getProperty("url"));
			}
			//
			OAHttpApi OaApi = new OAHttpApi(formatUrl.toString());
			if (strAppids == null && Gaia.APPLIST == null
					&& !OaApi.getApps(token)) {
				stopService(new Intent(PendingService.this,
						PendingService.class));
				return;
			} else if (strAppids == null) {
				JSONArray appids = new JSONArray();
				for (AppType app : Gaia.APPLIST) {
					appids.put(app.getAppid());
				}
				strAppids = appids.toString();
			}
			//
			Collection<PendingType> pendinglist = OaApi.getPending(
					token.toString(), strAppids);
			if (pendinglist != null
					&& PendingService.this.Lastpendinglist != null) {
				StringBuffer strbuffer = new StringBuffer();
				for (int i = 0; i < PendingService.this.Lastpendinglist.size(); i++) {
					strbuffer
							.append(((ArrayList<PendingType>) PendingService.this.Lastpendinglist)
									.get(i).getDocid());
				}
				for (int j = 0; j < pendinglist.size(); j++) {
					String str2 = ((ArrayList<PendingType>) pendinglist).get(j)
							.getDocid();
					if (strbuffer.indexOf(str2) == -1) {
						Intent mintent = new Intent(getApplicationContext(),
								PendingsActivity.class);
						NotificationCenter.sendPendingsNotification(mintent,
								getApplicationContext(), tip, title, content);
					}
				}
			}
			PendingService.this.Lastpendinglist = pendinglist;
		} catch (MalformedURLException e) {
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}
