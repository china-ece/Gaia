package com.chinaece.gaia.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.chinaece.gaia.R;
import com.chinaece.gaia.types.BossTrackingType;
import com.chinaece.gaia.types.BossTrackingType.Child;

public class TrackActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bosstrack);
		Bundle bundle = getIntent().getExtras();
		BossTrackingType boss = (BossTrackingType)bundle.getSerializable("bosstrack");
		EditText date = (EditText)findViewById(R.id.date);
		if(boss.getDate()!=null && boss.getDate().length()>12)
			date.setText(boss.getDate().substring(0, 10));
		else
			date.setText(boss.getDate());
		EditText num = (EditText)findViewById(R.id.num);
		num.setText(boss.getNum());
		EditText item = (EditText)findViewById(R.id.item);
		item.setText(boss.getItem());
		EditText describe = (EditText)findViewById(R.id.describe);
		describe.setText(boss.getDescribe());
		EditText depart = (EditText)findViewById(R.id.depart);
		depart.setText(boss.getDepart());
		EditText person = (EditText)findViewById(R.id.person);
		person.setText(boss.getPerson());
		EditText modality = (EditText)findViewById(R.id.modality);
		modality.setText(boss.getModality());
		EditText dateenter = (EditText)findViewById(R.id.dateenter);
		if(boss.getDateenter()!=null && boss.getDateenter().length()>10)
			dateenter.setText(boss.getDateenter().substring(0, 10));
		else
			dateenter.setText(boss.getDateenter());
		EditText datedone = (EditText)findViewById(R.id.datedone);
		if(boss.getDatedone()!=null && boss.getDatedone().length()>12)
			datedone.setText(boss.getDatedone().substring(0, 10));
		else
			datedone.setText(boss.getDatedone());
		
		List<Map<String, String>> childlist = new ArrayList<Map<String, String>>();
		ArrayList<Child> list = boss.getChilds();
		if(list.size()>0){
			for(int i = 0;i<list.size();i++){
				Map<String, String> map = new HashMap<String, String>();
				map.put("follow",String.valueOf(i+1)+".  "+list.get(i).getFollow());
				childlist.add(map);
			}
		}
		else{
			Map<String, String> map = new HashMap<String, String>();
			map.put("follow", "暂无督办跟踪");
			childlist.add(map);
		}
		ListView listview = (ListView)findViewById(R.id.listViewchild);
		SimpleAdapter childAdapter = new SimpleAdapter(getApplicationContext(),childlist,R.layout.bosschild,new String[]{"follow"},new int[]{R.id.txt_follow});
		listview.setAdapter(childAdapter);
	}

}
