package com.wenwen.chatui.adv;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class AdvClient {

	private Activity context;
	private AdvGallery gallery;
	private AdvIndex advIndex;
	private AdvTitle advTitle;
	
	private List<Adv> data = new ArrayList<Adv>();
	private AsyncAdvImageLoader loader;
	private ClientAdapter adapter = new ClientAdapter();

	public AdvClient(Activity context) {
		this.context = context;
		this.loader = new AsyncAdvImageLoader();
	}

	public void initClientById(int viewId, int indexId, int titleId) {
		gallery = (AdvGallery) context.findViewById(viewId);
		advIndex = (AdvIndex) context.findViewById(indexId);
		advTitle  = (AdvTitle) context.findViewById(titleId);
		gallery.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				int currentIndex = 0;
				if (data.size() != 0) {
					currentIndex = gallery.getSelectedItemPosition() % data.size();
				}
				advIndex.generatePageControl(currentIndex, data.size(), data); // 底部滚动索引
				advTitle.generatePageControl(currentIndex, data.size(), data); // 新闻标题
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		this.gallery.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				Adv adv = position == 0 ? data.get(0) : data.get(position % data.size());
				if (adv != null) {
					System.out.println("===================adv==========="	+ adv.getMessage());
				}
			}
		});
	}

	public void setData(List<Adv> data) {
		this.data = data;
		this.gallery.setAdapter(adapter);

	}

	public void notifData() {
		adapter.notifyDataSetChanged();
		int currentIndex = 0;
		if (data.size() != 0) {
			currentIndex = gallery.getSelectedItemPosition() % data.size();
		}
		advIndex.generatePageControl(currentIndex, data.size(),data);
		advTitle.generatePageControl(currentIndex, data.size(), data); // 新闻标题
	}

	public class ClientAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return Integer.MAX_VALUE;
		}

		@Override
		public Object getItem(int position) {
			return position == 0 ? data.get(0) : data.get(position % data.size());
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View view, ViewGroup parent) {
			if (view == null) {
				ImageView imgView = new ImageView(context);
				imgView.setLayoutParams(new Gallery.LayoutParams(
						Gallery.LayoutParams.FILL_PARENT,
						Gallery.LayoutParams.FILL_PARENT));
				view = imgView;
			}
			final ImageView imgView = (ImageView) view;
			final Adv adv = position == 0 ? data.get(0) : data.get(position % data.size());
			if (adv != null) {
				if (adv.getImageUrl() == null || "".equals(adv.getImageUrl())) {
					if (adv.getDefaultDrawable() != 0) {
						imgView.setBackgroundResource(adv.getDefaultDrawable());
					}
				} else {
					loader.loadDrawable(adv.getImageUrl(),
							new AsyncAdvImageLoader.ImageCallback() {
								@Override
								public void imageLoaded(Drawable imageDrawable,
										String imageUrl) {
									imgView.setBackgroundDrawable(imageDrawable);
								}
							});
				}

			}
			return imgView;
		}

	}

	public void start() {
		gallery.start();
	}

	public void cancel() {
		gallery.cancel();
	}
}
