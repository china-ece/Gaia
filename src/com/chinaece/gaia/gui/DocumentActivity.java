package com.chinaece.gaia.gui;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONArray;
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
import android.text.Layout;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Toast;

import com.chinaece.gaia.R;
import com.chinaece.gaia.db.DataStorage;
import com.chinaece.gaia.gui.FlowPathActivity.SubmitTask;
import com.chinaece.gaia.http.OAHttpApi;
import com.chinaece.gaia.types.DocumentType;
import com.chinaece.gaia.types.documentitem.BranchType;
import com.chinaece.gaia.types.documentitem.ItemType;

public class DocumentActivity extends Activity {
	private URL formatUrl;
	private String docid, formid, appid, token, str="",appname;
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
//		//
//		menu.add(Menu_OPINION, Menu.FIRST + 1, 1, "保存").setIcon(
//				android.R.drawable.ic_menu_save);
//		menu.add(Menu_OPINION,Menu.FIRST+2,1,"提交").setIcon(android.R.drawable.ic_menu_send);
//		menu.add(Menu_OPINION,Menu.FIRST+3,1,"填写意见").setIcon(android.R.drawable.ic_menu_edit);
//		//
//		menu.add(Menu_EXIT_OPINION,Menu.FIRST+4,1,"退出编辑").setIcon(android.R.drawable.ic_menu_save);
		return true;
	}

//	@Override
//	public boolean onPrepareOptionsMenu(Menu menu) {
//		if(isNormal){
//			menu.setGroupVisible(Menu_NORMAL, true);
//			menu.setGroupVisible(Menu_EXIT_OPINION, false);
//			menu.setGroupVisible(Menu_OPINION, false);
//		}
//		else if(isOpinion){
//			menu.setGroupVisible(Menu_NORMAL, false);
//			menu.setGroupVisible(Menu_EXIT_OPINION, true);
//			menu.setGroupVisible(Menu_OPINION, false);
//		}
//		else{
//			menu.setGroupVisible(Menu_NORMAL, false);
//			menu.setGroupVisible(Menu_EXIT_OPINION, false);
//			menu.setGroupVisible(Menu_OPINION, true);
//		}
//		return super.onPrepareOptionsMenu(menu);
//	}
	
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
//		case Menu.FIRST+3:
//			isOpinion = true;
//			for(int i = 0;i<linearLayout.getChildCount();i++){
//				View v = linearLayout.getChildAt(i);
//				if(v instanceof EditText && ((EditText) v).getInputType()== (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE) ){
//					v.setBackgroundColor(Color.GRAY);
//					getDialog((EditText) v);
//					v.setOnLongClickListener(new OnLongClickListener() {
//						
//						@Override
//						public boolean onLongClick(View v) {
//							getDialog((EditText) v).show();
//							return false;
//						}
//					});
//				}
//			}
//			break;
//		case Menu.FIRST+4:
//			isOpinion = false;
//			for(EditText edit :infoMap.keySet()){
//				edit.setBackgroundColor(Color.WHITE);
//				edit.setOnClickListener(null);
//			}
//			break;
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
			appname = getIntent().getExtras().getString("summary");
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
		LinearLayout lin = new LinearLayout(getApplicationContext());
		lin.setOrientation(LinearLayout.VERTICAL);
		final EditText info = new EditText(getApplicationContext());
		info.setMinLines(3);
		final RadioGroup rg = new RadioGroup(getApplicationContext());
		final RadioButton rb1 = new RadioButton(getApplicationContext());
		rb1.setText("已阅");
		final RadioButton rb2 = new RadioButton(getApplicationContext());
		rb2.setText("同意");
		final RadioButton rb3 = new RadioButton(getApplicationContext());
		rb3.setText("不同意");
		rg.addView(rb1);
		rg.addView(rb2);
		rg.addView(rb3);
		lin.addView(info);
		lin.addView(rg);
		rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if(checkedId == rb1.getId()){
					str = rb1.getText().toString();
					info.setText(str);
				}
				if(checkedId == rb2.getId()){
					str = rb2.getText().toString();
					info.setText(str);
				}
				if(checkedId == rb3.getId()){
					str = rb3.getText().toString();
					info.setText(str);
				}
			}
		});
		AlertDialog.Builder alertBuilder = new AlertDialog.Builder(DocumentActivity.this);
		alertBuilder.setTitle("请输入审批意见");
		alertBuilder.setView(lin);
		alertBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
//				if(str.equals("")){
//					Toast.makeText(getApplicationContext(), "请选择意见", Toast.LENGTH_SHORT).show();
//				}
//				else{
					if(info.getTag() == null)
						info.setTag(edit.getText());
					if(info.getText().toString().trim().equals("")){
						edit.setText(info.getTag().toString());
						return;
					}
					StringBuffer sb = new StringBuffer();
					System.err.println(str);
					SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					sb.append(info.getTag().toString());
					sb.append("\n");
					sb.append(info.getText().toString());
					sb.append(" ");
					sb.append(DataStorage.properties.getProperty("name"));
					sb.append(" ");
					sb.append(date.format(new Date()));
					edit.setText(sb.toString());
					str = "";
