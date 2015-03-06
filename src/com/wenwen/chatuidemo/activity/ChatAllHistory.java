package com.wenwen.chatuidemo.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContact;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.wenwen.chatuidemo.DemoApplication;
import com.wenwen.chatuidemo.R;
import com.wenwen.chatuidemo.adapter.ChatAllHistoryAdapter;
import com.wenwen.chatuidemo.db.InviteMessgeDao;
import com.wenwen.chatuidemo.db.SickUserDao;
import com.wenwen.chatuidemo.db.UserDao;
import com.wenwen.chatuidemo.domain.MyUser;
import com.wenwen.chatuidemo.domain.MyUser1;
import com.wenwen.chatuidemo.utils.MD5;

/**
 * 显示所有会话记录，比较简单的实现，更好的可能是把陌生人存入本地，这样取到的聊天记录是可控的
 * 
 */
public class ChatAllHistory extends BaseActivity {

	private InputMethodManager inputMethodManager;
	private ListView listView;
	private ChatAllHistoryAdapter adapter;
	private EditText query;
	private ImageButton clearSearch;
	public RelativeLayout errorItem;
	public TextView errorText;
	private boolean hidden;
	private List<EMGroup> groups;
	private List<EMConversation> conversationList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    // TODO Auto-generated method stub
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.fragment_conversation_history);
	    if(savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false))
            return;
	    init();
	}

	private void init() {
        // TODO Auto-generated method stub
        inputMethodManager = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
        errorItem = (RelativeLayout)findViewById(R.id.rl_error_item);
        errorText = (TextView) errorItem.findViewById(R.id.tv_connect_errormsg);
        conversationList = loadConversationsWithRecentChat();
        listView = (ListView) findViewById(R.id.list);
        adapter = new ChatAllHistoryAdapter(this, 1, conversationList);
        // 设置adapter
        listView.setAdapter(adapter);
        groups = EMGroupManager.getInstance().getAllGroups();
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EMConversation conversation = adapter.getItem(position);
                String username = conversation.getUserName();
                String acountname =null;
                if (username.equals(DemoApplication.getInstance().getUserName()))
                    Toast.makeText(ChatAllHistory.this, "不能和自己聊天", 0).show();
                else {
                    // 进入聊天页面
                    if (MD5.md5("ys_" +DemoApplication.getInstance().getUserName()).equals(username)) {
                        username = DemoApplication.getInstance().getAcount_name();
                    }
                    UserDao userDao = new UserDao(ChatAllHistory.this);
                    SickUserDao sickuserDao = new SickUserDao(ChatAllHistory.this);
                    Map<String, MyUser> users= userDao.getContactList();
                    Map<String, MyUser1> sickusers= sickuserDao.getSickContactList();
                    Iterator<Entry<String, MyUser>> iterator = users.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Entry<String, MyUser> entry = iterator.next();
                        if ((entry.getValue().getMdname().equals(username))) {
                            username = entry.getValue().getAccount_name();
                            acountname = entry.getValue().getAccount_username();
                        }
                    }
                    Iterator<Entry<String, MyUser1>> iterator1 = sickusers.entrySet().iterator();
                    while (iterator1.hasNext()) {
                        Entry<String, MyUser1> entry = iterator1.next();
                        if ((entry.getValue().getMdname().equals(username))) {
                            username = entry.getValue().getAccount_name();
                            acountname = entry.getValue().getAccount_username();
                        }
                    }
                    Intent intent = new Intent(ChatAllHistory.this, ChatActivity.class);
                    EMContact emContact = null;
                    groups = EMGroupManager.getInstance().getAllGroups();
                    for (EMGroup group : groups) {
                        if (group.getGroupId().equals(username)) {
                            emContact = group;
                            break;
                        }
                    }
                    if (emContact != null && emContact instanceof EMGroup) {
                        // it is group chat
                        intent.putExtra("chatType", ChatActivity.CHATTYPE_GROUP);
                        intent.putExtra("groupId", ((EMGroup) emContact).getGroupId());
                    } else {
                        // it is single chat
                        intent.putExtra("username", acountname);
                        intent.putExtra("acountname", username);
                    }
                    startActivity(intent);
                }
            }
        });
        // 注册上下文菜单
        registerForContextMenu(listView);
        listView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 隐藏软键盘
                hideSoftKeyboard();
                return false;
            }

        });
        // 搜索框
        query = (EditText)findViewById(R.id.query);
        // 搜索框中清除button
        clearSearch = (ImageButton)findViewById(R.id.search_clear);
        query.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
                if (s.length() > 0) {
                    clearSearch.setVisibility(View.VISIBLE);
                } else {
                    clearSearch.setVisibility(View.INVISIBLE);
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            public void afterTextChanged(Editable s) {
            }
        });
        clearSearch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                query.getText().clear();
                hideSoftKeyboard();
            }
        });

    
    }


	void hideSoftKeyboard() {
		if (this.getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
			if (this.getCurrentFocus() != null)
				inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		// if(((AdapterContextMenuInfo)menuInfo).position > 0){ m,
		this.getMenuInflater().inflate(R.menu.delete_message, menu);
		// }
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.delete_message) {
			EMConversation tobeDeleteCons = adapter.getItem(((AdapterContextMenuInfo) item.getMenuInfo()).position);
			// 删除此会话
			EMChatManager.getInstance().deleteConversation(tobeDeleteCons.getUserName(), tobeDeleteCons.isGroup());
			InviteMessgeDao inviteMessgeDao = new InviteMessgeDao(this);
			inviteMessgeDao.deleteMessage(tobeDeleteCons.getUserName());
			adapter.remove(tobeDeleteCons);
			adapter.notifyDataSetChanged();
			// 更新消息未读数
			//((MainActivity) getApplicationContext()).updateUnreadLabel();
			return true;
		}
		return super.onContextItemSelected(item);
	}

	/**
	 * 刷新页面
	 */
	public void refresh() {
		conversationList.clear();
		conversationList.addAll(loadConversationsWithRecentChat());
		adapter.notifyDataSetChanged();
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
	private void sortConversationByLastChatTime(List<EMConversation> conversationList) {
		Collections.sort(conversationList, new Comparator<EMConversation>() {
			@Override
			public int compare(final EMConversation con1, final EMConversation con2) {

				EMMessage con2LastMessage = con2.getLastMessage();
				EMMessage con1LastMessage = con1.getLastMessage();
				if (con2LastMessage.getMsgTime() == con1LastMessage.getMsgTime()) {
					return 0;
				} else if (con2LastMessage.getMsgTime() > con1LastMessage.getMsgTime()) {
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
		if (!hidden && ! ((MainActivity)getApplicationContext()).isConflict) {
			refresh();
		}
	}

	@Override
    public void onSaveInstanceState(Bundle outState) {
        if(((MainActivity)getApplicationContext()).isConflict)
            outState.putBoolean("isConflict", true);
        super.onSaveInstanceState(outState);
        
    }

}
