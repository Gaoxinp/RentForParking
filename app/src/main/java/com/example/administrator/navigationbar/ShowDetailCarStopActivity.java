package com.example.administrator.navigationbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.administrator.adapter.MyPublishRentOutBaseAdapter;
import com.example.administrator.model.PublishDetail;
import com.example.administrator.staticValues.StaticValues;
import com.example.administrator.thread.MyNormalThread;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

/*
* 查询指定地点车位情况后，用来显示车位信息的activity
* */
public class ShowDetailCarStopActivity extends AppCompatActivity {
    private String strPhoneNumber = "";
    private String strProvince = "";
    private String strCity = "";
    private String strArea = "";
    private String strDate = "";

    private Context context = ShowDetailCarStopActivity.this;

    private ListView listView = null;
    private MyPublishRentOutBaseAdapter adapter = null;
    private List<PublishDetail> detailList = null;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            detailList = new ArrayList<>();
            if (msg.what == 0) {
                adapter = new MyPublishRentOutBaseAdapter(context, detailList);
                listView.setAdapter(adapter);
            } else if (msg.what == 1) {
                JsonArray array = (JsonArray) msg.obj;
                JsonObject object = new JsonObject();
                for (int index = 0; index < array.size(); index++) {
                    object = array.get(index).getAsJsonObject();
                    PublishDetail detail = new PublishDetail(object.get("phoneNum").getAsString(),
                            object.get("province").getAsString(),
                            object.get("city").getAsString(),
                            object.get("area").getAsString(),
                            object.get("detailPos").getAsString(),
                            object.get("publishDate").getAsString(),
                            object.get("beginDate").getAsString(),
                            object.get("endDate").getAsString(),
                            object.get("duration").getAsString(),
                            object.get("money").getAsString(),
                            object.get("carPhone").getAsString());
                    detailList.add(detail);
                }
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                adapter = new MyPublishRentOutBaseAdapter(context, detailList);
                listView.setAdapter(adapter);
            } else if (msg.what == 2) {
                Toast.makeText(context, (String) msg.obj, Toast.LENGTH_SHORT).show();
                getCarStopDetail();
            } else if (msg.what == 3) {
                Toast.makeText(context, (String) msg.obj, Toast.LENGTH_SHORT).show();
            }
        }
    };
    //长按标记，用来识别长按了哪一条数据
    private int flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_detail_car_stop);


        listView = (ListView) findViewById(R.id.listView_showDetailCarStop);
        //为listView注册上下文菜单
        registerForContextMenu(listView);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                flag = position;
                System.out.println(flag + "");
                System.out.println("onItemLongClick事件触发了！！！！！！！！！！！！！");
                return false;
            }
        });

        Intent intent = getIntent();
        strPhoneNumber = intent.getStringExtra("phoneNum");
        strProvince = intent.getStringExtra("province");
        strCity = intent.getStringExtra("city");
        strArea = intent.getStringExtra("area");
        strDate = intent.getStringExtra("date");

        getCarStopDetail();


    }

    //主要功能为上下文菜单添加菜单项
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        menu.add(0, 0, Menu.NONE, "租入此车位");
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    //上下文菜单的item点击事件
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final PublishDetail publishDetail = (PublishDetail) listView.getAdapter().getItem(flag);

        if (item.getItemId() == 0) {
            new AlertDialog.Builder(context).setMessage("您将要租入此车位，点击\"继续\"将会付款，是否继续？")
                    .setPositiveButton("继续", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (StaticValues.hasLogin) {
                                // TODO: 2017/4/14 跳转到付款


                                JsonObject object = new JsonObject();
                                object.addProperty("Flag", "rentIn");
                                object.addProperty("phoneNum", strPhoneNumber);
                                object.addProperty("master", publishDetail.getPhoneNum());
                                object.addProperty("province", publishDetail.getProvince());
                                object.addProperty("city", publishDetail.getCity());
                                object.addProperty("area", publishDetail.getArea());
                                object.addProperty("detailPos", publishDetail.getDetailPos());
                                object.addProperty("beginDate", publishDetail.getBeginDate());
                                object.addProperty("endDate", publishDetail.getEndDate());
//                                object.addProperty("duration",publishDetail.getDuration());
                                object.addProperty("publishDate", publishDetail.getPublishDate());
                                object.addProperty("money", publishDetail.getMoney());
                                object.addProperty("carPhone", publishDetail.getCarPhone());
                                new MyNormalThread(context, object.toString(), handler).start();


                            } else {
                                Intent intent = new Intent(context, Switch_To_LoginActivity.class);
                                intent.putExtra("from", "myCount");
                                startActivityForResult(intent, 0);
                            }


                        }
                    }).setNegativeButton("取消", null).show();
        }


        return super.onContextItemSelected(item);
    }

    @Override
    protected void onResume() {
        getCarStopDetail();
        super.onResume();
    }

    private void getCarStopDetail() {
        JsonObject object = new JsonObject();
        object.addProperty("Flag", "getCarStopUserDetailPosAndDate");
        object.addProperty("province", strProvince);
        object.addProperty("city", strCity);
        object.addProperty("area", strArea);
        object.addProperty("date", strDate);
        new MyNormalThread(context, object.toString(), handler).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0) {
            strPhoneNumber = data.getStringExtra("phoneNumber");
        }
    }

    @Override
    public void onBackPressed() {
        if (StaticValues.hasLogin) {
            Intent intent = getIntent();
            intent.putExtra("phoneNum", strPhoneNumber);
            setResult(0, intent);
        }else{

        }
        super.onBackPressed();
    }
}
