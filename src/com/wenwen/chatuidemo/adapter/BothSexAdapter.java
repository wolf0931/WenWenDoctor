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
import com.wenwen.chatuidemo.domain.Notice;
import com.wenwen.chatuidemo.utils.StringUtil;

public class BothSexAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<Notice> data;

    public BothSexAdapter(Context context, List<Notice> mydata) {
        inflater = LayoutInflater.from(context);
        this.data = mydata;
    }

    @Override
    public int getCount() {
        return data.size() + 1;

    }
    @Override
    public Object getItem(int arg0) {
        return data.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int arg0, View view, ViewGroup group) {
        if (arg0 < data.size()) {
            if (view == null) {
                view = inflater.inflate(R.layout.news_layout_1_item, null);
            }
            ImageView newsImage = (ImageView) view
                    .findViewById(R.id.news_item_img_default);
            TextView dateText = (TextView) view
                    .findViewById(R.id.news_item_date);
            TextView titleText = (TextView) view
                    .findViewById(R.id.news_item_title);
            TextView subviewText = (TextView) view
                    .findViewById(R.id.news_item_sub_title);
            if (dateText == null || titleText == null || subviewText == null) {
                view = inflater.inflate(R.layout.news_layout_1_item, null);
                newsImage = (ImageView) view
                        .findViewById(R.id.news_item_img_default);
                dateText = (TextView) view.findViewById(R.id.news_item_date);
                titleText = (TextView) view.findViewById(R.id.news_item_title);
                subviewText = (TextView) view
                        .findViewById(R.id.news_item_sub_title);
            }
            Notice notice = data.get(arg0);
            if (notice != null) {
                if (arg0 % 2 != 0) {
                    newsImage.setImageResource(R.drawable.test_news_xizhuxi);
                } else {
                    newsImage.setImageResource(R.drawable.test_news_117_96);
                }
                dateText.setText(notice.getDate());
                titleText.setText(StringUtil.getSubString(notice.getTitle(), 12));
                subviewText.setText(StringUtil.getSubString( notice.getSubview(), 30));
            }
        } else {

        }

        return view;
    }

}
