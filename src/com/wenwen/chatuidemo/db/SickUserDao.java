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
package com.wenwen.chatuidemo.db;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.easemob.util.HanziToPinyin;
import com.wenwen.chatuidemo.Constant;
import com.wenwen.chatuidemo.domain.MyUser1;
import com.wenwen.chatuidemo.domain.ScikUser;
import com.wenwen.chatuidemo.utils.MD5;

public class SickUserDao {
	public static final String TABLE_NAME = "sickuers";
	public static final String COLUMN_NAME_ID = "username";
	public static final String ACCOUNT_ID = "accountid";
	public static final String COLUMN_MD_NAME="mdusername";
	public static final String COLUMN_ACCOUNT_ID = "accountname";

	private DbOpenHelper dbHelper;

	public SickUserDao(Context context) {
		dbHelper = DbOpenHelper.getInstance(context);
	}

	/**
	 * 保存好友list
	 * 
	 * @param contactList
	 */
	public void saveSickContactList(List<MyUser1> sickcontactList) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if (db.isOpen()) {
			db.delete(TABLE_NAME, null, null);
			for (MyUser1 user : sickcontactList) {
				ContentValues values = new ContentValues();
				values.put(COLUMN_NAME_ID, user.getAccount_name());
				values.put(COLUMN_ACCOUNT_ID, user.getAccount_username());
				values.put(ACCOUNT_ID, user.getAccount_id());
				values.put(COLUMN_MD_NAME, MD5.md5(user.getAccount_username()));
				db.replace(TABLE_NAME, null, values);
			}
		}
	}
	/**
     * 获取好友list
     * 
     * @return
     */
    public Map<String, MyUser1> getSickContactList() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Map<String, MyUser1> sickusers = new HashMap<String, MyUser1>();
        if (db.isOpen()) {
            Cursor cursor = db.rawQuery("select * from " + TABLE_NAME /* + " desc" */, null);
            while (cursor.moveToNext()) {
                String accountname = cursor.getString(cursor.getColumnIndex(COLUMN_ACCOUNT_ID));
                String username = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_ID));
                String accountid = cursor.getString(cursor.getColumnIndex(ACCOUNT_ID));
                String mdusername = cursor.getString(cursor.getColumnIndex(COLUMN_MD_NAME));
                MyUser1 user = new MyUser1();
                user.setAccount_username(accountname);
                user.setAccount_name(username);
                user.setAccount_id(accountid);
                user.setMdname(mdusername);
                sickusers.put(accountname, user);
            }
            cursor.close();
        }
        return sickusers;
    }
    
	
	/**
	 * 删除一个联系人
	 * @param username
	 */
	public void deleteSickContact(String username){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if(db.isOpen()){
			db.delete(TABLE_NAME, COLUMN_ACCOUNT_ID + " = ?", new String[]{username});
		}
	}
	
	/**
	 * 保存一个联系人
	 * @param user
	 */
	public void saveSickContact(MyUser1 user){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(COLUMN_ACCOUNT_ID, user.getAccount_username());
		if(db.isOpen()){
			db.replace(TABLE_NAME, null, values);
		}
	}
}
