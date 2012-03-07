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
	private static VersionInfo versionInfo = new VersionInfo();
	private static final String TAG = "AutoUpdate";

	public static VersionInfo getUpdateInfo(InputStream is) throws Exception {

		XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
		XmlPullParser parser = parserCreator.newPullParser();
		parser.setInput(is, "utf-8");
		int type = parser.getEventType();
		VersionInfo verUpdateInfo = new VersionInfo();
		while (type != XmlPullParser.END_DOCUMENT) {
			switch (type) {
			case XmlPullParser.START_TAG:
				if ("versionCode".equals(parser.getName())) {
					verUpdateInfo.setVersionCode(parser.nextText());
				} else if ("url".equals(parser.getName())) {
					verUpdateInfo.setUrl(parser.nextText());
				} else if ("changelog".equals(parser.getName())) {
					verUpdateInfo.setchangelog(parser.nextText());
				}
				break;
			}
			type = parser.next();
		}
		return verUpdateInfo;
	}

	public static int getCurrentVersionCode(Context context)
			throws NameNotFoundException {
		PackageInfo packInfo = context.getPackageManager().getPackageInfo(
				context.getPackageName(), 0);
		int versionCode = packInfo.versionCode;
		return versionCode;
	}

	public static String getServerVerCode(Context context) {
		URL Url;
		try {
			Url = new URL("http://10.0.0.82:18081/client/android.xml");
			versionInfo = UpdateVersionInfo.getUpdateInfo(Url.openStream());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String latestverCode = versionInfo.getVersionCode();
		return latestverCode;
	}

	public static void CheckVersionTask(final Context context) {
		new Thread(new Runnable() {
			public void run() {
				try {
					int versionCode = UpdateVersionInfo.getCurrentVersionCode(context);
					String latestverCode = UpdateVersionInfo.getServerVerCode(context);
					DataStorage.properties.put("changelog", versionInfo.getChangelog());
					DataStorage.save(context);
					int verCode = Integer.parseInt(latestverCode);
					if (versionCode == verCode) {
						Log.i(TAG, "版本号相同无需升级");
					} else {
						Log.i(TAG, "版本号不同 ,提示用户升级 ");
						Downloader downLoader = new Downloader(context, versionInfo.getUrl(),
								"ECEOA.apk", null);
						new Thread(downLoader).start();
					}
				} catch (NameNotFoundException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
}
