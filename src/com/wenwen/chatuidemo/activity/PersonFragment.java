/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wenwen.chatuidemo.activity;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.EMCallBack;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.wenwen.chatui.debug.DebugLog;
import com.wenwen.chatuidemo.DemoApplication;
import com.wenwen.chatuidemo.R;
import com.wenwen.chatuidemo.utils.HttpClientRequest;
import com.wenwen.chatuidemo.utils.MD5;
import com.wenwen.chatuidemo.utils.Urls;

/**
 * 个人界面
 * 
 * @author Administrator
 * 
 */
public class PersonFragment extends Fragment implements OnClickListener {
    private final String TAG = "PersonFragment";
    /**
     * 个人
     */
    private RelativeLayout latyout_personal;

    /**
     * 个人资料
     */
    private RelativeLayout latyout_personal_data;
    /**
     * 我的积分
     */
    private RelativeLayout layout_integral;
    /**
     * 聊天设置
     */
    private RelativeLayout layout_chatset_order;
    /**
     * 门诊预约
     */
    private RelativeLayout layout_clinic_order;
    /**
     * 电话预约
     */
    private RelativeLayout layout_phone_order;
    /**
     * 图文预约
     */
    private RelativeLayout layout_photo_text_order;
    /*
     * 退出登陆
     */
    private Button btn_logout;
    /*
     * 积分兑换
     */
    private Button btn_integral;
    private TextView uname;
    private ProgressDialog pd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        DebugLog.i(TAG, "onCreateView");
        return inflater.inflate(R.layout.fragment_tab_person, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null
                && savedInstanceState.getBoolean("isConflict", false))
            return;
        latyout_personal_data = (RelativeLayout) getView().findViewById(
                R.id.latyout_personal_data);
        layout_integral = (RelativeLayout) getView().findViewById(
                R.id.layout_integral);
        layout_chatset_order = (RelativeLayout) getView().findViewById(
                R.id.layout_chatset_order);
        layout_clinic_order = (RelativeLayout) getView().findViewById(
                R.id.layout_clinic_order);
        layout_phone_order = (RelativeLayout) getView().findViewById(
                R.id.layout_phone_order);
        layout_photo_text_order = (RelativeLayout) getView().findViewById(
                R.id.layout_photo_text_order);

        btn_logout = (Button) getView().findViewById(R.id.btn_logout);
        btn_integral = (Button) getView().findViewById(R.id.integral);
        uname = (TextView) getView().findViewById(R.id.uname);
        latyout_personal_data.setOnClickListener(this);
        layout_integral.setOnClickListener(this);
        layout_chatset_order.setOnClickListener(this);
        layout_clinic_order.setOnClickListener(this);
        layout_phone_order.setOnClickListener(this);
        layout_photo_text_order.setOnClickListener(this);

        btn_logout.setOnClickListener(this);
        btn_integral.setOnClickListener(this);
        initData();
    }

    private void initData() {
        // TODO Auto-generated method stub
        pd = new ProgressDialog(getActivity());
        pd.setMessage("正在获取...");
        RequestParams params = new RequestParams();
        params.put("uid", DemoApplication.getInstance().getUserUid());
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
                         *  * @ret int 0 参数有误 1 成功 -1 用户不存在 -2 密码不正确 -3 未知错误
                         */
                        try {
                            String res = new String(arg2);
                            DebugLog.i("res", res);
                            final JSONObject result = new JSONObject(res);
                            switch (Integer.valueOf(result.getString("ret"))) {
                            case 1:
                                uname.setText(result.getString("account_name"));
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

    @Override
    public void onClick(View v) {
        if (v == latyout_personal_data) {
            startActivity(new Intent(getActivity(), AlterPersonalData.class));
        } else if (v == layout_integral) {
            startActivity(new Intent(getActivity(), MyPoints.class));
        } else if (v == layout_chatset_order) {
            //startActivity(new Intent(getActivity(), SettingsActivity.class));
        } else if (v == layout_clinic_order) {
            startActivity(new Intent(getActivity(), ClinicOrderSetting.class));
        } else if (v == layout_phone_order) {
            startActivity(new Intent(getActivity(), PhoneOrderSetting.class));
        } else if (v == layout_photo_text_order) {
            startActivity(new Intent(getActivity(), PhotoTextSetting.class));
        } else if (v == btn_logout) {
            logout();
        } else if (v == btn_integral) {
            startActivity(new Intent(getActivity(), PointsOut.class));
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        DebugLog.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        DebugLog.i(TAG, "onResume");
        super.onResume();
    }
    void logout() {
        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("正在退出登陆..");
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        DemoApplication.getInstance().logout(new EMCallBack() {
            @Override
            public void onSuccess() {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        pd.dismiss();
                        // 重新显示登陆页面
                        ((MainActivity) getActivity()).finish();
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        
                    }
                });
            }
            
            @Override
            public void onProgress(int progress, String status) {
                
            }
            
            @Override
            public void onError(int code, String message) {
                
            }
        });
    }
}
