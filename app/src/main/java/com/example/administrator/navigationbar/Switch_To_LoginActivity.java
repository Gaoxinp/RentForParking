package com.example.administrator.navigationbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.staticValues.StaticValues;
import com.example.administrator.thread.MyNormalThread;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.HashMap;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;

@SuppressWarnings("ALL")
public class Switch_To_LoginActivity extends AppCompatActivity {
    private String strPhoneNumber="";
    private String strPassword="";
    private Context context = Switch_To_LoginActivity.this;


    RelativeLayout relativeLayout;
    EditText id;
    EditText password;
    Button login;
    TextView cannotLogin;
    TextView register;
    View view;
    /*
    *
    * 上一次点击登录按钮的时间 初始为0 当再次点击登录按钮时 记录当前系统时间值
    *
    * */
    private  long lastClick = 0;
    /*
    *
    * 屏幕宽度和高度(单位是px和dp)
    *
    * */
    public int screenWidth_PX;
    public int screenHeight_PX;
    public int screenWidth_DP;
    public int screenHeight_DP;

    /*
    *
    * Handler做定时器 del为延时时间 每过1ms RLayout会在屏幕上上升一小段距离
    * initPosition是RLayout到上边缘的初始距离，需要由屏幕高度来确定
    * endPosition是RLayout到上边缘的终点位置，固定为距离上边缘120dp
    *
    * */
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            String mag = (String) msg.obj;
            if ("No such account".equals(mag)) {
                Toast.makeText(context, "用户名不存在", Toast.LENGTH_SHORT).show();
                id.setText("");
                id.requestFocus();
            } else if ("Wrong password".equals(mag)) {
                Toast.makeText(context, "密码错误", Toast.LENGTH_SHORT).show();
                password.setText("");
                password.requestFocus();
            } else if ("Login successfully".equals(mag)) {
                StaticValues.hasLogin = true;


                Intent intent1 = getIntent();
                from = intent1.getStringExtra("from");
                if ("myCount".equals(from)){
                    intent1.putExtra("phoneNumber",strPhoneNumber);
                    setResult(0,intent1);
                    finish();
                }else if ("navigationBar".equals(from)){
                    Intent intent=new Intent();
                    intent.setClass(Switch_To_LoginActivity.this,NavigationBar.class);
                    intent.putExtra("phoneNumber",strPhoneNumber);
                    intent.putExtra("from","Switch_To_LoginActivity.class");
                    setResult(0,intent);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            }
        }

    };
    private int del = 1;
    private int initPosition_PX;
    private int endPosition_PX;
    private int initPosition_DP;
    private int endPosition_DP;

    //登录页面从哪传来
    private String from = "";

    /*
    *
    * 实现RLayout在屏幕上上升的动画效果
    *
     */
    Runnable runnable = new Runnable() {
        @Override
        public void run() {

            relativeLayout.setY(initPosition_PX);
            int spacing = (initPosition_PX - endPosition_PX) / 40;
            if (initPosition_PX != endPosition_PX) {
                handler.postDelayed(this, del);
                relativeLayout.setY(initPosition_PX - spacing);
                initPosition_PX = initPosition_PX - spacing;
            } else {
                relativeLayout.setY(endPosition_PX);
            }
        }
    };
    Runnable runnable_button = new Runnable() {

        @Override
        public void run() {
            handler.postDelayed(this, 400);
//            System.out.println("点击了一次！！！！！！！！！！！！！！！！");
            view.setBackgroundResource(R.drawable.activity_login_button_shape_normal);
            handler.removeCallbacks(this);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        relativeLayout = (RelativeLayout) findViewById(R.id.activity_login_RLayout);
        id = (EditText) findViewById(R.id.activity_login_et_id);
        password = (EditText) findViewById(R.id.activity_login_et_password);
        login = (Button) findViewById(R.id.activity_login_bt_ok);
        cannotLogin = (TextView) findViewById(R.id.activity_login_tv_cannotLogin);
        register = (TextView) findViewById(R.id.activity_login_tv_register);

        DisplayMetrics dm = getResources().getDisplayMetrics();
        screenWidth_PX = dm.widthPixels;
        screenHeight_PX = dm.heightPixels;
        screenWidth_DP = Dp2pxANDpx2dp.px2dip(Switch_To_LoginActivity.this, screenWidth_PX);
        screenHeight_DP = Dp2pxANDpx2dp.px2dip(Switch_To_LoginActivity.this, screenHeight_PX);
        initPosition_PX = screenHeight_PX - Dp2pxANDpx2dp.dip2px(Switch_To_LoginActivity.this, 200);
        endPosition_PX = 250;
        System.out.println(initPosition_PX);
        handler.postDelayed(runnable, del);

        //这地方一定要先做这一步操作！！！！
        //因为打开登录页面后然后点击“back”键，不需要设置手机号，
        // 但是如果不做此操作，将会设置手机号，但是到此为止还未给strPhoneNum赋初值
        setResult(1,getIntent());


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                strPhoneNumber = id.getText().toString();
                strPassword = password.getText().toString();

                if (!"".equals(strPhoneNumber)) {
                    if (!"".equals(password)) {
                        JsonObject object = new JsonObject();
                        object.addProperty("Flag", "login");
                        object.addProperty("Phone", strPhoneNumber);
                        object.addProperty("Password", strPassword);
                        MyNormalThread mySocketThread = new MyNormalThread(context, object.toString(), handler);
                        mySocketThread.start();
                    } else {
                        Toast.makeText(context, "请输入密码", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "请输入账号", Toast.LENGTH_SHORT).show();
                }
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                //打开注册页面
                RegisterPage registerPage = new RegisterPage();
                registerPage.setRegisterCallback(new EventHandler() {
                    public void afterEvent(int event, int result, Object data) {
// 解析注册结果
                        if (result == SMSSDK.RESULT_COMPLETE) {
                            @SuppressWarnings("unchecked")
                            HashMap<String, Object> phoneMap = (HashMap<String, Object>) data;
                            String country = (String) phoneMap.get("country");
                            String phone = (String) phoneMap.get("phone");
                            Toast.makeText(context, "验证成功", Toast.LENGTH_SHORT).show();
                            SMSSDK.unregisterEventHandler(this);
                            //验证成功后将会跳转到输入密码的界面：RegisterActivity.class
                            Intent intent = new Intent(context,RegisterActivity.class);
                            intent.putExtra("phone",phone);
                            startActivity(intent);
//                            finish();
                        }else if (result == SMSSDK.RESULT_ERROR){
                            // 根据服务器返回的网络错误，给toast提示
                            try {
                                Throwable throwable = (Throwable) data;
                                throwable.printStackTrace();
                                JSONObject object = new JSONObject(throwable.getMessage());
                                String des = object.optString("detail");//错误描述
                                int status = object.optInt("status");//错误代码
                                if (status > 0 && !TextUtils.isEmpty(des)) {
                                    Toast.makeText(context, des, Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            } catch (Exception e) {
                                //do something
                            }
                        }
                    }
                });
                registerPage.show(context);
            }
        });
    }
}


