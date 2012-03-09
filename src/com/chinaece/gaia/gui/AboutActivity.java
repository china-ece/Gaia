package com.chinaece.gaia.gui;

import com.chinaece.gaia.R;
import com.chinaece.gaia.db.DataStorage;

import android.app.TabActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TabHost;
import android.widget.TextView;

public class AboutActivity extends TabActivity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		DataStorage.load(getApplicationContext());
		super.onCreate(savedInstanceState);
		TabHost tabHost = this.getTabHost();
		LayoutInflater.from(this).inflate(R.layout.about, tabHost.getTabContentView(), true); 
		tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("软件说明").setContent(R.id.tab1));
		tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("版本说明").setContent(R.id.tab2));
		//
		final TextView copyright = (TextView)findViewById(R.id.tex2);
		copyright.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				copyright.setText("开发人员:\t\t设计人员:\n王艳妮\t\t\t朱婉菱\n张凌波\n周超");
			}
		});
		//
		TextView textview = (TextView)findViewById(R.id.tex3);
		if(DataStorage.properties.containsKey("changelog"))
			textview.setText(DataStorage.properties.get("changelog").toString());
		tabHost.setBackgroundColor(Color.CYAN);
		tabHost.setCurrentTab(0);
		setContentView(tabHost);
	}

}
