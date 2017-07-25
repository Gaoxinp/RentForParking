package com.example.administrator.navigationbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.telephony.SmsManager;
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

import java.net.URI;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by 高信朋 on 2017/4/5.
 */

public class FragmentUnderwayRentIn extends Fragment {

    private View view;

    private ListView listView = null;
    private MyListViewBaseAdapter adapter = null;
    private List<DealDetail> detailList = null;
    private String strPhoneNumber;
    private String carPhone = "";
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            detailList = new ArrayList<>();
            if (msg.what == 0) {     //没有待消费的租入信息
                adapter = new MyListViewBaseAdapter(getActivity().getBaseContext(), detailList);
                listView.setAdapter(adapter);
            } else if (msg.what == 1) {
                JsonArray array = (JsonArray) msg.obj;
                JsonObject object = new JsonObject();
                for (int index = 0; index < array.size(); index++) {
                    object = array.get(index).getAsJsonObject();
                    DealDetail detail = new DealDetail(object.get("publishDate").getAsString(), object.get("beginDate").getAsString(), object.get("endDate").getAsString(), object.get("province").getAsString(), object.get("city").getAsString(), object.get("area").getAsString(), object.get("phoneNum").getAsString(), object.get("master").getAsString(), object.get("detailPos").getAsString(), object.get("duration").getAsString(), object.get("money").getAsString(), object.get("carPhone").getAsString());
                    detailList.add(detail);
                }
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                adapter = new MyListViewBaseAdapter(getActivity().getBaseContext(), detailList);
                listView.setAdapter(adapter);
            } else if (msg.what == 2) {
                Toast.makeText(getActivity().getBaseContext(), "订单取消成功", Toast.LENGTH_SHORT).show();
                getDealDetail();
            } else if (msg.what == 3) {
                Toast.makeText(getActivity().getBaseContext(), "操作失败，请稍后再试", Toast.LENGTH_SHORT).show();
            } else if (msg.what == 4) {
                Toast.makeText(getActivity().getBaseContext(), "订单已确认完成", Toast.LENGTH_SHORT).show();
                getDealDetail();
            } else if (msg.what == 5) {
                Toast.makeText(getActivity().getBaseContext(), "订单确认失败，请稍后再试", Toast.LENGTH_SHORT).show();
            } else if (msg.what == 6) {      //通知服务器要开锁的车位手机号成功
                new AlertDialog.Builder(view.getContext()).setTitle("提示")
                        .setMessage("将发送一条短信息，以请求开锁，是否继续？（会花费基础短信费）")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                Uri uri = Uri.parse("smsto:"+carPhone);
//                                Intent intent = new Intent(Intent.ACTION_SENDTO,uri);
//                                intent.putExtra("sms_body","usr.cnAT+SOCKAEN=ON");
//                                startActivity(intent);
                                //不调用系统短信界面直接发短信
                                SmsManager manager = SmsManager.getDefault();
                                manager.sendTextMessage(carPhone, null, "usr.cnAT+SOCKAEN=ON", null, null);
                            }
                        }).setNegativeButton("取消", null).show();
            }
        }
    };
    private int flag = 0;

    // : 2017/4/6 通过socket获取details的值
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_underway_rentin, container, false);

        listView = (ListView) view.findViewById(R.id.listView_underway_renIn);
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

        return view;
    }


    //添加上下文菜单
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(flag, 0, Menu.NONE, "完成确认");
        menu.add(flag, 1, Menu.NONE, "取消订单");
        menu.add(flag, 2, Menu.NONE, "请求开锁");


        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (getUserVisibleHint()) {
            DealDetail detail = (DealDetail) listView.getAdapter().getItem(flag);
            switch (item.getItemId()) {
                case 0:
                    JsonObject object1 = new JsonObject();
                    object1.addProperty("Flag", "FinashOrder");
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
                    new MyNormalThread(getActivity().getBaseContext(), object1.toString(), handler).start();
                    break;
                case 1:
                    JsonObject object = new JsonObject();
                    object.addProperty("Flag", "cancelOrder_payFinash");
                    object.addProperty("phoneNum", strPhoneNumber);
                    object.addProperty("master", detail.getMasterPhoneNum());
                    object.addProperty("province", detail.getProvince());
                    object.addProperty("city", detail.getCity());
                    object.addProperty("area", detail.getArea());
                    object.addProperty("detailPos", detail.getDetailePos());
                    object.addProperty("beginDate", detail.getBeginDate());
                    object.addProperty("endDate", detail.getEndDate());
                    object.addProperty("publishDate", detail.getPublishDate());
                    object.addProperty("money", detail.getMoney());
                    new MyNormalThread(getActivity().getBaseContext(), object.toString(), handler).start();
                    break;
                case 2:
                    //通知服务器要开锁的车位手机号，以便比对是否是等待消费的车位
                    JsonObject object2 = new JsonObject();
                    object2.addProperty("Flag", "CarRegister");
                    object2.addProperty("carPhone", detail.getCarPhone());
                    new MyNormalThread(getActivity().getBaseContext(), object2.toString(), handler).start();
                    //将车位手机号保存为类变量，以便于发短信时使用
                    carPhone = detail.getCarPhone();
                    break;
            }
//        return super.onContextItemSelected(item);
            return true;
        }
        return super.onContextItemSelected(item);
    }

    // : 2017/4/7  根据用户手机号从服务器获取用户 待消费——租入列表
    private void getDealDetail() {
        JsonObject object = new JsonObject();
        object.addProperty("Flag", "getConsume_rentIn");
        object.addProperty("phoneNum", strPhoneNumber);
        new MyNormalThread(getActivity().getBaseContext(), object.toString(), handler).start();
    }

    @Override
    public void onResume() {
        getDealDetail();
        super.onResume();
    }

}
