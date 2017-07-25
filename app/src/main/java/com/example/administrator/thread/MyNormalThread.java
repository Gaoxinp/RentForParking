package com.example.administrator.thread;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by 高信朋 on 2017/4/10.
 */

public class MyNormalThread extends Thread {
    private Context context;
    private String strSend = "";
    private Handler handler;
    private Socket socket = null;
    //    DataInputStream in;
//    DataOutputStream out;
    InputStream in;
    OutputStream out;

    public MyNormalThread(Context context, String strObject, Handler handler) {
        this.context = context;
        this.strSend = strObject;
        this.handler = handler;
    }

    @Override
    public void run() {
        try {
            socket = new Socket("139.199.9.254", 10001);

            //使用DataInputStream可以方便的传输UTF-8格式的字符串
//            in = new DataInputStream(socket.getInputStream());
//            out = new DataOutputStream(socket.getOutputStream());
//            out.writeUTF(strSend);


            //由于需要与硬件通信，因此必须要使用字节流，可以使用InputStream的read(byte[] bytes)方法来读取
            //注意：在输入流未得到结束符之前，in会一直等待获取数据，除非发送方的流输出流out被关闭
            //还应注意：服务器与客户端使用的流应保持一致，即：同是字符流或者同是字节流，否则会出现例如传送过程中多出一个分号，空格等问题
            in = socket.getInputStream();
            out = socket.getOutputStream();
            System.out.println(strSend);
            out.write(strSend.getBytes("UTF-8"));
            out.flush();
//            String mag = in.readUTF();
            byte[] bt = new byte[1024];

            int len = 0;
            StringBuffer sb = new StringBuffer();
            while ((len = in.read(bt)) != -1) {

                sb.append(new String(bt, "UTF-8"));
                if (len < bt.length) {
                    break;
                }
            }
            String mag = sb.toString().trim();
            System.out.println(mag);
            sb = new StringBuffer();
            JsonParser ps = new JsonParser();
            JsonObject object = ps.parse(mag).getAsJsonObject();
            Message message = new Message();

            if (object.get("Flag").getAsString().equals("register")) {          //注册
//                如果注册成功，由子线程给主线程传递信息，finish掉注册界面
                if (object.get("Message").getAsString().equals("Registered successfully")) {
                    message.obj = "SUCCEED";
                }
//                Looper.prepare();
//                Toast.makeText(context, object.get("Message").getAsString(), Toast.LENGTH_SHORT).show();
//                Looper.loop();
            } else if (object.get("Flag").getAsString().equals("login")) {        //登录
                message.obj = object.get("Message").getAsString();
            } else if (object.get("Flag").getAsString().equals("newCarStop")) {       //增加车位
                message.obj = object.get("Message").getAsString();
            } else if (object.get("Flag").getAsString().equals("selectCarStop")) {      //查询车位信息
                if (object.get("Message").getAsString().equals("null")) {
                    message.what = 0;
                } else {
                    message.what = 1;
                    message.arg1 = Integer.parseInt(object.get("Message").getAsString());
                    message.obj = object.get("Content").getAsJsonArray();
                }
            } else if (object.get("Flag").getAsString().equals("deleteCarStop")) {      //删除车位信息
                if (object.get("Message").getAsString().equals("Delete failed")) {
                    message.what = 2;
                    message.obj = "Delete failed";
                } else {
                    message.what = 3;
                    message.obj = object.get("Message").getAsString();
                }
            } else if (object.get("Flag").getAsString().equals("rentout_publishing")) {      //发布出租信息，出租车位
                if (object.get("Message").getAsString().equals("publish failed")) {
                    message.what = 4;
                    message.obj = "操作不成功，请稍后再试";
                } else {
                    message.what = 5;
                    message.obj = "已加入出租";
                }
            } else if (object.get("Flag").getAsString().equals("cancelRentOut")) {      //取消发布出租信息，取消出租车位
                if (object.get("Message").getAsString().equals("cancel failed")) {
                    message.what = 6;
                    message.obj = "操作不成功，请稍后再试";
                } else {
                    message.what = 7;
                    message.obj = "已取消出租";
                }
            } else if (object.get("Flag").getAsString().equals("getCarStopUserDetailPosAndDate")) {      //根据用户指定的时间跟地点，获取发布的出租信息
                if (object.get("Message").getAsString().equals("null")) {
                    message.what = 0;
                } else {
                    message.what = 1;
                    message.arg1 = Integer.parseInt(object.get("Message").getAsString());
                    message.obj = object.get("Content").getAsJsonArray();
                }
            } else if ("rentIn".equals(object.get("Flag").getAsString())) {      //租入
                if (object.get("Message").getAsString().equals("rentIn successfully")) {
                    message.what = 2;
                    message.obj = "租入成功，等待消费";
                } else {
                    message.what = 3;
                    message.obj = "操作失败，请稍后再试";
                }
            } else if (object.get("Flag").getAsString().equals("getPublishING")) {      //获取发布的出租信息
                if (object.get("Message").getAsString().equals("null")) {
                    message.what = 0;
                } else {
                    message.what = 1;
                    message.arg1 = Integer.parseInt(object.get("Message").getAsString());
                    message.obj = object.get("Content").getAsJsonArray();
                }
            } else if (object.get("Flag").getAsString().equals("getConsume_rentOut")) {      //获取发布的出租信息
                if (object.get("Message").getAsString().equals("null")) {
                    message.what = 0;
                } else {
                    message.what = 1;
                    message.arg1 = Integer.parseInt(object.get("Message").getAsString());
                    message.obj = object.get("Content").getAsJsonArray();
                }
            } else if (object.get("Flag").getAsString().equals("getObligation_rentIn")) {      //获取待付款的信息列表-租入
                if (object.get("Message").getAsString().equals("null")) {
                    message.what = 0;
                } else {
                    message.what = 1;
                    message.arg1 = Integer.parseInt(object.get("Message").getAsString());
                    message.obj = object.get("Content").getAsJsonArray();
                }
            } else if (object.get("Flag").getAsString().equals("getConsume_rentIn")) {      //获取待消费的信息列表-租入
                if (object.get("Message").getAsString().equals("null")) {
                    message.what = 0;
                } else {
                    message.what = 1;
                    message.arg1 = Integer.parseInt(object.get("Message").getAsString());
                    message.obj = object.get("Content").getAsJsonArray();
                }
            } else if (object.get("Flag").getAsString().equals("cancelOrder")) {      //取消还未付款的订单
                if (object.get("Message").getAsString().equals("Cancel successfully")) {
                    message.what = 2;
                }
            } else if (object.get("Flag").getAsString().equals("goToPay")) {      //已付款 需要发给服务器数据以更新状态
                if (object.get("Message").getAsString().equals("Pay successfully")) {
                    message.what = 3;
                }
            } else if (object.get("Flag").getAsString().equals("cancelOrder_payFinash")) {      //取消已经付款的订单
                if (object.get("Message").getAsString().equals("cancel successfully")) {
                    message.what = 2;
                } else {
                    message.what = 3;

                }
            } else if (object.get("Flag").getAsString().equals("FinashOrder")) {      //完成订单
                if (object.get("Message").getAsString().equals("Finash successfully")) {
                    message.what = 4;
                } else {
                    message.what = 5;

                }
            } else if (object.get("Flag").getAsString().equals("getFinashed_rentIn")) {      //查看已完成的租入订单
                if (object.get("Message").getAsString().equals("null")) {
                    message.what = 0;
                } else {
                    message.what = 1;
                    message.arg1 = Integer.parseInt(object.get("Message").getAsString());
                    message.obj = object.get("Content").getAsJsonArray();
                }
            } else if (object.get("Flag").getAsString().equals("getRefund_rentIn")) {      //查看待退款的租入订单
                if (object.get("Message").getAsString().equals("null")) {
                    message.what = 0;
                } else {
                    message.what = 1;
                    message.arg1 = Integer.parseInt(object.get("Message").getAsString());
                    message.obj = object.get("Content").getAsJsonArray();
                }
            } else if (object.get("Flag").getAsString().equals("getFinashed_rentOut")) {      //查看已完成的的租出订单
                if (object.get("Message").getAsString().equals("null")) {
                    message.what = 0;
                } else {
                    message.what = 1;
                    message.arg1 = Integer.parseInt(object.get("Message").getAsString());
                    message.obj = object.get("Content").getAsJsonArray();
                }
            } else if (object.get("Flag").getAsString().equals("getRefund_rentOut")) {      //查看待退款的的租出订单
                if (object.get("Message").getAsString().equals("null")) {
                    message.what = 0;
                } else {
                    message.what = 1;
                    message.arg1 = Integer.parseInt(object.get("Message").getAsString());
                    message.obj = object.get("Content").getAsJsonArray();
                }
            } else if (object.get("Flag").getAsString().equals("goToRefund")) {      //去退款，出租者待退款界面
                if (object.get("Message").getAsString().equals("Refund successfully")) {
                    message.what = 2;
                } else {
                    message.what = 3;
                }
            }else if (object.get("Flag").getAsString().equals("CarRegister")) {      //通知服务器要注册的车位手机号
                if (object.get("Message").getAsString().equals("successfully")) {
                    message.what = 6;
                } else {
                    message.what = 7;
                }
            }


            handler.sendMessage(message);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
                in.close();
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
