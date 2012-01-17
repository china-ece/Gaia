package com.chinaece.gaia.gui;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.chinaece.gaia.R;
import com.chinaece.gaia.db.DataStorage;
import com.chinaece.gaia.http.OAHttpApi;
import com.chinaece.gaia.types.DocumentType;
import com.chinaece.gaia.types.documentitem.ItemType;

public class DocumentActivity extends Activity {
	private URL formatUrl;
	private String docid, formid, appid, token,summary;
	private ScrollView scrollView;
	private LinearLayout linearLayout;
	private FlowLayout layout;
	private DocumentType document;
	private HashMap<String, String> editables = new HashMap<String, String>();
	JSONObject forntlist = new JSONObject();

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Menu.NONE, Menu.FIRST + 1, 1, "保存").setIcon(
				android.R.drawable.ic_menu_save);
		menu.add(Menu.NONE,Menu.FIRST+2,1,"提交").setIcon(android.R.drawable.ic_menu_edit);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case Menu.FIRST + 1:
		case Menu.FIRST + 2:
			JSONObject saveParams = new JSONObject();
			JSONObject fields = new JSONObject();
			
			for (ItemType docItem : document.getItems()) {
				if (docItem != null)
					if(docItem.isChanged()){
						try {
							fields.put(URLEncoder.encode(docItem.getName().toString(),"UTF-8"), URLEncoder.encode(docItem.getInstanceValue(),"UTF-8"));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
			}
			
			try {
				saveParams.put("docid", docid);
				saveParams.put("appid", appid);
				saveParams.put("fields", fields);
				System.err.println(saveParams);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			SaveTask savetask = new SaveTask(item.getItemId() == Menu.FIRST + 1?false:true);
			savetask.execute(formatUrl.toString(), token.toString(),
					saveParams.toString());
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
		linearLayout.setBackgroundResource(R.drawable.login);
		scrollView.addView(linearLayout);
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

	class ApiTask extends AsyncTask<String, Integer, DocumentType> {
		private ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(DocumentActivity.this, "请稍等...",
					"正在初始化...");
		}

		@Override
		protected DocumentType doInBackground(String... params) {
			OAHttpApi OaApi = new OAHttpApi(params[0]);
			DocumentType document = OaApi.getDocument(params[1],
					params[2]);
			return document;
		}

		@Override
		protected void onPostExecute(DocumentType document) {
			dialog.dismiss();
			DocumentActivity.this.document = document;
			try {
				for (ItemType item : document.getItems()) {
					if (item!=null)
						linearLayout.addView(item.getMappingInstance(DocumentActivity.this));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	
	class SaveTask extends AsyncTask<String, Integer, Boolean> {
		private ProgressDialog dialog;
		private boolean isJump = false;
		
		public SaveTask(boolean isJump){
			this.isJump = isJump;
		}

		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(DocumentActivity.this, "请稍等...",
					"正在保存文档...");
		}
		
		@Override
		protected Boolean doInBackground(String... params) {
			OAHttpApi OaApi = new OAHttpApi(params[0]);
			Boolean flag = OaApi.saveDocumnet(params[1],
					params[2]);
			return flag;
		}
		
		@Override
		protected void onPostExecute(Boolean flag) {
			if(flag){
				if(isJump){
					if(document.getFlowPath() == null){ 
						Toast.makeText(DocumentActivity.this, "没有提交路径，请联系管理员", Toast.LENGTH_SHORT).show();
					}
					else{
						Bundle bundle = new Bundle();
						bundle.putSerializable("branches", document.getFlowPath());
						bundle.putString("currnodeid", document.getCurrNodeid());
						bundle.putString("docid", docid);
						bundle.putString("appid", appid);
						Intent intent = new Intent(DocumentActivity.this, FlowPathActivity.class);
						intent.putExtras(bundle);
						startActivity(intent);
					}
				}
				else{
					Toast.makeText(DocumentActivity.this, "保存成功", Toast.LENGTH_LONG).show();
				}
			}
			else {
				Toast.makeText(DocumentActivity.this, "保存失败", Toast.LENGTH_LONG).show();
			}
			dialog.dismiss();
		}
	}
}
