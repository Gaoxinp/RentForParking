package com.example.administrator.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.model.DealDetail;
import com.example.administrator.model.MyCarStop;
import com.example.administrator.navigationbar.Dp2pxANDpx2dp;
import com.example.administrator.navigationbar.R;

import java.util.List;

/**
 * Created by 高信朋 on 2017/4/12.
 */

public class MyCarStopListViewAdapter extends BaseAdapter {
    //车位详情类的数组
    private List<MyCarStop> carStops;
    private Context context = null;

    public MyCarStopListViewAdapter(List<MyCarStop> carStops, Context context) {
        this.carStops = carStops;
        this.context = context;
    }



    @Override
    public int getCount() {
        return carStops.size();
    }

    @Override
    public MyCarStop getItem(int position) {
        return carStops.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LinearLayout ll = null;
        if (convertView != null) {
            ll = (LinearLayout) convertView;
        } else {
            ll = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.mycarstop_listview_cell, null);
        }

        MyCarStop carstop = getItem(position);

        TextView province = (TextView) ll.findViewById(R.id.province_mycarstop_listview_cell);
        TextView city = (TextView) ll.findViewById(R.id.city_mycarstop_listview_cell);
        TextView area = (TextView) ll.findViewById(R.id.area_mycarstop_listview_cell);
        TextView detailPos = (TextView) ll.findViewById(R.id.detatilPos_mycarstop_listview_cell);
        TextView state = (TextView) ll.findViewById(R.id.state_mycarstop_listview_cell);

        province.setText(carstop.getProvince());
        city.setText(carstop.getCity());
        area.setText(carstop.getArea());
        detailPos.setText(carstop.getDetailPos());
        state.setText(carstop.getState());
        int minHight = Dp2pxANDpx2dp.dip2px(context, 50);
        ll.setMinimumHeight(minHight);

        return ll;
    }
}
