package com.example.administrator.navigationbar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.administrator.adapter.MyPublishRentOutBaseAdapter;
import com.example.administrator.adapter.MyRentOutFragmentPagerAdapter;
import com.example.administrator.model.PublishDetail;
import com.example.administrator.staticValues.StaticValues;
import com.example.administrator.thread.MyNormalThread;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 高信朋 on 2017/3/22.
 */

public class RentOutFragment extends Fragment implements RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener, View.OnClickListener {

    private RadioGroup radioGroupRG = null;

    private RadioButton publishRB = null;           //发布中
    private RadioButton consumptionRB = null;       //待消费
    private RadioButton finashedRB = null;          //已完成
    private RadioButton refundRB = null;            //待退款

    private TextView phoneNumber = null;
    private TextView textView_log_account = null;

    private LinearLayout noLoginLinearLayout = null;
    private LinearLayout okLoginLinearLayout = null;
    private Button toLogin = null;

    private ViewPager viewPager = null;

    private MyRentOutFragmentPagerAdapter adapter = null;
    private String strPhoneNumber;
    private List<PublishDetail> detailList;
    private List<Fragment> list;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_rent_out, container, false);

//        FragmentManager manager = getFragmentManager();
//        FragmentTransaction transaction = manager.beginTransaction();
//        FragmentPublishINGRentOut publishFragment = new FragmentPublishINGRentOut();
//        FragmentConsumptionRentOut consumptionFragment = new FragmentConsumptionRentOut();
//        FragmentFinashedRentOut finashedFragment = new FragmentFinashedRentOut();
//        FragmentRefundRentOut refundFragment = new FragmentRefundRentOut();
//
//        transaction.add(publishFragment, "publishFragment");
//        transaction.add(consumptionFragment, "consumptionFragment");
//        transaction.add(finashedFragment, "finashedFragment");
//        transaction.add(refundFragment, "refundFragment");
//
//        transaction.commit();
//        FragmentPublishINGRentOut publish = (FragmentPublishINGRentOut) getFragmentManager().findFragmentByTag("publishFragment");
//        publish.refrash(userName);

        list = new ArrayList<>();
        list.add(new FragmentPublishINGRentOut());
        list.add(new FragmentConsumptionRentOut());
        list.add(new FragmentFinashedRentOut());
        list.add(new FragmentRefundRentOut());
        adapter = new MyRentOutFragmentPagerAdapter(getChildFragmentManager(),list);

        noLoginLinearLayout = (LinearLayout) view.findViewById(R.id.noLoginLinearLayout_rentOut);
        okLoginLinearLayout = (LinearLayout) view.findViewById(R.id.okLoginLinearLayout_rentOut);

        publishRB = (RadioButton) view.findViewById(R.id.fabuzhongRadioButton_rentOut);
        radioGroupRG = (RadioGroup) view.findViewById(R.id.radiogroup_rentOut);
        consumptionRB = (RadioButton) view.findViewById(R.id.daixiaofeiRadioButton_rentOut);
        finashedRB = (RadioButton) view.findViewById(R.id.yiwanchengRadioButton_rentOut);
        refundRB = (RadioButton) view.findViewById(R.id.daituikuanRadioButton_rentOut);

        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        View heardLayout = navigationView.getHeaderView(0);
        phoneNumber = (TextView) heardLayout.findViewById(R.id.phoneNumberTextView_navigationBarHeader);
        textView_log_account = (TextView) heardLayout.findViewById(R.id.textView_log_account);
        toLogin = (Button) view.findViewById(R.id.toLoginButton_rentOut);

        radioGroupRG.setOnCheckedChangeListener(this);

        publishRB.setOnClickListener(this);
        consumptionRB.setOnClickListener(this);
        finashedRB.setOnClickListener(this);
        refundRB.setOnClickListener(this);

        viewPager = (ViewPager) view.findViewById(R.id.viewPager_rentOut);
        viewPager.setAdapter(adapter);
