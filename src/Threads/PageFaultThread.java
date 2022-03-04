/*
 * @Author: Meguri Ichinose
 * @Date: 2022-02-18 14:13:03
 * @description: 缺页中断处理线程
 */
package Threads;

import Hardware.Clock;
import Hardware.Memory;
import Manager.PageTable;
import Process.PCB;

public class PageFaultThread extends Thread{
    private static int Time=0;// 时钟
    private static boolean Interrupt=false;// 中断标志
    private static PCB pcb;// 进程
    private static int l_Address;// 逻辑地址

    public static PCB getPcb(){
        return pcb;
    }

    public static int getL_Address(){
        return l_Address;
    }

    public static void setL_Address(int l_Address){
        PageFaultThread.l_Address=l_Address;
    }

    public static void setPcb(PCB pcb){
        PageFaultThread.pcb=pcb;
    }

    @Override
    public void run(){
        while(true){
            if(!Clock.isStop()){
                PageFaultThread.Time++;
            }
            try{
                sleep(Clock.INTERVAL/Clock.getSpeed());
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    /**
     * @description: LRU算法换页
     * @param pcb
     *            进程
     * @param l_Address
     *            逻辑地址
     * @return
     */
    public static void LRU(PCB pcb,int l_Address){
        // 遍历页表，如果有空余位置，则加入
        for(int i=0;i<Memory.MAX_CONCURRENCY*Memory.PROCESS_PAGE_NUM;i++){
            if(PageTable.getProID(i)==pcb.getProID()){
                if(PageTable.getBlockNo(i)==0){
                    PageTable.setBlockNo(i,l_Address);
                    PageTable.ResetVisit(i);
                }
            }
        }
        // 已装满，遍历页表，找到访问位最大的页，进行换出，并将访问位置0
        int visitMax=0;
        for(int i=0;i<Memory.MAX_CONCURRENCY*Memory.PROCESS_PAGE_NUM;i++){
            if(PageTable.getProID(i)==pcb.getProID()){
                if(PageTable.getVisit(i)>visitMax)
                    visitMax=i;
            }
        }
        PageTable.setBlockNo(visitMax,l_Address);
        PageTable.ResetVisit(visitMax);
    }

    /**
     * @description: 缺页信号
     * @param pcb
     *            进程
     * @param l_Address
     *            逻辑地址
     * @return
     */
    public static void PageFault(PCB pcb,int l_Address){
        setPcb(pcb);
        setL_Address(l_Address);
        System.out.println("Page Fault.");
        LRU(pcb,l_Address);
    }

    public static int getTime(){
        return Time;
    }

    public static boolean isInterrupt(){
        return Interrupt;
    }

    public static void setInterrupt(boolean interrupt){
        Interrupt=interrupt;
    }

    public static void setTime(int time){
        Time=time;
    }
}
