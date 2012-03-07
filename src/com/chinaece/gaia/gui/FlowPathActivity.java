package com.chinaece.gaia.gui;

import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.chinaece.gaia.db.DataStorage;
import com.chinaece.gaia.http.OAHttpApi;
import com.chinaece.gaia.types.documentitem.BranchType;
import com.chinaece.gaia.types.documentitem.BranchType.User;

public class FlowPathActivity extends Activity {
	private JSONArray checkboxsdataValue;
	private String checkName = "",checkFlowtype = "";
	private int mode = 0;
	private ArrayList<String> checkIds = new ArrayList<String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final Bundle bundle = getIntent().getExtras();
		ArrayList<BranchType> branches = (ArrayList<BranchType>) bundle.get("branches");
		LinearLayout linearlayout = new LinearLayout(FlowPathActivity.this);
		linearlayout.setOrientation(LinearLayout.VERTICAL);
		RadioGroup radiogroup = new RadioGroup(FlowPathActivity.this);
		ScrollView scrollview = new ScrollView(FlowPathActivity.this);
		for(final BranchType branch:branches){
			final RadioButton radioButton = new RadioButton(FlowPathActivity.this);
			radioButton.setText(branch.getName());
			radiogroup.addView(radioButton);
			radioButton.setChecked(false);
			if(branch.getPossibleValue().size()!=0){
				ChoiceDialog dialog = new ChoiceDialog(FlowPathActivity.this);
				dialog.init(FlowPathActivity.this, branch.getPossibleValue());
				radioButton.setTag(dialog);
				radioButton.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						checkIds.clear();
						checkIds.add(branch.getPathid());
						mode = branch.getMode();
						checkFlowtype = branch.getFlowtype();
						checkName = branch.getName();
						((Dialog)radioButton.getTag()).show();
					}
				});
			}
			else {
				radioButton.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						checkIds.clear();
						mode = branch.getMode();
						checkIds.add(branch.getPathid());
						checkFlowtype = branch.getFlowtype();
						checkName = branch.getName();
					}
				});
			}
		}
		
		linearlayout.addView(radiogroup);
		Button button = new Button(FlowPathActivity.this);
		button.setText("提交");
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				System.err.println(checkIds+checkName);
				if(mode == 0){
					if(checkName.length() > 0){
						JSONObject  submit = new JSONObject();
						JSONArray submitTo = new JSONArray();
						JSONObject sub = new JSONObject();
						String currnodeid = bundle.getString("currnodeid");
						String docId = bundle.getString("docid");
						String appId = bundle.getString("appid");
						int version = bundle.getInt("version");
						try {
							sub.put("nodeid",checkIds.get(0));
							sub.put("isToPerson", checkboxsdataValue!=null?"true":"false");
							sub.put("userids",  checkboxsdataValue!=null?checkboxsdataValue.toString():"");
							submitTo.put(sub);
							submit.put("version", version);
							submit.put("docid", docId);
							submit.put("appid", appId);
							submit.put("currnodeid", currnodeid);
							JSONArray checks = new JSONArray();
							for(int i = 0;i<checkIds.size();i++){
								checks.put(checkIds.get(i));
							}
							submit.put("nextids", checks);
							submit.put("flowtype", checkFlowtype);
							submit.put("submitto", submitTo.toString());
							String token = DataStorage.properties.getProperty("token");
							URL formatUrl = new URL(DataStorage.properties.getProperty("url"));
							SubmitTask submittask = new SubmitTask();
							submittask.execute(formatUrl.toString(), token.toString(),
						submit.toString());
							System.err.println(submit.toString());
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					else{
						Toast.makeText(getApplicationContext(), "请选择提交路径", Toast.LENGTH_SHORT).show();
					}
				}
				else {
					if(checkName.length() > 0){
						JSONObject  submit = new JSONObject();
						String currnodeid = bundle.getString("currnodeid");
						String docId = bundle.getString("docid");
						String appId = bundle.getString("appid");
						int version = bundle.getInt("version");
						try {
							submit.put("version", version);
							submit.put("docid", docId);
							submit.put("appid", appId);
							submit.put("currnodeid", currnodeid);
							JSONArray checks = new JSONArray();
							for(int i = 0;i<checkIds.size();i++){
								checks.put(checkIds.get(i));
							}
							submit.put("nextids", checks);
							submit.put("flowtype", checkFlowtype);
							submit.put("submitto", "");
							String token = DataStorage.properties.getProperty("token");
							URL formatUrl = new URL(DataStorage.properties.getProperty("url"));
							SubmitTask submittask = new SubmitTask();
							submittask.execute(formatUrl.toString(), token.toString(),
						submit.toString());
							System.err.println(submit.toString());
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					else{
						Toast.makeText(getApplicationContext(), "请选择提交路径", Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
		
		linearlayout.addView(button);
		scrollview.addView(linearlayout);
		setContentView(scrollview);
		
	}
	
	class ChoiceDialog extends Dialog implements android.view.View.OnClickListener{
		private ArrayList<CheckBox> checkboxs = new ArrayList<CheckBox>();
		public ChoiceDialog(Context context) {
			super(context);
			setTitle("请选择审批人");
		}
		
		public void init(Context context,ArrayList<User> listValue){
			if(checkboxs.size()!=0){
				return;
			}
			Button button  = new Button(context);
			ScrollView scrollView = new ScrollView(context);
			TableLayout dialogLayout = new TableLayout(context);
			dialogLayout.setOrientation(LinearLayout.VERTICAL);
			button.setText("确定");
			button.setOnClickListener(this);
			dialogLayout.addView(button);
			for(int i = 0;i<listValue.size();i=i+2){
				TableRow row = new TableRow(context);
				row.setPadding(10, 0, 10, 0);
				CheckBox checkbox = new CheckBox(context);
				checkbox.setText(listValue.get(i).getDisplayValue());
				checkbox.setTag(listValue.get(i).getDataValue());
				checkboxs.add(checkbox);
				checkbox.setChecked(false);
				row.addView(checkbox);
				if(i+1 < listValue.size()){
					CheckBox checkbox1 = new CheckBox(context);
					checkbox1.setText(listValue.get(i+1).getDisplayValue());
					checkbox1.setTag(listValue.get(i+1).getDataValue());
					checkboxs.add(checkbox1);
					checkbox1.setChecked(false);
					row.addView(checkbox1);
				}
				dialogLayout.addView(row);
			}
			scrollView.addView(dialogLayout);
			setContentView(scrollView);
		}
		
		@Override
		public void onClick(View v) {
		 	checkboxsdataValue = new JSONArray();
			for(int i = 0;i<checkboxs.size();i++){
				if(checkboxs.get(i).isChecked()){
					checkboxsdataValue.put(checkboxs.get(i).getTag());
				}
			}
			if(checkboxsdataValue.length() == 0){
				for(int i = 0;i<checkboxs.size();i++){
					checkboxsdataValue.put(checkboxs.get(i).getTag());
				}
			}
			dismiss();
		}
	}
	
	class SubmitTask extends AsyncTask<String, Integer, Boolean> {
		private ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(FlowPathActivity.this, "请稍等...",
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
				Toast.makeText(getApplicationContext(), "提交成功", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(FlowPathActivity.this,MainActivity.class);
				startActivity(intent);
				FlowPathActivity.this.finish();
			}
			else{
				Toast.makeText(getApplicationContext(), "提交失败", Toast.LENGTH_LONG).show();
			}
		}
	}
	
	@Override
	protected void onDestroy() {
		checkIds.clear();
		super.onDestroy();
	}
}
				
