package com.wenwen.chatuidemo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.wenwen.chatuidemo.R;
import com.wenwen.chatuidemo.adapter.DepartmentAdapter;
import com.wenwen.chatuidemo.utils.Constants;

public class Department extends BaseActivity implements OnClickListener{
    private final String TAG = "Department";
    private ListView listView;
    private DepartmentAdapter departmentAdapter;
    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        setContentView(R.layout.activity_department);
        listView = (ListView) findViewById(R.id.department_list);
        departmentAdapter = new DepartmentAdapter(this,Constants.department);
        listView.setAdapter(departmentAdapter);
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> rootview, View contentview, int position,
                    long arg3) {
                // TODO Auto-generated method stub
                Bundle bundle = new Bundle();
                bundle.putInt("department", position);
                setResult(Activity.RESULT_OK, getIntent().putExtras(bundle));
                finish();
            }
        });
    }
    
    public void back(View view) {
        finish();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
    }

}
