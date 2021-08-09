package com.wyc.message.msg;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.wyc.message.R;

public class MessageActivity extends AppCompatActivity {

    private String TAG = "TEST_MSG";
    private Messenger mServiceMessenger;

    public static void start(Context context) {
        context.startActivity(new Intent(context, MessageActivity.class));
    }

    //Client
    @SuppressLint("HandlerLeak")
    private Handler mClientHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MessageService.MSG_FROM_SERVICE) {
                //接收Parcelable必须设置ClassLoader，否则无法解析
                msg.getData().setClassLoader(MessageEntity.class.getClassLoader()); //设置class loader
                String msgStr = "客户端：" + msg.getData().getParcelable(MessageService.SERVICE_MSG);
                Log.i(TAG, msgStr);
                Toast.makeText(MessageActivity.this, msgStr, Toast.LENGTH_LONG).show();
            }
        }
    };
    //mClientMessenger
    private Messenger mClientMessenger = new Messenger(mClientHandler);

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, "onServiceDisconnected");
            mServiceMessenger = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder iBinder) {
            Log.i(TAG, "onServiceConnected");
            //拿到服务器传给客户端的IBinder，通过new Messenger(service)拿到mServiceMessenger
            mServiceMessenger = new Messenger(iBinder);
            //Messenger传递的是Message，实例化一个Message
            Message msgFromClient = Message.obtain(null, MessageService.MSG_FROM_CLIENT);
            //Message传递的是Bundle
            Bundle bundle = new Bundle();
            //发送一个对象类型的数据
            bundle.putParcelable(MessageService.CLIENT_MSG, new MessageEntity(String.valueOf(System.currentTimeMillis()), "这里是客户端，呼叫服务器"));
            msgFromClient.setData(bundle);
            //一定不要忘了将mClientMessenger带到服务器去
            msgFromClient.replyTo = mClientMessenger;
            try {
                //调用mServiceMessenger.send将消息发送的服务器
                mServiceMessenger.send(msgFromClient);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        TextView btn = findViewById(R.id.btnMsg);
        Intent intent = new Intent(this, MessageService.class);
        //绑定Service
        bindService(intent, mConnection, BIND_AUTO_CREATE);
        //点击按钮发送客户端请求
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //由于在onServiceConnected中我们已经拿到了mServiceMessenger了，再发送消息的时候
                //直接新建Message对象就可以了，最后调用mServiceMessenger将消息发送给服务器
                Message msgFromClient = Message.obtain(null, MessageService.MSG_FROM_CLIENT);
                Bundle bundle = new Bundle();
                bundle.putParcelable(MessageService.CLIENT_MSG, new MessageEntity(String.valueOf(System.currentTimeMillis()), "客户端发起一次请求"));
                msgFromClient.setData(bundle);
                msgFromClient.replyTo = mClientMessenger;
                try {
                    mServiceMessenger.send(msgFromClient);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解绑Service
        unbindService(mConnection);
        mConnection = null;
    }
}
