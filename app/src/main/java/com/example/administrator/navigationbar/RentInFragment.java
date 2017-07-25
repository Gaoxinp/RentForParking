package com.example.administrator.navigationbar;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.administrator.adapter.MyRentInFragmentPagerAdapter;
import com.example.administrator.staticValues.StaticValues;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 高信朋 on 2017/3/22.
 */

public class RentInFragment extends Fragment implements ViewPager.OnPageChangeListener, RadioGroup.OnCheckedChangeListener, View.OnClickListener {
    private RadioGroup radioGroupRG = null;
    private RadioButton obligationRB = null;        //待付款
    private RadioButton underWayRB = null;          //待消费
    private RadioButton finashedRB = null;          //已完成
    private RadioButton refundRB = null;            //待退款

    private TextView phoneNumber = null;
    private TextView textView_log_account = null;


    private LinearLayout noLoginLinearLayout = null;
    private LinearLayout okLoginLinearLayout = null;
    private Button toLogin = null;

    private ViewPager viewPager = null;

    private MyRentInFragmentPagerAdapter adapter = null;
    private List<Fragment> list;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_rent_in, container, false);

        list = new ArrayList<>();
        list.add(new FragmentObligationRentIn());
        list.add(new FragmentUnderwayRentIn());
        list.add(new FragmentFinashedRentIn());
        list.add(new FragmentRefundRentIn());
        adapter = new MyRentInFragmentPagerAdapter(getChildFragmentManager(), list);

        noLoginLinearLayout = (LinearLayout) view.findViewById(R.id.noLoginLinearLayout_rentIn);
        okLoginLinearLayout = (LinearLayout) view.findViewById(R.id.okLoginLinearLayout_rentIn);


        radioGroupRG = (RadioGroup) view.findViewById(R.id.radiogroup_rentIn);
        obligationRB = (RadioButton) view.findViewById(R.id.daifukuanRadioButton_rentIn);
        underWayRB = (RadioButton) view.findViewById(R.id.jinxingzhongRadioButton_rentIn);
        finashedRB = (RadioButton) view.findViewById(R.id.yiwanchengRadioButton_rentIn);
        refundRB = (RadioButton) view.findViewById(R.id.daituikuanRadioButton_rentIn);

        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        View heardLayout = navigationView.getHeaderView(0);
        phoneNumber = (TextView) heardLayout.findViewById(R.id.phoneNumberTextView_navigationBarHeader);
        textView_log_account = (TextView) heardLayout.findViewById(R.id.textView_log_account);
        toLogin = (Button) view.findViewById(R.id.toLoginButton_rentIn);

        viewPager = (ViewPager) view.findViewById(R.id.viewPager_rentIn);
        radioGroupRG.setOnCheckedChangeListener(this);
        obligationRB.setChecked(true);

//        obligationRB.setOnClickListener(this);
//        underWayRB.setOnClickListener(this);
//        finashedRB.setOnClickListener(this);
//        refundRB.setOnClickListener(this);

        //获取actionBar的高度，并设置 RadioGroup 的 Y 值为该值
//        TypedArray actionbarSizeTypedArray = getActivity().obtainStyledAttributes(new int[]{
//                android.R.attr.actionBarSize
//        });
//        float h = actionbarSizeTypedArray.getDimension(0, 0);
//        radioGroupRG.setY(h);


        viewPager.setAdapter(adapter);
//        viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(this);


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
            String strPhoneNumber = data.getStringExtra("phoneNumber");
            textView_log_account.setVisibility(TextView.INVISIBLE);
            phoneNumber.setText(strPhoneNumber);
        }
    }

    //继承自RadioGroup.OnCheckedChangeListener  ，  RadioGroup 选择改变的时候调用
    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

        switch (checkedId) {
            case R.id.daifukuanRadioButton_rentIn:
                viewPager.setCurrentItem(StaticValues.PAGE_ONE);
                break;
            case R.id.jinxingzhongRadioButton_rentIn:
                viewPager.setCurrentItem(StaticValues.PAGE_TWO);
                break;
            case R.id.yiwanchengRadioButton_rentIn:
                viewPager.setCurrentItem(StaticValues.PAGE_THREE);
                break;
            case R.id.daituikuanRadioButton_rentIn:
                viewPager.setCurrentItem(StaticValues.PAGE_FOUR);
                break;
            default:
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
    /*state的状态有三个，0表示什么都没做，1正在滑动，2滑动完毕*/
        if (state == 2) {
            switch (viewPager.getCurrentItem()) {
                case StaticValues.PAGE_ONE:
                    obligationRB.setChecked(true);
                    break;
                case StaticValues.PAGE_TWO:
                    underWayRB.setChecked(true);
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
            case R.id.daifukuanRadioButton_rentIn:
                viewPager.setCurrentItem(StaticValues.PAGE_ONE);
                break;
            case R.id.jinxingzhongRadioButton_rentIn:
                viewPager.setCurrentItem(StaticValues.PAGE_TWO);
                break;
            case R.id.yiwanchengRadioButton_rentIn:
                viewPager.setCurrentItem(StaticValues.PAGE_THREE);
                break;
            case R.id.daituikuanRadioButton_rentIn:
                viewPager.setCurrentItem(StaticValues.PAGE_FOUR);
                break;
        }
    }

    @Override
    public void onDestroy() {
        System.out.println("我的租入fragment.onDestroy()");
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        System.out.println("我的租入fragment.onDestroyView()");
        super.onDestroyView();
    }
}
