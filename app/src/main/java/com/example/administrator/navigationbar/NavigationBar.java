package com.example.administrator.navigationbar;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.staticValues.StaticValues;


public class NavigationBar extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Fragment contentFragment = null;

    TextView textView_login_accout;
    TextView phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_bar);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View heardLayout = navigationView.getHeaderView(0);
        LinearLayout user = (LinearLayout) heardLayout.findViewById(R.id.userLinearLayout_navigationBarHeader);
        //      文本声明
        textView_login_accout = (TextView) heardLayout.findViewById(R.id.textView_log_account);
        phoneNumber = (TextView) heardLayout.findViewById(R.id.phoneNumberTextView_navigationBarHeader);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent inte = getIntent();
        //如果是从Switch_To_LoginActivity.class转到该界面，需要把手机号设置在指定位置
        if (inte.getStringExtra("from").equals("Switch_To_LoginActivity.class")) {
            phoneNumber = (TextView) heardLayout.findViewById(R.id.phoneNumberTextView_navigationBarHeader);
            phoneNumber.setText(inte.getStringExtra("phoneNumber"));
            textView_login_accout.setVisibility(TextView.INVISIBLE);
            StaticValues.hasLogin = true;
        } else {
            //如果是从StartPage.class转到该界面，什么也不做
        }


        //首先显示用户选择地点的界面
        contentFragment = new ChoosePositionFragment();
        Bundle bundle = new Bundle();
        contentFragment.setArguments(bundle);
        //为Fragment开启事务，用新的Fragment替换原来的Fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.nav_frame, contentFragment).commit();


//        打开和关闭navigation 抽屉
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(NavigationBar.this, Switch_Logout_Page.class);
                startActivityForResult(intent,0);

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 0){
            phoneNumber.setText("");
            textView_login_accout.setVisibility(TextView.VISIBLE);
        }else if (resultCode == 1){

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //如果点击了
        if (id == R.id.action_settings) {
            return true;
        }
        //如果点击了“搜索”选项
        else if (id == R.id.navigation_lookfor) {
            //首先显示用户选择地点的界面
            contentFragment = new ChoosePositionFragment();
            Bundle bundle = new Bundle();
            contentFragment.setArguments(bundle);
            //为Fragment开启事务，用新的Fragment替换原来的Fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.nav_frame, contentFragment).commit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        //TODO 点击“拍照”操作
        if (id == R.id.navigation_camera) {

            // Handle the camera action
        }
        //TODO 点击“图片浏览”操作
        else if (id == R.id.navigation_photo_search) {

        }
        //TODO 点击“消息中心”操作
        else if (id == R.id.navigation_news) {

        }
        // 点击“我的车位”操作
        else if (id == R.id.navigation_myCarStop) {
            contentFragment = new MyCarStopFragment();
        }
        // 点击“我的账户”操作
         else if (id == R.id.navigation_my_account) {
            contentFragment = new MyCountFragment();
        }
        //点击“我的租入”操作
        else if (id == R.id.navigation_zuru) {
            contentFragment = new RentInFragment();
        }
        //点击“我的租出”操作
        else if (id == R.id.navigation_zuchu) {
            contentFragment = new RentOutFragment();
        }

        Bundle bundle = new Bundle();
        contentFragment.setArguments(bundle);
        //为Fragment开启事务，用新的Fragment替换原来的Fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.nav_frame, contentFragment).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }


    /**
     * 捕捉返回事件按钮
     * <p>
     * 因为此 Activity 继承 TabActivity 用 onKeyDown 无响应，所以改用 dispatchKeyEvent
     * 一般的 Activity 用 onKeyDown 就可以了
     */


    private long exitTime = 0;

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
                this.exitApp();
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    /**
     * 退出程序
     */
    private void exitApp() {
//         判断2次点击事件时间
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(NavigationBar.this, "再按一次退出有车位", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            StaticValues.hasLogin = false;
            finish();
//            退出程序
        }
    }

    //当程序被强行终结时，需要将登陆状态重置
    @Override
    protected void onDestroy() {
        StaticValues.hasLogin = false;
        super.onDestroy();
    }
}
