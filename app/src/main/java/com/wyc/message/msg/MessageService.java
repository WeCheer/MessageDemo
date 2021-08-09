package com.wyc.message.msg;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

/**
 * 作者： wyc
 * <p>
 * 创建时间： 2020/4/15 19:41
 * <p>
 * 文件名字： com.wyc.vivodemo.msg
 * <p>
 * 类的介绍：
 */
public class MessageService extends Service {
    private String TAG = "TEST_MSG";

    public static final int MSG_FROM_CLIENT = 0;
    public static final int MSG_FROM_SERVICE = 1;

    public static final String SERVICE_MSG = "service_msg";
    public static final String CLIENT_MSG = "client_msg";
    // mServiceHandler
    @SuppressLint("HandlerLeak")
    private Handler mServiceHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_FROM_CLIENT) {//拿到客户端发来的消息
                /**
                 * 防止出现异常 android.os.BadParcelableException: ClassNotFoundException when unmarshalling
                 * 不是基础类需要设置setClassLoader
                 */
                msg.getData().setClassLoader(MessageEntity.class.getClassLoader()); //设置class loader
                String msgStr = "服务器：" + msg.getData().getParcelable(CLIENT_MSG);
                Log.d(TAG, msgStr);
                Toast.makeText(MessageService.this, msgStr, Toast.LENGTH_LONG).show();
//                //拿到客户端的mClientMessenger
                Messenger messengerService = msg.replyTo;
                //Messenger传递的是Message，所以新建一个Message实例
                Message msgFromService = Message.obtain(null, MSG_FROM_SERVICE);
                //Message传递的是Bundle
                Bundle bundle = new Bundle();
                //发送一个对象类型的数据
                bundle.putParcelable(SERVICE_MSG, new MessageEntity(String.valueOf(System.currentTimeMillis()), "这里是服务器，收到客户端请求"));
                msgFromService.setData(bundle);
                try {
                    //调用mClientMessenger.send将消息发送给客户端
                    messengerService.send(msgFromService);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    };
    // mServiceMessenger关联mServiceHandler
    private Messenger mServiceMessenger = new Messenger(mServiceHandler);

    @Override
    public IBinder onBind(Intent intent) {
        //将IBinder传给客户端，客户端通过new Messenger(IBinder)拿到mServiceMessenger;
        Log.d(TAG, "intent = " + intent);
        return mServiceMessenger.getBinder();
    }
}
