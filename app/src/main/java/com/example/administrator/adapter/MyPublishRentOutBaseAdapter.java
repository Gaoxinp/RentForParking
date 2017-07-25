package com.example.administrator.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.model.PublishDetail;
import com.example.administrator.navigationbar.Dp2pxANDpx2dp;
import com.example.administrator.navigationbar.R;

import java.util.List;

/**
 * Created by 高信朋 on 2017/4/13.
 */

public class MyPublishRentOutBaseAdapter extends BaseAdapter {

    private Context context;
    private List<PublishDetail> publishDetails;

    public MyPublishRentOutBaseAdapter(Context context, List<PublishDetail> publishDetails) {
        this.context = context;
        this.publishDetails = publishDetails;
    }

    @Override
    public int getCount() {
        return publishDetails.size();
    }

    @Override
    public PublishDetail getItem(int position) {
        return publishDetails.get(position);
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
            ll = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.publish_rentout_listview_cell, null);
        }
        PublishDetail detail = getItem(position);

        TextView beginDate = (TextView) ll.findViewById(R.id.beginDate_publish_rentOut_listview_cell);
        TextView detailPos = (TextView) ll.findViewById(R.id.detailPos_publish_rentOut_listview_cell);
        TextView duration = (TextView) ll.findViewById(R.id.duration_publish_rentOut_listview_cell);
        TextView money = (TextView) ll.findViewById(R.id.money_publish_rentOut_listview_cell);

        beginDate.setText(detail.getBeginDate());
        detailPos.setText(detail.getProvince()+detail.getCity()+detail.getArea()+detail.getDetailPos());
        duration.setText(detail.getDuration());
        money.setText(detail.getMoney());

        int minHight = Dp2pxANDpx2dp.dip2px(context, 50);
        ll.setMinimumHeight(minHight);

        return ll;
    }
}
