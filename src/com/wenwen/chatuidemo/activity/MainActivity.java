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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.easemob.chat.CmdMessageBody;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.util.EMLog;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;
import com.wenwen.chatui.debug.DebugLog;
import com.wenwen.chatuidemo.DemoApplication;
import com.wenwen.chatuidemo.R;

public class MainActivity extends BaseActivity {

    protected static final String TAG = "MainActivity";
    private Button[] mTabs;
    private ContactlistFragment contactListFragment;
    private ContactSickFragment contactSickFragment;
    // private ChatHistoryFragment chatHistoryFragment;
    private NewFragment newFragment;
    private HomeFragment homeFragment;
    private PersonFragment settingFragment;
    private Fragment[] fragments;
    private int index;
    // 当前fragment的index
    private int currentTabIndex;
    // 账号在别处登录
    public boolean isConflict = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null
                && savedInstanceState.getBoolean("isConflict", false)) {
            // 防止被T后，没点确定按钮然后按了home键，长期在后台又进app导致的crash
            // 三个fragment里加的判断同理
            finish();
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }
        setContentView(R.layout.activity_main);
        initView();
        UmengUpdateAgent.update(this); // 在wifi条件下友盟自动更新
        MobclickAgent.setDebugMode(true);
        MobclickAgent.updateOnlineConfig(this);
        homeFragment = new HomeFragment();
        newFragment = new NewFragment();
        contactSickFragment = new ContactSickFragment();
        contactListFragment = new ContactlistFragment();
        settingFragment = new PersonFragment();
        fragments = new Fragment[] { homeFragment, 
                contactListFragment,contactSickFragment,newFragment, settingFragment };
        // 添加显示第一个fragment
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, homeFragment).show(homeFragment).commit();
        // 注册一个ack回执消息的BroadcastReceiver
        IntentFilter ackMessageIntentFilter = new IntentFilter(EMChatManager.getInstance().getAckMessageBroadcastAction());
        ackMessageIntentFilter.setPriority(3);
        registerReceiver(ackMessageReceiver, ackMessageIntentFilter);
        // 注册一个透传消息的BroadcastReceiver
        IntentFilter cmdMessageIntentFilter = new IntentFilter(EMChatManager.getInstance().getCmdMessageBroadcastAction());
        cmdMessageIntentFilter.setPriority(3);
        registerReceiver(cmdMessageReceiver, cmdMessageIntentFilter);
        // 通知sdk，UI 已经初始化完毕，注册了相应的receiver和listener, 可以接受broadcast了
        EMChat.getInstance().setAppInited();
    }

    /**
     * 初始化组件
     */
    private void initView() {
        // unreadAddressLable = (TextView)
        // findViewById(R.id.unread_address_number);
        mTabs = new Button[5];
        mTabs[0] = (Button) findViewById(R.id.btn_home);
        mTabs[1] = (Button) findViewById(R.id.btn_address_list);
        mTabs[2] = (Button) findViewById(R.id.btn_sick_list);
        mTabs[3] = (Button) findViewById(R.id.btn_conversation);
        mTabs[4] = (Button) findViewById(R.id.btn_setting);
        // 把第一个tab设为选中状态
        mTabs[0].setSelected(true);
    }

    /**
     * button点击事件
     * @param view
     */
    public void onTabClicked(View view) {
        switch (view.getId()) {
        case R.id.btn_home:
            index = 0;
            break;
        case R.id.btn_conversation:
            index = 3;
            break;
        case R.id.btn_sick_list:
            index = 2;
            break;
        case R.id.btn_address_list:
            index = 1;
            break;
        case R.id.btn_setting:
            index = 4;
            break;
        }
        if (currentTabIndex != index) {
            FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
            trx.hide(fragments[currentTabIndex]);
            if (!fragments[index].isAdded()) {
                trx.add(R.id.fragment_container, fragments[index]);
            }
            trx.show(fragments[index]).commit();
        }
        mTabs[currentTabIndex].setSelected(false);
        // 把当前tab设为选中状态
        mTabs[index].setSelected(true);
        currentTabIndex = index;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       
        try {
            unregisterReceiver(ackMessageReceiver);
        } catch (Exception e) {
        }
        if (conflictBuilder != null) {
            conflictBuilder.create().dismiss();
            conflictBuilder = null;
        }

    }

   

    /**
     * 消息回执BroadcastReceiver
     */
    private BroadcastReceiver ackMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            abortBroadcast();
            String msgid = intent.getStringExtra("msgid");
            String from = intent.getStringExtra("from");
            DebugLog.i(TAG, "消息回执");
            DebugLog.i(TAG, from);
            DebugLog.i(TAG, msgid);
            EMConversation conversation = EMChatManager.getInstance().getConversation(from);
            if (conversation != null) {
                // 把message设为已读
                EMMessage msg = conversation.getMessage(msgid);
                if (msg != null) {
                    // 2014-11-5 修复在某些机器上，在聊天页面对方发送已读回执时不立即显示已读的bug
                    if (ChatActivity.activityInstance != null) {
                        if (msg.getChatType() == ChatType.Chat) {
                            if (from.equals(ChatActivity.activityInstance.getToChatUsername()))
                                return;
                        }
                    }
                    msg.isAcked = true;
                }
            }

        }
    };

    /**
     * 透传消息BroadcastReceiver
     */
    private BroadcastReceiver cmdMessageReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            abortBroadcast();
            EMLog.d(TAG, "收到透传消息");
            // 获取cmd message对象
            String msgId = intent.getStringExtra("msgid");
            EMMessage message = intent.getParcelableExtra("message");
            // 获取消息body
            CmdMessageBody cmdMsgBody = (CmdMessageBody) message.getBody();
            String action = cmdMsgBody.action;// 获取自定义action
            // 获取扩展属性 此处省略
            // message.getStringAttribute("");
            EMLog.d(TAG,String.format("透传消息：action:%s,message:%s", action,message.toString()));
            Toast.makeText(MainActivity.this, "收到透传：action：" + action,Toast.LENGTH_SHORT).show();
        }
    };
    /**
     * 刷新未读消息数
     */
    public void updateUnreadLabel() {
        int count = getUnreadMsgCountTotal();
    }

    @Override
    protected void onResume() {
        updateUnreadLabel();
        EMChatManager.getInstance().activityResumed();
        super.onResume();
    }
    public int getUnreadMsgCountTotal() {
        int unreadMsgCountTotal = 0;
        unreadMsgCountTotal = EMChatManager.getInstance().getUnreadMsgsCount();
        DebugLog.i(TAG, "未读"+unreadMsgCountTotal);
        return unreadMsgCountTotal;
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("isConflict", isConflict);
        super.onSaveInstanceState(outState);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private android.app.AlertDialog.Builder conflictBuilder;
    private boolean isConflictDialogShow;
    /**
     * 显示帐号在别处登录dialog
     */
    private void showConflictDialog() {
        isConflictDialogShow = true;
        DemoApplication.getInstance().logout(null);

        if (!MainActivity.this.isFinishing()) {
            // clear up global variables
            try {
                if (conflictBuilder == null)
                    conflictBuilder = new android.app.AlertDialog.Builder(
                            MainActivity.this);
                conflictBuilder.setTitle("下线通知");
                conflictBuilder.setMessage(R.string.connect_conflict);
                conflictBuilder.setPositiveButton(R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                    int which) {
                                dialog.dismiss();
                                conflictBuilder = null;
                                finish();
                                startActivity(new Intent(MainActivity.this,
                                        LoginActivity.class));
                            }
                        });
                conflictBuilder.setCancelable(false);
                conflictBuilder.create().show();
                isConflict = true;
            } catch (Exception e) {
                
            }

        }

    }
}
