package com.wenwen.chatui.adv;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Gallery;

public class AdvGallery extends Gallery {

	public AdvGallery(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onFling (MotionEvent paramMotionEvent1, MotionEvent paramMotionEvent2, float velocityX, float velocityY) {
	       
		 int keyCode;
		  if (isScrollingLeft(paramMotionEvent1, paramMotionEvent2))
		  {
		   keyCode = KeyEvent.KEYCODE_DPAD_LEFT;
		  } else
		  {
		   keyCode = KeyEvent.KEYCODE_DPAD_RIGHT;
		  }
		  onKeyDown(keyCode, null);

		return false;
	
	}

	private boolean isScrollingLeft(MotionEvent paramMotionEvent1,
			MotionEvent paramMotionEvent2) {
		  float f2 = paramMotionEvent2.getX();
		  float f1 = paramMotionEvent1.getX();
		  if (f2 > f1){
			  return true;
		  }
		  return false;
	}
	
	private static final int timerAnimation = 1;
	 private final Handler mHandler = new Handler()
	 {
	  public void handleMessage(android.os.Message msg)
	  {
	   switch (msg.what)
	   {
	   case timerAnimation:
	    int position = getSelectedItemPosition();
	       if (position >= (getCount() - 1))
	    {
	     setSelection(0);
//	     onKeyDown(KeyEvent.KEYCODE_DPAD_LEFT, null);
	    } else
	    {
	     onKeyDown(KeyEvent.KEYCODE_DPAD_RIGHT, null);
	    }
	    break;

	   default:
	    break;
	   }
	  };
	 };

	 private final Timer timer = new Timer();
	 private final TimerTask task = new TimerTask()
	 {
	  public void run()
	  {
	   mHandler.sendEmptyMessage(timerAnimation);
	  }
	 };

	 public void start(){
		 timer.schedule(task, 3000, 3000);
	 }

	public void cancel(){
		timer.cancel();
	}
	
	protected int getChildDrawingOrder(int childCount, int i) {
		int selectedIndex = getSelectedItemPosition()
				- getFirstVisiblePosition();
		if (selectedIndex < 0)
			return i;
		if (i == childCount - 1) {
			return selectedIndex;
		} else if (i >= selectedIndex) {
			return childCount - 1 - (i - selectedIndex);
		} else {
			return i;
		}
	}
}
