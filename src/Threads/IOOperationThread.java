/*
 * @Author: Meguri Ichinose
 * @Date: 2022-02-18 14:11:17
 * @description: 输入输出处理线程（涉及键盘和屏幕）
 */
package Threads;

import Hardware.Clock;
import Manager.ProcessManage;

public class IOOperationThread extends Thread{
    private static int Time;// 时钟
    private static int KeyboardTime=2;// 键盘时间
    private static int ScreenTime=1;// 屏幕时间

    @Override
    public void run(){
        while(true){
            if(!Clock.isStop()){
                Time++;
                // 如果键盘的阻塞队列非空，则键盘时间计1秒，在这之后，如果键盘时间为0，则代表键盘输入完数据，进程出阻塞队列，返回就绪队列，重置键盘时间
                if(ProcessManage.getBlock1().size()>0){
                    KeyboardTime--;
                    if(KeyboardTime<=0){
                        ProcessManage.WakeupProcess(ProcessManage.getBlock1().get(0),2);
                        KeyboardTime=2;
                    }
                }
                // 屏幕同理
                if(ProcessManage.getBlock2().size()>0){
                    ScreenTime--;
                    if(ScreenTime<=0){
                        ProcessManage.WakeupProcess(ProcessManage.getBlock2().get(0),3);
                        KeyboardTime=1;
                    }
                }
            }
            try{
                sleep(Clock.INTERVAL/Clock.getSpeed());
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    public static int getScreenTime(){
        return ScreenTime;
    }

    public static void setScreenTime(int screenTime){
        ScreenTime=screenTime;
    }

    public static int getKeyboardTime(){
        return KeyboardTime;
    }

    public static void setKeyboardTime(int keyboardTime){
        KeyboardTime=keyboardTime;
    }

    public static int getTime(){
        return Time;
    }

    public static void setTime(int time){
        Time=time;
    }
}
