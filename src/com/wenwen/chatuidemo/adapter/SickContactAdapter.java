/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wenwen.chatuidemo.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wenwen.chatuidemo.R;
import com.wenwen.chatuidemo.domain.MyUser1;

/**
 * 简单的好友Adapter实现
 *
 */
public class SickContactAdapter extends BaseAdapter{

    private LayoutInflater layoutInflater;
    private List<MyUser1> mydata;
    public SickContactAdapter(Context context, List<MyUser1> objects) {
        layoutInflater = LayoutInflater.from(context);
        this.mydata = objects;
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mydata.size();
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
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.row_contact, null);
            holder.avatar = (ImageView) convertView.findViewById(R.id.avatar);
            holder.unreadMsgView = (TextView) convertView.findViewById(R.id.unread_msg_number);
            holder.nameTextview = (TextView) convertView.findViewById(R.id.name);
            holder.tvHeader = (TextView) convertView.findViewById(R.id.header);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.nameTextview.setText(mydata.get(position).getAccount_name());
        holder.avatar.setImageResource(R.drawable.default_avatar);
        return convertView;
    }

    private class ViewHolder {
        private ImageView avatar;
        private TextView unreadMsgView;
        private TextView nameTextview;
        private TextView tvHeader;
    }
}
