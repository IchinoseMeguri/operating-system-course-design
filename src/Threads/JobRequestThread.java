/*
 * @Author: Meguri Ichinose
 * @Date: 2022-02-18 14:02:39
 * @description: 作业请求线程
 * 假设计算机每 5 秒(s)查询一次，判断是否有新作业的执行请求，由作业并发请求中
 * 断的线程单独实现
 */
package Threads;

import Hardware.Clock;
import Manager.ProcessManage;

public class JobRequestThread extends Thread{
    private static int Time=0;

    @Override
    public void run(){
        int start=Time;// 时间计5秒发动一次
        while(true){
            if(!Clock.isStop()){
                if(Time-start>=5){
                    ProcessManage.JobRequest();// 发动作业请求
                    start=Time;
                }
                JobRequestThread.Time++;
            }
            try{
                sleep(Clock.INTERVAL/Clock.getSpeed());
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    public static int getTime(){
        return Time;
    }

    public static void setTime(int time){
        Time=time;
    }

}
