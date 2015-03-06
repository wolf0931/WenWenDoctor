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

public class PhoneOrderSetting extends BaseActivity implements OnClickListener{
    private final String TAG ="PhoneOrderSetting";
    private TextView time_tv,time_sw,time_xw;
    private TextView time_tv1,time_sw1,time_xw1;
    private TextView time_tv2,time_sw2,time_xw2;
    private TextView time_tv3,time_sw3,time_xw3;
    private TextView time_tv4,time_sw4,time_xw4;
    private TextView time_tv5,time_sw5,time_xw5;
    private TextView time_tv6,time_sw6,time_xw6;
    private TextView time_tv7,time_sw7,time_xw7;
    private int width;
    private Button save;
    private EditText et_points_every_num;
    private ImageView iv_switch_open_vip;
    private ImageView iv_switch_close_vip;
    private RelativeLayout rl_switch_vip;
    private int isvip =1;
    private String week1=null,week2=null,week3=null,week4=null,week5=null,week6=null,week7=null;
    private StringBuffer weekBuilder =null;
    private String time1 =null,time2 =null,time3=null,time4=null,time5=null,time6=null,time7=null;
    private String morning1 =null,morning2 =null,morning3=null,morning4=null,morning5=null,morning6=null,morning7=null;
    private String sworxw_1 = null,sworxw_2 = null,sworxw_3 = null,sworxw_4 = null,sworxw_5 = null,sworxw_6 = null,sworxw_7 = null;
    private String tworxw_1 = null,tworxw_2 = null,tworxw_3 = null,tworxw_4 = null,tworxw_5 = null,tworxw_6 = null,tworxw_7 = null;
    private StringBuffer timeBuilder =null;
    private StringBuffer mornBuilder =null;
    private TextView  tv;
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
        setContentView(R.layout.activity_phoneorder_setting);
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
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
        if(week<cl.get(Calendar.WEEK_OF_YEAR)){  
            year+=1;  
        }  
        System.out.println(year+"年第"+week+"周");
        tv.setText(year+"年第 "+week+"周");
        
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
                            tworxw_1 = timeArr[i]; 
                            DebugLog.i(TAG, "sworxw_1"+sworxw_1);
                            DebugLog.i(TAG, "tworxw_1"+tworxw_1);
                            continue;
                        }
                        if (week[i].equals("2")) {
                            week2 = week[i];
                            sworxw_2 = swOrXw[i];
                            tworxw_2 = timeArr[i];
                            DebugLog.i(TAG, "sworxw_2"+sworxw_2);
                            DebugLog.i(TAG, "tworxw_2"+tworxw_2);
                            continue;
                        }
                        if (week[i].equals("3")) {
                            week3 = week[i];
                            sworxw_3 = swOrXw[i];
                            tworxw_3 = timeArr[i];
                            DebugLog.i(TAG, "sworxw_3"+sworxw_3);
                            DebugLog.i(TAG, "tworxw_3"+tworxw_3);
                            continue;
                        }
                        if (week[i].equals("4")) {
                            week4 = week[i];
                            sworxw_4 = swOrXw[i];
                            tworxw_4 = timeArr[i];
                            DebugLog.i(TAG, "sworxw_4"+sworxw_4);
                            DebugLog.i(TAG, "tworxw_4"+tworxw_4);
                            continue;
                        }
                        if (week[i].equals("5")) {
                            week5 = week[i];
                            sworxw_5 = swOrXw[i];
                            tworxw_5 = timeArr[i];
                            DebugLog.i(TAG, "sworxw_5"+sworxw_5);
                            DebugLog.i(TAG, "tworxw_5"+tworxw_5);
                            continue;
                        }
                        if (week[i].equals("6")) {
                            week6 = week[i];
                            sworxw_6 = swOrXw[i];
                            tworxw_6 = timeArr[i];
                            DebugLog.i(TAG, "sworxw_6"+sworxw_6);
                            DebugLog.i(TAG, "tworxw_6"+tworxw_6);
                            continue;
                        }
                        if (week[i].equals("7")) {
                            week7 = week[i];
                            sworxw_7 = swOrXw[i];
                            tworxw_7 = timeArr[i];
                            DebugLog.i(TAG, "sworxw_7"+sworxw_7);
                            DebugLog.i(TAG, "tworxw_7"+tworxw_7);
                            continue;
                        }
                    }
                   
                    if (week1 != null) {
                        if(sworxw_1 != null){
                            if(sworxw_1.indexOf(",") > -1){
                               String[] t = tworxw_1.split(",");
                                time_sw1.setText(t[0]);
                                time_xw1.setText(t[1]);
                            }else if(sworxw_1.equals("1")){
                                time_sw1.setText(tworxw_1);
                            }else if(sworxw_1.equals("2")){
                                time_xw1.setText(tworxw_1);
                            }
                        }else{
                            time_sw1.setText("");
                            time_xw1.setText("");
                        }
                    }
                    
                    if (week2 != null) {
                        if(sworxw_2 != null){
                            if(sworxw_2.indexOf(",") > -1){
                                String[] t = tworxw_2.split(",");
                                time_sw2.setText(t[0]);
                                time_xw2.setText(t[1]);
                            }else if(sworxw_2.equals("1")){
                                time_sw2.setText(tworxw_2);
                            }else if(sworxw_2.equals("2")){
                                time_xw2.setText(tworxw_2);
                            }
                        }else{
                            time_sw1.setText("");
                            time_xw1.setText("");
                        }
                    }
                    if (week3 != null) {
                        if(sworxw_3 != null){
                            if(sworxw_3.indexOf(",") > -1){
                                String[] t = tworxw_3.split(",");
                                time_sw3.setText(t[0]);
                                time_xw3.setText(t[1]);
                            }else if(sworxw_3.equals("1")){
                                time_sw3.setText(tworxw_3);
                            }else if(sworxw_3.equals("2")){
                                time_xw3.setText(tworxw_3);
                            }
                        }
                    }
                    
                    if (week4 != null) {
                        if(sworxw_4 != null){
                            if(sworxw_4.indexOf(",") > -1){
                                String[] t = tworxw_4.split(",");
                                time_sw4.setText(t[0]);
                                time_xw4.setText(t[1]);
                            }else if(sworxw_4.equals("1")){
                                time_sw4.setText(tworxw_4);
                            }else if(sworxw_4.equals("2")){
                                time_xw4.setText(tworxw_4);
                            }
                        }else{
                            time_sw1.setText("");
                            time_xw1.setText("");
                        }
                    }
                    
                    if (week5 != null) {
                        if(sworxw_5 != null){
                            if(sworxw_5.indexOf(",") > -1){
                                String[] t = tworxw_5.split(",");
                                time_sw5.setText(t[0]);
                                time_xw5.setText(t[1]);
                            }else if(sworxw_5.equals("1")){
                                time_sw5.setText(tworxw_5);
                            }else if(sworxw_5.equals("2")){
                                time_xw5.setText(tworxw_5);
                            }
                        }else{
                            time_sw1.setText("");
                            time_xw1.setText("");
                        }
                    }
                    
                    if (week6 != null) {
                        if(sworxw_6 != null){
                            if(sworxw_6.indexOf(",") > -1){
                                String[] t = tworxw_6.split(",");
                                time_sw6.setText(t[0]);
                                time_xw6.setText(t[1]);
                            }else if(sworxw_6.equals("1")){
                                time_sw6.setText(tworxw_6);
                            }else if(sworxw_6.equals("2")){
                                time_xw6.setText(tworxw_6);
                            }
                        }else{
                            time_sw1.setText("");
                            time_xw1.setText("");
                        }
                    }
                    
                    if (week7 != null) {
                        if(sworxw_7 != null){
                            if(sworxw_7.indexOf(",") > -1){
                                String[] t = tworxw_7.split(",");
                                time_sw7.setText(t[0]);
                                time_xw7.setText(t[1]);
                            }else if(sworxw_7.equals("1")){
                                time_sw7.setText(tworxw_7);
                            }else if(sworxw_7.equals("2")){
                                time_xw7.setText(tworxw_7);
                            }
                        }else{
                            time_sw1.setText("");
                            time_xw1.setText("");
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
        params.put("flag", "2");
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
        time_tv.setWidth(3/10*width);
        time_sw.setWidth(4/10*width);
        time_xw.setWidth(4/10*width);
        
        time_tv1.setWidth(3/10*width);
        time_sw1.setWidth(4/10*width);
        time_xw1.setWidth(4/10*width);
        
        time_tv2.setWidth(3/10*width);
        time_sw2.setWidth(4/10*width);
        time_xw2.setWidth(4/10*width);
        
        time_tv3.setWidth(3/10*width);
        time_sw3.setWidth(4/10*width);
        time_xw3.setWidth(4/10*width);
        
        time_tv4.setWidth(3/10*width);
        time_sw4.setWidth(4/10*width);
        time_xw4.setWidth(4/10*width);
        
        time_tv5.setWidth(3/10*width);
        time_sw5.setWidth(4/10*width);
        time_xw5.setWidth(4/10*width);
        
        time_tv6.setWidth(3/10*width);
        time_sw6.setWidth(4/10*width);
        time_xw6.setWidth(4/10*width);
        
        time_tv7.setWidth(3/10*width);
        time_sw7.setWidth(4/10*width);
        time_xw7.setWidth(4/10*width);
    }
    private void init() {
        // TODO Auto-generated method stub
        tv = (TextView) findViewById(R.id.tv);
        save = (Button) findViewById(R.id.save);
        save.setOnClickListener(this);
        rl_switch_vip = (RelativeLayout) findViewById(R.id.rl_switch_vip);
        rl_switch_vip.setOnClickListener(this);
        et_points_every_num = (EditText) findViewById(R.id.et_points_every_num);
        iv_switch_open_vip  = (ImageView) findViewById(R.id.iv_switch_open_vip);
        iv_switch_close_vip = (ImageView) findViewById(R.id.iv_switch_close_vip);
        
        time_tv = (TextView) findViewById(R.id.time_tv);
        time_sw = (TextView) findViewById(R.id.time_sw);
        time_xw = (TextView) findViewById(R.id.time_xw);
        
        time_tv1 = (TextView) findViewById(R.id.time_tv1);
        time_sw1 = (TextView) findViewById(R.id.time_sw1);
        time_xw1 = (TextView) findViewById(R.id.time_xw1);
        
        time_tv2 = (TextView) findViewById(R.id.time_tv2);
        time_sw2 = (TextView) findViewById(R.id.time_sw2);
        time_xw2 = (TextView) findViewById(R.id.time_xw2);
        
        time_tv3 = (TextView) findViewById(R.id.time_tv3);
        time_sw3 = (TextView) findViewById(R.id.time_sw3);
        time_xw3 = (TextView) findViewById(R.id.time_xw3);
        
        time_tv4 = (TextView) findViewById(R.id.time_tv4);
        time_sw4 = (TextView) findViewById(R.id.time_sw4);
        time_xw4 = (TextView) findViewById(R.id.time_xw4);
        
        time_tv5 = (TextView) findViewById(R.id.time_tv5);
        time_sw5 = (TextView) findViewById(R.id.time_sw5);
        time_xw5 = (TextView) findViewById(R.id.time_xw5);
        
        time_tv6 = (TextView) findViewById(R.id.time_tv6);
        time_sw6 = (TextView) findViewById(R.id.time_sw6);
        time_xw6 = (TextView) findViewById(R.id.time_xw6);
        
        time_tv7 = (TextView) findViewById(R.id.time_tv7);
        time_sw7 = (TextView) findViewById(R.id.time_sw7);
        time_xw7 = (TextView) findViewById(R.id.time_xw7);
    }
    @Override
    public void onClick(View v) {
        if (v == save) {
            // type int 预约类型【1 图文，2 电话，3 门诊】
            weekBuilder = new StringBuffer();
            timeBuilder = new StringBuffer();
            mornBuilder = new StringBuffer();
            setWeek();
            if(timeBuilder.toString().equals("") || timeBuilder.toString().equals("")){
                Toast.makeText(PhoneOrderSetting.this, "请设置时间", 0).show();
                return;
            }
            final ProgressDialog pd = new ProgressDialog(this);
            pd.setMessage("正在设置...");
            RequestParams params = new RequestParams();
            if (weekBuilder.substring(0,1).equals("|")) {
                DebugLog.i(TAG,  weekBuilder.substring(1,weekBuilder.length()));
                params.put("set_week", weekBuilder.substring(1,weekBuilder.length()));
            }else{
                DebugLog.i(TAG,  weekBuilder.toString());
                params.put("set_week", weekBuilder.toString());
            }
            
            if (timeBuilder.substring(0,1).equals("|")) {
                DebugLog.i(TAG,  timeBuilder.substring(1,timeBuilder.length()));
                params.put("set_time", timeBuilder.substring(1,timeBuilder.length()));
            }else{
                DebugLog.i(TAG,  timeBuilder.toString());
                params.put("set_time", timeBuilder.toString());
            }
            
            if (mornBuilder.substring(0,1).equals("|")) {
                DebugLog.i(TAG,  mornBuilder.substring(1,mornBuilder.length()));
                params.put("set_morning_afternoon", mornBuilder.substring(1,mornBuilder.length()));
            }else{
                DebugLog.i(TAG,  mornBuilder.toString());
                params.put("set_morning_afternoon", mornBuilder.toString());
            }
            params.put("userid", DemoApplication.getInstance().getUserUid());
            params.put("type", "2");
            params.put("isneed_coin", isvip);
            params.put("coin_once", et_points_every_num.getText().toString().trim());
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
                                    Toast.makeText(PhoneOrderSetting.this,"设置成功", 0).show();
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
    private void setWeek() {
        // TODO Auto-generated method stub
        if(!time_sw1.getText().toString().trim().equals("") || !time_xw1.getText().toString().trim().equals("")){
            week1 = "1";
            if (time_sw1.getText().toString().trim().equals("")) {
                time1 = time_xw1.getText().toString().trim();
                morning1 ="2";
            }else if(time_xw1.getText().toString().trim().equals("")){
                time1=time_sw1.getText().toString().trim();
                morning1 ="1";
            }else{
                time1 = time_xw1.getText().toString().trim()+","+time_sw1.getText().toString().trim();
                morning1="1"+","+"2";
            }
            DebugLog.i(TAG, "time1=="+time1);
            weekBuilder.append(week1);
            timeBuilder.append(time1);
            mornBuilder.append(morning1);
        }
        if(!time_sw2.getText().toString().trim().equals("") || !time_xw2.getText().toString().trim().equals("")){
            weekBuilder.append("|");
            timeBuilder.append("|");
            mornBuilder.append("|");
            week2 = "2";
            
            if (time_sw2.getText().toString().trim().equals("")) {
                time2 = time_xw2.getText().toString().trim();
                morning2 ="2";
            }else if(time_xw2.getText().toString().trim().equals("")){
                time2=time_sw2.getText().toString().trim();
                morning2="1";
            }else{
                time2 = time_xw2.getText().toString().trim()+","+time_sw2.getText().toString().trim();
                morning2="1"+","+"2";
            }
            DebugLog.i(TAG, "time2=="+time2);
            weekBuilder.append(week2);
            timeBuilder.append(time2);
            mornBuilder.append(morning2);;
        }
        if(!time_sw3.getText().toString().trim().equals("") || !time_xw3.getText().toString().trim().equals("")){
            weekBuilder.append("|");
            timeBuilder.append("|");
            mornBuilder.append("|");
            week3 = "3";
            if (time_sw3.getText().toString().trim().equals("")) {
                time3 = time_xw3.getText().toString().trim();
                morning3 ="2";
            }else if(time_xw3.getText().toString().trim().equals("")){
                time3=time_sw3.getText().toString().trim();
                morning3 = "1";
            }else{
                time3 = time_xw3.getText().toString().trim()+","+time_sw3.getText().toString().trim();
                morning3="1"+","+"2";
            }
            DebugLog.i(TAG, "time3=="+time3);
            weekBuilder.append(week3);
            timeBuilder.append(time3);
            mornBuilder.append(morning3);
            
        } 
        if(!time_sw4.getText().toString().trim().equals("") || !time_xw4.getText().toString().trim().equals("")){
            weekBuilder.append("|");
            timeBuilder.append("|");
            mornBuilder.append("|");
            week4 = "4";
            
            if (time_sw4.getText().toString().trim().equals("")) {
                time4 = time_xw4.getText().toString().trim();
                morning4 = "2";
            }else if(time_xw4.getText().toString().trim().equals("")){
                time4=time_sw4.getText().toString().trim();
                morning4 ="1";
            }else{
                time4 = time_xw4.getText().toString().trim()+","+time_sw4.getText().toString().trim();
                morning4="1"+","+"2";
            }
            DebugLog.i(TAG, "time4=="+time4);
            weekBuilder.append(week4);
            timeBuilder.append(time4);
            mornBuilder.append(morning4);
            
        }
        if(!time_sw5.getText().toString().trim().equals("") || !time_xw5.getText().toString().trim().equals("")){
            weekBuilder.append("|");
            timeBuilder.append("|");
            mornBuilder.append("|");
            week5 = "5";
            if (time_sw5.getText().toString().trim().equals("")) {
                time5 = time_xw5.getText().toString().trim();
                morning5="2";
            }else if(time_xw5.getText().toString().trim().equals("")){
                time5=time_sw5.getText().toString().trim();
                morning5 ="1";
            }else{
                time5 = time_xw5.getText().toString().trim()+","+time_sw5.getText().toString().trim();
                morning5 ="1"+","+"2";
            }
            DebugLog.i(TAG, "time5=="+time5);
            weekBuilder.append(week5);
            timeBuilder.append(time5);
            mornBuilder.append(morning5);
        }
        if(!time_sw6.getText().toString().trim().equals("") || !time_xw6.getText().toString().trim().equals("")){
            weekBuilder.append("|");
            timeBuilder.append("|");
            mornBuilder.append("|");
            week6 = "6";
            
            if (time_sw6.getText().toString().trim().equals("")) {
                time6 = time_xw2.getText().toString().trim();
                morning6="2";
            }else if(time_xw6.getText().toString().trim().equals("")){
                time6=time_sw6.getText().toString().trim();
                morning6="1";
            }else{
                time6 = time_xw6.getText().toString().trim()+","+time_sw6.getText().toString().trim();
                morning6="1"+","+"2";
            }
            DebugLog.i(TAG, "time6=="+time6);
            weekBuilder.append(week6);
            timeBuilder.append(time6);
            mornBuilder.append(morning6);
        }
        if(!time_sw7.getText().toString().trim().equals("") || !time_xw7.getText().toString().trim().equals("")){
            weekBuilder.append("|");
            timeBuilder.append("|");
            mornBuilder.append("|");
            week7 = "7";
            
            if (time_sw7.getText().toString().trim().equals("")) {
                time7 = time_xw7.getText().toString().trim();
                morning7="2";
            }else if(time_xw7.getText().toString().trim().equals("")){
                time7=time_sw7.getText().toString().trim();
                morning7="1";
            }else{
                time7 = time_xw7.getText().toString().trim()+","+time_sw7.getText().toString().trim();
                morning7 ="1"+","+"2";
            }
            DebugLog.i(TAG, "time7=="+time7);
            weekBuilder.append(week7);
            timeBuilder.append(time7);
            mornBuilder.append(morning7);
        }
    }
}
