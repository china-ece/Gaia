 package com.chinaece.gaia.types.documentitem;



import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinaece.gaia.db.DataStorage;
import com.chinaece.gaia.util.Downloader;

public class AttachmentUploadField extends ItemType{
	private URL formatUrl;
	

	public AttachmentUploadField(JSONObject obj) throws JSONException {
		super(obj);
		if(obj.getString("type").equals("AttachmentUploadField"))
			type = "AttachmentUploadField";
		else
			throw new IllegalStateException("bad init RadioField");
	}
	

	@Override
	public View getMappingInstance(final Context context) throws JSONException {
		try {
			formatUrl= new URL(DataStorage.properties.get("url").toString());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		LinearLayout linearLayout = new LinearLayout(context);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		TextView textView1 = new TextView(context);
		textView1.setText("点击下列文件名下载文件");
		linearLayout.addView(textView1);
		if(list_value!=null){
		for(int i = 0;i<list_value.length();i++){
			final TextView textview = new TextView(context);
			textview.setText(list_value.getJSONObject(i).getString("displayValue"));
			textview.setTag(list_value.getJSONObject(i).getString("dataValue"));
			textview.setTextColor(Color.RED);
			textview.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					AlertDialog.Builder builder = new AlertDialog.Builder(context);
					builder.setTitle("提示");
					builder.setMessage("是否要下载"+ textview.getText().toString());
					builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							System.err.println(formatUrl);
							Downloader down = new Downloader(context, (formatUrl+textview.getTag().toString()).toString(), textview.getText().toString());
							new Thread(down).start();
	//			            Bundle bundle = new Bundle();
	//			            bundle.putString("urlStr", urlStr);
	//			            bundle.putString("path", path);
	//			            bundle.putString("fileName", fileName);
	//			            Intent intent = new Intent(context,
	//								DownActivity.class);
	//						intent.putExtras(bundle);
	//						context.startActivity(intent);
						}
					});
					builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
					builder.show();
				}
			});
			linearLayout.addView(textview);
		}
		return linearLayout;
		}
		else{
		return linearLayout;
		}
	}

	@Override
	public String getInstanceValue() {
		return dataValue;
	}
}
