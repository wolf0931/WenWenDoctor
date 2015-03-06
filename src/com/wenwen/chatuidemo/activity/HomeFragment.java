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
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.exceptions.EaseMobException;
import com.wenwen.chatui.adv.Adv;
import com.wenwen.chatui.adv.AdvClient;
import com.wenwen.chatui.debug.DebugLog;
import com.wenwen.chatuidemo.DemoApplication;
import com.wenwen.chatuidemo.R;
import com.wenwen.chatuidemo.utils.StringUtil;

public class HomeFragment extends Fragment implements OnClickListener {
    private final String TAG = "HomeFragment";
    private RelativeLayout latyout_home_line;
    private RelativeLayout latyout_home_phone;
    private RelativeLayout latyout_home_mz;
    private TextView unread_line_number, line_number;
    private TextView unread_phone_number, phone_number;
    private TextView unread_mz_number, mz_number;
    private AdvClient client;

    private NewMessageBroadcastReceiver msgReceiver;

    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        DebugLog.i(TAG, "onCreateView");
        return inflater.inflate(R.layout.fragment_tab_home, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
        client = new AdvClient(getActivity());
        client.initClientById(R.id.scroll_layout, R.id.page_control,R.id.page_title);
        List<Adv> data = new ArrayList<Adv>();
        for (int i = 0; i < 4; i++) {
            Adv adv = new Adv();
            //adv.setMessage("adv Num is:" + (i + 1));
            if (i == 0) {
                adv.setMessage(StringUtil.getSubString("贝克汉姆空降北京,开始了他“中超形象大使”之旅的第一站", 14));
                adv.setDefaultDrawable(R.drawable.qwee1);
            }
            if (i == 1) {
                adv.setMessage(StringUtil.getSubString("青岛农贸市场摊主围堵城管 双方冲突", 14));
                adv.setDefaultDrawable(R.drawable.qwee2);
            }
            if (i == 2) {
                adv.setMessage(StringUtil.getSubString("世界睡眠日，中国小姐的睡姿优美", 14));
                adv.setDefaultDrawable(R.drawable.qwee3);
            }
            if (i == 3) {
                adv.setMessage(StringUtil.getSubString("亚洲杯预选赛,中国1：0胜伊拉克", 14));
                adv.setDefaultDrawable(R.drawable.qwee4);
            }
            data.add(adv);
        }
        client.setData(data);
        client.start();
        setText();

    }

    private void setText() {
        // TODO Auto-generated method stub
        unread_line_number.setText("2");
        line_number.setText("2");
        unread_phone_number.setText("2");
        phone_number.setText("2");
        unread_mz_number.setText("2");
        mz_number.setText("2");
        unread_line_number.setVisibility(View.GONE);
        unread_phone_number.setVisibility(View.GONE);
        unread_mz_number.setVisibility(View.GONE);
        
    }

    private void init() {
        // TODO Auto-generated method stub
        latyout_home_line = (RelativeLayout) getView().findViewById(
                R.id.latyout_home_line);
        latyout_home_line.setOnClickListener(this);
        latyout_home_phone = (RelativeLayout) getView().findViewById(
                R.id.latyout_home_phone);
        latyout_home_phone.setOnClickListener(this);
        latyout_home_mz = (RelativeLayout) getView().findViewById(
                R.id.latyout_home_mz);
        latyout_home_mz.setOnClickListener(this);
        unread_line_number = (TextView) getView().findViewById(
                R.id.unread_line_number);
        line_number = (TextView) getView().findViewById(R.id.line_number);

        unread_phone_number = (TextView) getView().findViewById(
                R.id.unread_phone_number);
        phone_number = (TextView) getView().findViewById(R.id.phone_number);

        unread_mz_number = (TextView) getView().findViewById(
                R.id.unread_mz_number);
        mz_number = (TextView) getView().findViewById(R.id.mz_number);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        DebugLog.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        // 注册一个接收消息的BroadcastReceiver
        msgReceiver = new NewMessageBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter(EMChatManager
                .getInstance().getNewMessageBroadcastAction());
        intentFilter.setPriority(3);
        getActivity().registerReceiver(msgReceiver, intentFilter);
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        DebugLog.i(TAG, "onResume");
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        if (v == latyout_home_line) {
            startActivity(new Intent(getActivity(), HomeLine.class));
        } else if (v == latyout_home_phone) {
            
        } else if (v == latyout_home_mz) {

        }
    }
    /**
     * 新消息广播接收者
     * 
     * 
     */
    private class NewMessageBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 主页面收到消息后，主要为了提示未读，实际消息内容需要到chat页面查看
            String from = intent.getStringExtra("from");
            // 消息id
            String msgId = intent.getStringExtra("msgid");
            DebugLog.i(TAG, "新消息广播接收者");
            DebugLog.i(TAG, from);
            DebugLog.i(TAG, msgId);
            EMMessage message = EMChatManager.getInstance().getMessage(msgId);
            DebugLog.i(TAG, "新消息广播接收者==消息"+message);
            try {
                DebugLog.i(TAG, "新消息广播接收者==消息==昵称"+message.getStringAttribute("accountname"));
            } catch (EaseMobException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            // 2014-10-22 修复在某些机器上，在聊天页面对方发消息过来时不立即显示内容的bug
            if (ChatActivity.activityInstance != null) {
                if (message.getChatType() == ChatType.GroupChat) {
                    if (message.getTo().equals(ChatActivity.activityInstance.getToChatUsername()))
                        return;
                } else {
                    if (from.equals(ChatActivity.activityInstance.getToChatUsername()))
                        return;
                }
            }
            // 注销广播接收者，否则在ChatActivity中会收到这个广播
            abortBroadcast();
            ((BaseActivity) getActivity()).notifyNewMessage(message);
            if (DemoApplication.getActivityByName("HomeLine") !=null) {
               ((HomeLine) DemoApplication.getActivityByName("HomeLine")).refresh();
            }
        }
    }
    
    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        // 注销广播接收者
        try {
            getActivity().unregisterReceiver(msgReceiver);
        } catch (Exception e) {
        }
    }
}
