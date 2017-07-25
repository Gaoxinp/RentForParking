package com.example.administrator.navigationbar;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

public class FragmentRefundRentOut extends Fragment {
    private ListView listView = null;
    private MyListViewBaseAdapter adapter = null;
    private List<DealDetail> details = new ArrayList<>();
    private String strPhoneNumber;
    private int flag = 0;
    private Handler handler = new Handler(){
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
                    DealDetail detail = new DealDetail(object.get("publishDate").getAsString(), object.get("beginDate").getAsString(), object.get("endDate").getAsString(), object.get("province").getAsString(), object.get("city").getAsString(), object.get("area").getAsString(), object.get("phoneNum").getAsString(), object.get("master").getAsString(), object.get("detailPos").getAsString(), object.get("duration").getAsString(), object.get("money").getAsString(), object.get("carPhone").getAsString());
                    details.add(detail);
                }
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                adapter = new MyListViewBaseAdapter(getActivity().getBaseContext(), details);
                listView.setAdapter(adapter);
            }else if (msg.what == 2){
                Toast.makeText(getActivity().getBaseContext(),"退款成功",Toast.LENGTH_SHORT).show();
                getDealDetail();
            }else if (msg.what == 3){
                Toast.makeText(getActivity().getBaseContext(),"操作失败，请稍后重试",Toast.LENGTH_SHORT).show();

            }

        }
    };

    // : 2017/4/6 通过socket获取details的值
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_refund_rentout,container,false);
        listView = (ListView) view.findViewById(R.id.listView_refund_renOut);
        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        View heardLayout = navigationView.getHeaderView(0);
        TextView phoneNumber = (TextView) heardLayout.findViewById(R.id.phoneNumberTextView_navigationBarHeader);
        strPhoneNumber = phoneNumber.getText().toString();
        //从服务器获取交易数据
        getDealDetail();
// 为listview注册上下文菜单
        registerForContextMenu(listView);
        //添加长按监听器，获得长按的位置
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                flag = position;
                return false;
            }
        });

//        adapter = new MyListViewBaseAdapter(getActivity().getBaseContext(),details);
//        listView.setAdapter(adapter);
        return view;
    }

    // : 2017/4/7  根据用户手机号从服务器获取用户 待退款——租出列表
    private void getDealDetail() {
        JsonObject object = new JsonObject();
        object.addProperty("Flag", "getRefund_rentOut");
        object.addProperty("phoneNum", strPhoneNumber);
        new MyNormalThread(getActivity().getBaseContext(), object.toString(), handler).start();
    }
    //添加上下文菜单
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(flag, 0, Menu.NONE, "同意退款");
//        menu.add(flag, 1, Menu.NONE, "取消订单");


        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (getUserVisibleHint()){
            DealDetail detail = (DealDetail) listView.getAdapter().getItem(flag);

            switch (item.getItemId()) {
                case 0:
                    JsonObject object1 = new JsonObject();
                    object1.addProperty("Flag", "goToRefund");
                    object1.addProperty("phoneNum", detail.getPhoneNum());
                    object1.addProperty("master", detail.getMasterPhoneNum());
                    object1.addProperty("province", detail.getProvince());
                    object1.addProperty("city", detail.getCity());
                    object1.addProperty("area", detail.getArea());
                    object1.addProperty("detailPos", detail.getDetailePos());
                    object1.addProperty("beginDate", detail.getBeginDate());
                    object1.addProperty("endDate", detail.getEndDate());
                    object1.addProperty("publishDate", detail.getPublishDate());
                    object1.addProperty("money", detail.getMoney());
                    object1.addProperty("carPhone",detail.getCarPhone());
                    new MyNormalThread(getActivity().getBaseContext(), object1.toString(), handler).start();
                    break;

            }
            return true;
        }

        return super.onContextItemSelected(item);
    }

    @Override
    public void onResume() {
        getDealDetail();
//        adapter = new MyListViewBaseAdapter(getActivity().getBaseContext(),details);
//        listView.setAdapter(adapter);
        super.onResume();
    }
}
