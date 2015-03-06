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

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.wenwen.chatui.debug.DebugLog;
import com.wenwen.chatuidemo.DemoApplication;
import com.wenwen.chatuidemo.R;
import com.wenwen.chatuidemo.domain.MyUser;
import com.wenwen.chatuidemo.utils.HttpClientRequest;
import com.wenwen.chatuidemo.utils.Urls;

public class AddContactActivity extends BaseActivity {
    private final String TAG = "AddContactActivity";
    private EditText editText;
    private LinearLayout searchedUserLayout;
    private TextView nameText;
    private Button searchBtn;
    private ImageView avatar;
    private InputMethodManager inputMethodManager;
    private String toAddUsername;
    private MyUser myUser;
    private String toid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_contact);

        editText = (EditText) findViewById(R.id.edit_note);
        searchedUserLayout = (LinearLayout) findViewById(R.id.ll_user);
        nameText = (TextView) findViewById(R.id.name);
        searchBtn = (Button) findViewById(R.id.search);
        avatar = (ImageView) findViewById(R.id.avatar);
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    /**
     * 查找contact
     * 
     * @param v
     */
    public void searchContact(View v) {
        final String name = editText.getText().toString();
        String saveText = searchBtn.getText().toString();

        if (getString(R.string.button_search).equals(saveText)) {
            toAddUsername = name;
            if (TextUtils.isEmpty(name)) {
                startActivity(new Intent(this, AlertDialog.class).putExtra("msg", "请输入用户名"));
                return;
            }
            final ProgressDialog pd = new ProgressDialog( AddContactActivity.this);
            pd.setMessage("正在查找...");
            RequestParams params = new RequestParams();
            params.put("data", editText.getText().toString().trim());
            params.put("flag", "1");
            HttpClientRequest.post(Urls.FINDUSER, params, 3000,
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
                                DebugLog.i(TAG, "返回结果" + res);
                                JSONObject result = new JSONObject(res);
                                switch (Integer.valueOf(result.getString("ret"))) {
                                case -1:
                                    Toast.makeText(AddContactActivity.this,"用户不存在", Toast.LENGTH_SHORT).show();
                                    break;
                                case 1:
                                    searchedUserLayout.setVisibility(View.VISIBLE);
                                    nameText.setText(toAddUsername);
                                    nameText.setText(result.getString("account_name"));
                                    myUser = new MyUser();
                                    myUser.setAccount_id(result.getString("account_id"));
                                    myUser.setAccount_image(result.getString("account_image"));
                                    myUser.setAccount_name(result.getString("account_name"));
                                    myUser.setAccount_username(result.getString("account_username"));
                                    break;
                                case 0:
                                    Toast.makeText(AddContactActivity.this,"查找失败", Toast.LENGTH_SHORT).show();
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
        }
    }

    /**
     * 添加contact
     * 
     * @param view
     */
    public void addContact(View view) {
        if (DemoApplication.getInstance().getUserName()
                .equals(nameText.getText().toString())) {
            startActivity(new Intent(this, AlertDialog.class).putExtra("msg",
                    "不能添加自己"));
            return;
        }
        final ProgressDialog pd = new ProgressDialog(AddContactActivity.this);
        pd.setMessage("正在添加...");
        RequestParams params = new RequestParams();
        DebugLog.i(TAG, "uid" + DemoApplication.getInstance().getUserUid());
        params.put("fromuid", DemoApplication.getInstance().getUserUid());
        params.put("touid", myUser.getAccount_id());
        params.put("flag", "1");
        HttpClientRequest.post(Urls.FRIENDSSET, params, 3000,
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
                            DebugLog.i(TAG, "返回结果" + res);
                            JSONObject result = new JSONObject(res);
                            switch (Integer.valueOf(result.getString("ret"))) {
                            case 0:
                                Toast.makeText(AddContactActivity.this, "添加失败",Toast.LENGTH_SHORT).show();
                                break;
                            case 1:
                                Toast.makeText(AddContactActivity.this, "添加成功",Toast.LENGTH_SHORT).show();
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("myuser", myUser);
                                Intent data=new Intent();
                                data.putExtras(bundle);
                                setResult(Activity.RESULT_OK,data); // 成功返回父界面做跳转控制
                                finish();
                                break;
                            case -2:
                                Toast.makeText(AddContactActivity.this,"已经是好友", Toast.LENGTH_SHORT).show();
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

    public void back(View v) {
        finish();
    }
}
