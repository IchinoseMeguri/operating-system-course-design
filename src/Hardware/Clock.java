/*
 * @Author: Meguri Ichinose
 * @Date: 2022-02-17 19:30:31
 * @Description: 仿真时钟(计时器)
 */
package Hardware;

import GUI.Gui;

public class Clock extends Thread{
    public static final int INTERVAL=1000;
    private static int Speed=1;

    private static int Time;// 时间

    // volatile的作用是作为指令关键字，确保本条指令不会因编译器的优化而省略，且要求每次直接读值。
    private static boolean Stop=true;// 运行标志
    private static boolean Interrupt=false;// 中断标志

    /**
     * @description: 时钟run方法
     * @param
     * @return
     */
    @Override
    public void run(){
        while(true){
            if(!Stop){
                // 如果时钟为非停止状态，则刷新界面信息，发生一次时钟中断，时间自增
                Gui.Show();
                Clock.Interrupt=true;
                Clock.Time++;
            }
            Clock.Interrupt=false;
            try{
                sleep(INTERVAL/Speed);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    /**
     * @description: 时钟停止
     * @param
     * @return
     */
    public static void StopClock(){
        setStop(true);
    }

    /**
     * @description: 重置中断信号为false
     * @param
     * @return
     */
    public static void ResetInterrupt(){
        setInterrupt(false);
    }

    public static boolean isInterrupt(){
        return Interrupt;
    }

    public static void setInterrupt(boolean interrupt){
        Clock.Interrupt=interrupt;
    }

    public static int getSpeed(){
        return Speed;
    }

    public static boolean isStop(){
        return Stop;
    }

    public static void setStop(boolean stop){
        Stop=stop;
    }

    public static void setSpeed(int speed){
        Speed=speed;
    }

    public static int getTime(){
        return Time;
    }

    public static void setTime(int time){
        Time=time;
    }
}
