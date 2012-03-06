package com.chinaece.gaia.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.R;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;
import android.widget.Toast;

import com.chinaece.gaia.db.DataStorage;

public class UpdateVersionInfo {
	private static VersionInfo versioninfo = new VersionInfo();
	private static final String TAG = "AutoUpdate";

	public static VersionInfo getUpdateInfo(InputStream is) throws Exception {

		XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
		XmlPullParser parser = parserCreator.newPullParser();
		parser.setInput(is, "utf-8");
		int type = parser.getEventType();
		VersionInfo versioninfo = new VersionInfo();
		while (type != XmlPullParser.END_DOCUMENT) {
			switch (type) {
			case XmlPullParser.START_TAG:
				if ("versionCode".equals(parser.getName())) {
					versioninfo.setVersionCode(parser.nextText());
				} else if ("url".equals(parser.getName())) {
					versioninfo.setUrl(parser.nextText());
				} else if ("changelog".equals(parser.getName())) {
					versioninfo.setchangelog(parser.nextText());
				}
				break;
			}
			type = parser.next();
		}
		return versioninfo;
	}

	public static int getCurrentVersionCode(Context context)
			throws NameNotFoundException {
		PackageInfo packInfo = context.getPackageManager().getPackageInfo(
				context.getPackageName(), 0);
		int versionCode = packInfo.versionCode;
		return versionCode;
	}

	public static String getServerVerCode(Context context) {
		URL strUrl;
		try {
			strUrl = new URL("http://10.0.0.82:18081/client/android.xml");
			versioninfo = UpdateVersionInfo.getUpdateInfo(strUrl.openStream());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String latestverCode = versioninfo.getVersionCode();
		return latestverCode;
	}

	public static void CheckVersionTask(Context context) {
		try {
			int versionCode = UpdateVersionInfo.getCurrentVersionCode(context);
			String latestverCode = UpdateVersionInfo.getServerVerCode(context);
			DataStorage.properties.put("changelog", versioninfo.getChangelog());
			DataStorage.save(context);
			int verCode = Integer.parseInt(latestverCode);
			if (versionCode == verCode) {
				Log.i(TAG, "版本号相同无需升级");
			} else {
				Log.i(TAG, "版本号不同 ,提示用户升级 ");
				showUpdateDialog(context, versioninfo.getUrl());
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static AlertDialog showUpdateDialog(final Context context,
			final String strUrl) {
		AlertDialog.Builder updateAlert = new AlertDialog.Builder(context);
		updateAlert.setTitle("软件更新");
		updateAlert.setIcon(R.drawable.ic_partial_secure);
		updateAlert.setMessage("是否需要更新?");
		updateAlert.setPositiveButton("确定更新",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Downloader downLoader = new Downloader(context, strUrl,
								"ECEOA.apk", null);
						new Thread(downLoader).start();
						Toast.makeText(context, "正在下载，请稍等...",
								Toast.LENGTH_SHORT).show();
					}
				});
		updateAlert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
		AlertDialog dialog = updateAlert.create();
		dialog.show();
		return dialog;
	}

}
