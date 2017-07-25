package com.example.administrator.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.administrator.navigationbar.FragmentConsumptionRentOut;
import com.example.administrator.navigationbar.FragmentFinashedRentOut;
import com.example.administrator.navigationbar.FragmentPublishINGRentOut;
import com.example.administrator.navigationbar.FragmentRefundRentOut;
import com.example.administrator.staticValues.StaticValues;

import java.util.List;

/**
 * Created by 高信朋 on 2017/4/5.
 */

public class MyRentOutFragmentPagerAdapter extends FragmentPagerAdapter {

    private final int PAGER_COUNT = 4;

    private FragmentPublishINGRentOut publish = null;
    private FragmentConsumptionRentOut consumption = null;
    private FragmentFinashedRentOut finashed = null;
    private FragmentRefundRentOut refund = null;
    private List<Fragment> list;

    public MyRentOutFragmentPagerAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        this.list = list;
    }

    public MyRentOutFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        publish = new FragmentPublishINGRentOut();
        consumption = new FragmentConsumptionRentOut();
        finashed = new FragmentFinashedRentOut();
        refund = new FragmentRefundRentOut();
        list.add(publish);
        list.add(consumption);
        list.add(finashed);
        list.add(refund);

    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment = null;
        switch (position) {

            case StaticValues.PAGE_ONE:
                fragment = list.get(0);
                break;
            case StaticValues.PAGE_TWO:
                fragment = list.get(1);
                break;
            case StaticValues.PAGE_THREE:
                fragment = list.get(2);
                break;
            case StaticValues.PAGE_FOUR:
                fragment = list.get(3);
                break;
        }


        return fragment;
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
