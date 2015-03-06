package com.wenwen.chatuidemo.activity;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.wenwen.chatui.debug.DebugLog;
import com.wenwen.chatuidemo.R;
import com.wenwen.chatuidemo.utils.HttpClientRequest;
import com.wenwen.chatuidemo.utils.Urls;

public class ColleaguesInfo extends BaseActivity {
    private final String TAG ="ColleaguesInfo";
    private TextView title, name, hospital, information;
    private ImageView avatar;
    private String uid;
    private ProgressDialog pd;
    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        DebugLog.i(TAG, "ColleaguesInfo");
        setContentView(R.layout.colleagues_info);
        uid =  getIntent().getStringExtra("uid");
        init();
        initdata();
    }
    private void initdata() {
        // TODO Auto-generated method stub

        // TODO Auto-generated method stub
        pd = new ProgressDialog(ColleaguesInfo.this);
        pd.setMessage("正在获取...");
        RequestParams params = new RequestParams();
        params.put("uid", uid);
        params.put("flag", "1");
        HttpClientRequest.post(Urls.GETUSERDETAILS, params, 3000,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onStart() {
                        // TODO Auto-generated method stub
                        super.onStart();
                        pd.show();
                    }
                    @Override
                    public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                        // TODO Auto-generated method stub
                        /*
                         * ﻿{"ret":"1","account_id":"32","account_username":"13646871111",
                         * "account_name":"1\u5f00","account_image":"",
                         * "account_hospital":"\u6d59\u6c5f\u5927\u5b66\u533b\u5b66\u9662\u9644\u5c5e\u7b2c\u4e8c\u533b\u9662",
                         * "account_department":"\u5185\u79d1",
                         * "account_info":"\u4e0d\u5b55\u4e0d\u80b2",
                         * "account_job":"\u4e3b\u6cbb\u533b\u751f",
                         * "account_set_order":"123",
                         * "gg_content":"",
                         * "gg_addtime":""}
                         */
                        try {
                            String res = new String(arg2);
                            DebugLog.i("res", res);
                            final JSONObject result = new JSONObject(res);
                            switch (Integer.valueOf(result.getString("ret"))) {
                            case 1:
                                title.setText(result.getString("account_name"));
                                name.setText(result.getString("account_name")+"  "+result.getString("account_job"));
                                hospital.setText(result.getString("account_hospital"));
                                information.setText(result.getString("account_info"));
                                break;
                            default:
                                break;

                            }
                        } catch (NumberFormatException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFinish() {
                        // TODO Auto-generated method stub
                        super.onFinish();
                        pd.dismiss();
                    }

                    @Override
                    public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                            Throwable arg3) {
                        // TODO Auto-generated method stub

                    }
                });
    
    
    
    }
    private void init() {
        // TODO Auto-generated method stub
        title = (TextView) findViewById(R.id.title);
        name = (TextView) findViewById(R.id.name);
        hospital = (TextView) findViewById(R.id.hospital);
        information = (TextView) findViewById(R.id.information);
        avatar = (ImageView) findViewById(R.id.avatar);
    }
}
