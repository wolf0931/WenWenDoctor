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

import com.wenwen.chatuidemo.domain.MyUser;
import com.wenwen.chatuidemo.domain.User;
import com.wenwen.chatuidemo.utils.MD5;

public class UserDao {
	public static final String TABLE_NAME = "uers";
	public static final String ACCOUNT_ID = "accountid";
	public static final String COLUMN_NAME_ID = "username";
	public static final String COLUMN_ACOUNT_ID = "countname";
	public static final String COLUMN_MD_NAME="mdusername";
	public static final String COLUMN_NAME_IS_STRANGER = "is_stranger";

	private DbOpenHelper dbHelper;

	public UserDao(Context context) {
		dbHelper = DbOpenHelper.getInstance(context);
	}

	/**
	 * 保存好友list
	 * 
	 * @param myUser
	 */
	public void saveContactList(List<MyUser> myUser) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if (db.isOpen()) {
			db.delete(TABLE_NAME, null, null);
			for (MyUser user : myUser) {
				ContentValues values = new ContentValues();
				values.put(COLUMN_NAME_ID, user.getAccount_name());
				values.put(ACCOUNT_ID, user.getAccount_id());
				values.put(COLUMN_ACOUNT_ID, user.getAccount_username());
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
	public Map<String, MyUser> getContactList() {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Map<String, MyUser> users = new HashMap<String, MyUser>();
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery("select * from " + TABLE_NAME /* + " desc" */, null);
			while (cursor.moveToNext()) {
				String username = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_ID));
				String accountid = cursor.getString(cursor.getColumnIndex(ACCOUNT_ID));
				String acountname = cursor.getString(cursor.getColumnIndex(COLUMN_ACOUNT_ID));
				String mdusername = cursor.getString(cursor.getColumnIndex(COLUMN_MD_NAME));
				MyUser user = new MyUser();
				user.setAccount_name(username);
				user.setAccount_id(accountid);
				user.setAccount_username(acountname);
				user.setMdname(mdusername);
				users.put(username, user);
			}
			cursor.close();
		}
		return users;
	}
	
	/**
	 * 删除一个联系人
	 * @param username
	 */
	public void deleteContact(String username){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if(db.isOpen()){
			db.delete(TABLE_NAME, COLUMN_NAME_ID + " = ?", new String[]{username});
		}
	}
	
	/**
	 * 保存一个联系人
	 * @param user
	 */
	public void saveContact(User user){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(COLUMN_NAME_ID, user.getUsername());
		if(db.isOpen()){
			db.replace(TABLE_NAME, null, values);
		}
	}
}
