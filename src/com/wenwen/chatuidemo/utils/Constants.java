package com.wenwen.chatuidemo.utils;

public class Constants {
    // 默认http传输时的编码为utf-8
    public static final String DEFAULT_URL_ENCODING = "UTF-8";
    
    public static final class ActionCode {
        public static final int ACT_HOSPITAL_SELECT = 1000;    // 医院选择
        public static final int ACT_DEPARTMENT_SELECT = 1001;    //科室选择
        public static final int  ACT_FIND= 1002;//查找
    }
    public static final String[] thesis = { "医师", "护士",
            "主治医生", "副主任医生", "主任医生"};

    public static final String[] department = {"妇科", "不孕不育科","男科","生殖中心" };
}
