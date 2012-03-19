package com.chinaece.gaia.gui;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.chinaece.gaia.db.DataStorage;
import com.chinaece.gaia.http.OAHttpApi;
import com.chinaece.gaia.types.BossTrackingType;

public class BossTrackingsActivity extends ListActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pendinglist);
		refreshData();
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		refreshData();
	}
	
	private void refreshData(){
		try {
			URL formatUrl = new URL(DataStorage.properties.getProperty("url"));
			ApiTask task = new ApiTask();
			task.execute(formatUrl.toString());
		} catch (MalformedURLException e) {
		}
	}
	
	class ApiTask extends AsyncTask<String, Integer, Collection<BossTrackingType>> {
		private ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(BossTrackingsActivity.this, "请稍等...",
					"正在初始化...");
		}

		@Override
		protected Collection<BossTrackingType> doInBackground(String... params) {
			OAHttpApi OaApi = new OAHttpApi(params[0]);
			Collection<BossTrackingType> bosstracklist = OaApi.getBossTrack();
			return bosstracklist;
		}

		@Override
		protected void onPostExecute(final Collection<BossTrackingType> bosstracklist) {
			if(bosstracklist!=null){
				if(bosstracklist!=null && bosstracklist.size()>0) {
					final List<Map<String, String>> list = new ArrayList<Map<String, String>>();
					 ArrayList<BossTrackingType> arr =  (ArrayList<BossTrackingType>)bosstracklist;
					 final ArrayList<BossTrackingType> newlist = new ArrayList<BossTrackingType>();
						for(int i  = arr.size();i>0;i--){
						newlist.add((BossTrackingType) arr.get(i-1));	
						Map<String, String> map = new HashMap<String, String>();
						map.put("title",((BossTrackingType) arr.get(i-1)).getItem());
						map.put("info",((BossTrackingType) arr.get(i-1)).getDepart()+"   "+((BossTrackingType) arr.get(i-1)).getNum());
						list.add(map);
						}
					SimpleAdapter adapter = new SimpleAdapter(BossTrackingsActivity.this,
							list, R.layout.tracks, new String[] { "title", "info",},
							new int[] { R.id.title, R.id.info });
					ListView listview = (ListView) findViewById(android.R.id.list);
					listview.setAdapter(adapter);
					dialog.dismiss();
					listview.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int arg2, long arg3) {
							BossTrackingType abossTrack = ((ArrayList<BossTrackingType>) newlist )
									.get(arg2);
							Bundle bundle = new Bundle();
							bundle.putSerializable("bosstrack", abossTrack);
							Intent intent = new Intent(BossTrackingsActivity.this,
									TrackActivity.class);
							intent.putExtras(bundle);
							startActivity(intent);
						}
					});
				} 
				else{
					dialog.dismiss();
					Toast.makeText(getApplicationContext(), "沒有跟踪事项!", Toast.LENGTH_LONG).show();
				}
			}
			else
			{
				dialog.dismiss();
				Toast.makeText(getApplicationContext(), "数据错误请稍候再试...", Toast.LENGTH_LONG).show();
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
