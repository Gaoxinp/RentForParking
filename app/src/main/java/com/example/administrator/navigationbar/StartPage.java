package com.example.administrator.navigationbar;

import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.TextView;


/**
 * Created by Administrator on 2016/10/15.
 * 将时间设置为2s， 不显示具体的时间倒计时
 */

public class StartPage extends AppCompatActivity{

    /*
    * TextView  用来当做定时器，以提示剩余几秒进入主页面
    * */
    TextView activity_main_tv_time ;
    /*
    * 将使用Handler自带的定时器*/
    Handler handler = new Handler();
    /*
    * 初始显示的剩余时间是5秒*/
    private int time = 1;
    /*
    * 延时时间为1000毫秒 即1秒*/
    private int del = 1000;

    /*
    *
    * 自定义定时器
    *
    * */
    Runnable runnable = new Runnable() {

        @Override
        public void run() {
            try {
                if (time != -1) {

                    handler.postDelayed(this, del);
                    activity_main_tv_time.setText(Integer.toString(time--));

                } else {
                    Intent intent = new Intent();
                    intent.setClass(StartPage.this,NavigationBar.class);
                    intent.putExtra("from","StartPage.class");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
//                    可以增加切换动画
                    finish();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page_load);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        activity_main_tv_time= (TextView) findViewById(R.id.activity_main_textView_time);
        handler.postDelayed(runnable, del);



    }

    /**
     *抓取用户点击手机的后退键
     */

//    private long exitTime=0;
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
//                this.exitApp();
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

}
