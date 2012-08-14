package com.chinaece.gaia.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;

import android.R.array;
import android.R.integer;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.Vibrator;

import com.chinaece.gaia.constant.Gaia;
import com.chinaece.gaia.db.DataStorage;
import com.chinaece.gaia.gui.PendingsActivity;
import com.chinaece.gaia.gui.newDataActivity;
import com.chinaece.gaia.http.OAHttpApi;
import com.chinaece.gaia.types.AppType;
import com.chinaece.gaia.types.PendingType;
import com.chinaece.gaia.util.NotificationCenter;

public class PendingService extends Service {
	public static final String EXTRA_UPDATE_RATE = "update-rate";
	private String tip = "待办事项提醒";
	private String title = "通知";
	private String token;
	private URL formatUrl;
	private String content = "有新待办事项";
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
				Thread.sleep(6000 * 20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void doRequestService() {
		try {
			DataStorage.load(getApplicationContext());
			if (DataStorage.properties.size() <= 1) {
				stopService(new Intent(PendingService.this,
						PendingService.class));
				return;
			} else {
				 token = DataStorage.properties.getProperty("token");
				 formatUrl = new URL(DataStorage.properties.getProperty("url"));
			}
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
//				Collection<PendingType> pendinglist = OaApi.getPending(
//						token.toString(), strAppids);
				Collection<PendingType> pendinglists = new ArrayList<PendingType>();
				try {
					JSONArray jsa = new JSONArray(strAppids);
					for(int i = 0;i<jsa.length();i++){
						JSONArray appids = new JSONArray();
						appids.put(jsa.get(i));
						OAHttpApi OaApi1 = new OAHttpApi(formatUrl.toString());
						Collection<PendingType> pendinglist = OaApi1.getPending(token,
								appids.toString());
						if(pendinglist != null){
							pendinglists.addAll(pendinglist);
						}
						else{
							try{
								NotificationCenter.clearNotification(65536);
							}catch (Exception e) {
							}
						}						
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				if (pendinglists != null&& PendingService.this.Lastpendinglist != null) {
					ArrayList arrayList=new ArrayList();										
					for (int i = 0; i < PendingService.this.Lastpendinglist.size(); i++) {
						arrayList.add(((ArrayList<PendingType>) PendingService.this.Lastpendinglist).get(i).getDocid());
					}
					for (int j = 0; j < pendinglists.size(); j++) {
						String str2 = ((ArrayList<PendingType>) pendinglists).get(j).getDocid();
						int judge =0;
						for (int k = 0; k < arrayList.size(); k++) {
							if(str2.equals(arrayList.get(k))){
								judge=1;
							}
						}
						if(judge==0){
						Intent mintent = new Intent(getApplicationContext(),PendingsActivity.class);
						NotificationCenter.sendPendingsNotification(mintent,getApplicationContext(), tip, title, content);
					    }
					}

					
//					StringBuffer strbuffer = new StringBuffer();
//					for (int i = 0; i < PendingService.this.Lastpendinglist.size(); i++) {
//						strbuffer
//								.append(((ArrayList<PendingType>) PendingService.this.Lastpendinglist)
//										.get(i).getDocid());
//					}
//					for (int j = 0; j < pendinglists.size(); j++) {
//						String str2 = ((ArrayList<PendingType>) pendinglists).get(j)
//								.getDocid();
//						if (strbuffer.indexOf(str2) == -1) {
//							Intent mintent = new Intent(getApplicationContext(),
//									PendingsActivity.class);
//							NotificationCenter.sendPendingsNotification(mintent,
//									getApplicationContext(), tip, title, content);
//						}
//					}
			}
				PendingService.this.Lastpendinglist = pendinglists;
			
		} catch (MalformedURLException e) {
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}
