package com.example.administrator.navigationbar;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.adapter.MyArrayAdapter;
import com.example.administrator.thread.MyNormalThread;
import com.google.gson.JsonObject;

public class NewCarStopActivity extends AppCompatActivity {

    private Context context = NewCarStopActivity.this;

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
    private Button newCarStopButton_normal = null;
    private Button newCarStopButton_straight = null;
    private EditText detailPosEditText_normal = null;
    private EditText detailPosEditText_straight = null;
    private EditText carPhone_normal = null;
    private EditText carPhone_straight = null;
    private View line_normal = null;
    private View line_straight = null;

    private ArrayAdapter<String> adapter;


    private String province = "";
    private String city = "";
    private String area = "";
    private String straightCity = "";
    private String straightArea = "";

    String[] array = new String[]{};
    private String strPhoneNum = "";
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            String mag = (String) msg.obj;
            switch (mag){
                case "newCarStop successfully":
                    Toast.makeText(context,"绑定成功",Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case "newCarStop failed":
                    Toast.makeText(context,"绑定失败，请稍后再试",Toast.LENGTH_SHORT).show();
                    break;
            }



        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_car_stop);

        strPhoneNum = getIntent().getStringExtra("phone");

        provinceSpinner = (Spinner)findViewById(R.id.provinceSpinner_newCarStop);
        citySpinner = (Spinner)findViewById(R.id.citySpinner_newCarStop);
        areaSpinner = (Spinner)findViewById(R.id.areaSpinner_newCarStop);
        straightCitySpinner = (Spinner)findViewById(R.id.straightCitySpinner_newCarStop);
        straightAreaSpinner = (Spinner)findViewById(R.id.straightAreaSpinner_newCarStop);
        normalLinerLayout = (LinearLayout)findViewById(R.id.normalProvinceLinearLayout_newCarStop);
        straightProvinceLinearLayout = (LinearLayout)findViewById(R.id.straightProvinceLinearLayout_newCarStop);
        newCarStopButton_normal = (Button)findViewById(R.id.selectCarStopButton_newCarStop_normal);
        newCarStopButton_straight = (Button)findViewById(R.id.selectCarStopButton2_newCarStop_straight);
        detailPosEditText_normal = (EditText) findViewById(R.id.detailPosEditText_newcarstop_normal);
        detailPosEditText_straight = (EditText) findViewById(R.id.detailPosEditText_newcarstop_straight);
        carPhone_normal = (EditText) findViewById(R.id.carPhone_newcarstop_normal);
        carPhone_straight = (EditText) findViewById(R.id.carPhone_newcarstop_straight);
        line_normal = findViewById(R.id.line_normal);
        line_straight = findViewById(R.id.line_straight);

        newCarStopButton_straight.setVisibility(Button.INVISIBLE);
        detailPosEditText_straight.setVisibility(EditText.INVISIBLE);
        line_straight.setVisibility(View.INVISIBLE);
//        detailPosEditText_normal.clearFocus();

