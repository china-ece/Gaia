package com.chinaece.gaia.types.documentitem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.AutoCompleteTextView.Validator;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

public class UserField extends ItemType{
	private AutoCompleteTextView autocompletetextview = null;
	private HashMap<String, JSONObject> users = new HashMap<String, JSONObject>();
	public UserField(JSONObject obj) throws JSONException {
		super(obj);
		if(obj.getString("type").equals("UserField"))
		type = "UserField";
		else
			throw new IllegalStateException("bad init UserField");
	}

	@Override
	public View getMappingInstance(Context context) throws JSONException {
		if (autocompletetextview != null)
			return autocompletetextview;
		autocompletetextview = new AutoCompleteTextView(context);
		autocompletetextview.setThreshold(1);
		if (display == 1 || display == 3 || display == 4) {
			JSONObject original = new JSONObject();
			original.put("displayValue", displayValue);
			original.put("dataValue", dataValue);
			users.put(displayValue, original);
			autocompletetextview.setClickable(false);
			if (display == 3)
				autocompletetextview.setVisibility(View.INVISIBLE);
		} else if (display == 2) {
			for (int i = 0; i < list_value.length(); i++)
				users.put(
						list_value.getJSONObject(i).getString("displayValue"),
						list_value.getJSONObject(i));
		}
		ArrayList<JSONObject> data = new ArrayList<JSONObject>(
				users.values());
		KVAdapter<JSONObject> adapter = new KVAdapter<JSONObject>(context,
				android.R.layout.simple_dropdown_item_1line, data);
		autocompletetextview.setText(displayValue);
		autocompletetextview.setAdapter(adapter);
		autocompletetextview.setDropDownWidth(150);
		autocompletetextview.setValidator(new Validator() {

			@Override
			public boolean isValid(CharSequence text) {
				return users.keySet().contains(text.toString());
			}

			@Override
			public CharSequence fixText(CharSequence invalidText) {
				return null;
			}
		});
		return autocompletetextview;
	}

	@Override
	public String getInstanceValue() {
		try {
			return users.get(autocompletetextview.getText().toString()).getString(
					"dataValue");
		} catch (JSONException e) {
			return dataValue;
		}
	}

	class KVAdapter<T> extends BaseAdapter implements Filterable {

		private Context context;
		private HashMap<String, JSONObject> objects = new HashMap<String, JSONObject>();
		private ArrayList<JSONObject> dataSet;

		public KVAdapter(Context context, int textViewResourceId,
				List<JSONObject> objects) {
			try {
				for (JSONObject obj : objects)
					this.objects.put(obj.getString("displayValue"), obj);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			this.context = context;
			dataSet = (ArrayList<JSONObject>) objects;
		}

		@Override
		public int getCount() {
			return dataSet.size();
		}

		@Override
		public String getItem(int position) {
			try {
				return dataSet.get(position).getString("displayValue");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View v = inflater.inflate(
					android.R.layout.simple_dropdown_item_1line, null);
			TextView lable = (TextView) v.findViewById(android.R.id.text1);
			try {
				lable.setText(dataSet.get(position).getString("displayValue"));
				v.setTag(dataSet.get(position).getString("dataValue"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return v;
		}

		@Override
		public Filter getFilter() {
			return new Filter() {

				@Override
				protected void publishResults(CharSequence constraint,
						FilterResults results) {
					if (results.count > 0) {
						dataSet = (ArrayList<JSONObject>) results.values;
						notifyDataSetChanged();
					} else
						notifyDataSetInvalidated();
				}

				@Override
				protected FilterResults performFiltering(CharSequence constraint) {
					FilterResults rst = new FilterResults();
					ArrayList<JSONObject> matchs = new ArrayList<JSONObject>();
					if (constraint != null)
						for (String disV : objects.keySet()) {
							if (disV.indexOf(constraint.toString()) != -1)
								matchs.add(objects.get(disV));
						}
					rst.values = matchs;
					rst.count = matchs.size();
					return rst;
				}
			};
		}
	}

}
