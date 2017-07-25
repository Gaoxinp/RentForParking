package com.example.administrator.navigationbar;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.thread.MyNormalThread;
import com.google.gson.JsonObject;

public class RegisterActivity extends AppCompatActivity {
    private EditText new_account_name_PhoneNumber, passwordET_register, againPasswordET_register;
    private Button new_account_register_btn;
    private Context context = RegisterActivity.this;

    private String strPhoneNumber = "";
    private String strPassword = "";


    //        由子线程给主线程传递信息时使用，目的是注册成功后finish掉注册界面
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mag = (String) msg.obj;
            Toast.makeText(context, mag, Toast.LENGTH_SHORT).show();
            if (mag.equals("SUCCEED")) {
                Toast.makeText(context, "注册成功", Toast.LENGTH_SHORT).show();

                finish();
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        new_account_name_PhoneNumber = (EditText) findViewById(R.id.new_account_name_PhoneNumber);
        passwordET_register = (EditText) findViewById(R.id.passwordET_register);
        againPasswordET_register = (EditText) findViewById(R.id.againPasswordET_register);
        new_account_register_btn = (Button) findViewById(R.id.new_account_register_btn);

        Intent intent = getIntent();
        strPhoneNumber = intent.getStringExtra("phone");
        new_account_name_PhoneNumber.setText(strPhoneNumber);

        new_account_register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass1 = passwordET_register.getText().toString();
                String pass2 = againPasswordET_register.getText().toString();

                if (pass1.length() == 0 || pass2.length() == 0) {
                    Toast.makeText(context, "请输入密码", Toast.LENGTH_SHORT).show();
                } else if (pass1.length() < 6) {
                    Toast.makeText(context, "密码长度不能少于6位", Toast.LENGTH_SHORT).show();
                } else if (!pass1.equals(pass2)) {
                    Toast.makeText(context, "两次输入的密码不一样", Toast.LENGTH_SHORT).show();
                } else if (pass1.equals(pass2)) {
                    strPassword = pass1;
                    JsonObject object = new JsonObject();
                    object.addProperty("Flag", "register");
                    object.addProperty("Phone", strPhoneNumber);
                    object.addProperty("Password", strPassword);
                    System.out.println(object.toString());
                    new MyNormalThread(context, object.toString(), handler).start();
                }
            }
        });


    }


}
