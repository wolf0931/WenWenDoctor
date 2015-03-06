package com.wenwen.chatuidemo.activity;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.wenwen.chatui.debug.DebugLog;
import com.wenwen.chatuidemo.DemoApplication;
import com.wenwen.chatuidemo.R;
import com.wenwen.chatuidemo.utils.HttpClientRequest;
import com.wenwen.chatuidemo.utils.Urls;

public class PhotoTextSetting extends BaseActivity implements OnClickListener{
    private final String TAG ="PhotoTextSetting";
    private RelativeLayout rl_switch_vip;
    private ImageView iv_switch_open_vip;
    private ImageView iv_switch_close_vip;
    private EditText et_points_every_num;
    private EditText et_points_weekly_num;
    private CheckBox check_box;
    private int isvip =1;
    private Button save;
    private Handler handler;
    private String order_isneed_coin;// 是否要积分
    private String order_coin_once;// 每次收多少
    private String order_coin_week; // 每周收多少
    private String order_replay_24hours;// 是否24小时回复
    private String order_set_week;// 周几
    private String order_set_morning_afternoon;// 上午
    private String order_set_time;// 时间段
    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        DebugLog.i(TAG, "onCreate");
        setContentView(R.layout.activity_phototext_setting);
        init();
        getset();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                super.handleMessage(msg);
                switch (msg.what) {
                case 1:
                    et_points_every_num.setText(order_coin_once);
                    if (order_isneed_coin.equals("1")) {
                        iv_switch_open_vip.setVisibility(View.VISIBLE);
                        iv_switch_close_vip.setVisibility(View.GONE);
                    } else {
                        iv_switch_open_vip.setVisibility(View.GONE);
                        iv_switch_close_vip.setVisibility(View.VISIBLE);
                    }
                    et_points_every_num.setText(order_coin_once);
                    et_points_weekly_num.setText(order_coin_week);
                }
            }
        };
    }
    private void getset() {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("正在设置...");
        RequestParams params = new RequestParams();
        params.put("uid", DemoApplication.getInstance().getUserUid());
        params.put("flag", "1");
        HttpClientRequest.post(Urls.GETORDERDETAILS, params, 3000,
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
                        try {
                            String res = new String(arg2);
                            DebugLog.i(TAG, "res" + res);
                            JSONObject result = new JSONObject(res);
                            switch (Integer.valueOf(result.getString("ret"))) {
                            case 1:
                                order_isneed_coin = result.getString("order_isneed_coin");
                                order_coin_once = result.getString("order_coin_once");
                                order_coin_week = result.getString("order_coin_week");
                                handler.sendEmptyMessage(1);
                                break;
                            default:
                                break;
                            }
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
        rl_switch_vip = (RelativeLayout) findViewById(R.id.rl_switch_vip);
        rl_switch_vip.setOnClickListener(this);
        iv_switch_open_vip  = (ImageView) findViewById(R.id.iv_switch_open_vip);
        iv_switch_close_vip = (ImageView) findViewById(R.id.iv_switch_close_vip);
        et_points_every_num = (EditText) findViewById(R.id.et_points_every_num);
        et_points_weekly_num = (EditText) findViewById(R.id.et_points_weekly_num);
        check_box = (CheckBox) findViewById(R.id.check_box);
        save = (Button) findViewById(R.id.save);
        save.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v == save) {
            final ProgressDialog pd = new ProgressDialog(this);
            pd.setMessage("正在设置...");
            pd.show();
            RequestParams params = new RequestParams();
            //type  int 预约类型【1 图文，2 电话，3 门诊】
            params.put("userid",DemoApplication.getInstance().getUserUid());
            params.put("type", 1);
            params.put("isneed_coin", isvip);
            params.put("coin_once", et_points_every_num.getText().toString().trim());
            params.put("coin_week", et_points_weekly_num.getText().toString().trim());
            params.put("replay_24hours", 1);
            HttpClientRequest.post(Urls.DOCTORSETORDER, params, 3000,
                    new AsyncHttpResponseHandler() {
                        @Override
                        public void onStart() {
                            // TODO Auto-generated method stub
                            super.onStart();
                            pd.show();
                        }

                        @Override
                        public void onSuccess(int arg0, Header[] arg1,
                                byte[] arg2) {
                            // TODO Auto-generated method stub
                            try {
                                String res = new String(arg2);
                                JSONObject result = new JSONObject(res);
                                switch (Integer.valueOf(result.getString("ret"))) {
                                case 1:
                                    Toast.makeText(PhotoTextSetting.this, "设置成功", 0).show();
                                    pd.dismiss();
                                    break;
                                default:
                                    break;
                                }
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFinish() {
                            // TODO Auto-generated method stub
                            super.onFinish();
                        }

                        @Override
                        public void onFailure(int arg0, Header[] arg1,
                                byte[] arg2, Throwable arg3) {
                            // TODO Auto-generated method stub

                        }
                    });
            

        
        }else if(v == rl_switch_vip){
            if (iv_switch_open_vip.getVisibility() == View.VISIBLE) {
                iv_switch_open_vip.setVisibility(View.INVISIBLE);
                isvip = 0;
                iv_switch_close_vip.setVisibility(View.VISIBLE);
            } else {
                iv_switch_open_vip.setVisibility(View.VISIBLE);
                isvip = 1;
                iv_switch_close_vip.setVisibility(View.INVISIBLE);
            }
        }
    }
    
    public void back(View view) {
        finish();
    }
}
