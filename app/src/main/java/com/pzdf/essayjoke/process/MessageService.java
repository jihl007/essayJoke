package com.pzdf.essayjoke.process;

import android.app.Notification;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.pzdf.essayjoke.ProcessConnection;

/**
 * author  jihl on 2019/3/22.
 * version 1.0
 * Description:qq聊天通信    代码需要  轻
 */
public class MessageService extends Service{
    private final int MessageId=1;
    @Override
    public void onCreate() {
        super.onCreate();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //提高服务的优先级
        startForeground(MessageId,new Notification());

        //绑定建立链接
        bindService(new Intent(this, MessageService.class),
                mServiceConnection,
                Context.BIND_IMPORTANT);

        return START_STICKY;
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new ProcessConnection.Stub() {

        };
    }
    private ServiceConnection mServiceConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //链接成功
            Toast.makeText(MessageService.this,"",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            //断开链接,重新启动 重新绑定
            startService(new Intent(MessageService.this,GuardService.class));

            bindService(new Intent(MessageService.this,GuardService.class),
                    mServiceConnection,
                    Context.BIND_IMPORTANT);


        }
    };
}
