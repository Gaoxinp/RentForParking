package com.example.administrator.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.model.DealDetail;
import com.example.administrator.navigationbar.Dp2pxANDpx2dp;
import com.example.administrator.navigationbar.R;

import java.util.List;

/**
 * Created by 高信朋 on 2017/4/6.
 */

public class MyListViewBaseAdapter extends BaseAdapter {

    //交易详情类的数组
    private List<DealDetail> details = null;
    private Context context = null;

    public MyListViewBaseAdapter(Context contexts, List<DealDetail> details) {
        this.context = contexts;
        this.details = details;
    }


    @Override
    public int getCount() {
        return details.size();
    }

    @Override
    public DealDetail getItem(int position) {
        return details.get(position);
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
            ll = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.listview_detail_cell, null);
        }

        DealDetail dealDetail = getItem(position);

        TextView timeTV = (TextView) ll.findViewById(R.id.timeTextView_listCell);
        TextView positionTV = (TextView) ll.findViewById(R.id.positionTextView_listCell);
        TextView minsTV = (TextView) ll.findViewById(R.id.minsTextView_listCell);
        TextView moneyTV = (TextView) ll.findViewById(R.id.moneyTextView_listCell);

        timeTV.setText(dealDetail.getPublishDate());
        positionTV.setText(dealDetail.getProvince()+dealDetail.getCity()+dealDetail.getArea()+dealDetail.getDetailePos());
        minsTV.setText(dealDetail.getMins());
        moneyTV.setText(dealDetail.getMoney());
        int minHight = Dp2pxANDpx2dp.dip2px(context, 50);
        ll.setMinimumHeight(minHight);

//        if ((position%2) == 0){
//            ll.setBackgroundColor(0xFFC800);
//            System.out.println("diaoyong1");
//        }else {
//            ll.setBackgroundColor(0x000000FF);
//            System.out.println("diaoyong2");
//        }
        return ll;
    }
}
