package com.wenwen.chatui.adv;

import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wenwen.chatuidemo.R;

public class AdvIndex extends LinearLayout {

	public AdvIndex(Context context, AttributeSet attrs) {	
		super(context, attrs);
	}

	public void generatePageControl(int currentIndex, int count, List<Adv> data) {
		this.removeAllViews();
		
		for (int i = 0; i < count; i++) {
			ImageView imageView = new ImageView(getContext());
			if (i == currentIndex) {
				imageView.setBackgroundResource(R.drawable.page_indicator_focused);
			} else {
				imageView.setBackgroundResource(R.drawable.page_indicator);
			}

			this.addView(imageView);
		}
	}

}
