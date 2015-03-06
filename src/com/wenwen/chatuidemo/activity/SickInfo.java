package com.wenwen.chatuidemo.activity;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.wenwen.chatui.debug.DebugLog;
import com.wenwen.chatuidemo.R;
import com.wenwen.chatuidemo.utils.HttpClientRequest;
import com.wenwen.chatuidemo.utils.Urls;

public class SickInfo extends BaseActivity {
    private final String TAG = "SickInfo";
    private TextView tag;
    private EditText username; //名字
    private RadioGroup sex; //性别
    private RadioButton male,female;
    private RadioGroup marital_status;//婚姻状况
    private RadioButton status1,status2; //status1已婚
    private RadioGroup bady;//怀孕史
    private RadioButton bady1,bady2;//bady1 是
    private EditText work;//本人职业
    private EditText ed_zq;//月经周期
    private EditText ed_jz;//男方精子
    private EditText tv_fsh_txt;//基础FSH值
    private EditText tv_lh_txt;//基础LH值
    private EditText tv1_ed1;//不孕原因
    private EditText tv_ivf_txt;//曾经做过IVF
    private EditText female_work;//配儿职业
    private RadioGroup history;//有无遗传史
    private RadioButton history1,history2;//history1 有
    private RadioGroup allergy;//有无过敏史  
    private RadioButton allergy1,allergy2;//allergy1有
    private EditText readme;//自述
    
    private String uid;
    private ProgressDialog pd;
    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        setContentView(R.layout.activity_sickpersondata);
        uid =  getIntent().getStringExtra("uid");
        DebugLog.i(TAG, "uid="+uid);
        init();
        initdata();
    }
    private void initdata() {
        // TODO Auto-generated method stub
        pd = new ProgressDialog(SickInfo.this);
        pd.setMessage("正在获取...");
        RequestParams params = new RequestParams();
        params.put("uid", uid);
        params.put("flag", "2");
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
                         *  * @ret int 0 参数有误 1 成功 -1 用户不存在 -2 密码不正确 -3 未知错误
                         *  ﻿{"ret":"1","account_id":"1","account_username":"13646875594",
                         *  "account_name":"\u674e\u56db","account_image":"","account_sex":"1",
                         *  "account_wedding":"0","account_occupation_own":"\u533b\u751f",
                         *  "account_heredity":"0","account_irritability":"0","account_info":"\u674e\u56db",
                         *  "account_report_url":"",
                         *  "account_birth_date":"0000-00-00 00:00:00"}
                         *  
                         */
                        try {
                            String res = new String(arg2);
                            DebugLog.i("res", res);
                            final JSONObject result = new JSONObject(res);
                            switch (Integer.valueOf(result.getString("ret"))) {
                            case 1:
                                tag.setText(result.getString("account_name"));
                                username.setText(result.getString("account_name"));
                                if (result.getString("account_sex").equals("1")) {
                                    male.setChecked(true);
                                }else {
                                    female.setChecked(true);
                                }
                                if (result.getString("account_wedding").equals("1")) {
                                    status1.setChecked(true);
                                }else {
                                    status2.setChecked(true);
                                }
                                
                                if (result.getString("account_heredity").equals("1")) {
                                    history1.setChecked(true);
                                }else {
                                    history2.setChecked(true);
                                }
                                if (result.getString("account_irritability").equals("1")) {
                                    allergy1.setChecked(true);
                                }else {
                                    allergy2.setChecked(true);
                                }
                                readme.setText(result.getString("account_info"));
                                work.setText(result.getString("account_occupation_own"));
                                
                                
                                
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
        tag = (TextView) findViewById(R.id.tag);
        username = (EditText) findViewById(R.id.username);
        username.setFocusable(false);
        username.setClickable(false);
        sex = (RadioGroup) findViewById(R.id.sex);
        male = (RadioButton) findViewById(R.id.male);
        female = (RadioButton) findViewById(R.id.female);
        marital_status = (RadioGroup) findViewById(R.id.marital_status);
        status1 = (RadioButton) findViewById(R.id.status1);
        status2 = (RadioButton) findViewById(R.id.status2);
        
        bady = (RadioGroup) findViewById(R.id.bady);
        bady1 = (RadioButton) findViewById(R.id.bady1);
        bady2 = (RadioButton) findViewById(R.id.bady2);
        
        work = (EditText) findViewById(R.id.work);
        work.setClickable(false);
        work.setFocusable(false);
        
        ed_zq = (EditText) findViewById(R.id.ed_zq);
        ed_zq.setClickable(false);
        ed_zq.setFocusable(false);
        
        ed_jz = (EditText) findViewById(R.id.ed_jz);
        ed_jz.setClickable(false);
        ed_jz.setFocusable(false);
        
        tv_fsh_txt = (EditText) findViewById(R.id.tv_fsh_txt);
        tv_fsh_txt.setClickable(false);
        tv_fsh_txt.setFocusable(false);
        
        tv_lh_txt = (EditText) findViewById(R.id.tv_lh_txt);
        tv_lh_txt.setClickable(false);
        tv_lh_txt.setFocusable(false);
        
        tv1_ed1 = (EditText) findViewById(R.id.tv1_ed1);
        tv1_ed1.setClickable(false);
        tv1_ed1.setFocusable(false);
        
        tv_ivf_txt = (EditText) findViewById(R.id.tv_ivf_txt);
        tv_ivf_txt.setClickable(false);
        tv_ivf_txt.setFocusable(false);
        
        female_work = (EditText) findViewById(R.id.female_work);
        female_work.setClickable(false);
        female_work.setFocusable(false);
        
        history = (RadioGroup) findViewById(R.id.history);
        history1 = (RadioButton) findViewById(R.id.history1);
        history2 = (RadioButton) findViewById(R.id.history2);
        
        allergy = (RadioGroup) findViewById(R.id.allergy);
        allergy1 = (RadioButton) findViewById(R.id.allergy1);
        allergy2 = (RadioButton) findViewById(R.id.allergy2);
        
        readme = (EditText) findViewById(R.id.readme);
        readme.setClickable(false);
        readme.setFocusable(false);
    }
}
