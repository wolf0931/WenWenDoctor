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
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.wenwen.chatui.debug.DebugLog;
import com.wenwen.chatuidemo.DemoApplication;
import com.wenwen.chatuidemo.DemoHXSDKHelper;
import com.wenwen.chatuidemo.R;
import com.wenwen.chatuidemo.utils.CommonUtils;
import com.wenwen.chatuidemo.utils.HttpClientRequest;
import com.wenwen.chatuidemo.utils.MD5;
import com.wenwen.chatuidemo.utils.Urls;

/**
 * 登陆页面
 * 
 */
public class LoginActivity extends BaseActivity {
	public static final int REQUEST_CODE_SETNICK = 1;
	private EditText usernameEditText;
	private EditText passwordEditText;
	private Gson gson = new Gson();
	private boolean autoLogin = false;
	public Handler loginhandler;
	private ProgressDialog pd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 如果用户名密码都有，直接进入主页面
		if (DemoHXSDKHelper.getInstance().isLogined()) {
			autoLogin = true;
			startActivity(new Intent(LoginActivity.this, MainActivity.class));
			return;
		}
		setContentView(R.layout.activity_login);
		usernameEditText = (EditText) findViewById(R.id.username);
		if (DemoApplication.getInstance().getUserName() !=null &&!DemoApplication.getInstance().getUserName().equals("")) {
		    usernameEditText.setText(DemoApplication.getInstance().getUserName());
        }
		passwordEditText = (EditText) findViewById(R.id.password);
		loginhandler = new Handler(){
		    @Override
		    public void handleMessage(Message msg) {
		        // TODO Auto-generated method stub
		        int key = msg.what;
		        switch (key) {
                case 1:
                    EMChatManager.getInstance().login(MD5.md5(usernameEditText.getText().toString().trim()),
                            MD5.md5("ys_" + passwordEditText.getText().toString().trim()).toUpperCase(), new EMCallBack() {
                        @Override
                        public void onSuccess() {
                            // 登陆成功，保存用户名密码
                            DemoApplication.getInstance().setUserName(usernameEditText.getText().toString().trim());
                            DemoApplication.getInstance().setPassword(MD5.md5("ys_" + passwordEditText.getText().toString().trim()).toUpperCase());
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            pd.dismiss();
                            finish();
                        }
                        @Override
                        public void onProgress(int progress, String status) {

                        }
                        @Override
                        public void onError(final int code, final String message) {
                          //  loginFailure2Umeng(start,code,message);
                        }
                    });
                    break;
                }
		        super.handleMessage(msg);
		    }
		};
		// 如果用户名改变，清空密码
		usernameEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				passwordEditText.setText(null);
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			    
			}
			@Override
			public void afterTextChanged(Editable s) {

			}
		});

	}
	/**
	 * 登陆
	 * 
	 * @param view
	 */
	public void login(View view) {
		if (!CommonUtils.isNetWorkConnected(this)) {
			Toast.makeText(this, R.string.network_isnot_available, Toast.LENGTH_SHORT).show();
			return;
		}
        pd = new ProgressDialog(LoginActivity.this);
        pd.setMessage("正在登陆...");
        RequestParams params = new RequestParams();
        params.put("username", usernameEditText.getText().toString().trim());
        params.put("password", MD5.md5("ys_" + passwordEditText.getText().toString().trim()).toUpperCase());
        params.put("type", "1");
        HttpClientRequest.post(Urls.LOGIG, params, 3000,
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
                            DebugLog.i("res", res);
                            final JSONObject result = new JSONObject(res);
                            switch (Integer.valueOf(result.getString("ret"))) {
                            case -1:
                                Toast.makeText(LoginActivity.this, "用户不存在,请注册", Toast.LENGTH_SHORT).show();
                                break;
                            case 1:
                                DemoApplication.getInstance().setUserUid(result.getString("uid")) ;
                                DemoApplication.getInstance().setAccout_name(result.getString("uname"));
                                // 调用sdk登陆方法登陆聊天服务器
                                loginhandler.sendEmptyMessage(1);
                                break;
                            case -2:
                                Toast.makeText(LoginActivity.this, "密码不正确",Toast.LENGTH_SHORT).show();
                                break;
                            case -3:
                            case 0:
                                Toast.makeText(LoginActivity.this, "登陆失败",Toast.LENGTH_SHORT).show();
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
                    }

                    @Override
                    public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                            Throwable arg3) {
                        // TODO Auto-generated method stub

                    }
                });

	}
	/**
	 * 注册
	 * 
	 * @param view
	 */
	public void register(View view) {
		startActivity(new Intent(this, RegisterActivity.class));
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
	
}