        newCarStopButton_normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JsonObject object = new JsonObject();
                object.addProperty("Flag","newCarStop");
                object.addProperty("PhoneNum",strPhoneNum);
                object.addProperty("Province",(String)provinceSpinner.getSelectedItem());
                object.addProperty("City",(String)citySpinner.getSelectedItem());
                object.addProperty("Area",(String)areaSpinner.getSelectedItem());
                object.addProperty("DetailPos",detailPosEditText_normal.getText().toString());
                object.addProperty("CarPhone",carPhone_normal.getText().toString());
                new MyNormalThread(context,object.toString(),handler).start();
            }
        });
        newCarStopButton_straight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JsonObject object = new JsonObject();
                object.addProperty("Flag","newCarStop");
                object.addProperty("PhoneNum",strPhoneNum);
                object.addProperty("Province",(String)straightCitySpinner.getSelectedItem());
                object.addProperty("City","");
                object.addProperty("Area",(String)straightAreaSpinner.getSelectedItem());
                object.addProperty("DetailPos",detailPosEditText_straight.getText().toString());
                object.addProperty("CarPhone",carPhone_straight.getText().toString());
                new MyNormalThread(context,object.toString(),handler).start();
            }
        });

        array = getResources().getStringArray(R.array.省份);
        System.out.println(array[0]);
        adapter = new MyArrayAdapter<String>(context, R.layout.spinner_text_cell_newcarstop, array);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        provinceSpinner.setAdapter(adapter);
        straightCitySpinner.setAdapter(adapter);
        array = getResources().getStringArray(R.array.默认市);
        adapter = new MyArrayAdapter<String>(context, R.layout.spinner_text_cell_newcarstop, array);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        citySpinner.setAdapter(adapter);
        citySpinner.setEnabled(false);

        array = getResources().getStringArray(R.array.默认区县);
        adapter = new MyArrayAdapter<String>(context, R.layout.spinner_text_cell_newcarstop, array);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        areaSpinner.setAdapter(adapter);
        straightAreaSpinner.setAdapter(adapter);
        areaSpinner.setEnabled(false);

        //初始化城市，根据所选省/直辖市决定
        //初始化区县，根据所选市/直辖市决定
        initCity();


    }

    //选择的值
    String value = "";

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

                        newCarStopButton_straight.setVisibility(Button.VISIBLE);
                        detailPosEditText_straight.setVisibility(EditText.VISIBLE);
                        carPhone_straight.setVisibility(TextView.VISIBLE);
                        line_straight.setVisibility(View.VISIBLE);
                        newCarStopButton_normal.setVisibility(Button.INVISIBLE);
                        detailPosEditText_normal.setVisibility(EditText.INVISIBLE);
                        carPhone_normal.setVisibility(EditText.INVISIBLE);
                        line_normal.setVisibility(View.INVISIBLE);
                        detailPosEditText_straight.setText("");
                        carPhone_straight.setText(carPhone_normal.getText().toString());

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
                        adapter = new MyArrayAdapter<String>(context, R.layout.spinner_text_cell_newcarstop, array);
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
                        adapter = new MyArrayAdapter<String>(context, R.layout.spinner_text_cell_newcarstop, array);
                        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                        citySpinner.setAdapter(adapter);


                    }
                } else {
                    array = getResources().getStringArray(R.array.默认市);
                    adapter = new MyArrayAdapter<String>(context, R.layout.spinner_text_cell_newcarstop, array);
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
                    adapter = new MyArrayAdapter<String>(context, R.layout.spinner_text_cell_newcarstop, array);
                    adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                    areaSpinner.setAdapter(adapter);
                } else {
                    array = getResources().getStringArray(R.array.默认区县);
                    adapter = new MyArrayAdapter<String>(context, R.layout.spinner_text_cell_newcarstop, array);
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
                        adapter = new MyArrayAdapter<String>(context, R.layout.spinner_text_cell_newcarstop, array);
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

                        newCarStopButton_straight.setVisibility(Button.INVISIBLE);
                        detailPosEditText_straight.setVisibility(EditText.INVISIBLE);
                        carPhone_straight.setVisibility(EditText.INVISIBLE);
                        line_straight.setVisibility(View.INVISIBLE);
                        newCarStopButton_normal.setVisibility(Button.VISIBLE);
                        detailPosEditText_normal.setVisibility(EditText.VISIBLE);
                        carPhone_normal.setVisibility(EditText.VISIBLE);
                        line_normal.setVisibility(View.VISIBLE);
                        detailPosEditText_normal.setText("");
                        carPhone_normal.setText(carPhone_straight.getText().toString());

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
                        adapter = new MyArrayAdapter<String>(context, R.layout.spinner_text_cell_newcarstop, array);
                        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                        citySpinner.setAdapter(adapter);


                    }


                }
                //选择了第一个
                else {

                    array = getResources().getStringArray(R.array.默认区县);
                    adapter = new MyArrayAdapter<String>(context, R.layout.spinner_text_cell_newcarstop, array);
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