//        viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(this);
        publishRB.setChecked(true);


        if (StaticValues.hasLogin == false) {
            okLoginLinearLayout.setVisibility(LinearLayout.INVISIBLE);
            noLoginLinearLayout.setVisibility(LinearLayout.VISIBLE);
        } else {
            noLoginLinearLayout.setVisibility(LinearLayout.INVISIBLE);
            okLoginLinearLayout.setVisibility(LinearLayout.VISIBLE);
        }
        toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), Switch_To_LoginActivity.class);
                intent.putExtra("from", "myCount");
                startActivityForResult(intent, 0);

            }
        });


        return view;
    }

    @Override
    public void onResume() {
        if (StaticValues.hasLogin == false) {
            okLoginLinearLayout.setVisibility(LinearLayout.INVISIBLE);
            noLoginLinearLayout.setVisibility(LinearLayout.VISIBLE);
        } else {
            noLoginLinearLayout.setVisibility(LinearLayout.INVISIBLE);
            okLoginLinearLayout.setVisibility(LinearLayout.VISIBLE);
        }
        super.onResume();
    }


    //从本页面登陆后，需要将用户的手机号填写在指定位置
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 0) {
            strPhoneNumber = data.getStringExtra("phoneNumber");
            textView_log_account.setVisibility(TextView.INVISIBLE);
            phoneNumber.setText(strPhoneNumber);

        }
    }

    //继承自RadioGroup.OnCheckedChangeListener  ，  RadioGroup 选择改变的时候调用
    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId) {
            case StaticValues.PAGE_ONE:
                viewPager.setCurrentItem(StaticValues.PAGE_ONE);
                break;
            case StaticValues.PAGE_TWO:
                viewPager.setCurrentItem(StaticValues.PAGE_TWO);
                break;
            case StaticValues.PAGE_THREE:
                viewPager.setCurrentItem(StaticValues.PAGE_THREE);
                break;
            case StaticValues.PAGE_FOUR:
                viewPager.setCurrentItem(StaticValues.PAGE_FOUR);
                break;
        }
    }

    // 继承自ViewPager.OnPageChangeListener  ，  ViewPager 的重写方法
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    //因为ViewPager+Fragment会预加载，所以在滑动到相邻fragment的时候不会重新加载，此方法可以设置page选择后重新调用onResume方法加载数据
    @Override
    public void onPageSelected(int position) {

        Fragment fragment = list.get(position);
        fragment.onResume();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

        //state的状态有三个，0表示什么都没做，1正在滑动，2滑动完毕
        if (state == 2) {
            switch (viewPager.getCurrentItem()) {

                case StaticValues.PAGE_ONE:
                    publishRB.setChecked(true);
                    break;
                case StaticValues.PAGE_TWO:
                    consumptionRB.setChecked(true);
                    break;
                case StaticValues.PAGE_THREE:
                    finashedRB.setChecked(true);
                    break;
                case StaticValues.PAGE_FOUR:
                    refundRB.setChecked(true);
                    break;

            }
        }
    }

    //当radioButton被点击时调用
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabuzhongRadioButton_rentOut:
                System.out.println("R.id.fabuzhongRadioButton_rentOut");
                viewPager.setCurrentItem(StaticValues.PAGE_ONE);
                break;
            case R.id.daixiaofeiRadioButton_rentOut:
                System.out.println("R.id.daixiaofeiRadioButton_rentOut");
                viewPager.setCurrentItem(StaticValues.PAGE_TWO);
                break;
            case R.id.yiwanchengRadioButton_rentOut:
                System.out.println("R.id.yiwanchengRadioButton_rentOut");
                viewPager.setCurrentItem(StaticValues.PAGE_THREE);
                break;
            case R.id.daituikuanRadioButton_rentOut:
                System.out.println("R.id.daituikuanRadioButton_rentOut");
                viewPager.setCurrentItem(StaticValues.PAGE_FOUR);
                break;

        }
    }
}
