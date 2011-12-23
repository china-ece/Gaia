package com.chinaece.gaia.gui;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import com.chinaece.gaia.R;
import com.chinaece.gaia.db.DataStorage;
import com.chinaece.gaia.http.OAHttpApi;

public class MainActivity extends Activity {
	String token;
	private URL formatUrl;

	/** Called when the activity is first created. */
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DataStorage.load(MainActivity.this);
		token = DataStorage.properties.get("token").toString();
		try {
			formatUrl = new URL(DataStorage.properties.get("url").toString());
			ApiTask task = new ApiTask();
			task.execute(formatUrl.toString(), token.toString());
		} catch (MalformedURLException e) {
		}
		setContentView(R.layout.mainlayout);
		GridView gridview = (GridView) findViewById(R.id.gridview);
		Integer[] images = { R.drawable.userinfo,
        		R.drawable.datalibrary,R.drawable.weatherforecast,
        		R.drawable.ecenewspaper,R.drawable.announcement,
        		R.drawable.pendings,R.drawable.urgentwarning,
        		R.drawable.contact
        		};
		ArrayList<HashMap<String, Object>> meumList = new ArrayList<HashMap<String, Object>>();
		String[] mainmenu = { "个人信息", "资料馆", "天气预报", "华东有色报", "公告", "待办提醒",
				"紧急公文" , "通信录"};
		for (int i = 0; i < mainmenu.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("ItemImage", images[i]);
			map.put("ItemText", mainmenu[i]);
			meumList.add(map);
		}
		SimpleAdapter saMenuItem = new SimpleAdapter(this, meumList,
				R.layout.menuitem, new String[] { "ItemImage", "ItemText" },
				new int[] { R.id.ItemImage, R.id.ItemText });
		gridview.setAdapter(saMenuItem);
		gridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				switch (arg2) {
				case 2:
					Intent intent2 = new Intent(MainActivity.this,
							WeatherActivity.class);
					startActivity(intent2);
					break;
				case 5:
					Intent intent = new Intent(MainActivity.this,
							PendingsActivity.class);
					startActivity(intent);
					break;
				case 7:
					Intent intent7 = new Intent(MainActivity.this,
							ContactsActivity.class);
					startActivity(intent7);
					break;
				default:
					break;
				}
			}
		});

	}

    class ApiTask extends AsyncTask<String, Integer, Boolean> {
		private ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(MainActivity.this, "请稍等...",
					"正在初始化...");
		}

		@Override
		protected Boolean doInBackground(String... params) {
			OAHttpApi OaApi = new OAHttpApi(params[0]);
			boolean flag = OaApi.getApps(params[1]);
			return flag;
		}

		@Override
		protected void onPostExecute(Boolean flag) {
			dialog.dismiss();
		}

	}
}
