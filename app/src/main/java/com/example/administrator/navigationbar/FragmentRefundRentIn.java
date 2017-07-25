package com.example.administrator.navigationbar;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.adapter.MyListViewBaseAdapter;
import com.example.administrator.model.DealDetail;
import com.example.administrator.thread.MyNormalThread;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 高信朋 on 2017/4/5.
 */

public class FragmentRefundRentIn extends Fragment {
    private ListView listView = null;
    private MyListViewBaseAdapter adapter = null;
    private List<DealDetail> details = new ArrayList<>();
    private String strPhoneNumber;
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            details = new ArrayList<>();
            if (msg.what == 0) {
                adapter = new MyListViewBaseAdapter(getActivity().getBaseContext(), details);
                listView.setAdapter(adapter);
            } else if (msg.what == 1) {
                JsonArray array = (JsonArray) msg.obj;
                JsonObject object = new JsonObject();
                for (int index = 0; index < array.size(); index++) {
                    object = array.get(index).getAsJsonObject();
                    DealDetail detail = new DealDetail(object.get("publishDate").getAsString(), object.get("beginDate").getAsString(), object.get("endDate").getAsString(), object.get("province").getAsString(), object.get("city").getAsString(), object.get("area").getAsString(), object.get("phoneNum").getAsString(), object.get("master").getAsString(), object.get("detailPos").getAsString(), object.get("duration").getAsString(), object.get("money").getAsString());
                    details.add(detail);
                }
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                adapter = new MyListViewBaseAdapter(getActivity().getBaseContext(), details);
                listView.setAdapter(adapter);
            }


        }
    };

    // : 2017/4/6 通过socket获取details的值

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_refund_rentin, container, false);

        listView = (ListView) view.findViewById(R.id.listView_refund_renIn);
        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        View heardLayout = navigationView.getHeaderView(0);
        TextView phoneNumber = (TextView) heardLayout.findViewById(R.id.phoneNumberTextView_navigationBarHeader);
        strPhoneNumber = phoneNumber.getText().toString();
        //从服务器获取交易数据
        getDealDetail();

//        adapter = new MyListViewBaseAdapter(getActivity().getBaseContext(),details);
//        listView.setAdapter(adapter);
        return view;
    }

    // : 2017/4/7  根据用户手机号从服务器获取用户 待退款——租入列表
    private void getDealDetail() {
        JsonObject object = new JsonObject();
        object.addProperty("Flag", "getRefund_rentIn");
        object.addProperty("phoneNum", strPhoneNumber);
        new MyNormalThread(getActivity().getBaseContext(), object.toString(), handler).start();
    }

    @Override
    public void onResume() {
        getDealDetail();
//        adapter = new MyListViewBaseAdapter(getActivity().getBaseContext(),details);
//        listView.setAdapter(adapter);
        super.onResume();
    }

}
