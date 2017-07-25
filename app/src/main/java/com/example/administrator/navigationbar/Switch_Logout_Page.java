package com.example.administrator.navigationbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.administrator.staticValues.StaticValues;

/**
 * Created by Administrator on 2016/10/15.
 * 切换账号和注销账号页面
 */

public class Switch_Logout_Page extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.switch_logout_page);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button log_out_button = (Button) findViewById(R.id.log_out_button);
        Button switch_button = (Button) findViewById(R.id.switch_button);

//      点击了退出之后的动作
//      出现了一个对话框，用来确认
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        log_out_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.setTitle("消息框");
                builder.setMessage("你确定要注销吗？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        StaticValues.hasLogin = false;
                        Intent intent = getIntent();
                        intent.putExtra("flag","已注销");
                        setResult(0,intent);
                        finish();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                什么也不做
                    }
                });
                builder.create().show();
            }
        });


        switch_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Switch_Logout_Page.this, Switch_To_LoginActivity.class);
                intent.putExtra("from", "navigationBar");
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = getIntent();
        setResult(1,intent);


        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
