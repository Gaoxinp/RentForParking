package com.example.administrator.adapter;



import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.administrator.navigationbar.FragmentFinashedRentIn;
import com.example.administrator.navigationbar.FragmentObligationRentIn;
import com.example.administrator.navigationbar.FragmentRefundRentIn;
import com.example.administrator.navigationbar.FragmentUnderwayRentIn;
import com.example.administrator.staticValues.StaticValues;

import java.util.List;


/**
 * Created by 高信朋 on 2017/4/5.
 */

public class MyRentInFragmentPagerAdapter extends FragmentPagerAdapter {

    private final int PAGER_COUNT = 0;
    private FragmentObligationRentIn obligation = null;
    private FragmentUnderwayRentIn underway = null;
    private FragmentFinashedRentIn finashed = null;
    private FragmentRefundRentIn refund = null;

    public MyRentInFragmentPagerAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        this.list = list;
    }

    private List<Fragment> list  = null;

    public MyRentInFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        obligation = new FragmentObligationRentIn();
        underway = new FragmentUnderwayRentIn();
        finashed = new FragmentFinashedRentIn();
        refund = new FragmentRefundRentIn();
        list.add(obligation);
        list.add(underway);
        list.add(finashed);
        list.add(refund);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position){
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

}
