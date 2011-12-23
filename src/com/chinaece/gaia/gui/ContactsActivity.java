package com.chinaece.gaia.gui;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.chinaece.gaia.R;
import com.chinaece.gaia.db.DataStorage;
import com.chinaece.gaia.http.OAHttpApi;
import com.chinaece.gaia.types.ContactType;

public class ContactsActivity extends Activity {
	private URL formatUrl;
	private String token;
	private Collection<ContactType> contactlist;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DataStorage.load(ContactsActivity.this);
		setContentView(R.layout.contacts);
		final EditText edittext = (EditText)findViewById(R.id.editText1);
		Button button = (Button)findViewById(R.id.button1);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					token = DataStorage.properties.get("token").toString();
					formatUrl = new URL(DataStorage.properties.get("url").toString());
					JSONObject search = new JSONObject();
					try {
						search.put("search", URLEncoder.encode(edittext.getText().toString(),"UTF-8"));
					} catch (JSONException e) {
						e.printStackTrace();
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					ApiTask task = new ApiTask();
					task.execute(formatUrl.toString(),token.toString(),search.toString());
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
			}
		});
	}
	class ApiTask extends AsyncTask<String, Integer, Collection<ContactType>> {
		private ProgressDialog dialog;
		
		@Override
        protected void onPreExecute() {
			dialog = ProgressDialog.show(ContactsActivity.this, "请稍等...", "正在打开...");
        }
		
		@Override
		protected Collection<ContactType> doInBackground(String... params) {
			OAHttpApi OaApi = new OAHttpApi(params[0]);
			Collection<ContactType> contactlist = OaApi.getContact(params[1], params[2]);
			return contactlist;
		}
		
		@Override
		protected void onPostExecute(Collection<ContactType> contactlist) {
			if(contactlist!=null){
				ContactsActivity.this.contactlist = contactlist;
				final List<Map<String, String>> list = new ArrayList<Map<String, String>>();
				for (ContactType con : contactlist) {
					Map<String, String> map = new HashMap<String, String>();
					map.put("name", con.getName());
					list.add(map);
				}
				SimpleAdapter adapter = new SimpleAdapter(ContactsActivity.this,
						list, R.layout.contactitem, new String[] { "name" },
						new int[] { R.id.name });
				ListView listview = (ListView) findViewById(R.id.listView1);
				listview.setAdapter(adapter);
				dialog.dismiss();
			}
			else{
				dialog.dismiss();
				Toast.makeText(ContactsActivity.this, "数据错误请稍候再试...", Toast.LENGTH_LONG).show();  
			}
		}
    }
}
