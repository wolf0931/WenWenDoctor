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
package com.wenwen.chatuidemo;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Intent;
import android.content.IntentFilter;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.Type;
import com.easemob.chat.OnMessageNotifyListener;
import com.wenwen.applib.controller.HXSDKHelper;
import com.wenwen.applib.model.HXSDKModel;
import com.wenwen.chatuidemo.activity.MainActivity;
import com.wenwen.chatuidemo.db.SickUserDao;
import com.wenwen.chatuidemo.db.UserDao;
import com.wenwen.chatuidemo.domain.MyUser;
import com.wenwen.chatuidemo.domain.MyUser1;
import com.wenwen.chatuidemo.domain.ScikUser;
import com.wenwen.chatuidemo.domain.User;
import com.wenwen.chatuidemo.receiver.VoiceCallReceiver;
import com.wenwen.chatuidemo.utils.CommonUtils;

/**
 * Demo UI HX SDK helper class which subclass HXSDKHelper
 * @author easemob
 *
 */
public class DemoHXSDKHelper extends HXSDKHelper{

    /**
     * contact list in cache
     */
    private Map<String, User> contactList;
    
    private Map<String, ScikUser> sickcontactList;
    
    @Override
    protected void initHXOptions(){
        super.initHXOptions();
        // you can also get EMChatOptions to set related SDK options
        // EMChatOptions options = EMChatManager.getInstance().getChatOptions();
    }

    @Override
    protected OnMessageNotifyListener getMessageNotifyListener(){
        // 取消注释，app在后台，有新消息来时，状态栏的消息提示换成自己写的
      return new OnMessageNotifyListener() {

          @Override
          public String onNewMessageNotify(EMMessage message) {
              // 设置状态栏的消息提示，可以根据message的类型做相应提示
              String ticker = CommonUtils.getMessageDigest(message, appContext);
              if(message.getType() == Type.TXT)
                  ticker = ticker.replaceAll("\\[.{2,3}\\]", "[表情]");
              String username = message.getFrom();
              UserDao userDao = new UserDao(appContext);
              SickUserDao sickuserDao = new SickUserDao(appContext);
              Map<String, MyUser> users= userDao.getContactList();
              Map<String, MyUser1> sickusers= sickuserDao.getSickContactList();
              Iterator<Entry<String, MyUser>> iterator = users.entrySet().iterator();
              while (iterator.hasNext()) {
                  Entry<String, MyUser> entry = iterator.next();
                  if ((entry.getValue().getMdname().equals(username))) {
                      username = entry.getValue().getAccount_name();
                  }
              }
              Iterator<Entry<String, MyUser1>> iterator1 = sickusers.entrySet().iterator();
              while (iterator1.hasNext()) {
                  Entry<String, MyUser1> entry = iterator1.next();
                  if ((entry.getValue().getMdname().equals(username))) {
                      username = entry.getValue().getAccount_name();
                  }
              }
              return username + ": " + ticker;
          }

          @Override
          public String onLatestMessageNotify(EMMessage message, int fromUsersNum, int messageNum) {
              return null;
             // return fromUsersNum + "个基友，发来了" + messageNum + "条消息";
          }

          @Override
          public String onSetNotificationTitle(EMMessage message) {
              //修改标题,这里使用默认
              return null;
          }

          @Override
          public int onSetSmallIcon(EMMessage message) {
              //设置小图标
              return 0;
          }
      };
    }
    
//    @Override
//    protected OnNotificationClickListener getNotificationClickListener(){
//        return new OnNotificationClickListener() {
//
//            @Override
//            public Intent onNotificationClick(EMMessage message) {
//                Intent intent = new Intent(appContext, ChatActivity.class);
//                ChatType chatType = message.getChatType();
//                if (chatType == ChatType.Chat) { // 单聊信息
//                    intent.putExtra("username", message.getFrom());
//                    intent.putExtra("chatType", ChatActivity.CHATTYPE_SINGLE);
//                } else { // 群聊信息
//                            // message.getTo()为群聊id
//                    intent.putExtra("groupId", message.getTo());
//                    intent.putExtra("chatType", ChatActivity.CHATTYPE_GROUP);
//                }
//                return intent;
//            }
//        };
//    }
    
    @Override
    protected void onConnectionConflict(){
        Intent intent = new Intent(appContext, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("conflict", true);
        appContext.startActivity(intent);
    }
    
    @Override
    protected void initListener(){
        super.initListener();
        IntentFilter callFilter = new IntentFilter(EMChatManager.getInstance().getIncomingVoiceCallBroadcastAction());
        appContext.registerReceiver(new VoiceCallReceiver(), callFilter);    
    }

    @Override
    protected HXSDKModel createModel() {
        return new DemoHXSDKModel(appContext);
    }
    
    /**
     * get demo HX SDK Model
     */
    public DemoHXSDKModel getModel(){
        return (DemoHXSDKModel) hxModel;
    }
    
    /**
     * 获取内存中好友user list
     *
     * @return
     */
//    public Map<String, User> getContactList() {
//        if (getHXId() != null && contactList == null) {
//            contactList = ((DemoHXSDKModel) getModel()).getContactList();
//        }
//        
//        return contactList;
//    }
//    public Map<String, MyUser1> getSickContactList() {
//        if (getHXId() != null && sickcontactList == null) {
//            sickcontactList = ((DemoHXSDKModel) getModel()).getSickContactList();
//        }
//        
//        return sickcontactList;
//    }
    /**
     * 设置好友user list到内存中
     *
     * @param contactList
     */
    public void setContactList(Map<String, User> contactList) {
        this.contactList = contactList;
    }
    /**
     * 设置好友user list到内存中
     *
     * @param contactList
     */
    public void setSickContactList(Map<String, ScikUser> sickcontactList) {
        this.sickcontactList = sickcontactList;
    }
    @Override
    public void logout(final EMCallBack callback){
        super.logout(new EMCallBack(){

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                setContactList(null);
                getModel().closeDB();
                if(callback != null){
                    callback.onSuccess();
                }
            }

            @Override
            public void onError(int code, String message) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void onProgress(int progress, String status) {
                // TODO Auto-generated method stub
                if(callback != null){
                    callback.onProgress(progress, status);
                }
            }
            
        });
    }
}
