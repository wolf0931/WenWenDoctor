package com.wenwen.chatuidemo.utils;

import android.os.AsyncTask;

import com.wenwen.chatuidemo.R;
import com.wenwen.chatuidemo.activity.BaseActivity;

/**
 * @Title: BaseAsyncTask.java
 * @version :
 * @Description:
 */
public class BaseAsyncTask extends AsyncTask<Void, Void, String> {

    protected BaseActivity mActivity;

    public BaseAsyncTask(BaseActivity activity) {
        mActivity = activity;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected String doInBackground(Void... params) {
        return null;
    }

    @Override
    protected void onPostExecute(String result) {

    }
}
