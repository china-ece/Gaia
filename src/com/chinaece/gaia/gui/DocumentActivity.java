package com.chinaece.gaia.gui;

import android.app.Activity;
import android.os.Bundle;

public class DocumentActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		System.err.println(getIntent().getExtras().getString("docid"));
	}

}
