package com.chinaece.gaia.gui;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
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
	private String docid, formid, appid, token;
	private LinearLayout linearLayout;
	private DocumentType document;
	private boolean isNormal = true, isOpinion = false;
	private HashMap<EditText, AlertDialog> infoMap = new HashMap<EditText, AlertDialog>();
	private static final int Menu_NORMAL = 1;
	private static final int Menu_EXIT_OPINION = 2;
	private static final int Menu_OPINION = 3;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Menu_NORMAL, Menu.FIRST + 1, 1, "保存").setIcon(
				android.R.drawable.ic_menu_save);
		menu.add(Menu_NORMAL,Menu.FIRST+2,1,"提交").setIcon(android.R.drawable.ic_menu_send);
		//
		menu.add(Menu_OPINION, Menu.FIRST + 1, 1, "保存").setIcon(
				android.R.drawable.ic_menu_save);
		menu.add(Menu_OPINION,Menu.FIRST+2,1,"提交").setIcon(android.R.drawable.ic_menu_send);
		menu.add(Menu_OPINION,Menu.FIRST+3,1,"填写意见").setIcon(android.R.drawable.ic_menu_edit);
		//
		menu.add(Menu_EXIT_OPINION,Menu.FIRST+4,1,"退出编辑").setIcon(android.R.drawable.ic_menu_save);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if(isNormal){
			menu.setGroupVisible(Menu_NORMAL, true);
			menu.setGroupVisible(Menu_EXIT_OPINION, false);
			menu.setGroupVisible(Menu_OPINION, false);
		}
		else if(isOpinion){
			menu.setGroupVisible(Menu_NORMAL, false);
			menu.setGroupVisible(Menu_EXIT_OPINION, true);
			menu.setGroupVisible(Menu_OPINION, false);
		}
		else{
			menu.setGroupVisible(Menu_NORMAL, false);
			menu.setGroupVisible(Menu_EXIT_OPINION, false);
			menu.setGroupVisible(Menu_OPINION, true);
		}
		return super.onPrepareOptionsMenu(menu);
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
				saveParams.put("version", document.getVersion());
				saveParams.put("docid", docid);
				saveParams.put("appid", appid);
				saveParams.put("fields", fields);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			SaveTask savetask = new SaveTask(item.getItemId() == Menu.FIRST + 1?false:true);
			savetask.execute(formatUrl.toString(), token.toString(),
					saveParams.toString());
			break;
		case Menu.FIRST+3:
			isOpinion = true;
			for(int i = 0;i<linearLayout.getChildCount();i++){
				View v = linearLayout.getChildAt(i);
				if(v instanceof EditText && ((EditText) v).getInputType()== (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE) ){
					v.setBackgroundColor(Color.GRAY);
					getDialog((EditText) v);
					v.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(final View v) {
							getDialog((EditText) v).show();
						}
					});
				}
			}
			break;
		case Menu.FIRST+4:
			isOpinion = false;
			for(EditText edit :infoMap.keySet()){
				edit.setBackgroundColor(Color.WHITE);
				edit.setOnClickListener(null);
			}
			break;
		}
		return false;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ScrollView scrollView = new ScrollView(getApplicationContext());
		linearLayout = new LinearLayout(getApplicationContext());
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		linearLayout.setBackgroundResource(R.drawable.documentblack);
		scrollView.addView(linearLayout);
		setContentView(scrollView);
		try {
			token = DataStorage.properties.getProperty("token");
			formatUrl = new URL(DataStorage.properties.getProperty("url"));
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
	
	private AlertDialog getDialog(final EditText edit){
		if(infoMap.containsKey(edit))
			return infoMap.get(edit);
		final EditText info = new EditText(getApplicationContext());
		info.setMinLines(5);
		AlertDialog.Builder alertBuilder = new AlertDialog.Builder(DocumentActivity.this);
		alertBuilder.setTitle("请输入审批意见");
		alertBuilder.setView(info);
		alertBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(info.getTag() == null)
					info.setTag(edit.getText());
				if(info.getText().toString().trim().equals("")){
					edit.setText(info.getTag().toString());
					return;
				}
				StringBuffer sb = new StringBuffer();
				SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				sb.append(info.getTag().toString());
				sb.append("\n");
				sb.append(info.getText().toString());
				sb.append(" ");
				sb.append(DataStorage.properties.getProperty("name"));
				sb.append(" ");
				sb.append(date.format(new Date()));
				edit.setText(sb.toString());
			}
		});
		alertBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		AlertDialog dialog = alertBuilder.create();
		infoMap.put(edit, dialog);
		return dialog;
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
			if(document == null){
				Toast.makeText(getApplicationContext(), "加载失败，请重试。", Toast.LENGTH_SHORT).show();
				return;
			}
			DocumentActivity.this.document = document;
			try {
				for (ItemType item : document.getItems()) {
					if (item!=null){
						linearLayout.addView(item.getMappingInstance(DocumentActivity.this));
						if(item.getName() != null && item.getName().toLowerCase().trim().equals("processview"))
							isNormal = false;
					}
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
				document.setVersion(document.getVersion()+1);
				if(isJump){
					if(document.getFlowPath() == null){ 
						Toast.makeText(getApplicationContext(), "没有提交路径，请联系管理员", Toast.LENGTH_SHORT).show();
					}
					else{
						Bundle bundle = new Bundle();
						bundle.putSerializable("branches", document.getFlowPath());
						bundle.putString("currnodeid", document.getCurrNodeid());
						bundle.putString("docid", docid);
						bundle.putString("appid", appid);
						bundle.putInt("version", document.getVersion());
						Intent intent = new Intent(DocumentActivity.this, FlowPathActivity.class);
						intent.putExtras(bundle);
						startActivity(intent);
						DocumentActivity.this.finish();
					}
				}
				else{
					Toast.makeText(getApplicationContext(), "保存成功", Toast.LENGTH_LONG).show();
				}
			}
			else {
				Toast.makeText(getApplicationContext(), "保存失败", Toast.LENGTH_LONG).show();
			}
			dialog.dismiss();
		}
	}
	
	@Override
	protected void onDestroy() {
		//infoMap.clear();
		document.getItems().clear();
		super.onDestroy();
	}
}
