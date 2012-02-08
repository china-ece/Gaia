package com.chinaece.gaia.gui;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.chinaece.gaia.R;
import com.chinaece.gaia.db.DataStorage;
import com.chinaece.gaia.http.OAHttpApi;

public class MainActivity extends Activity {
	String token, name;
	private URL formatUrl;

	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DataStorage.load(MainActivity.this);
		token = DataStorage.properties.getProperty("token");
		name = DataStorage.properties.getProperty("name");
		if(token.indexOf("null") != -1){
			AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
			builder.setTitle("提示");
			builder.setMessage("请登陆OA系统生成鉴证码");
			builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Intent intent = new Intent(MainActivity.this,GaiaActivity.class);
					startActivity(intent);
					MainActivity.this.finish();
				}
			});
			builder.show();
			return;
		}
		try {
			formatUrl = new URL(DataStorage.properties.get("url").toString());
			ApiTask task = new ApiTask();
			task.execute(formatUrl.toString(), token.toString());
		} catch (MalformedURLException e) {
		}		
		setContentView(R.layout.mainlayout);
		GridView gridview = (GridView) findViewById(R.id.gridview);
		Integer[] images = { R.drawable.weatherforecast,
        		R.drawable.pendings,
        		R.drawable.contact
		};
		ArrayList<HashMap<String, Object>> meumList = new ArrayList<HashMap<String, Object>>();
		String[] mainmenu = {"天气预报", "待办提醒" , "通信录"};
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
				case 0:
					Intent weatherIntent = new Intent(MainActivity.this,
							WeatherActivity.class);
					startActivity(weatherIntent);
					break;
				case 1:
					Intent pendingIntent = new Intent(MainActivity.this,
							PendingsActivity.class);
					startActivity(pendingIntent);
					break;
				case 2:
					Intent contactsIntent = new Intent(MainActivity.this,
							ContactsActivity.class);
					startActivity(contactsIntent);
					break;
				default:
					break;
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Menu.NONE, Menu.FIRST + 1, 1, "注销").setIcon(
				android.R.drawable.ic_menu_close_clear_cancel);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case Menu.FIRST + 1:
			DataStorage.clear(MainActivity.this);
			Intent intent = new Intent(MainActivity.this, GaiaActivity.class);
			startActivity(intent);
			this.finish();
			break;
		}
		return false;
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
			if(flag){
				TextView txtview = (TextView) findViewById(R.id.textView2);
				txtview.setText("欢迎" + name + "进入华东有色地勘局OA系统");
			}
			else{
				Toast.makeText(MainActivity.this, "请先登陆网页生成验证码", Toast.LENGTH_LONG).show();
			}
		}

	}
}
