package com.wenwen.chatuidemo.activity;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.wenwen.chatui.debug.DebugLog;
import com.wenwen.chatuidemo.DemoApplication;
import com.wenwen.chatuidemo.R;
import com.wenwen.chatuidemo.utils.Constants;
import com.wenwen.chatuidemo.utils.HttpClientRequest;
import com.wenwen.chatuidemo.utils.Urls;

public class PersonalData extends BaseActivity implements OnClickListener {
    private final String TAG = "PersonalData";
    private RelativeLayout layout_laboratory, layout_hospital,layout_thesis;
    private TextView thesis,laboratory;
    private EditText username,hospital,skilled;

    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        DebugLog.i(TAG, "onCreate");
        setContentView(R.layout.activity_persondata);
        layout_laboratory = (RelativeLayout) findViewById(R.id.layout_laboratory);
        layout_laboratory.setOnClickListener(this);

        layout_hospital = (RelativeLayout) findViewById(R.id.layout_hospital);
        layout_hospital.setOnClickListener(this);
        layout_thesis = (RelativeLayout) findViewById(R.id.layout_thesis);
        layout_thesis.setOnClickListener(this);
        hospital = (EditText) findViewById(R.id.hospital);
        laboratory = (TextView) findViewById(R.id.laboratory);
        username = (EditText) findViewById(R.id.username);
        thesis = (TextView) findViewById(R.id.thesis);
        skilled = (EditText) findViewById(R.id.skilled);

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v == layout_laboratory) {
            startActivityForRet(Department.class, null, Constants.ActionCode.ACT_DEPARTMENT_SELECT);
        } else if (v == layout_thesis) {
            startActivityForRet(Hospital.class, null, Constants.ActionCode.ACT_HOSPITAL_SELECT);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK)
            return;
        switch (requestCode) {
        case Constants.ActionCode.ACT_DEPARTMENT_SELECT:
            laboratory.setText(Constants.department[data.getExtras().getInt("department")]);
            break;
        case Constants.ActionCode.ACT_HOSPITAL_SELECT:
            thesis.setText(Constants.thesis[data.getExtras().getInt("hospital")]);
            break;
        default:
            break;
        }
    }
    public void sub(View view){
        final String name = username.getText().toString().trim();
        final String the = thesis.getText().toString().trim();
        final String skill = skilled.getText().toString().trim();
        final String hospit = hospital.getText().toString().trim();
        final String laborat = laboratory.getText().toString().trim();
        
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "名字不能为空！", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(the)) {
            Toast.makeText(this, "职称不能为空！", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(skill)) {
            Toast.makeText(this, "擅长及诊所介绍不能为空！", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(hospit)) {
            Toast.makeText(this, "医院不能为空", Toast.LENGTH_SHORT).show();
            return;
        }else if (TextUtils.isEmpty(laborat)) {
            Toast.makeText(this, "科室不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }else{
            final ProgressDialog pd = new ProgressDialog(PersonalData.this);
            pd.setMessage("完善中...");
            RequestParams params = new RequestParams();
            params.put("userid",  DemoApplication.getInstance().getUserUid());
            params.put("name", name);
            params.put( "hospital",hospit);
            params.put("department", laborat);
            params.put("job", the);
            params.put("info", skill);
            HttpClientRequest.post(Urls.UPDATEDOCTOR, params, 3000,
                    new AsyncHttpResponseHandler() {
                        @Override
                        public void onStart() {
                            // TODO Auto-generated method stub
                            super.onStart();
                            pd.show();
                        }

                        @Override
                        public void onSuccess(int arg0, Header[] arg1,
                                byte[] arg2) {
                            // TODO Auto-generated method stub
                            try {
                                String res = new String(arg2);
                                JSONObject result = new JSONObject(res);
                                System.err.println("===="+Integer.valueOf(result.getString("ret")));
                                switch (Integer.valueOf(result.getString("ret"))) {
                                case -1:
                                    Toast.makeText(PersonalData.this,"用户不存在", Toast.LENGTH_SHORT).show();
                                    break;
                                case 1:
                                    Toast.makeText(PersonalData.this,"成功", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(PersonalData.this, LoginActivity.class));
                                    finish();
                                    break;
                                case 0:
                                case -2:
                                    Toast.makeText(PersonalData.this,"失败", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    break;
                                }
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFinish() {
                            // TODO Auto-generated method stub
                            super.onFinish();
                            pd.dismiss();
                        }

                        @Override
                        public void onFailure(int arg0, Header[] arg1,
                                byte[] arg2, Throwable arg3) {
                            // TODO Auto-generated method stub

                        }
                    });
        }
    }
    public void back(View view) {
        finish();
    }

}
