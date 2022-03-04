/*
 * @Author: Meguri Ichinose
 * @Date: 2022-02-10 14:03:39
 * @Description: CPU
 */
package Hardware;

import GUI.Gui;
import Process.Command;
import Process.PCB;

public class CPU{
    private static int PC;
    private static int IR;
    private static Command Cmd;// 正在执行的指令
    private static PCB pcb;// 正在使用CPU的进程PCB
    private static CPUStates PSW;// 状态
    private static boolean Free=true;// CPU是否空闲
    private static int StartTime;// 进程开始运行时间
    private static int RunTime;// 表示该条指令还需执行的时间

    public static int getRunTime(){
        return RunTime;
    }

    public static void setRunTime(int runTime){
        RunTime=runTime;
    }

    /**
     * @description: CPU保护现场，将信息写入到进程PCB中
     * @param
     * @return 进程PCB
     */
    public static PCB ProtectScene(){
        // 将寄存器PC和IR的数据保存到PCB中
        pcb.setIR(IR);
        pcb.setPC(PC);
        // PCB中新增一条运行记录
        pcb.AddRunTime(StartTime,Clock.getTime()-StartTime);
        PCB pCB=pcb;
        // CPU置空闲
        setCmd(null);
        setPcb(null);
        setFree(true);
        // 函数返回值为该PCB，可以给其他函数用
        return pCB;
    }

    /**
     * @description: CPU恢复现场，将PCB信息读入CPU
     * @param pcb
     *            进程PCB
     * @return
     */
    public static void RestoreScene(PCB pcb){
        // 将进程信息写入CPU
        setPcb(pcb);
        setPC(pcb.getPC());
        setIR(pcb.getIR());
        setCmd(pcb.getJob().getCommandQueue().get(IR-1));
        // 现在是进程开始运行时间
        setStartTime(Clock.getTime());
        // 写入进程所需循行的时间
        setRunTime(Cmd.getInRunTimes());
        // CPU置非空闲
        setFree(false);
        // 置基址寄存器值
        MMU.setBaseAddress(pcb.getBaseAddress());
    }

    /**
     * @description: 运行一条程序执行1秒
     * @param
     * @return
     */
    public static void RunProcess(){
        if(pcb!=null){
            // 将PCB的物理地址值置为现在访问的物理地址
            pcb.setP_Address(MMU.getPhysicalAddress(pcb,Cmd.getL_Address()));
            Gui.DialogueUpdate(GUI.DialogueType.RUN,pcb);
            CPU.RunTime--;
        }
    }

    /**
     * @description: PC自增
     * @param
     * @return
     */
    public static void incPC(){
        CPU.PC++;
    }

    /**
     * @description: IR自增
     * @param
     * @return
     */
    public static void incIR(){
        CPU.IR++;
    }

    public static int getStartTime(){
        return StartTime;
    }

    public static Command getCmd(){
        return Cmd;
    }

    public static void setCmd(Command cmd){
        Cmd=cmd;
    }

    public static void setStartTime(int startTime){
        StartTime=startTime;
    }

    public static boolean isFree(){
        return Free;
    }

    public static void setFree(boolean free){
        Free=free;
    }

    public static int getPC(){
        return PC;
    }

    public static void setPC(int pC){
        PC=pC;
    }

    public static int getIR(){
        return IR;
    }

    public static void setIR(int iR){
        IR=iR;
    }

    public static PCB getPcb(){
        return pcb;
    }

    public static void setPcb(PCB pcb){
        CPU.pcb=pcb;
    }

    public static CPUStates getPSW(){
        return PSW;
    }

    public static void setPSW(CPUStates pSW){
        PSW=pSW;
    }

}
