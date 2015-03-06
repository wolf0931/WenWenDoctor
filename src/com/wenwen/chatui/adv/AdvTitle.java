package com.wenwen.chatui.adv;

import java.util.List;


import android.R.color;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AdvTitle extends LinearLayout {

	public AdvTitle(Context context, AttributeSet attrs) {	
		super(context, attrs);
	}

	public void generatePageControl(int currentIndex, int count, List<Adv> data) {
		this.removeAllViews();
		TextView tv = new TextView(getContext());
		for (int i = 0; i < count; i++) {
			if (i == currentIndex) {
				tv.setBackgroundColor(color.white);
				tv.setTextSize(16);
				tv.setText(data.get(currentIndex).getMessage());
				//System.out.println("textView text=>"+tv.getText());
			}
		}
		this.addView(tv);
	}

}
