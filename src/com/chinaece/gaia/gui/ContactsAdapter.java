package com.chinaece.gaia.gui;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.chinaece.gaia.R;

public class ContactsAdapter extends BaseAdapter{
	private LayoutInflater mInflater;
	private List<Map<String, String>> list;

	public ContactsAdapter(Context context, List<Map<String, String>> list) {
		this.mInflater = LayoutInflater.from(context);
		this.list = list;
	}

	public final class ViewHolder {
		public ImageButton imgbtnCall;
		public TextView name;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.contactitem, null);
			holder.imgbtnCall = (ImageButton) convertView
					.findViewById(R.id.imageButton1);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.name.setText((String) list.get(position).get("name"));
		String phone_number = list.get(position).get("telephone");
		if (phone_number != null && !phone_number.equals("")){
			holder.imgbtnCall.setVisibility(View.VISIBLE);
			holder.imgbtnCall.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
						Uri uri = Uri.parse("tel:" + list.get(position).get("telephone"));
						Intent intent = new Intent(Intent.ACTION_CALL, uri);
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						v.getContext().startActivity(intent);					
				}
			});
		}else
			holder.imgbtnCall.setVisibility(View.INVISIBLE);
		return convertView;
	}
}
