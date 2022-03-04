/*
 * @Author: Meguri Ichinose
 * @Date: 2022-02-22 17:35:34
 * @Description: 队列等的进出事件统计
 */
package Manager;

import Process.PCB;

public class QueueDialogue{
    private PCB pcb;// 进程
    private int inTime;// 进入时间
    private int OutTime;// 离开时间

    public QueueDialogue(PCB pcb,int inTime,int outTime){
        this.pcb=pcb;
        this.inTime=inTime;
        this.OutTime=outTime;
    }

    public PCB getPcb(){
        return pcb;
    }

    public void setPcb(PCB pcb){
        this.pcb=pcb;
    }

    public int getInTime(){
        return inTime;
    }

    public void setInTime(int inTime){
        this.inTime=inTime;
    }

    public int getOutTime(){
        return OutTime;
    }

    public void setOutTime(int outTime){
        OutTime=outTime;
    }

}
