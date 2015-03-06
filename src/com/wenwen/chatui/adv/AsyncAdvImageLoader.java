package com.wenwen.chatui.adv;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;

public class AsyncAdvImageLoader {

	private HashMap<String, SoftReference<Drawable>> imageCache;
//	private HashMap<String, WeakReference<Drawable>> imageCache;
	public AsyncAdvImageLoader() {
		imageCache = new HashMap<String, SoftReference<Drawable>>();
	}

	public Drawable loadDrawable(final String imageUrl,
			final ImageCallback imageCallback) {
		final Handler handler = new Handler() {
			public void handleMessage(Message message) {
				imageCallback.imageLoaded((Drawable) message.obj, imageUrl);
			}
		};
		if (imageCache.containsKey(imageUrl)) {
			SoftReference<Drawable> softReference = imageCache.get(imageUrl);
			Drawable drawable = softReference.get();
			if (drawable != null) {
				Message message = handler.obtainMessage(0, drawable);
				handler.sendMessage(message);
				return drawable;
			}else {
				new Thread(new Runnable() {
					@Override
					public void run() {
						Drawable drawable = loadImageFromUrl(imageUrl);
						if(drawable != null){
							imageCache.put(imageUrl, new SoftReference<Drawable>(drawable));
							Message message = handler.obtainMessage(0, drawable);
							handler.sendMessage(message);
						}

					}
				}).start();
				return drawable;
			}
		}else {
			new Thread(new Runnable() {
				@Override
				public void run() {
					Drawable drawable = loadImageFromUrl(imageUrl);
					if(drawable != null){
						imageCache.put(imageUrl, new SoftReference<Drawable>(drawable));
						Message message = handler.obtainMessage(0, drawable);
						handler.sendMessage(message);
					}
				}
			}).start();
		}


		return null;
	}

	public static Drawable loadImageFromUrl(String url) {
		URL m;
		InputStream i = null;
		try {
			m = new URL(url);
			i = (InputStream) m.getContent();
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
//		BitmapFactory.Options opts = new BitmapFactory.Options();
//		opts.inSampleSize = 2; //将图片设为原来宽高的1/2，防止内存溢�?
//		
//		Bitmap bitmap = BitmapFactory.decodeStream(i, null, opts);
//		Drawable d = new BitmapDrawable(bitmap);
		Drawable d = Drawable.createFromStream(i, " src ");
		return d;
	}

	public interface ImageCallback {
		public void imageLoaded(Drawable imageDrawable, String imageUrl);
	}

}
