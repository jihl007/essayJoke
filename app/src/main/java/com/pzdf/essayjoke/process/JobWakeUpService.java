package com.pzdf.essayjoke.process;

import android.app.ActivityManager;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.List;

/**
 * author  jihl on 2019/3/22.
 * version 1.0
 * Description:  5.0以上才有
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class JobWakeUpService extends JobService {
    private final int jobWakeUpId=1;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //开启一个轮寻
        JobInfo.Builder jobBuilder=new JobInfo.Builder(jobWakeUpId,
                new ComponentName(this, JobWakeUpService.class));
        //设置轮寻时间
        jobBuilder.setPeriodic(2000);

        JobScheduler jobScheduler= (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(jobBuilder.build());

        return START_STICKY;
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        //开启定时任务，定时轮寻。看指定任务又没有被kill掉。

        //判断服务有没有被kill
        boolean messageServiceAlive=serviceAlive(MessageService.class.getName());
        if(!messageServiceAlive){
            startService(new Intent(this,MessageService.class));
        }
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }

    /**
     * 判断某个服务是否正在运行
     * true:正在运行        false：没有运行
     * @param serviceName
     *         serviceName:包名+服务的类名（例如：net.loonggg.textbackstage.TestService）
     * @return
     */
    private boolean serviceAlive(String serviceName){
        boolean isWork=false;
        ActivityManager myAM=(ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> mList=myAM.getRunningServices(100);
        if(mList.size()<=0){
            return false;
        }
        for (int i = 0; i < mList.size(); i++) {
            String mName=mList.get(i).service.getClassName().toString();
            if (mName.equals(serviceName)) {
                isWork=true;
                break;
            }
        }
        return isWork;
    }
}
