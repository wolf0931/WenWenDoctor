package com.wenwen.chatuidemo.activity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.wenwen.chatui.debug.DebugLog;
import com.wenwen.chatuidemo.DemoApplication;
import com.wenwen.chatuidemo.R;
import com.wenwen.chatuidemo.utils.HttpClientRequest;
import com.wenwen.chatuidemo.utils.Urls;

public class ClinicOrderSetting extends BaseActivity implements OnClickListener {
    private final String TAG = "ClinicOrderSetting";
    private TextView time_tv, time_sw, time_xw;
    private RelativeLayout time_sw1_layout, time_xw1_layout;
    private TextView time_tv1, time_sw1, time_xw1, time_sw11, time_xw11;
    private RelativeLayout time_sw2_layout, time_xw2_layout;
    private TextView time_tv2, time_sw2, time_xw2, time_sw21, time_xw21;
    private RelativeLayout time_sw3_layout, time_xw3_layout;
    private TextView time_tv3, time_sw3, time_xw3, time_sw31, time_xw31;
    private RelativeLayout time_sw4_layout, time_xw4_layout;
    private TextView time_tv4, time_sw4, time_xw4, time_sw41, time_xw41;
    private RelativeLayout time_sw5_layout, time_xw5_layout;
    private TextView time_tv5, time_sw5, time_xw5, time_sw51, time_xw51;
    private RelativeLayout time_sw6_layout, time_xw6_layout;
    private TextView time_tv6, time_sw6, time_xw6, time_sw61, time_xw61;
    private RelativeLayout time_sw7_layout, time_xw7_layout;
    private TextView time_tv7, time_sw7, time_xw7, time_sw71, time_xw71;
    private int width;
    private Button save;
    private EditText et_points_every_num;
    private ImageView iv_switch_open_vip;
    private ImageView iv_switch_close_vip;
    private RelativeLayout rl_switch_vip;
    private int isvip = 1;
    private TextView tv;
    private String week1 = null, week2 = null, week3 = null, week4 = null,
            week5 = null, week6 = null, week7 = null;
    private StringBuffer weekBuilder = null;
    private String sworxw_1 = null,sworxw_2 = null,sworxw_3 = null,sworxw_4 = null,sworxw_5 = null,sworxw_6 = null,sworxw_7 = null;
    private String time1 = null, time2 = null, time3 = null, time4 = null,
            time5 = null, time6 = null, time7 = null;
    private StringBuffer timeBuilder = null;
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
        setContentView(R.layout.activity_clinicorder_setting);
        WindowManager wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        width = wm.getDefaultDisplay().getWidth();
        init();
        getset();
        setWith();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cl = Calendar.getInstance();
        cl.setTime(new Date());
        int week = cl.get(Calendar.WEEK_OF_YEAR);
        System.out.println(week);
        cl.add(Calendar.DAY_OF_MONTH, -7);
        int year = cl.get(Calendar.YEAR);
        if (week < cl.get(Calendar.WEEK_OF_YEAR)) {
            year += 1;
        }
        System.out.println(year + "年第" + week + "周");
        tv.setText(year + "年第" + week + "周");
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
                    String[] week = order_set_week.split("\\|");
                    
                    String[] swOrXw = order_set_morning_afternoon.split("\\|");
                    
                    String[] timeArr = order_set_time.split("\\|");
                    
                    for (int i = 0; i < week.length; i++) {
                        if (week[i].equals("1")) {
                            week1 = week[i];
                            sworxw_1 = swOrXw[i];
                            //time1 = timeArr[i];  -- 门诊预约不需要时间段。 不注释会报错。
                            continue;
                        }
                        if (week[i].equals("2")) {
                            week2 = week[i];
                            sworxw_2 = swOrXw[i];
                            //time2 = timeArr[i];
                            continue;
                        }
                        if (week[i].equals("3")) {
                            week3 = week[i];
                            sworxw_3 = swOrXw[i];
                            //time3 = timeArr[i];
                            continue;
                        }
                        if (week[i].equals("4")) {
                            week4 = week[i];
                            sworxw_4 = swOrXw[i];
                            //time4 = timeArr[i];
                            continue;
                        }
                        if (week[i].equals("5")) {
                            week5 = week[i];
                            sworxw_5 = swOrXw[i];
                            //time5 = timeArr[i];
                            continue;
                        }
                        if (week[i].equals("6")) {
                            week6 = week[i];
                            sworxw_6 = swOrXw[i];
                            //time6 = timeArr[i];
                            continue;
                        }
                        if (week[i].equals("7")) {
                            week7 = week[i];
                            sworxw_7 = swOrXw[i];
                            //time7 = timeArr[i];
                            continue;
                        }
                    }
                   
