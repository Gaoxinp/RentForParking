package com.example.administrator.navigationbar;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.staticValues.StaticValues;

/**
 * Created by 高信朋 on 2017/3/22.
 */

public class MyCountFragment extends Fragment {
    private TextView cardNumber = null;
    private TextView phoneNumber = null;
    private TextView textView_log_account = null;
    private TextView phoneNumberTextView = null;


    private LinearLayout noLoginLinearLayout = null;
    private LinearLayout okLoginLinearLayout = null;
    private Button toLogin = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_my_count, container, false);



        phoneNumberTextView = (TextView) view.findViewById(R.id.phoneNumberTextView_myCount);
        cardNumber = (TextView) view.findViewById(R.id.cardNumberTextView_myCount);
        noLoginLinearLayout = (LinearLayout) view.findViewById(R.id.noLoginLinearLayout_myCount);
        okLoginLinearLayout = (LinearLayout) view.findViewById(R.id.okLoginLinearLayout_myCount);
        toLogin = (Button) view.findViewById(R.id.toLoginButton_myCount);
//        NavigationView navigationView = (NavigationView) view.findViewById(R.id.nav_view);
//        View heardLayout = navigationView.getHeaderView(0);
        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        View heardLayout = navigationView.getHeaderView(0);
        phoneNumber = (TextView) heardLayout.findViewById(R.id.phoneNumberTextView_navigationBarHeader);
        textView_log_account = (TextView) heardLayout.findViewById(R.id.textView_log_account);

        //        浮动的小信封
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        if (StaticValues.hasLogin == false) {
            okLoginLinearLayout.setVisibility(LinearLayout.INVISIBLE);
            noLoginLinearLayout.setVisibility(LinearLayout.VISIBLE);


        } else {
            // TODO: 2017/4/4 已经登录成功时，需要从服务器获取用户信息：银行卡张数
            noLoginLinearLayout.setVisibility(LinearLayout.INVISIBLE);
            okLoginLinearLayout.setVisibility(LinearLayout.VISIBLE);

            phoneNumberTextView.setText(phoneNumber.getText());


        }
        toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), Switch_To_LoginActivity.class);
                intent.putExtra("from", "myCount");
                startActivityForResult(intent,0);

            }
        });


        return view;
    }

    // TODO: 2017/4/7 刷新界面，需要根据用户手机号从服务器获取用户账户信息
    @Override
    public void onResume() {
        super.onResume();
    }

    //从本页面登陆后，需要将用户的手机号填写在指定位置
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 0) {
            String strPhoneNumber = data.getStringExtra("phoneNumber");
            textView_log_account.setVisibility(TextView.INVISIBLE);
            phoneNumber.setText(strPhoneNumber);
        }
    }
}
