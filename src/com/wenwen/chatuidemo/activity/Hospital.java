package com.wenwen.chatuidemo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.wenwen.chatuidemo.R;
import com.wenwen.chatuidemo.adapter.HospitalAdapter;
import com.wenwen.chatuidemo.utils.Constants;

public class Hospital extends BaseActivity {
    private final String TAG = "Hospital";
    private ListView listView;
    private HospitalAdapter hospitalAdapter;

    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        setContentView(R.layout.activity_hospital);
        listView = (ListView) findViewById(R.id.hospital_list);
        hospitalAdapter = new HospitalAdapter(this,Constants.thesis);
        listView.setAdapter(hospitalAdapter);
        
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> rootview, View contentview, int position,
                    long arg3) {
                // TODO Auto-generated method stub
                Bundle bundle = new Bundle();
                bundle.putInt("hospital", position);
                setResult(Activity.RESULT_OK, getIntent().putExtras(bundle));
                finish();
            }
        });
    }

    public void back(View view) {
        finish();
    }

}
