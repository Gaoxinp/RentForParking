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

import com.example.administrator.adapter.MyPublishRentOutBaseAdapter;
import com.example.administrator.model.PublishDetail;
import com.example.administrator.thread.MyNormalThread;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 高信朋 on 2017/4/12.
 */

public class FragmentPublishINGRentOut extends Fragment {
    private List<PublishDetail> detailList;
    private ListView listView = null;
    private MyPublishRentOutBaseAdapter adapter;
    private String strPhoneNumber;
    private View view;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            detailList = new ArrayList<>();
            if (msg.what == 0) {     //没有出租车位

            } else if (msg.what == 1) {
                JsonArray array = (JsonArray) msg.obj;
                JsonObject object = new JsonObject();
                for (int index = 0; index < array.size(); index++) {
                    object = array.get(index).getAsJsonObject();
                    PublishDetail detail = new PublishDetail(strPhoneNumber,object.get("province").getAsString(), object.get("city").getAsString(),object.get("area").getAsString(),object.get("detailPos").getAsString(), object.get("publishDate").getAsString(),object.get("beginDate").getAsString(),object.get("endDate").getAsString(),object.get("duration").getAsString(), object.get("money").getAsString());
                    detailList.add(detail);
                }
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                adapter = new MyPublishRentOutBaseAdapter(getActivity().getBaseContext(), detailList);
                listView.setAdapter(adapter);
            }
        }
    };





    private void getPublishDetail() {
        JsonObject object = new JsonObject();
        object.addProperty("Flag", "getPublishING");
        object.addProperty("phoneNum", strPhoneNumber);
        new MyNormalThread(getActivity().getBaseContext(), object.toString(), handler).start();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_publishing_rentout, container, false);
        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        View heardLayout = navigationView.getHeaderView(0);
        TextView phoneNumber = (TextView) heardLayout.findViewById(R.id.phoneNumberTextView_navigationBarHeader);
        strPhoneNumber = phoneNumber.getText().toString();


        listView = (ListView) view.findViewById(R.id.listView_publish_renOut);


        return view;
    }

    @Override
    public void onResume() {
        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        View heardLayout = navigationView.getHeaderView(0);
        TextView phoneNumber = (TextView) heardLayout.findViewById(R.id.phoneNumberTextView_navigationBarHeader);
        strPhoneNumber = phoneNumber.getText().toString();
        getPublishDetail();
        super.onResume();
    }
}
