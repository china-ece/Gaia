package com.chinaece.gaia.gui;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.chinaece.gaia.R;
import com.chinaece.gaia.constant.Gaia;
import com.chinaece.gaia.db.DataStorage;
import com.chinaece.gaia.http.OAHttpApi;
import com.chinaece.gaia.types.AppType;
import com.chinaece.gaia.types.PendingType;

public class PendingsActivity extends ListActivity {
	String token;
	private URL formatUrl;
	private Collection<PendingType> pendinglist;

	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pendinglist);
		refreshData();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		refreshData();
	}
	
	private void refreshData(){
		token = DataStorage.properties.getProperty("token");
		try {
			formatUrl = new URL(DataStorage.properties.getProperty("url"));
			ApiTask task = new ApiTask();
			JSONArray appids = new JSONArray();
			for (AppType app : Gaia.APPLIST) {
				appids.put(app.getAppid());
			}
			task.execute(formatUrl.toString(), token.toString(),
					appids.toString());
		} catch (MalformedURLException e) {
		}
	}
	
	class ApiTask extends AsyncTask<String, Integer, Collection<PendingType>> {
		private ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(PendingsActivity.this, "请稍等...",
					"正在初始化...");
		}

		@Override
		protected Collection<PendingType> doInBackground(String... params) {
			OAHttpApi OaApi = new OAHttpApi(params[0]);
			Collection<PendingType> pendinglist = OaApi.getPending(params[1],
					params[2]);
			return pendinglist;
		}

		@Override
		protected void onPostExecute(Collection<PendingType> pendinglist) {
			if(pendinglist!=null) {
				PendingsActivity.this.pendinglist = pendinglist;
				final List<Map<String, String>> list = new ArrayList<Map<String, String>>();
				for (PendingType pet : pendinglist) {
					Map<String, String> map = new HashMap<String, String>();
					map.put("title", pet.getSummary());
					map.put("info", pet.getDate());
					list.add(map);
				}
				SimpleAdapter adapter = new SimpleAdapter(PendingsActivity.this,
						list, R.layout.pendings, new String[] { "title", "info", },
						new int[] { R.id.title, R.id.info });
				ListView listview = (ListView) findViewById(android.R.id.list);
				listview.setAdapter(adapter);
				dialog.dismiss();
				listview.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						PendingType aPending = ((ArrayList<PendingType>) PendingsActivity.this.pendinglist)
								.get(arg2);
						Bundle bundle = new Bundle();
						bundle.putString("docid", aPending.getDocid());
						bundle.putString("formid", aPending.getFormid());
						bundle.putString("appid", aPending.getAppid());
						bundle.putString("summary", aPending.getSummary());
						Intent intent = new Intent(PendingsActivity.this,
								DocumentActivity.class);
						intent.putExtras(bundle);
						startActivity(intent);
					}
				});
			}                                                                                               
			else
			{
				dialog.dismiss();
				Toast.makeText(PendingsActivity.this, "数据错误请稍候再试...", Toast.LENGTH_LONG).show();
			}
		}
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Menu.NONE, Menu.FIRST + 1, 1, "刷新").setIcon(
				android.R.drawable.ic_menu_rotate);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case Menu.FIRST + 1:
			refreshData();
			break;
		}
		return false;
	}
}