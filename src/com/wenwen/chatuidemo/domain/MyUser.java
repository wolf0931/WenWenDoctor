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
package com.wenwen.chatuidemo.domain;

import java.io.Serializable;


public class MyUser implements Serializable{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String header;
    private String account_id;
    private String account_name;
    private String account_image;
    private String account_username;
    private String mdname;
    
    
	public String getMdname() {
        return mdname;
    }

    public void setMdname(String mdname) {
        this.mdname = mdname;
    }


    @Override
    public String toString() {
        return "MyUser [header=" + header + ", account_id=" + account_id
                + ", account_name=" + account_name + ", account_image="
                + account_image + ", account_username=" + account_username
                + ", mdname=" + mdname + "]";
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getAccount_username() {
        return account_username;
    }

    public void setAccount_username(String account_username) {
        this.account_username = account_username;
    }

    public String getAccount_id() {
        return account_id;
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
    }

    public String getAccount_name() {
        return account_name;
    }

    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }

    public String getAccount_image() {
        return account_image;
    }

    public void setAccount_image(String account_image) {
        this.account_image = account_image;
    }

}
