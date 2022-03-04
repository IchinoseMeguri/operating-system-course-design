/*
 * @Author: Meguri Ichinose
 * @Date: 2022-02-18 14:13:51
 * @description: 磁盘文件操作线程（主要是需要使用缓冲区，包括打印函数）
 */
package Threads;

import java.util.ArrayList;

import GUI.DialogueType;
import GUI.Gui;
import Hardware.Clock;
import Hardware.Memory;
import Manager.ProcessManage;
import Manager.QueueDialogue;
import Manager.StorageManage;
import Process.PCB;

public class BufferThread extends Thread{
    private static int Time;// 时钟
    private static int BufferTime=0;// 该当进程还须使用缓冲区的时间
    private static ArrayList<PCB> BufferQueue=new ArrayList<PCB>();// 等待使用缓冲区的队列
    private static PCB Buffer=null;// 正在使用缓冲区的进程

    @Override
    public void run(){
        while(true){
            if(!Clock.isStop()){
                Time++;
                // 如果缓冲区内有进程，则缓冲区时间计1秒
                if(Buffer!=null){
                    BufferTime--;
                    switch(Buffer.getJob().getCommandQueue().get(Buffer.getIR()-2).getInstruc_State()){
                        case 4:
                            Gui.DialogueUpdate(DialogueType.BUFFER_IN,Buffer);
                            break;
                        case 5:
                            Gui.DialogueUpdate(DialogueType.BUFFER_OUT,Buffer);
                            break;
                        case 6:
                            Gui.DialogueUpdate(DialogueType.PRINT_BUFFER,Buffer);
                            break;
                        default:
                    }
                    // 在这之后，如果缓冲区时间为0，即进程已经使用完缓冲区，则唤醒进程，从缓冲区中移除进程，返回就绪队列
                    if(BufferTime==0){
                        ProcessManage.WakeupProcess(Buffer,
                                Buffer.getJob().getCommandQueue().get(Buffer.getIR()-2).getInstruc_State());
                        BufferQueue.remove(Buffer);
                        Gui.DialogueUpdate(DialogueType.COPY_OUT,Buffer);
                        Buffer=null;
                        StorageManage.ClearMemory(Memory.BUFFER_START_BLOCK_NO,Memory.BUFFER_PAGE_NUM);
                    }
                    // 如果缓冲区为空，则将缓冲区等待队列的队首加入到缓冲区，设置缓冲区使用时间，为其分配缓冲区
                }else{
                    if(BufferQueue.size()>0){
                        Buffer=BufferQueue.get(0);
                        StorageManage.AllocateBuffer(Memory.BUFFER_PAGE_NUM);
                        BufferTime=Buffer.getJob().getCommandQueue().get(Buffer.getIR()-2).getInRunTimes();
                        StorageManage.getBufferDialogue().add(new QueueDialogue(Buffer,Time,Time+BufferTime));
                        Gui.DialogueUpdate(DialogueType.COPY_IN,Buffer);
                    }else{
                        Gui.DialogueUpdate(DialogueType.BUFFER_FREE,null);
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

    public static PCB getBuffer(){
        return Buffer;
    }

    public static void setBuffer(PCB buffer){
        Buffer=buffer;
    }

    public static int getBufferTime(){
        return BufferTime;
    }

    public static void setBufferTime(int bufferTime){
        BufferTime=bufferTime;
    }

    public static ArrayList<PCB> getBufferQueue(){
        return BufferQueue;
    }

    public static void setBufferQueue(ArrayList<PCB> bufferQueue){
        BufferQueue=bufferQueue;
    }

    public static int getTime(){
        return Time;
    }

    public static void setTime(int time){
        Time=time;
    }
}
