package com.wenwen.chatuidemo.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.wenwen.chatuidemo.R;

public class DialogUtil {

    public static Dialog createProgressDialog(Context context, String message) {
        
        Dialog dialog = null;
        dialog = new Dialog(context, R.style.dialog_progress);
        dialog.setContentView(R.layout.progress_dialog);
        dialog.setCanceledOnTouchOutside(false); // 设置点击边缘是否消失
        // dialog.setCancelable(false);
        ImageView img = (ImageView) dialog.findViewById(R.id.progress_img);
        Animation animation = AnimationUtils.loadAnimation(context,
                R.anim.progress_anim);
        img.startAnimation(animation);
        TextView text = (TextView) dialog.findViewById(R.id.progress_msg);
        text.setText(message);
        return dialog;
        
    }

}
