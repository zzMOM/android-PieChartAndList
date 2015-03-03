package com.example.piechartandlist;

import java.util.List;
import java.util.zip.Inflater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter{
	private Context context;
	private List<List<String>> value;

	
	static class ViewHolder{
		public TextView name, displayName, field1, field2, field3;
	}
	
	public MyAdapter(Context context, List<List<String>> value){
		this.context = context;
		this.value = value;
	}

	@Override
	public int getCount() {
		return value.size();
	}

	@Override
	public Object getItem(int position) {
		return value.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.list_item, parent, false);
			ViewHolder holder = new ViewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.displayName = (TextView) convertView.findViewById(R.id.display_name);
			holder.field1 = (TextView) convertView.findViewById(R.id.field1);
			holder.field2 = (TextView) convertView.findViewById(R.id.field2);
			holder.field3 = (TextView) convertView.findViewById(R.id.field3);
			convertView.setTag(holder);
		}
		ViewHolder holder = (ViewHolder) convertView.getTag();
		holder.name.setText(value.get(position).get(0));
		holder.displayName.setText(value.get(position).get(1));
		holder.field1.setText(value.get(position).get(2));
		holder.field2.setText(value.get(position).get(3));
		holder.field3.setText(value.get(position).get(4));
		return convertView;
	}


}
