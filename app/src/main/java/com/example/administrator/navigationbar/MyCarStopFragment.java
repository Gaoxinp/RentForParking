package com.example.administrator.navigationbar;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.telephony.SmsManager;
import android.text.InputType;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.adapter.MyCarStopListViewAdapter;
import com.example.administrator.model.MyCarStop;
import com.example.administrator.staticValues.StaticValues;
import com.example.administrator.thread.MyNormalThread;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by 高信朋 on 2017/4/7.
 */

public class MyCarStopFragment extends Fragment {
    private Calendar cal = null;

    //出租起始日期,月份从0开始
    private int beginYear = 0, beginMonth = 0, beginDay = 0;
    //出租结束日期,月份从0开始
    private int endYear = 0, endMonth = 0, endDay = 0;
    //出租后可以得到的出租费
    private int money = 0;


    private LinearLayout linearlayout = null;
    private ListView listView = null;
    private FloatingActionButton fab = null;
    private LinearLayout noLoginLinearLayout_mycarstop = null;
    private Button tologin = null;

    private TextView phoneNumber = null;
    private TextView textView_log_account = null;

    private MyCarStopListViewAdapter adapter = null;

    //标记，用来判断长按了listview的哪条数据，从0开始
    private int flag = -1;

    //车位信息
    private List<MyCarStop> carStopArray;
    private String strPhoneNumber = "";
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            carStopArray = new ArrayList<>();
            System.out.println(msg.what + "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            if (msg.what == 1) {        //获取的车位列表为非空
                int rows = msg.arg1;
                JsonArray array = (JsonArray) msg.obj;
                JsonObject object = new JsonObject();
                //   将传回的车位信息解析出来
                for (int index = 0; index < array.size(); index++) {
                    object = array.get(index).getAsJsonObject();

                    String a = object.get("rentOutState").getAsString();
                    if ("false".equals(a)) {
                        a = "未出租";
                    } else if ("true".equals(a)) {
                        a = "已出租";
                    }
                    MyCarStop carstop = new MyCarStop(object.get("province").getAsString(), object.get("city").getAsString(), object.get("area").getAsString(), object.get("detailPos").getAsString(), object.get("carPhone").getAsString(), a);
                    carStopArray.add(carstop);
                }
                System.out.println(carStopArray.get(0) + "~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                adapter = new MyCarStopListViewAdapter(carStopArray, getActivity().getBaseContext());
                listView.setAdapter(adapter);
            } else if (msg.what == 0) {        //获取的车位列表为空
//                linearlayout.setVisibility(LinearLayout.INVISIBLE);
//                TextView tv = new TextView(view.getContext());
//                tv.setWidth(noLoginLinearLayout_mycarstop.getWidth());
//                tv.setHeight(noLoginLinearLayout_mycarstop.getHeight());
//                tv.setTop(linearlayout.getTop());
//                tv.setText("没有数据");
//                tv.setTextColor(0xAAAAAA);
//                tv.setTextSize(18);
//                tv.setGravity(Gravity.CENTER);
//
//                RelativeLayout rl = (RelativeLayout) view.findViewById(R.id.relativeLayout_myCarStop);
//                rl.addView(tv);
                System.out.println("msg.what 不是1！！！！！！！！！！！！");
            } else if (msg.what == 2) {     //删除失败
                Toast.makeText(getActivity().getBaseContext(), (String) msg.obj, Toast.LENGTH_SHORT).show();
            } else if (msg.what == 3) {       //删除成功
                Toast.makeText(getActivity().getBaseContext(), (String) msg.obj, Toast.LENGTH_SHORT).show();
                getDealDetail();
            } else if (msg.what == 4) {     //加入出租列表失败
                Toast.makeText(getActivity().getBaseContext(), (String) msg.obj, Toast.LENGTH_SHORT).show();
            } else if (msg.what == 5) {       //加入出租列表成功
                Toast.makeText(getActivity().getBaseContext(), (String) msg.obj, Toast.LENGTH_SHORT).show();
                getDealDetail();
            }else if (msg.what == 6) {     //取消出租失败
                Toast.makeText(getActivity().getBaseContext(), (String) msg.obj, Toast.LENGTH_SHORT).show();
            } else if (msg.what == 7) {       //取消出租成功
                Toast.makeText(getActivity().getBaseContext(), (String) msg.obj, Toast.LENGTH_SHORT).show();
                getDealDetail();
            }


        }
    };


    //    /**
