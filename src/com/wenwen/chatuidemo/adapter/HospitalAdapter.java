package com.wenwen.chatuidemo.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wenwen.chatuidemo.R;

public class HospitalAdapter extends BaseAdapter {
    private Context mContext;
    private String[] mydata;
    private LayoutInflater layoutInflater;

    public HospitalAdapter(Context context, String[] data) {
        // TODO Auto-generated constructor stub
        this.mContext = context;
        this.mydata = data;
        this.layoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mydata.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return getItem(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.activity_department_item, null);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        List<String> date = new ArrayList<String>();
        for (int i = 0; i < mydata.length; i++) {
            date.add(mydata[i]);
        }
        holder.tv_name.setText(date.get(position));
        return convertView;
    }

    private class ViewHolder {
        private TextView tv_name;

    }
}