//				}
				
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
			for(int i = 0;i<linearLayout.getChildCount();i++){
				View v = linearLayout.getChildAt(i);
				if(v instanceof EditText && ((EditText) v).getInputType()== (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE) ){
					getDialog((EditText) v);
					v.setOnClickListener(null);
					v.setOnLongClickListener(new OnLongClickListener() {
						
						@Override
						public boolean onLongClick(View v) {
							getDialog((EditText) v).show();
							return false;
						}
					});
				}
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
						if(document.getFlowPath().size() == 1 && document.getFlowPath().get(0).getPossibleValue().size()== 0){
							for(BranchType bundle:document.getFlowPath()){
								if(bundle.getMode()==0){
									JSONObject  submit = new JSONObject();
									JSONArray submitTo = new JSONArray();
									JSONObject sub = new JSONObject();
									String currnodeid = document.getCurrNodeid();
									int version = document.getVersion();
									try {
										sub.put("nodeid",bundle.getPathid().toString());
										sub.put("isToPerson", "false");
										sub.put("userids", "[]");
										submitTo.put(sub);
										submit.put("version", version);
										submit.put("docid", docid);
										submit.put("appid", appid);
										submit.put("currnodeid", currnodeid);
										JSONArray ja = new JSONArray();
										ja.put(bundle.getPathid().toString());
										submit.put("nextids",ja );
										submit.put("flowtype", bundle.getFlowtype().toString());
										submit.put("submitto", submitTo.toString());
										String token = DataStorage.properties.getProperty("token");
										URL formatUrl = new URL(DataStorage.properties.getProperty("url"));
										SubmitTask submittask = new SubmitTask();
										submittask.execute(formatUrl.toString(), token.toString(),
									submit.toString());
										System.err.println(submit.toString());
									} catch (JSONException e) {
										e.printStackTrace();
									} catch (MalformedURLException e) {
										e.printStackTrace();
									}
								}
								else {
									JSONObject  submit = new JSONObject();
									String currnodeid = document.getCurrNodeid();
									int version = document.getVersion();
									try {
										submit.put("version", version);
										submit.put("docid", docid);
										submit.put("appid", appid);
										submit.put("currnodeid", currnodeid);
										JSONArray ja = new JSONArray();
										ja.put(bundle.getPathid().toString());
										submit.put("nextids",ja );
										submit.put("flowtype", bundle.getFlowtype().toString());
										submit.put("submitto", "");
										System.err.println(submit.toString());
										String token = DataStorage.properties.getProperty("token");
										URL formatUrl = new URL(DataStorage.properties.getProperty("url"));
										SubmitTask submittask = new SubmitTask();
										submittask.execute(formatUrl.toString(), token.toString(),
									submit.toString());
									} catch (JSONException e) {
										e.printStackTrace();
									} catch (MalformedURLException e) {
										e.printStackTrace();
									}
								}
							}
						}
						else{
							Bundle bundle = new Bundle();
							bundle.putSerializable("branches", document.getFlowPath());
							bundle.putString("currnodeid", document.getCurrNodeid());
							bundle.putString("docid", docid);
							bundle.putString("appid", appid);
							bundle.putString("appname", appname);
							bundle.putInt("version", document.getVersion());
							Intent intent = new Intent(DocumentActivity.this, FlowPathActivity.class);
							intent.putExtras(bundle);
							startActivity(intent);
							DocumentActivity.this.finish();
						}
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
	
	class SubmitTask extends AsyncTask<String, Integer, Boolean> {
		private ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(DocumentActivity.this, "请稍等...",
					"正在提交...");
		}
		
		@Override
		protected Boolean doInBackground(String... params) {
			OAHttpApi OaApi = new OAHttpApi(params[0]);
			Boolean flag = OaApi.submitDocumnet(params[1],
					params[2]);
			return flag;
		}
		
		@Override
		protected void onPostExecute(Boolean flag) {
			dialog.dismiss();
			if(flag){
				
				Intent intent = new Intent(DocumentActivity.this,PendingsActivity.class);
				Bundle bundle = new Bundle();
				bundle.putBoolean("flag", true);
				bundle.putString("appname", appname);
				intent.putExtras(bundle);
				startActivity(intent);
				Intent intent1 = new Intent(DocumentActivity.this,MainActivity.class);
				startActivity(intent1);
				DocumentActivity.this.finish();
				Toast.makeText(getApplicationContext(), "提交成功", Toast.LENGTH_SHORT).show();
			}
			else{
				Toast.makeText(getApplicationContext(), "提交失败", Toast.LENGTH_LONG).show();
			}
		}
	}
	@Override
	protected void onDestroy() {
		if(infoMap!=null)
			infoMap.clear();
		if(document!=null)
			document.getItems().clear();
		super.onDestroy();
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			DocumentActivity.this.finish();
//			Intent intent = new Intent(DocumentActivity.this,PendingsActivity.class);
//			startActivity(intent);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
