package com.example.administrator.navigationbar;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.adapter.MyArrayAdapter;
import com.example.administrator.staticValues.StaticValues;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by 高信朋 on 2017/3/22.
 */

public class ChoosePositionFragment extends Fragment {
    private Spinner provinceSpinner = null;
    private Spinner citySpinner = null;
    private Spinner areaSpinner = null;
    private Spinner straightAreaSpinner = null;
    private Spinner straightCitySpinner = null;

    private LinearLayout normalLinerLayout = null;
    private LinearLayout straightProvinceLinearLayout = null;
    private LinearLayout chooseDateLinearLayout = null;

    private TextView chooseDateTextView = null;
    private TextView todayDateTextView = null;
    private Button selectCarStopButton = null;

    private ArrayAdapter<String> adapter;


    private String strPhoneNumber = "";
    private String strProvince = "";
    private String strCity = "";
    private String strArea = "";
    private String strDate = "";

    String[] array = new String[]{};
    private Calendar cal = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {


        final View view = inflater.inflate(R.layout.fragment_main_choose_position, container, false);

        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        strDate = format.format(today);

        provinceSpinner = (Spinner) view.findViewById(R.id.provinceSpinner);
        citySpinner = (Spinner) view.findViewById(R.id.citySpinner);
        areaSpinner = (Spinner) view.findViewById(R.id.areaSpinner);
        straightCitySpinner = (Spinner) view.findViewById(R.id.straightCitySpinner);
        straightAreaSpinner = (Spinner) view.findViewById(R.id.straightAreaSpinner);
        normalLinerLayout = (LinearLayout) view.findViewById(R.id.normalProvinceLinearLayout);
        straightProvinceLinearLayout = (LinearLayout) view.findViewById(R.id.straightProvinceLinearLayout);
        chooseDateLinearLayout = (LinearLayout) view.findViewById(R.id.chooseDateLinearLayout);
        chooseDateTextView = (TextView) view.findViewById(R.id.chooseDateTextView);
        todayDateTextView = (TextView) view.findViewById(R.id.todayDateTextView);
        selectCarStopButton = (Button) view.findViewById(R.id.selectCarStopButton);

        array = getResources().getStringArray(R.array.省份);
        System.out.println(array[0]);
        adapter = new MyArrayAdapter<String>(this.getActivity().getApplicationContext(), R.layout.spinner_text_item, array);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        provinceSpinner.setAdapter(adapter);
        straightCitySpinner.setAdapter(adapter);
        array = getResources().getStringArray(R.array.默认市);
        adapter = new MyArrayAdapter<String>(this.getActivity().getApplicationContext(), R.layout.spinner_text_item, array);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        citySpinner.setAdapter(adapter);
        citySpinner.setEnabled(false);

        array = getResources().getStringArray(R.array.默认区县);
        adapter = new MyArrayAdapter<String>(this.getActivity().getApplicationContext(), R.layout.spinner_text_item, array);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        areaSpinner.setAdapter(adapter);
        straightAreaSpinner.setAdapter(adapter);
        areaSpinner.setEnabled(false);


        //设置初始显示日期为系统日期
        cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        chooseDateTextView.setText((cal.get(Calendar.MONTH) + 1) + "月" + cal.get(Calendar.DAY_OF_MONTH) + "日");
        System.out.println((cal.get(Calendar.MONTH) + 1) + "月" + Calendar.DAY_OF_MONTH + "日");


        //初始化城市，根据所选省/直辖市决定
        //初始化区县，根据所选市/直辖市决定
        initCity();


        chooseDateLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                //这里的红线虽然提示有错误 ，但实际不影响运行，可以忽略，设置主题为黑色
                DatePickerDialog dialog = new DatePickerDialog(view.getContext(), AlertDialog.THEME_HOLO_DARK, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        //设置选择的日期， month 的范围是 0~11
                        strDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                        chooseDateTextView.setText((month + 1) + "月" + dayOfMonth + "日");
                        //如果选择的时间就是今天
                        if (cal.get(Calendar.YEAR) == year && cal.get(Calendar.MONTH) == month && cal.get(Calendar.DAY_OF_MONTH) == dayOfMonth) {
                            todayDateTextView.setText("今天");
                        } else {
                            Calendar calendar = Calendar.getInstance();
                            Date date = new Date(year - 1900, month, dayOfMonth);
                            SimpleDateFormat format = new SimpleDateFormat("E");
                            todayDateTextView.setText(format.format(date));
                        }

                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });

