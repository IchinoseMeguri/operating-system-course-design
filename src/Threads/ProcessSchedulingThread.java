/*
 * @Author: Meguri Ichinose
 * @Date: 2022-02-18 14:10:18
 * @description: 进程调度线程
 */
package Threads;

import GUI.DialogueType;
import GUI.Gui;
import Hardware.CPU;
import Hardware.CPUStates;
import Hardware.Clock;
import Manager.ProcessManage;
import Process.PCB;

public class ProcessSchedulingThread extends Thread{
    private static int Timepiece=2;// 时间片
    private static int Time=0;// 时钟

    public static int getTime(){
        return Time;
    }

    public static void setTime(int time){
        Time=time;
    }

    @Override
    public void run(){
        while(true){
            if(!Clock.isStop()){
                Timepiece--;
                // 在CPU非空闲的时候
                if(!CPU.isFree()){
                    // 如果此时CPU中进程指令未做完，则做1秒
                    if(CPU.getIR()<=CPU.getPcb().getInstrucNum()){
                        CPU.RunProcess();
                        // 随后，根据指令类型，如果需阻塞，则阻塞进程，PC，IR自增，保护现场，交由相应的线程处理，随后，CPU运行信息清空
                        switch(CPU.getCmd().getInstruc_State()){
                            case 2, 3:
                                CPU.setPSW(CPUStates.KERNEL);
                                Gui.Show();
                                CPU.incPC();
                                CPU.incIR();
                                CPU.setRunTime(0);
                                ProcessManage.BlockProcess(CPU.getPcb(),CPU.getCmd().getInstruc_State());
                                break;
                            case 4, 5:
                                CPU.setPSW(CPUStates.KERNEL);
                                Gui.Show();
                                PCB pcb=CPU.getPcb();
                                Gui.DialogueUpdate(DialogueType.COPY_IN,pcb);
                                CPU.incPC();
                                CPU.incIR();
                                CPU.setRunTime(0);
                                BufferThread.getBufferQueue().add(pcb);
                                ProcessManage.BlockProcess(pcb,CPU.getCmd().getInstruc_State());
                                break;
                            case 6:
                                CPU.setPSW(CPUStates.KERNEL);
                                Gui.Show();
                                pcb=CPU.getPcb();
                                Gui.DialogueUpdate(DialogueType.PRINT,pcb);
                                CPU.incPC();
                                CPU.incIR();
                                CPU.setRunTime(0);
                                BufferThread.getBufferQueue().add(pcb);
                                ProcessManage.BlockProcess(pcb,CPU.getCmd().getInstruc_State());
                                break;
                            default:
                                // 指令类型为0和1，正常调度，做完指令后，PC，IR自增
                                if(CPU.getRunTime()<=0){
                                    CPU.incPC();
                                    CPU.incIR();
                                    // 如果时间片已经用完，则保护现场，将进程返回就绪队列
                                    if(Timepiece<=0){
                                        ProcessManage.ReturnReady(CPU.getPcb());
                                        CPU.ProtectScene();
                                    }else{
                                        // 如果时间片未用完，则读取下一条指令
                                        if(CPU.getIR()<=CPU.getPcb().getInstrucNum())
                                            CPU.RestoreScene(CPU.ProtectScene());
                                        else{
                                            // 如果指令已经做完，则返回就绪队列
                                            ProcessManage.ReturnReady(CPU.getPcb());
                                            CPU.ProtectScene();
                                        }
                                    }
                                }
                        }
                    }
                }
                // 如果CPU空闲且时间片用完，则进行低级调度，在CPU非空闲时不会进行低级调度，因为执行一条指令时不会中断
                if(CPU.isFree()&&Timepiece<=0){
                    Scheduling();
                    Timepiece=2;// 重置时间片
                }
                ProcessSchedulingThread.Time++;
            }
            try{
                sleep(Clock.INTERVAL/Clock.getSpeed());
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    /**
     * @description: 低级调度
     * @param
     * @return
     */
    public static void Scheduling(){
        // 如果就绪队列非空，则找到优先级最高（优先数最低）的进程
        if(ProcessManage.getReady().size()>0){
            PCB pCB=ProcessManage.getReady().get(0);
            for(PCB pcb:ProcessManage.getReady()){
                if(pcb.getPriority()<pCB.getPriority())
                    pCB=pcb;
            }
            // 如果指令未做完，则执行，如果做完，则撤销进程
            if(pCB.getIR()<=pCB.getJob().getInstrucNum())
                ProcessManage.RunProcess(pCB);
            else
                ProcessManage.DeleteProcess(pCB);
        }
    }

    public static int getTimepiece(){
        return Timepiece;
    }

    public static void setTimepiece(int timepiece){
        Timepiece=timepiece;
    }

}