//     * 重写此方法从服务器获取数据，可以保证在fragment执行完onDestroyView()方法后数据没有丢失
//     * @param savedInstanceState
//     */
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        //获取用户车位列表
//        getDealDetail();
//    }
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_car_stop, container, false);
        System.out.println("onCreateView方法执行了！！！！！！！！！！！！！！！！！！！！！");

        linearlayout = (LinearLayout) view.findViewById(R.id.linearlayout_mycarstop);
        noLoginLinearLayout_mycarstop = (LinearLayout) view.findViewById(R.id.noLoginLinearLayout_mycarstop);
        tologin = (Button) view.findViewById(R.id.toLoginButton_mycarstop);
        listView = (ListView) view.findViewById(R.id.listView_mycarstop);
        fab = (FloatingActionButton) view.findViewById(R.id.fab_myCarStop);

        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        View heardLayout = navigationView.getHeaderView(0);
        phoneNumber = (TextView) heardLayout.findViewById(R.id.phoneNumberTextView_navigationBarHeader);
        textView_log_account = (TextView) heardLayout.findViewById(R.id.textView_log_account);
        strPhoneNumber = phoneNumber.getText().toString();

        if (!StaticValues.hasLogin) {
            linearlayout.setVisibility(LinearLayout.INVISIBLE);
            noLoginLinearLayout_mycarstop.setVisibility(LinearLayout.VISIBLE);
            listView.setVisibility(ListView.INVISIBLE);
            fab.setVisibility(FloatingActionButton.INVISIBLE);

        } else {
            linearlayout.setVisibility(LinearLayout.VISIBLE);
            noLoginLinearLayout_mycarstop.setVisibility(LinearLayout.INVISIBLE);
            listView.setVisibility(ListView.VISIBLE);
            fab.setVisibility(FloatingActionButton.VISIBLE);

            //从服务器获取数居
            getDealDetail();


        }
        tologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), Switch_To_LoginActivity.class);
                intent.putExtra("from", "myCount");
                startActivityForResult(intent, 0);

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getBaseContext(), NewCarStopActivity.class);
                System.out.println(strPhoneNumber);
                intent.putExtra("phone", strPhoneNumber);
                startActivity(intent);
            }
        });

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

        return view;
    }


    //主要功能为上下文菜单添加菜单项
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MyCarStop carStop = (MyCarStop) listView.getAdapter().getItem(flag);
        if ("未出租".equals(carStop.getState())) {
            //为上下文菜单添加菜单项(未出租item
            menu.add(flag, 0, Menu.NONE, "出租该车位");
            menu.add(flag, 1, Menu.NONE, "删除该车位");
            menu.add(flag, 2, Menu.NONE, "开启该车位");
        } else {
            //为上下文菜单添加菜单项(已出租item
            menu.add(flag, 0, Menu.NONE, "取消出租该车位");
            menu.add(flag, 1, Menu.NONE, "删除该车位");


        }


        super.onCreateContextMenu(menu, v, menuInfo);
    }

    //上下文菜单的item点击事件
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        final MyCarStop carStop = (MyCarStop) listView.getAdapter().getItem(flag);

        // : 2017/4/12  选中上下文菜单的操作
        switch (item.getItemId()) {
            case 0:             //出租该项 或者 取消出租该项
                if ("未出租".equals(carStop.getState())) {     //出租该项
                    getRentOutBeginDate(carStop);
                } else if ("已出租".equals(carStop.getState())) {       // 取消出租该项
                    JsonObject object = new JsonObject();
                    object.addProperty("Flag", "cancelRentOut");
                    object.addProperty("phoneNum", strPhoneNumber);
                    object.addProperty("province", carStop.getProvince());
                    object.addProperty("city", carStop.getCity());
                    object.addProperty("area", carStop.getArea());
                    object.addProperty("detailPos", carStop.getDetailPos());
                    new MyNormalThread(getActivity().getBaseContext(), object.toString(), handler).start();


                }
                break;
            case 1:             //删除该项
                JsonObject object = new JsonObject();
                object.addProperty("Flag", "deleteCarStop");
                object.addProperty("phoneNum", strPhoneNumber);
                object.addProperty("province", carStop.getProvince());
                object.addProperty("city", carStop.getCity());
                object.addProperty("area", carStop.getArea());
                object.addProperty("detailPos", carStop.getDetailPos());
                new MyNormalThread(getActivity().getBaseContext(), object.toString(), handler).start();

                break;
            case 2:             //开启车位锁
                new AlertDialog.Builder(view.getContext()).setTitle("提示")
                        .setMessage("将发送一条短信息，以请求开锁，是否继续？（会花费基础短信费）")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //不调用系统短信界面直接发短信
                                SmsManager manager = SmsManager.getDefault();
                                manager.sendTextMessage(carStop.getCarPhone(), null, "usr.cnAT+SOCKAEN=ON", null, null);
                            }
                        }).setNegativeButton("取消", null).show();

                break;
        }

        System.out.println(carStop.getProvince() + carStop.getCity() + carStop.getArea() + carStop.getDetailPos() + "~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println(item.getItemId() + "~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println(item.getGroupId() + "~~~~~~~~~~~~~~~~~~~~~~~");
        return super.onContextItemSelected(item);
    }

    private void cancelRentOut(MyCarStop carStop) {
        JsonObject object = new JsonObject();
        object.addProperty("Flag", "cancelRentOut");
        object.addProperty("phoneNum", strPhoneNumber);
        object.addProperty("province", carStop.getProvince());
        object.addProperty("city", carStop.getCity());
        object.addProperty("area", carStop.getArea());
        object.addProperty("detailPos", carStop.getDetailPos());
        new MyNormalThread(view.getContext(), object.toString(), handler).start();
    }

    //  2017/4/12 获取用户想要出租车位的信息
    private void getRentOutBeginDate(final MyCarStop carStop) {
        cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        //这里的红线虽然提示有错误 ，但实际不影响运行，可以忽略，设置主题为黑色
        final DatePickerDialog dialog = new DatePickerDialog(view.getContext(), android.app.AlertDialog.THEME_HOLO_DARK,
                null, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        dialog.setTitle("选择出租车位的起始日期");
        dialog.setButton(DatePickerDialog.BUTTON_POSITIVE, "完成", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog1, int which) {
                //通过mDialog.getDatePicker()获得dialog上的DatePicker组件，然后可以获取日期信息
                DatePicker datePicker = dialog.getDatePicker();
                int year = datePicker.getYear();
                int month = datePicker.getMonth();
                int dayOfMonth = datePicker.getDayOfMonth();
                if (year > cal.get(Calendar.YEAR)) {
                    beginYear = year;
                    beginMonth = month;
                    beginDay = dayOfMonth;
                    getRentOutEndDate(carStop);
                } else if (year < cal.get(Calendar.YEAR)) {
                    Toast.makeText(getActivity().getBaseContext(), "请选择未来的某一日期", Toast.LENGTH_SHORT).show();
                } else if (year == cal.get(Calendar.YEAR)) {
                    if (month > cal.get(Calendar.MONTH)) {
                        beginYear = year;
                        beginMonth = month;
                        beginDay = dayOfMonth;
                        getRentOutEndDate(carStop);
                    } else if (month < cal.get(Calendar.MONTH)) {
                        Toast.makeText(getActivity().getBaseContext(), "请选择未来的某一日期", Toast.LENGTH_SHORT).show();
                    } else if (month == cal.get(Calendar.MONTH)) {
                        if (dayOfMonth > cal.get(Calendar.DAY_OF_MONTH)) {
                            beginYear = year;
                            beginMonth = month;
                            beginDay = dayOfMonth;
                            getRentOutEndDate(carStop);
                        } else {
                            Toast.makeText(getActivity().getBaseContext(), "出租起始日期最近从明天开始", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
        dialog.setButton(DatePickerDialog.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog.show();
    }

    private void getRentOutEndDate(final MyCarStop carStop) {
        //这里的红线虽然提示有错误 ，但实际不影响运行，可以忽略，设置主题为黑色
        final DatePickerDialog dialog = new DatePickerDialog(view.getContext(), android.app.AlertDialog.THEME_HOLO_DARK,
                null, beginYear, beginMonth, beginDay);
        dialog.setTitle("选择出租车位的结束日期");
        dialog.setButton(DatePickerDialog.BUTTON_POSITIVE, "完成", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog1, int which) {
                //通过mDialog.getDatePicker()获得dialog上的DatePicker组件，然后可以获取日期信息
                DatePicker datePicker = dialog.getDatePicker();
                int year = datePicker.getYear();
                int month = datePicker.getMonth();
                int dayOfMonth = datePicker.getDayOfMonth();
                if (year > beginYear) {
                    endYear = year;
                    endMonth = month;
                    endDay = dayOfMonth;
                    getMoney(carStop);
                } else if (year < beginYear) {
                    Toast.makeText(getActivity().getBaseContext(), "请选择未来的某一日期", Toast.LENGTH_SHORT).show();
                } else if (year == beginYear) {
                    if (month > beginMonth) {
                        endYear = year;
                        endMonth = month;
                        endDay = dayOfMonth;
                        getMoney(carStop);
                    } else if (month < beginMonth) {
                        Toast.makeText(getActivity().getBaseContext(), "请选择未来的某一日期", Toast.LENGTH_SHORT).show();
                    } else if (month == beginMonth) {
                        if (dayOfMonth > beginDay) {
                            endYear = year;
                            endMonth = month;
                            endDay = dayOfMonth;
                            getMoney(carStop);
                        } else {
                            Toast.makeText(getActivity().getBaseContext(), "出租起始日期最近从明天开始", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
        dialog.setButton(DatePickerDialog.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog.show();
    }

    private void getMoney(final MyCarStop carStop) {
        final EditText editText = new EditText(view.getContext());
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        new AlertDialog.Builder(view.getContext()).setTitle("请输入总计租费").setView(editText).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!"".equals(editText.getText().toString())) {
                    money = Integer.parseInt(editText.getText().toString());

                    //将当前系统日期转换为“yyyy-MM-dd”格式
                    Date date = new Date(cal.get(Calendar.YEAR) - 1900, cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");


                    // : 2017/4/13 将出租信息传给服务器
                    JsonObject object = new JsonObject();
                    object.addProperty("Flag", "rentout_publishing");
                    object.addProperty("phoneNum", strPhoneNumber);
                    object.addProperty("province", carStop.getProvince());
                    object.addProperty("city", carStop.getCity());
                    object.addProperty("area", carStop.getArea());
                    object.addProperty("detailPos", carStop.getDetailPos());
                    object.addProperty("carPhone", carStop.getCarPhone());
                    object.addProperty("publishDate", format.format(date));
                    object.addProperty("beginDate", beginYear + "-" + (beginMonth + 1) + "-" + beginDay);
                    object.addProperty("endDate", endYear + "-" + (endMonth + 1) + "-" + endDay);
                    object.addProperty("money", "" + money);
                    new MyNormalThread(view.getContext(), object.toString(), handler).start();
                } else {
                    Toast.makeText(getActivity().getBaseContext(), "输入内容不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        }).setNegativeButton("取消", null).show();
    }


    //  根据用户手机号从服务器获取用户车位列表
    private void getDealDetail() {
        System.out.println("getDealDetail()方法执行了！！！！！！！！！！！！");
//        carStopArray = new ArrayList<>();
        JsonObject object = new JsonObject();
        object.addProperty("Flag", "selectCarStop");
        object.addProperty("PhoneNum", strPhoneNumber);
        new MyNormalThread(getActivity().getBaseContext(), object.toString(), handler).start();
    }

    @Override
    public void onResume() {
        System.out.println("onResume()方法执行了！！！！！！！！！！！！");
        if (StaticValues.hasLogin == false) {
            linearlayout.setVisibility(LinearLayout.INVISIBLE);
            noLoginLinearLayout_mycarstop.setVisibility(LinearLayout.VISIBLE);
            listView.setVisibility(ListView.INVISIBLE);
            fab.setVisibility(FloatingActionButton.INVISIBLE);

        } else {
            linearlayout.setVisibility(LinearLayout.VISIBLE);
            noLoginLinearLayout_mycarstop.setVisibility(LinearLayout.INVISIBLE);
            listView.setVisibility(ListView.VISIBLE);
            fab.setVisibility(FloatingActionButton.VISIBLE);
            //获取用户车位列表
            getDealDetail();

        }
        super.onResume();
    }

    //从本页面登陆后，需要将用户的手机号填写在指定位置
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 0) {
            strPhoneNumber = data.getStringExtra("phoneNumber");
            System.out.println("onActivityResult方法执行了！！！！！！！！！！！！！！！！！！！！！");
            System.out.println(strPhoneNumber + "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            textView_log_account.setVisibility(TextView.INVISIBLE);
            phoneNumber.setText(strPhoneNumber);
        }
    }
}
