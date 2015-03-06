package com.wenwen.chatuidemo.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMMessage;
import com.easemob.exceptions.EaseMobException;
import com.wenwen.chatui.debug.DebugLog;
import com.wenwen.chatuidemo.DemoApplication;
import com.wenwen.chatuidemo.R;
import com.wenwen.chatuidemo.adapter.ChatAllHistoryAdapter;

public class HomeLine extends BaseActivity {
    private final String TAG ="HomeLine";
    private InputMethodManager inputMethodManager;
    private ListView listView;
    private ChatAllHistoryAdapter adapter;
    public RelativeLayout errorItem;
    public TextView errorText;
    private List<EMGroup> groups;
    private List<EMConversation> conversationList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        DebugLog.i(TAG, "HomeLine");
        setContentView(R.layout.home_line);
        DemoApplication.register(this);
        inputMethodManager = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
        errorItem = (RelativeLayout)findViewById(R.id.rl_error_item);
        errorText = (TextView) errorItem.findViewById(R.id.tv_connect_errormsg);
        conversationList = loadConversationsWithRecentChat();
        
        listView = (ListView)findViewById(R.id.list);
        DebugLog.i(TAG, "conversationList==="+conversationList.size());
        adapter = new ChatAllHistoryAdapter(HomeLine.this, 1, conversationList);
        // 设置adapter
        listView.setAdapter(adapter);
        
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                EMConversation conversation = adapter.getItem(position);
                String username = conversation.getUserName();
                String acountname =null;
                String toacountname =null;
                try {
                    acountname = conversation.getLastMessage().getStringAttribute("accountname");
                    toacountname =conversation.getLastMessage().getStringAttribute("toaccountname");
                } catch (EaseMobException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                DebugLog.i(TAG, "acountname=="+acountname);
                DebugLog.i(TAG, "toacountname=="+toacountname);
                if (username.equals(DemoApplication.getInstance().getUserName()))
                    Toast.makeText(HomeLine.this, "不能和自己聊天", 0).show();
                else {
                    Intent intent = new Intent(HomeLine.this,ChatActivity.class);
                    intent.putExtra("username", username);
                    intent.putExtra("accountname", acountname);
                    intent.putExtra("toaccountname", toacountname);
                    intent.putExtra("type", "1");
                    startActivity(intent);
                }
            }
        });
        listView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 隐藏软键盘
                hideSoftKeyboard();
                return false;
            }

        });

    }
    
    void hideSoftKeyboard() {
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                inputMethodManager.hideSoftInputFromWindow(this
                        .getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    /**
     * 获取所有会话
     * 
     * @param context
     * @return
     */
    private List<EMConversation> loadConversationsWithRecentChat() {
        // 获取所有会话，包括陌生人
        Hashtable<String, EMConversation> conversations = EMChatManager.getInstance().getAllConversations();
        List<EMConversation> list = new ArrayList<EMConversation>();
        // 过滤掉messages seize为0的conversation
        DebugLog.i(TAG, "消息个数"+list.size());
        for (EMConversation conversation : conversations.values()) {
            if (conversation.getAllMessages().size() != 0)
                list.add(conversation);
        }
        // 排序
        sortConversationByLastChatTime(list);
        return list;
    }

    /**
     * 根据最后一条消息的时间排序
     * 
     * @param usernames
     */
    private void sortConversationByLastChatTime(
            List<EMConversation> conversationList) {
        Collections.sort(conversationList, new Comparator<EMConversation>() {
            @Override
            public int compare(final EMConversation con1,
                    final EMConversation con2) {
                EMMessage con2LastMessage = con2.getLastMessage();
                EMMessage con1LastMessage = con1.getLastMessage();
                if (con2LastMessage.getMsgTime() == con1LastMessage
                        .getMsgTime()) {
                    return 0;
                } else if (con2LastMessage.getMsgTime() > con1LastMessage
                        .getMsgTime()) {
                    return 1;
                } else {
                    return -1;
                }
            }

        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }
    
    /**
     * 刷新页面
     */
    public void refresh() {
        conversationList.clear();
        conversationList.addAll(loadConversationsWithRecentChat());
        adapter.notifyDataSetChanged();
    }
}
