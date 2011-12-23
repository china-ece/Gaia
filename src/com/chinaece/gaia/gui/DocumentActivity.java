package com.chinaece.gaia.gui;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.chinaece.gaia.R;
import com.chinaece.gaia.db.DataStorage;
import com.chinaece.gaia.http.OAHttpApi;
import com.chinaece.gaia.types.DocumentType;

public class DocumentActivity extends Activity{
	private URL formatUrl;
    private String docid,formid,appid,token;
    private ScrollView scrollView;
    private LinearLayout linearLayout;
    private Collection<DocumentType> documentlist;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		scrollView = new ScrollView(this);
		linearLayout = new LinearLayout(this);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		scrollView.addView(linearLayout);
		scrollView.setBackgroundResource(R.drawable.login);
		DataStorage.load(DocumentActivity.this);
		try {
			token = DataStorage.properties.get("token").toString();
			formatUrl = new URL(DataStorage.properties.get("url").toString());
			JSONObject document = new JSONObject();
			docid = getIntent().getExtras().getString("docid");
			formid = getIntent().getExtras().getString("formid");
			appid = getIntent().getExtras().getString("appid");
			try {
				document.put("docid", docid);
				document.put("formid", formid);
				document.put("appid",appid);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			ApiTask task = new ApiTask();
			task.execute(formatUrl.toString(),token.toString(),document.toString());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	class ApiTask extends AsyncTask<String,Integer,Collection<DocumentType>>{
		private ProgressDialog dialog;
		
		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(DocumentActivity.this, "请稍等...",
					"正在初始化...");
		}

		@Override
		protected Collection<DocumentType> doInBackground(String... params) {
			OAHttpApi OaApi = new OAHttpApi(params[0]);
			Collection<DocumentType> documentlist = OaApi.getDocument(params[1],
					params[2]);
			return documentlist;
		}
		@Override
		protected void onPostExecute(Collection<DocumentType> documentlist) {
			if(documentlist!=null){
			DocumentActivity.this.documentlist = documentlist;
			LinearLayout.LayoutParams p = new  LinearLayout.LayoutParams(
            		  LinearLayout.LayoutParams.FILL_PARENT,
            		  LinearLayout.LayoutParams.WRAP_CONTENT);
			for(DocumentType docu:documentlist){
				if(docu.getDisplay().equals("1")||equals("4")){
					TextView textview = new TextView(DocumentActivity.this);
					textview.setText(docu.getDesc()+"   "+docu.getValue());
					textview.setTextColor(Color.BLACK);
					linearLayout.addView(textview,p);
				}
				if(docu.getDisplay().equals("2")){
					TextView textview = new TextView(DocumentActivity.this);
					EditText edittext = new EditText(DocumentActivity.this);
					if(docu.getDirection().equals("0")){
						textview.setText(docu.getDesc());
						textview.setTextColor(Color.BLACK);
						edittext.setText(docu.getValue());
						linearLayout.addView(textview,p);
						linearLayout.addView(edittext,p);
					}else{
						edittext.setText(docu.getDesc());
						textview.setText(docu.getValue());
						textview.setTextColor(Color.BLACK);
						linearLayout.addView(edittext,p);
						linearLayout.addView(textview,p);
					}
				}
			}
			setContentView(scrollView);
			dialog.dismiss();
			}
			else{
			dialog.dismiss();
			Toast.makeText(DocumentActivity.this, "数据错误请稍候再试...", Toast.LENGTH_LONG).show();
		}
		}
	}
}