                    if (week1 != null) {
                        if(sworxw_1 != null){
                            if(sworxw_1.indexOf(",") > -1){
                                time_sw11.setVisibility(View.VISIBLE);
                                time_xw11.setVisibility(View.VISIBLE);
                            }else if(sworxw_1.equals("1")){
                                time_sw11.setVisibility(View.VISIBLE);
                            }else if(sworxw_1.equals("2")){
                                time_xw11.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                    
                    if (week2 != null) {
                        if(sworxw_2 != null){
                            if(sworxw_2.indexOf(",") > -1){
                                time_sw21.setVisibility(View.VISIBLE);
                                time_xw21.setVisibility(View.VISIBLE);
                            }else if(sworxw_2.equals("1")){
                                time_sw21.setVisibility(View.VISIBLE);
                            }else if(sworxw_2.equals("2")){
                                time_xw21.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                    if (week3 != null) {
                        if(sworxw_3 != null){
                            if(sworxw_3.indexOf(",") > -1){
                                DebugLog.e("rx","111111");
                                time_sw31.setVisibility(View.VISIBLE);
                                time_xw31.setVisibility(View.VISIBLE);
                            }else if(sworxw_3.equals("1")){
                                DebugLog.e("rx","222222");
                                time_sw31.setVisibility(View.VISIBLE);
                            }else if(sworxw_3.equals("2")){
                                DebugLog.e("rx","333333");
                                time_xw31.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                    
                    if (week4 != null) {
                        if(sworxw_4 != null){
                            if(sworxw_4.indexOf(",") > -1){
                                time_sw41.setVisibility(View.VISIBLE);
                                time_xw41.setVisibility(View.VISIBLE);
                            }else if(sworxw_4.equals("1")){
                                time_sw41.setVisibility(View.VISIBLE);
                            }else if(sworxw_4.equals("2")){
                                time_xw41.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                    
                    if (week5 != null) {
                        if(sworxw_5 != null){
                            if(sworxw_5.indexOf(",") > -1){
                                time_sw51.setVisibility(View.VISIBLE);
                                time_xw51.setVisibility(View.VISIBLE);
                            }else if(sworxw_5.equals("1")){
                                time_sw51.setVisibility(View.VISIBLE);
                            }else if(sworxw_5.equals("2")){
                                time_xw51.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                    
                    if (week6 != null) {
                        if(sworxw_6 != null){
                            if(sworxw_6.indexOf(",") > -1){
                                time_sw61.setVisibility(View.VISIBLE);
                                time_xw61.setVisibility(View.VISIBLE);
                            }else if(sworxw_6.equals("1")){
                                time_sw61.setVisibility(View.VISIBLE);
                            }else if(sworxw_6.equals("2")){
                                time_xw61.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                    
                    if (week7 != null) {
                        if(sworxw_7 != null){
                            if(sworxw_7.indexOf(",") > -1){
                                time_sw71.setVisibility(View.VISIBLE);
                                time_xw71.setVisibility(View.VISIBLE);
                            }else if(sworxw_7.equals("1")){
                                time_sw71.setVisibility(View.VISIBLE);
                            }else if(sworxw_7.equals("2")){
                                time_xw71.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                    
                }
            }
        };

    }


    private void getset() {
        // TODO Auto-generated method stub
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("正在获取...");
        RequestParams params = new RequestParams();
        params.put("uid", DemoApplication.getInstance().getUserUid());
        params.put("flag", "3");
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
                            /**
                             * order_id, 预约id order_isneed_coin, 是否需要积分 0 否 1 是
                             * order_coin_once, 每次收取积分 order_coin_week,
                             * 每周服务积分[仅限图文咨询] order_replay_24hours, 是否承诺24小时内答复
                             * 0 否 1 是 order_set_week , 周几[1-7]， 以｜隔开的字符串
                             * order_set_morning_afternoon, 上午/下午 1 上午， 2 下午
                             * 以｜隔开的字符串 order_set_time 时段 如:10:00-14:30 以｜隔开的字符串
                             */
                            String res = new String(arg2);
                            DebugLog.i(TAG, "res" + res);
                            JSONObject result = new JSONObject(res);
                            switch (Integer.valueOf(result.getString("ret"))) {
                            case 1:
                                order_isneed_coin = result
                                        .getString("order_isneed_coin");
                                order_coin_once = result
                                        .getString("order_coin_once");
                                order_coin_week = result
                                        .getString("order_coin_week");
                                order_replay_24hours = result
                                        .getString("order_replay_24hours");
                                order_set_week = result
                                        .getString("order_set_week");
                                order_set_morning_afternoon = result
                                        .getString("order_set_morning_afternoon");
                                order_set_time = result
                                        .getString("order_set_time");
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
                        DebugLog.e("rx","onFailure");
                    }
                });
    }

    private void setWith() {
        // TODO Auto-generated method stub
        time_tv.setWidth(3 / 10 * width);
        time_sw.setWidth(4 / 10 * width);
        time_xw.setWidth(4 / 10 * width);

        time_tv1.setWidth(3 / 10 * width);
        time_sw1.setWidth(4 / 10 * width);
        time_xw1.setWidth(4 / 10 * width);
        time_sw11.setWidth(4 / 10 * width);
        time_xw11.setWidth(4 / 10 * width);

        time_tv2.setWidth(3 / 10 * width);
        time_sw2.setWidth(4 / 10 * width);
        time_xw2.setWidth(4 / 10 * width);
        time_sw21.setWidth(4 / 10 * width);
        time_xw21.setWidth(4 / 10 * width);

        time_tv3.setWidth(3 / 10 * width);
        time_sw3.setWidth(4 / 10 * width);
        time_xw3.setWidth(4 / 10 * width);
        time_sw31.setWidth(4 / 10 * width);
        time_xw31.setWidth(4 / 10 * width);

        time_tv4.setWidth(3 / 10 * width);
        time_sw4.setWidth(4 / 10 * width);
        time_xw4.setWidth(4 / 10 * width);
        time_sw41.setWidth(4 / 10 * width);
        time_xw41.setWidth(4 / 10 * width);

        time_tv5.setWidth(3 / 10 * width);
        time_sw5.setWidth(4 / 10 * width);
        time_xw5.setWidth(4 / 10 * width);

        time_tv6.setWidth(3 / 10 * width);
        time_sw6.setWidth(4 / 10 * width);
        time_xw6.setWidth(4 / 10 * width);
        time_sw61.setWidth(4 / 10 * width);
        time_xw61.setWidth(4 / 10 * width);

        time_tv7.setWidth(3 / 10 * width);
        time_sw7.setWidth(4 / 10 * width);
        time_xw7.setWidth(4 / 10 * width);
        time_sw71.setWidth(4 / 10 * width);
        time_xw71.setWidth(4 / 10 * width);
    }

    private void init() {
        // TODO Auto-generated method stub
        save = (Button) findViewById(R.id.save);
        save.setOnClickListener(this);
        rl_switch_vip = (RelativeLayout) findViewById(R.id.rl_switch_vip);
        rl_switch_vip.setOnClickListener(this);
        et_points_every_num = (EditText) findViewById(R.id.et_points_every_num);
        iv_switch_open_vip = (ImageView) findViewById(R.id.iv_switch_open_vip);
        iv_switch_close_vip = (ImageView) findViewById(R.id.iv_switch_close_vip);
        tv = (TextView) findViewById(R.id.tv);

        time_tv = (TextView) findViewById(R.id.time_tv);
        time_sw = (TextView) findViewById(R.id.time_sw);
        time_xw = (TextView) findViewById(R.id.time_xw);

        time_tv1 = (TextView) findViewById(R.id.time_tv1);
        time_sw1_layout = (RelativeLayout) findViewById(R.id.time_sw1_layout);
        time_sw1_layout.setOnClickListener(this);
        time_sw1 = (TextView) findViewById(R.id.time_sw1);
        time_sw11 = (TextView) findViewById(R.id.time_sw11);
        time_xw1_layout = (RelativeLayout) findViewById(R.id.time_xw1_layout);
        time_xw1_layout.setOnClickListener(this);
        time_xw1 = (TextView) findViewById(R.id.time_xw1);
        time_xw11 = (TextView) findViewById(R.id.time_xw11);

        time_tv2 = (TextView) findViewById(R.id.time_tv2);
        time_sw2_layout = (RelativeLayout) findViewById(R.id.time_sw2_layout);
        time_sw2_layout.setOnClickListener(this);
        time_sw2 = (TextView) findViewById(R.id.time_sw2);
        time_sw21 = (TextView) findViewById(R.id.time_sw21);
        time_xw2_layout = (RelativeLayout) findViewById(R.id.time_xw2_layout);
        time_xw2_layout.setOnClickListener(this);
        time_xw2 = (TextView) findViewById(R.id.time_xw2);
        time_xw21 = (TextView) findViewById(R.id.time_xw21);

        time_tv3 = (TextView) findViewById(R.id.time_tv3);
        time_sw3_layout = (RelativeLayout) findViewById(R.id.time_sw3_layout);
        time_sw3_layout.setOnClickListener(this);
        time_sw3 = (TextView) findViewById(R.id.time_sw3);
        time_sw31 = (TextView) findViewById(R.id.time_sw31);
        time_xw3_layout = (RelativeLayout) findViewById(R.id.time_xw3_layout);
        time_xw3_layout.setOnClickListener(this);
        time_xw3 = (TextView) findViewById(R.id.time_xw3);
        time_xw31 = (TextView) findViewById(R.id.time_xw31);

        time_tv4 = (TextView) findViewById(R.id.time_tv4);
        time_sw4_layout = (RelativeLayout) findViewById(R.id.time_sw4_layout);
        time_sw4_layout.setOnClickListener(this);
        time_sw4 = (TextView) findViewById(R.id.time_sw4);
        time_sw41 = (TextView) findViewById(R.id.time_sw41);
        time_xw4_layout = (RelativeLayout) findViewById(R.id.time_xw4_layout);
        time_xw4_layout.setOnClickListener(this);
        time_xw4 = (TextView) findViewById(R.id.time_xw4);
        time_xw41 = (TextView) findViewById(R.id.time_xw41);

        time_tv5 = (TextView) findViewById(R.id.time_tv5);
        time_sw5_layout = (RelativeLayout) findViewById(R.id.time_sw5_layout);
        time_sw5_layout.setOnClickListener(this);
        time_sw5 = (TextView) findViewById(R.id.time_sw5);
        time_sw51 = (TextView) findViewById(R.id.time_sw51);
        time_xw5_layout = (RelativeLayout) findViewById(R.id.time_xw5_layout);
        time_xw5_layout.setOnClickListener(this);
        time_xw5 = (TextView) findViewById(R.id.time_xw5);
        time_xw51 = (TextView) findViewById(R.id.time_xw51);

        time_tv6 = (TextView) findViewById(R.id.time_tv6);
        time_sw6_layout = (RelativeLayout) findViewById(R.id.time_sw6_layout);
        time_sw6_layout.setOnClickListener(this);
        time_sw6 = (TextView) findViewById(R.id.time_sw6);
        time_sw61 = (TextView) findViewById(R.id.time_sw61);
        time_xw6_layout = (RelativeLayout) findViewById(R.id.time_xw6_layout);
        time_xw6_layout.setOnClickListener(this);
        time_xw6 = (TextView) findViewById(R.id.time_xw6);
        time_xw61 = (TextView) findViewById(R.id.time_xw61);

        time_tv7 = (TextView) findViewById(R.id.time_tv7);
        time_sw7_layout = (RelativeLayout) findViewById(R.id.time_sw7_layout);
        time_sw7_layout.setOnClickListener(this);
        time_sw7 = (TextView) findViewById(R.id.time_sw7);
        time_sw71 = (TextView) findViewById(R.id.time_sw71);
        time_xw7_layout = (RelativeLayout) findViewById(R.id.time_xw7_layout);
        time_xw7_layout.setOnClickListener(this);
        time_xw7 = (TextView) findViewById(R.id.time_xw7);
        time_xw71 = (TextView) findViewById(R.id.time_xw71);
    }

    @Override
    public void onClick(View v) {
        if (v == save) {
            // type int 预约类型【1 图文，2 电话，3 门诊】
            weekBuilder = new StringBuffer();
            timeBuilder = new StringBuffer();
            setweek();
            if (timeBuilder.toString().equals("")
                    || timeBuilder.toString().equals("")) {
                Toast.makeText(ClinicOrderSetting.this, "请设置时间", 0).show();
                return;
            }
            final ProgressDialog pd = new ProgressDialog(this);
            pd.setMessage("正在设置...");
            pd.show();
            RequestParams params = new RequestParams();
            if (weekBuilder.substring(0, 1).equals("|")) {
                DebugLog.i(TAG, weekBuilder.substring(1, weekBuilder.length()));
                params.put("set_week",
                        weekBuilder.substring(1, weekBuilder.length()));
            } else {
                DebugLog.i(TAG, weekBuilder.toString());
                params.put("set_week", weekBuilder.toString());
            }

            if (timeBuilder.substring(0, 1).equals("|")) {
                DebugLog.i(TAG, timeBuilder.substring(1, timeBuilder.length()));
                params.put("set_morning_afternoon",
                        timeBuilder.substring(1, timeBuilder.length()));
            } else {
                DebugLog.i(TAG, timeBuilder.toString());
                params.put("set_morning_afternoon", timeBuilder.toString());
            }

            params.put("userid", DemoApplication.getInstance().getUserUid());
            params.put("type", "3");
            params.put("isneed_coin", isvip + "");
            params.put("coin_once", et_points_every_num.getText().toString()
                    .trim());
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
                                    Toast.makeText(ClinicOrderSetting.this,
                                            "设置成功", 0).show();
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

        } else if (v == rl_switch_vip) {
            if (iv_switch_open_vip.getVisibility() == View.VISIBLE) {
                iv_switch_open_vip.setVisibility(View.INVISIBLE);
                isvip = 0;
                iv_switch_close_vip.setVisibility(View.VISIBLE);
            } else {
                iv_switch_open_vip.setVisibility(View.VISIBLE);
                isvip = 1;
                iv_switch_close_vip.setVisibility(View.INVISIBLE);
            }
        } else if (v == time_sw1_layout) {
            if (time_sw1.getVisibility() == View.VISIBLE) {
                time_sw1.setVisibility(View.GONE);
                time_sw11.setVisibility(View.VISIBLE);
            } else {
                time_sw1.setVisibility(View.VISIBLE);
                time_sw11.setVisibility(View.GONE);
            }
        } else if (v == time_xw1_layout) {
            if (time_xw1.getVisibility() == View.VISIBLE) {
                time_xw1.setVisibility(View.GONE);
                time_xw11.setVisibility(View.VISIBLE);
            } else {
                time_xw1.setVisibility(View.VISIBLE);
                time_xw11.setVisibility(View.GONE);
            }
        } else if (v == time_sw2_layout) {
            if (time_sw2.getVisibility() == View.VISIBLE) {
                time_sw2.setVisibility(View.GONE);
                time_sw21.setVisibility(View.VISIBLE);
            } else {
                time_sw2.setVisibility(View.VISIBLE);
                time_sw21.setVisibility(View.GONE);
            }
        } else if (v == time_xw2_layout) {
            if (time_xw2.getVisibility() == View.VISIBLE) {
                time_xw2.setVisibility(View.GONE);
                time_xw21.setVisibility(View.VISIBLE);
            } else {
                time_xw2.setVisibility(View.VISIBLE);
                time_xw21.setVisibility(View.GONE);
            }
        } else if (v == time_sw3_layout) {
            if (time_sw3.getVisibility() == View.VISIBLE) {
                time_sw3.setVisibility(View.GONE);
                time_sw31.setVisibility(View.VISIBLE);
            } else {
                time_sw3.setVisibility(View.VISIBLE);
                time_sw31.setVisibility(View.GONE);
            }
        } else if (v == time_xw3_layout) {
            if (time_xw3.getVisibility() == View.VISIBLE) {
                time_xw3.setVisibility(View.GONE);
                time_xw31.setVisibility(View.VISIBLE);
            } else {
                time_xw3.setVisibility(View.VISIBLE);
                time_xw31.setVisibility(View.GONE);
            }
        } else if (v == time_sw4_layout) {
            if (time_sw4.getVisibility() == View.VISIBLE) {
                time_sw4.setVisibility(View.GONE);
                time_sw41.setVisibility(View.VISIBLE);
            } else {
                time_sw4.setVisibility(View.VISIBLE);
                time_sw41.setVisibility(View.GONE);
            }
        } else if (v == time_xw4_layout) {
            if (time_xw4.getVisibility() == View.VISIBLE) {
                time_xw4.setVisibility(View.GONE);
                time_xw41.setVisibility(View.VISIBLE);
            } else {
                time_xw4.setVisibility(View.VISIBLE);
                time_xw41.setVisibility(View.GONE);
            }
        } else if (v == time_sw5_layout) {
            if (time_sw5.getVisibility() == View.VISIBLE) {
                time_sw5.setVisibility(View.GONE);
                time_sw51.setVisibility(View.VISIBLE);
            } else {
                time_sw5.setVisibility(View.VISIBLE);
                time_sw51.setVisibility(View.GONE);
            }
        } else if (v == time_xw5_layout) {
            if (time_xw5.getVisibility() == View.VISIBLE) {
                time_xw5.setVisibility(View.GONE);
                time_xw51.setVisibility(View.VISIBLE);
            } else {
                time_xw5.setVisibility(View.VISIBLE);
                time_xw51.setVisibility(View.GONE);
            }
        } else if (v == time_sw6_layout) {
            if (time_sw6.getVisibility() == View.VISIBLE) {
                time_sw6.setVisibility(View.GONE);
                time_sw61.setVisibility(View.VISIBLE);
            } else {
                time_sw6.setVisibility(View.VISIBLE);
                time_sw61.setVisibility(View.GONE);
            }
        } else if (v == time_xw6_layout) {
            if (time_xw6.getVisibility() == View.VISIBLE) {
                time_xw6.setVisibility(View.GONE);
                time_xw61.setVisibility(View.VISIBLE);
            } else {
                time_xw6.setVisibility(View.VISIBLE);
                time_xw61.setVisibility(View.GONE);
            }
        } else if (v == time_sw7_layout) {
            if (time_sw7.getVisibility() == View.VISIBLE) {
                time_sw7.setVisibility(View.GONE);
                time_sw71.setVisibility(View.VISIBLE);
            } else {
                time_sw7.setVisibility(View.VISIBLE);
                time_sw71.setVisibility(View.GONE);
            }
        } else if (v == time_xw7_layout) {
            if (time_xw7.getVisibility() == View.VISIBLE) {
                time_xw7.setVisibility(View.GONE);
                time_xw71.setVisibility(View.VISIBLE);
            } else {
                time_xw7.setVisibility(View.VISIBLE);
                time_xw71.setVisibility(View.GONE);
            }
        }
    }

    private void setweek() {
        // TODO Auto-generated method stub
        if (time_sw11.getVisibility() == View.VISIBLE
                || time_xw11.getVisibility() == View.VISIBLE) {
            week1 = "1";
            weekBuilder.append(week1);
            if (time_sw11.getVisibility() == View.VISIBLE) {
                time1 = "1";
            }
            if (time_xw11.getVisibility() == View.VISIBLE) {
                time1 = "2";
            }
            if (time_sw11.getVisibility() == View.VISIBLE
                    && time_xw11.getVisibility() == View.VISIBLE) {
                time1 = "1" + "," + "2";
            }
            DebugLog.i(TAG, "10==" + time_sw11.getVisibility());
            DebugLog.i(TAG, "10==" + time_xw11.getVisibility());
            timeBuilder.append(time1);
        }
        if (time_sw21.getVisibility() == View.VISIBLE
                || time_xw21.getVisibility() == View.VISIBLE) {
            weekBuilder.append("|");
            week2 = "2";
            weekBuilder.append(week2);
            timeBuilder.append("|");
            if (time_sw21.getVisibility() == View.VISIBLE) {
                time2 = "1";
            }
            if (time_xw21.getVisibility() == View.VISIBLE) {
                time2 = "2";
            }
            if (time_sw21.getVisibility() == View.VISIBLE
                    && time_xw21.getVisibility() == View.VISIBLE) {
                time2 = "1" + "," + "2";
            }
            timeBuilder.append(time2);
        }
        if (time_sw31.getVisibility() == View.VISIBLE
                || time_xw31.getVisibility() == View.VISIBLE) {
            weekBuilder.append("|");
            week3 = "3";
            weekBuilder.append(week3);
            timeBuilder.append("|");
            if (time_sw31.getVisibility() == View.VISIBLE) {
                time3 = "1";
            }
            if (time_xw31.getVisibility() == View.VISIBLE) {
                time3 = "2";
            }
            if (time_sw31.getVisibility() == View.VISIBLE
                    && time_xw31.getVisibility() == View.VISIBLE) {
                time3 = "1" + "," + "2";
            }
            timeBuilder.append(time3);
        }
        if (time_sw41.getVisibility() == View.VISIBLE
                || time_xw41.getVisibility() == View.VISIBLE) {
            weekBuilder.append("|");
            week4 = "4";
            weekBuilder.append(week4);

            timeBuilder.append("|");
            if (time_sw41.getVisibility() == View.VISIBLE) {
                time4 = "1";
            }
            if (time_xw41.getVisibility() == View.VISIBLE) {
                time4 = "2";
            }
            if (time_sw41.getVisibility() == View.VISIBLE
                    && time_xw41.getVisibility() == View.VISIBLE) {
                time4 = "1" + "," + "2";
            }
            timeBuilder.append(time4);

        }
        if (time_sw51.getVisibility() == View.VISIBLE
                || time_xw51.getVisibility() == View.VISIBLE) {
            weekBuilder.append("|");
            week5 = "5";
            weekBuilder.append(week5);

            timeBuilder.append("|");
            if (time_sw51.getVisibility() == View.VISIBLE) {
                time5 = "1";
            }
            if (time_xw51.getVisibility() == View.VISIBLE) {
                time5 = "2";
            }
            if (time_sw51.getVisibility() == View.VISIBLE
                    && time_xw51.getVisibility() == View.VISIBLE) {
                time5 = "1" + "," + "2";
            }
            timeBuilder.append(time5);

        }
        if (time_sw61.getVisibility() == View.VISIBLE
                || time_xw61.getVisibility() == View.VISIBLE) {
            weekBuilder.append("|");
            week6 = "6";
            weekBuilder.append(week6);
            timeBuilder.append("|");
            if (time_sw61.getVisibility() == View.VISIBLE) {
                time6 = "1";
            }
            if (time_xw61.getVisibility() == View.VISIBLE) {
                time6 = "2";
            }
            if (time_sw61.getVisibility() == View.VISIBLE
                    && time_xw61.getVisibility() == View.VISIBLE) {
                time6 = "1" + "," + "2";
            }
            timeBuilder.append(time6);

        }
        if (time_sw71.getVisibility() == View.VISIBLE
                || time_xw71.getVisibility() == View.VISIBLE) {
            weekBuilder.append("|");
            week7 = "7";
            weekBuilder.append(week7);

            timeBuilder.append("|");
            if (time_sw71.getVisibility() == View.VISIBLE) {
                time7 = "1";
            }
            if (time_xw21.getVisibility() == View.VISIBLE) {
                time7 = "2";
            }
            if (time_sw71.getVisibility() == View.VISIBLE
                    && time_xw71.getVisibility() == View.VISIBLE) {
                time7 = "1" + "," + "2";
            }
            timeBuilder.append(time7);
        }
    }
}
