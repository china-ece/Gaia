package com.chinaece.gaia.gui;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.chinaece.gaia.db.DataStorage;
import com.chinaece.gaia.http.OAHttpApi;
import com.chinaece.gaia.types.documentitem.ItemType;

public class DocumentActivity extends Activity {
	private URL formatUrl;
	private String docid, formid, appid, token;
	private ScrollView scrollView;
	private LinearLayout linearLayout;
	private FlowLayout layout;
	private Collection<ItemType> itemList = new ArrayList<ItemType>();
	private HashMap<String, String> editables = new HashMap<String, String>();
	JSONObject forntlist = new JSONObject();

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Menu.NONE, Menu.FIRST + 1, 1, "保存").setIcon(
				android.R.drawable.ic_menu_save);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case Menu.FIRST + 1:
			System.err.println(forntlist);
			for (ItemType It : itemList) {
				if (It == null) {
				} else if (It.getInstanceValue() != null) {
					System.err.println(It.getInstanceValue());
				} else {
				}
			}
			break;
		}
		return false;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		scrollView = new ScrollView(this);
		linearLayout = new LinearLayout(this);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		layout = new FlowLayout(getApplicationContext());
		layout.setBackgroundColor(Color.GREEN);
		//scrollView.addView(linearLayout);
		//scrollView.setBackgroundResource(R.drawable.login);
		setContentView(scrollView);
		DataStorage.load(DocumentActivity.this);
		try {
			token = DataStorage.properties.get("token").toString();
			formatUrl = new URL(DataStorage.properties.get("url").toString());
			JSONObject document = new JSONObject();
			ApiTask task = new ApiTask();
			docid = getIntent().getExtras().getString("docid");
			formid = getIntent().getExtras().getString("formid");
			appid = getIntent().getExtras().getString("appid");
			try {
				document.put("docid", docid);
				document.put("formid", formid);
				document.put("appid", appid);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			task.execute(formatUrl.toString(), token.toString(),
					document.toString());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	class ApiTask extends AsyncTask<String, Integer, Collection<ItemType>> {
		private ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(DocumentActivity.this, "请稍等...",
					"正在初始化...");
		}

		@Override
		protected Collection<ItemType> doInBackground(String... params) {
			OAHttpApi OaApi = new OAHttpApi(params[0]);
			Collection<ItemType> documentlist = OaApi.getDocument(params[1],
					params[2]);
			return documentlist;
		}

		@Override
		protected void onPostExecute(Collection<ItemType> documentlist) {
			dialog.dismiss();
			DocumentActivity.this.itemList = documentlist;
			int count = 1;
			FlowLayout.LayoutParams pa = new FlowLayout.LayoutParams(FlowLayout.LayoutParams.MATCH_PARENT, FlowLayout.LayoutParams.WRAP_CONTENT);
			try {
				for (ItemType item : documentlist) {
					if (count == 1) {
						if (item != null)
							layout.addView(item.getMappingInstance(DocumentActivity.this),pa);
						else
							layout.addView(new DummyWidget(DocumentActivity.this),pa);
						count++;
						continue;
					} else if (count == 2) {
						if (item != null)
							layout.addView(item.getMappingInstance(DocumentActivity.this),pa);
						else
							layout.addView(new DummyWidget(DocumentActivity.this),pa);
						count = 1;
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			setContentView(layout);
		}
	}

}
