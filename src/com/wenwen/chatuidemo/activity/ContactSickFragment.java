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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.http.Header;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.wenwen.chatui.debug.DebugLog;
import com.wenwen.chatuidemo.DemoApplication;
import com.wenwen.chatuidemo.R;
import com.wenwen.chatuidemo.adapter.SickContactAdapter;
import com.wenwen.chatuidemo.domain.MyUser1;
import com.wenwen.chatuidemo.domain.ScikUser;
import com.wenwen.chatuidemo.utils.HttpClientRequest;
import com.wenwen.chatuidemo.utils.Urls;

/**
 * 联系人列表页
 * 
 */
public class ContactSickFragment extends Fragment {
    private final String TAG = "ContactSickFragment";
    private SickContactAdapter adapter;
    private List<MyUser1> contactList;
    private ListView listView;
    private InputMethodManager inputMethodManager;
    private Gson gson = new Gson();
    private ProgressDialog pd;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sick_contact_list, container,false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        DebugLog.i(TAG, "onCreate");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        listView = (ListView) getView().findViewById(R.id.list);
        contactList = new ArrayList<MyUser1>();
        // 获取设置contactlist
        getContactList();
        // 设置adapter
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                startActivity(new Intent(getActivity(), SickInfo.class).putExtra("uid", contactList.get(position).getAccount_id()));
            }
        });
        listView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 隐藏软键盘
                if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
                    if (getActivity().getCurrentFocus() != null)
                        inputMethodManager.hideSoftInputFromWindow(
                                getActivity().getCurrentFocus()
                                        .getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return false;
            }
        });

    }

    /**
     * 获取联系人列表，并过滤掉黑名单和排序
     */
    private void getContactList() {
        try {
            RequestParams params = new RequestParams();
            params.put("uid", DemoApplication.getInstance().getUserUid());
            params.put("flag", "1");
            params.put("pageindex", "1");
            params.put("pagesize", "20");
            pd = new ProgressDialog(getActivity());
            pd.setMessage("正在获取...");
            HttpClientRequest.post(Urls.FRIENDSGET, params, 3000,
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
                            String res = new String(arg2);
                            DebugLog.i(TAG, "患者列表=="+res);
                            JsonParser parser = new JsonParser();
                            JsonArray Jarray = parser.parse(res).getAsJsonArray();
                            MyUser1 myuser1 = null;
                            ScikUser user = null;
                            for (JsonElement obj : Jarray) {
                                myuser1 = gson.fromJson(obj, MyUser1.class);
                                if (!myuser1.getAccount_name().equals("") && myuser1.getAccount_name() != null) {
                                    contactList.add(myuser1);
                                }
                            }
                            // 排序
                            Collections.sort(contactList,
                                    new Comparator<MyUser1>() {
                                        @Override
                                        public int compare(MyUser1 lhs,MyUser1 rhs) {
                                            return lhs.getAccount_username().compareTo(rhs.getAccount_username());
                                        }
                                    });
                            adapter = new SickContactAdapter(getActivity(), contactList);
                            listView.setAdapter(adapter);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
