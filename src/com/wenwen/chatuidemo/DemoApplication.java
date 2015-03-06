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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.easemob.EMCallBack;
import com.wenwen.chatuidemo.domain.ScikUser;
import com.wenwen.chatuidemo.domain.User;

public class DemoApplication extends Application {

    public static Context applicationContext;
    private static DemoApplication instance;
    // login user name
    public final String PREF_USERNAME = "username";
    public String useruid;
    public String Accout_name;
    public String account_id;
    public static List<Activity> activityList = new ArrayList<Activity>();
    /**
     * 当前用户nickname,为了苹果推送不是userid而是昵称
     */
    public static String currentUserNick = "";
    public static DemoHXSDKHelper hxSDKHelper = new DemoHXSDKHelper();

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = this;
        instance = this;

        /**
         * this function will initialize the HuanXin SDK
         * 
         * @return boolean true if caller can continue to call HuanXin related
         *         APIs after calling onInit, otherwise false.
         * 
         *         环信初始化SDK帮助函数
         *         返回true如果正确初始化，否则false，如果返回为false，请在后续的调用中不要调用任何和环信相关的代码
         * 
         *         for example: 例子：
         * 
         *         public class DemoHXSDKHelper extends HXSDKHelper
         * 
         *         HXHelper = new DemoHXSDKHelper();
         *         if(HXHelper.onInit(context)){ // do HuanXin related work }
         */
        hxSDKHelper.onInit(applicationContext);
    }

    public static DemoApplication getInstance() {
        return instance;
    }

    /**
     * 获取内存中好友user list
     * 
     * @return
     */
//    public Map<String, User> getContactList() {
//        return hxSDKHelper.getContactList();
//    }

//    public Map<String, ScikUser> getSickContactList() {
//        return hxSDKHelper.getSickContactList();
//    }

    public String getUserUid() {
        return hxSDKHelper.getUid();

    }

    public void setUserUid(String uid) {
        hxSDKHelper.setUid(uid);
    }
    public String getAcount_name() {
        return hxSDKHelper.getAccountName();

    }
    public void setAccout_name(String accoutname) {
        hxSDKHelper.setAccountName(accoutname);
    }
    /**
     * 设置好友user list到内存中
     * 
     * @param contactList
     */
    public void setContactList(Map<String, User> contactList) {
        hxSDKHelper.setContactList(contactList);
    }

    /**
     * 设置好友user list到内存中
     * 
     * @param contactList
     */
    public void setSickContactList(Map<String, ScikUser> sickcontactList) {
        hxSDKHelper.setSickContactList(sickcontactList);
    }

    /**
     * 获取当前登陆用户名
     * 
     * @return
     */
    public String getUserName() {
        return hxSDKHelper.getHXId();
    }

    /**
     * 获取密码
     * 
     * @return
     */
    public String getPassword() {
        return hxSDKHelper.getPassword();
    }

    /**
     * 设置用户名
     * 
     * @param user
     */
    public void setUserName(String username) {
        hxSDKHelper.setHXId(username);
    }
    
    /**
     * 设置密码 下面的实例代码 只是demo，实际的应用中需要加password 加密后存入 preference 环信sdk
     * 内部的自动登录需要的密码，已经加密存储了
     * 
     * @param pwd
     */
    public void setPassword(String pwd) {
        hxSDKHelper.setPassword(pwd);
    }

    /**
     * 退出登录,清空数据
     */
    public void logout(final EMCallBack emCallBack) {
        // 先调用sdk logout，在清理app中自己的数据
        hxSDKHelper.logout(emCallBack);
    }
    
    public String getaccount_id() {
        return account_id;

    }

    public void setaccount_id(String account_id) {
        this.account_id = account_id;
    }
    
    public synchronized static void register(Activity activity) {
        for (int i = activityList.size() - 1; i >= 0; i--) {
            Activity ac = activityList.get(i);
            
            if(activity.getClass().getName() == ac.getClass().getName()){ //存在
                activityList.remove(ac);
                if (!ac.isFinishing()) {
                    ac.finish();
                }
                break;
            }
        }
        activityList.add(activity);
    }
    
    /** Activity被销毁时，从Activities中移除 */
    public synchronized static void unregister(Activity activity) {
        if (activityList != null && activityList.size() != 0) {
            activityList.remove(activity);
            if (!activity.isFinishing()) {
                activity.finish();
            }
        } else {
            // DebugLog.i("UserStatus","No Activity in pool! unregister");
        }
    }
    public static Activity getActivityByName(String name) {

        for (int i = activityList.size() - 1; i >= 0; i--) {
            Activity ac = activityList.get(i);
            if (ac.isFinishing())
                continue;
            if (ac.getClass().getName().indexOf(name) >= 0) {
                return ac;
            }
        }
        return null;
    }
}