        //// : 2017/4/2 点击查询按钮可以进行查询
        selectCarStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //查询指定地点指定时间的车位信息
                getCarStopUserDetailPosAndDate();

            }
        });


        return view;

    }

    // : 2017/4/2 点击查询按钮可以进行查询
    //查询指定地点指定时间的车位信息
    private void getCarStopUserDetailPosAndDate() {
        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        View heardLayout = navigationView.getHeaderView(0);
        TextView phoneNumber = (TextView) heardLayout.findViewById(R.id.phoneNumberTextView_navigationBarHeader);
        strPhoneNumber = phoneNumber.getText().toString();


        if (normalLinerLayout.getVisibility() == LinearLayout.VISIBLE) {
            strProvince = provinceSpinner.getSelectedItem().toString();
            strCity = citySpinner.getSelectedItem().toString();
            strArea = areaSpinner.getSelectedItem().toString();
        } else if (straightProvinceLinearLayout.getVisibility() == LinearLayout.VISIBLE) {
            strProvince = straightCitySpinner.getSelectedItem().toString();
            strCity = "";
            strArea = straightAreaSpinner.getSelectedItem().toString();
        }
        if (strProvince.equals("（省/直辖市）")||strCity.equals("（市）")||strArea.equals("（区/县）")){
            Toast.makeText(getActivity().getBaseContext(),"请选择地址",Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(getActivity().getBaseContext(), ShowDetailCarStopActivity.class);
        intent.putExtra("phoneNum", strPhoneNumber);
        intent.putExtra("province", strProvince);
        intent.putExtra("city", strCity);
        intent.putExtra("area", strArea);
        intent.putExtra("date", strDate);

        startActivityForResult(intent, 0);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 0) {
            if (StaticValues.hasLogin) {

                NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
                View heardLayout = navigationView.getHeaderView(0);
                TextView phoneNumber = (TextView) heardLayout.findViewById(R.id.phoneNumberTextView_navigationBarHeader);
                phoneNumber.setText(data.getStringExtra("phoneNum"));
                TextView textView_login_accout = (TextView) heardLayout.findViewById(R.id.textView_log_account);
                textView_login_accout.setVisibility(TextView.INVISIBLE);
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    //选择的值
    String value = "";

    //初始化城市，根据所选省/直辖市决定
    //初始化区县，根据所选市/直辖市决定
    private void initCity() {
        provinceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //获取省份选择的值
                value = (String) provinceSpinner.getSelectedItem();
                if (provinceSpinner.getSelectedItemPosition() != 0) {
                    citySpinner.setEnabled(true);

                    //如果选择的省份是 直辖市 或者 特别行政区 或者 台湾
                    if ("北京".equals(value) || "上海".equals(value) || "天津".equals(value) || "重庆".equals(value) || "香港特别行政区".equals(value) || "澳门特别行政区".equals(value) || "台湾".equals(value)) {
                        normalLinerLayout.setVisibility(LinearLayout.INVISIBLE);
                        straightProvinceLinearLayout.setVisibility(LinearLayout.VISIBLE);
                        straightCitySpinner.setSelection(provinceSpinner.getSelectedItemPosition());
                        straightAreaSpinner.setEnabled(true);

                        switch (provinceSpinner.getSelectedItemPosition()) {
                            case 1:
                                array = getResources().getStringArray(R.array.北京);
                                break;
                            case 2:
                                array = getResources().getStringArray(R.array.天津);
                                break;
                            case 3:
                                array = getResources().getStringArray(R.array.上海);
                                break;
                            case 4:
                                array = getResources().getStringArray(R.array.重庆);
                                break;
                            case 32:
                                array = getResources().getStringArray(R.array.香港特别行政区);
                                break;
                            case 33:
                                array = getResources().getStringArray(R.array.澳门特别行政区);
                                break;
                            case 34:
                                array = getResources().getStringArray(R.array.台湾);
                                break;
                            default:
                        }
                        adapter = new MyArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.spinner_text_item, array);
                        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                        straightAreaSpinner.setAdapter(adapter);

                    }
                    //如果选择的省份是一般省份
                    else {
                        switch (provinceSpinner.getSelectedItemPosition()) {
                            case 5:
                                array = getResources().getStringArray(R.array.安徽);
                                break;
                            case 6:
                                array = getResources().getStringArray(R.array.福建);
                                break;
                            case 7:
                                array = getResources().getStringArray(R.array.甘肃);
                                break;
                            case 8:
                                array = getResources().getStringArray(R.array.广东);
                                break;
                            case 9:
                                array = getResources().getStringArray(R.array.广西壮族自治区);
                                break;
                            case 10:
                                array = getResources().getStringArray(R.array.贵州);
                                break;
                            case 11:
                                array = getResources().getStringArray(R.array.海南);
                                break;
                            case 12:
                                array = getResources().getStringArray(R.array.河北);
                                break;
                            case 13:
                                array = getResources().getStringArray(R.array.黑龙江);
                                break;
                            case 14:
                                array = getResources().getStringArray(R.array.河南);
                                break;
                            case 15:
                                array = getResources().getStringArray(R.array.湖北);
                                break;
                            case 16:
                                array = getResources().getStringArray(R.array.湖南);
                                break;
                            case 17:
                                array = getResources().getStringArray(R.array.江苏);
                                break;
                            case 18:
                                array = getResources().getStringArray(R.array.江西);
                                break;
                            case 19:
                                array = getResources().getStringArray(R.array.吉林);
                                break;
                            case 20:
                                array = getResources().getStringArray(R.array.辽宁);
                                break;
                            case 21:
                                array = getResources().getStringArray(R.array.内蒙古自治区);
                                break;
                            case 22:
                                array = getResources().getStringArray(R.array.宁夏回族自治区);
                                break;
                            case 23:
                                array = getResources().getStringArray(R.array.青海);
                                break;
                            case 24:
                                array = getResources().getStringArray(R.array.陕西);
                                break;
                            case 25:
                                array = getResources().getStringArray(R.array.山东);
                                break;
                            case 26:
                                array = getResources().getStringArray(R.array.山西);
                                break;
                            case 27:
                                array = getResources().getStringArray(R.array.四川);
                                break;
                            case 28:
                                array = getResources().getStringArray(R.array.新疆维吾尔族自治区);
                                break;
                            case 29:
                                array = getResources().getStringArray(R.array.西藏自治区);
                                break;
                            case 30:
                                array = getResources().getStringArray(R.array.云南);
                                break;
                            case 31:
                                array = getResources().getStringArray(R.array.浙江);
                                break;
                            default:
                        }

                        //为 区县 设置适配器
                        adapter = new MyArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.spinner_text_item, array);
                        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                        citySpinner.setAdapter(adapter);


                    }
                } else {
                    array = getResources().getStringArray(R.array.默认市);
                    adapter = new MyArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.spinner_text_item, array);
                    adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                    citySpinner.setAdapter(adapter);
                    citySpinner.setEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });
        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (citySpinner.getSelectedItemPosition() != 0) {
                    areaSpinner.setEnabled(true);
                    switch (provinceSpinner.getSelectedItemPosition()) {
                        case 5:
                            switch (citySpinner.getSelectedItemPosition()) {
                                case 1:
                                    array = getResources().getStringArray(R.array.合肥);
                                    break;
                            }
                            break;

                        case 6:
                            switch (citySpinner.getSelectedItemPosition()) {
                                case 1:
                                    array = getResources().getStringArray(R.array.福州);
                                    break;
                            }
                            break;
                        case 7:
                            switch (citySpinner.getSelectedItemPosition()) {
                                case 1:
                                    array = getResources().getStringArray(R.array.兰州);
                                    break;
                            }
                            break;

                        case 8:
                            switch (citySpinner.getSelectedItemPosition()) {
                                case 1:
                                    array = getResources().getStringArray(R.array.广州);
                                    break;
                            }
                            break;

                        case 9:
                            switch (citySpinner.getSelectedItemPosition()) {
                                case 1:
                                    array = getResources().getStringArray(R.array.南宁);
                                    break;
                            }
                            break;

                        case 10:
                            switch (citySpinner.getSelectedItemPosition()) {
                                case 1:
                                    array = getResources().getStringArray(R.array.贵阳);
                                    break;
                            }
                            break;

                        case 11:
                            switch (citySpinner.getSelectedItemPosition()) {
                                case 1:
                                    array = getResources().getStringArray(R.array.海口);
                                    break;
                            }
                            break;

                        case 12:
                            switch (citySpinner.getSelectedItemPosition()) {
                                case 1:
                                    array = getResources().getStringArray(R.array.石家庄);
                                    break;
                                case 2:
                                    array = getResources().getStringArray(R.array.唐山);
                                    break;
                                case 3:
                                    array = getResources().getStringArray(R.array.秦皇岛);
                                    break;
                                case 4:
                                    array = getResources().getStringArray(R.array.邯郸);
                                    break;
                                case 5:
                                    array = getResources().getStringArray(R.array.邢台);
                                    break;
                                case 6:
                                    array = getResources().getStringArray(R.array.保定);
                                    break;
                                case 7:
                                    array = getResources().getStringArray(R.array.张家口);
                                    break;
                                case 8:
                                    array = getResources().getStringArray(R.array.承德);
                                    break;
                                case 9:
                                    array = getResources().getStringArray(R.array.廊坊);
                                    break;
                                case 10:
                                    array = getResources().getStringArray(R.array.衡水);
                                    break;
                                case 11:
                                    array = getResources().getStringArray(R.array.沧州);
                                    break;
                            }
                            break;

                        case 13:
                            switch (citySpinner.getSelectedItemPosition()) {
                                case 1:
                                    array = getResources().getStringArray(R.array.哈尔滨);
                                    break;
                            }
                            break;

                        case 14:
                            switch (citySpinner.getSelectedItemPosition()) {
                                case 1:
                                    array = getResources().getStringArray(R.array.郑州);
                                    break;
                            }
                            break;

                        case 15:
                            switch (citySpinner.getSelectedItemPosition()) {
                                case 1:
                                    array = getResources().getStringArray(R.array.武汉);
                                    break;
                            }
                            break;

                        case 16:
                            switch (citySpinner.getSelectedItemPosition()) {
                                case 1:
                                    array = getResources().getStringArray(R.array.长沙);
                                    break;
                            }
                            break;

                        case 17:
                            switch (citySpinner.getSelectedItemPosition()) {
                                case 1:
                                    array = getResources().getStringArray(R.array.南京);
                                    break;
                            }
                            break;

                        case 18:
                            switch (citySpinner.getSelectedItemPosition()) {
                                case 1:
                                    array = getResources().getStringArray(R.array.南昌);
                                    break;
                            }
                            break;

                        case 19:
                            switch (citySpinner.getSelectedItemPosition()) {
                                case 1:
                                    array = getResources().getStringArray(R.array.长春);
                                    break;
                            }
                            break;

                        case 20:
                            switch (citySpinner.getSelectedItemPosition()) {
                                case 1:
                                    array = getResources().getStringArray(R.array.沈阳);
                                    break;
                            }
                            break;

                        case 21:

                            switch (citySpinner.getSelectedItemPosition()) {
                                case 1:
                                    array = getResources().getStringArray(R.array.呼和浩特);
                                    break;
                            }
                            break;

                        case 22:
                            switch (citySpinner.getSelectedItemPosition()) {
                                case 1:
                                    array = getResources().getStringArray(R.array.银川);
                                    break;
                            }
                            break;

                        case 23:
                            switch (citySpinner.getSelectedItemPosition()) {
                                case 1:
                                    array = getResources().getStringArray(R.array.西宁);
                                    break;
                            }
                            break;

                        case 24:
                            switch (citySpinner.getSelectedItemPosition()) {
                                case 1:
                                    array = getResources().getStringArray(R.array.西安);
                                    break;
                            }
                            break;

                        case 25:
                            switch (citySpinner.getSelectedItemPosition()) {
                                case 1:
                                    array = getResources().getStringArray(R.array.济南);
                                    break;
                            }
                            break;

                        case 26:
                            switch (citySpinner.getSelectedItemPosition()) {
                                case 1:
                                    array = getResources().getStringArray(R.array.太原);
                                    break;
                            }
                            break;

                        case 27:
                            switch (citySpinner.getSelectedItemPosition()) {
                                case 1:
                                    array = getResources().getStringArray(R.array.成都);
                                    break;
                            }
                            break;

                        case 28:
                            switch (citySpinner.getSelectedItemPosition()) {
                                case 1:
                                    array = getResources().getStringArray(R.array.乌鲁木齐);
                                    break;
                            }
                            break;

                        case 29:
                            switch (citySpinner.getSelectedItemPosition()) {
                                case 1:
                                    array = getResources().getStringArray(R.array.拉萨);
                                    break;
                            }
                            break;

                        case 30:
                            switch (citySpinner.getSelectedItemPosition()) {
                                case 1:
                                    array = getResources().getStringArray(R.array.昆明);
                                    break;
                            }
                            break;

                        case 31:
                            switch (citySpinner.getSelectedItemPosition()) {
                                case 1:
                                    array = getResources().getStringArray(R.array.杭州);
                                    break;
                            }
                            break;
                    }
                    adapter = new MyArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.spinner_text_item, array);
                    adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                    areaSpinner.setAdapter(adapter);
                } else {
                    array = getResources().getStringArray(R.array.默认区县);
                    adapter = new MyArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.spinner_text_item, array);
                    adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                    areaSpinner.setAdapter(adapter);
                    areaSpinner.setEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });
        straightCitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //获取省份选择的值
                value = (String) straightCitySpinner.getSelectedItem();
                if (straightCitySpinner.getSelectedItemPosition() != 0) {
                    straightAreaSpinner.setEnabled(true);

                    //选择了 直辖市 或者 特别行政区 或者 台湾
                    if ("北京".equals(value) || "上海".equals(value) || "天津".equals(value) || "重庆".equals(value) || "香港特别行政区".equals(value) || "澳门特别行政区".equals(value) || "台湾".equals(value)) {
                        System.out.println("选择了特殊省份~~~~~~~~~~~~~~");
                        switch (straightCitySpinner.getSelectedItemPosition()) {
                            case 1:
                                array = getResources().getStringArray(R.array.北京);
                                break;
                            case 2:
                                array = getResources().getStringArray(R.array.天津);
                                break;
                            case 3:
                                array = getResources().getStringArray(R.array.上海);
                                break;
                            case 4:
                                array = getResources().getStringArray(R.array.重庆);
                                break;
                            case 32:
                                array = getResources().getStringArray(R.array.香港特别行政区);
                                break;
                            case 33:
                                array = getResources().getStringArray(R.array.澳门特别行政区);
                                break;
                            case 34:
                                array = getResources().getStringArray(R.array.台湾);
                                break;
                            default:
                        }
                        adapter = new MyArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.spinner_text_item, array);
                        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                        straightAreaSpinner.setAdapter(adapter);
                    }
                    //选择了一般的省份
                    else {
                        System.out.println("选择了一般省份~~~~~~~~~~~~~~");
                        normalLinerLayout.setVisibility(LinearLayout.VISIBLE);
                        straightProvinceLinearLayout.setVisibility(LinearLayout.INVISIBLE);
                        provinceSpinner.setSelection(straightCitySpinner.getSelectedItemPosition());
                        citySpinner.setEnabled(true);

                        switch (straightCitySpinner.getSelectedItemPosition()) {
                            case 5:
                                array = getResources().getStringArray(R.array.安徽);
                                break;
                            case 6:
                                array = getResources().getStringArray(R.array.福建);
                                break;
                            case 7:
                                array = getResources().getStringArray(R.array.甘肃);
                                break;
                            case 8:
                                array = getResources().getStringArray(R.array.广东);
                                break;
                            case 9:
                                array = getResources().getStringArray(R.array.广西壮族自治区);
                                break;
                            case 10:
                                array = getResources().getStringArray(R.array.贵州);
                                break;
                            case 11:
                                array = getResources().getStringArray(R.array.海南);
                                break;
                            case 12:
                                array = getResources().getStringArray(R.array.河北);
                                break;
                            case 13:
                                array = getResources().getStringArray(R.array.黑龙江);
                                break;
                            case 14:
                                array = getResources().getStringArray(R.array.河南);
                                break;
                            case 15:
                                array = getResources().getStringArray(R.array.湖北);
                                break;
                            case 16:
                                array = getResources().getStringArray(R.array.湖南);
                                break;
                            case 17:
                                array = getResources().getStringArray(R.array.江苏);
                                break;
                            case 18:
                                array = getResources().getStringArray(R.array.江西);
                                break;
                            case 19:
                                array = getResources().getStringArray(R.array.吉林);
                                break;
                            case 20:
                                array = getResources().getStringArray(R.array.辽宁);
                                break;
                            case 21:
                                array = getResources().getStringArray(R.array.内蒙古自治区);
                                break;
                            case 22:
                                array = getResources().getStringArray(R.array.宁夏回族自治区);
                                break;
                            case 23:
                                array = getResources().getStringArray(R.array.青海);
                                break;
                            case 24:
                                array = getResources().getStringArray(R.array.陕西);
                                break;
                            case 25:
                                array = getResources().getStringArray(R.array.山东);
                                break;
                            case 26:
                                array = getResources().getStringArray(R.array.山西);
                                break;
                            case 27:
                                array = getResources().getStringArray(R.array.四川);
                                break;
                            case 28:
                                array = getResources().getStringArray(R.array.新疆维吾尔族自治区);
                                break;
                            case 29:
                                array = getResources().getStringArray(R.array.西藏自治区);
                                break;
                            case 30:
                                array = getResources().getStringArray(R.array.云南);
                                break;
                            case 31:
                                array = getResources().getStringArray(R.array.浙江);
                                break;
                            default:
                        }

                        //为 区县 设置适配器
                        adapter = new MyArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.spinner_text_item, array);
                        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                        citySpinner.setAdapter(adapter);


                    }


                }
                //选择了第一个
                else {

                    array = getResources().getStringArray(R.array.默认区县);
                    adapter = new MyArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.spinner_text_item, array);
                    adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                    straightAreaSpinner.setAdapter(adapter);
                    straightAreaSpinner.setEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


}
