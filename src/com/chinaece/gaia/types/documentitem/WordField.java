package com.chinaece.gaia.types.documentitem;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import com.chinaece.gaia.db.DataStorage;
import com.chinaece.gaia.util.Downloader;
import com.chinaece.gaia.util.FileUtil;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class WordField extends ItemType{
	private TextView text ;

	public WordField(JSONObject obj) throws JSONException {
		super(obj);
		if(obj.getString("type").equals("WordField"))
			type = "WordField";
		else
			throw new IllegalStateException("bad init WordField");
	}

	@Override
	public View getMappingInstance(final Context context) throws JSONException {
		
		if(text != null)
			return text;
		text = new TextView(context);
		text.setTextColor(Color.RED);
		if(displayValue.length() > 0){
			text.setTag(displayValue);
			text.setText("点击查看Word正文");
			text.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					System.err.println("111");
					File file = new File(android.os.Environment.getExternalStorageDirectory()
				.toString()+"/ece/word"+text.getTag().toString());
					if(file.exists())
						context.startActivity(FileUtil.openFile(file));
					else{
						try {
							Toast.makeText(context, "正在下载，请稍候", Toast.LENGTH_SHORT).show();
							URL formatUrl = new URL(DataStorage.properties.get("url").toString());
							Downloader downloader = new Downloader(context, formatUrl+"/uploads/doc/"+text.getTag().toString(), text.getTag().toString(), "word");
							new Thread(downloader).start();
						} catch (MalformedURLException e) {
							e.printStackTrace();
						}
						
					}
				}
			});
		}
		else{
			text.setText("未附加Word正文");
		}
		return text;
	}

	@Override
	public String getInstanceValue() {
		return dataValue;
	}

}
