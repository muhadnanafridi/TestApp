package com.valucart_project.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.valucart_project.R;

public class AdapterForSpinner extends ArrayAdapter<String> {
	private int layout;
	private LayoutInflater inflater;

	public AdapterForSpinner(Context context, int txtViewResourceId, String[] objects) {
		super(context, txtViewResourceId, objects);
		inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layout=txtViewResourceId;
		
		}
	
	@Override
	public View getDropDownView(int position, View convertView, ViewGroup prnt) {
		return getCustomView(position, convertView, prnt);
		}
	@Override
	public View getView(int pos, View convertView, ViewGroup prnt) {
		return getCustomView(pos, convertView, prnt);
	}
	public View getCustomView(int position, View convertView, ViewGroup parent) {
		String item = getItem(position);
		convertView = inflater.inflate(layout, parent, false);
		TextView txtResidentName = (TextView) convertView .findViewById(R.id.txtItemName);
		txtResidentName.setText("       "+item+"       ");
		
		return convertView;
	} 
}
