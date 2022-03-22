/*
 * @Author: Meguri Ichinose
 * @Date: 2022-02-16 20:41:31
 * @Description: 进程管理
 */
package Manager;

import java.util.ArrayList;

import GUI.Gui;
import Hardware.CPU;
import Hardware.CPUStates;
import Hardware.Clock;
import Hardware.Disk;
import Hardware.Memory;
import Process.Command;
import Process.Job;
import Process.PCB;
import Process.ProStates;
import Threads.JobRequestThread;

public class ProcessManage{
    private static ArrayList<Job> Jobs=new ArrayList<Job>();// 已载入程序的全部作业列表
    private static ArrayList<Job> Backup=new ArrayList<Job>();// 后备队列
    private static ArrayList<PCB> Ready=new ArrayList<PCB>();// 就绪队列
    private static ArrayList<PCB> Block1=new ArrayList<PCB>();// 阻塞队列1
    private static ArrayList<PCB> Block2=new ArrayList<PCB>();// 阻塞队列2
    private static ArrayList<PCB> Block3=new ArrayList<PCB>();// 阻塞队列3
    private static ArrayList<PCB> Block4=new ArrayList<PCB>();// 阻塞队列4
    private static ArrayList<PCB> Block5=new ArrayList<PCB>();// 阻塞队列5

    // 这些队列用于记录队列中进入过的进程的进入时间离开时间
    private static ArrayList<QueueDialogue> ReadyQueueDialogue=new ArrayList<QueueDialogue>();
    private static ArrayList<QueueDialogue> BlockQueue1Dialogue=new ArrayList<QueueDialogue>();
    private static ArrayList<QueueDialogue> BlockQueue2Dialogue=new ArrayList<QueueDialogue>();
    private static ArrayList<QueueDialogue> BlockQueue3Dialogue=new ArrayList<QueueDialogue>();
    private static ArrayList<QueueDialogue> BlockQueue4Dialogue=new ArrayList<QueueDialogue>();
    private static ArrayList<QueueDialogue> BlockQueue5Dialogue=new ArrayList<QueueDialogue>();

    private static ArrayList<PCB> PCBTable=new ArrayList<PCB>(8);// 系统PCB表

    /**
     * @description: 实时作业请求，随机创建一个新作业
     * @param
     * @return
     */
    public static void CreateNewJob(){
        // 生成作业号，随机生成优先级，进入时间为现在，指令数量与指令随机生成，与此同时遵循规律
        int JobID=Jobs.size()+1;
        int Priority=(int)(Math.random()*5)+1;
        int InTimes=Clock.getTime();
        int InstrucNum=(int)(Math.random()*30)+1;
        ArrayList<Command> Commands=new ArrayList<Command>();
        for(int i=0;i<InstrucNum;i++){
            int No=i+1;
            int State=(int)(Math.random()*7);
            int LA=0;
            int RunTime=0;
            switch(State){
                case 0:
                    LA=(int)(Math.random()*24)+1;
                    RunTime=1;
                    break;
                case 1:
                    LA=(int)(Math.random()*10)+10;
                    RunTime=2;
                    break;
                case 2:
                    LA=(int)(Math.random()*24)+1;
                    RunTime=2;
                    break;
                case 3:
                    LA=(int)(Math.random()*24)+1;
                    RunTime=3;
                    break;
                case 4:
                    LA=(int)(Math.random()*10)+10;
                    RunTime=3;
                    break;
                case 5:
                    LA=(int)(Math.random()*10)+10;
                    RunTime=4;
                    break;
                case 6:
                    LA=(int)(Math.random()*24)+1;
                    RunTime=5;
                    break;
            }
            Commands.add(new Command(No,State,LA,RunTime));
        }
        // 生成作业，加入到作业集中，加入到后备队列
        Job job=new Job(JobID,Priority,InTimes,InstrucNum,Commands);
        Jobs.add(job);
        Backup.add(job);
        // 为作业分配外存，置作业的外存地址值
        job.setDiskAddress(StorageManage.AllocateDisk(Disk.JOB_CAN_USE_BLOCK_NUM));
        Gui.DialogueUpdate(GUI.DialogueType.NEW,new PCB(job));
    }

    /**
     * @description: 获取进程在何队列
     * @param pcb
     * @return 队列号，若无，返回-1
     */
    public static int ProcessWhereQueue(PCB pcb){
        if(Ready.contains(pcb))
            return 0;
        if(Block1.contains(pcb))
            return 1;
        if(Block2.contains(pcb))
            return 2;
        if(Block3.contains(pcb))
            return 3;
        if(Block4.contains(pcb))
            return 4;
        if(Block5.contains(pcb))
            return 5;
        return -1;
    }

    /**
     * @description: 作业请求函数，对后备队列中符合条件的作业创建进程
     * @param
     * @return
     */
    public static void JobRequest(){
        ArrayList<Job> create=new ArrayList<Job>();
        // 遍历后备队列，找到所有符合条件的作业
        for(Job job:Backup){
            if(job.getInTimes()<=JobRequestThread.getTime()){
                create.add(job);
            }
        }
        // 为其创建进程
        for(Job job:create){
            CreateProcess(job);
        }
    }

    /**
     * @description: 创建进程原语，新建->就绪态
     * @param job
     *            作业
     * @return
     */
    public static void CreateProcess(Job job){
        if(Backup.remove(job)){
            PCB pcb=new PCB(job);
            pcb.setPSW(ProStates.READY);// 设置PCB的状态为就绪态
            PCBTable.add(pcb);// PCB表增加进程
            Backup.remove(job);// 后备队列移出作业// 追记：冗余代码
            Ready.add(pcb);// 将PCB移动到就绪队列
            pcb.setQueue(0,Ready.indexOf(pcb),Clock.getTime());// PCB中添加一条加入到就绪队列项目
            pcb.setBaseAddress(StorageManage.AllocateMemory(Memory.PROCESS_PAGE_NUM));// 设置基地址
            // 设置页表
            for(int i=0;i<Memory.PROCESS_PAGE_NUM;i++)
                PageTable.setProID(Memory.getBlockNo(pcb.getBaseAddress())+i,pcb.getProID());
            pcb.setVirtualBaseAddress(StorageManage.AllocateSwap(Disk.PROCESS_SWAP_PAGE));// 分配虚存并置值
            Gui.DialogueUpdate(GUI.DialogueType.CREATE,pcb);
        }
    }

    /**
     * @description: 撤销进程原语，运行态->撤销
     * @param pcb
     *            进程PCB
     * @return
     */
    public static void DeleteProcess(PCB pcb){
        pcb.setPSW(ProStates.EXIT);// 设置进程PCB状态字
        pcb.setEndTimes(Clock.getTime());// 设置结束时间
        pcb.setTurnTimes(pcb.getEndTimes()-pcb.getInTimes());// 设置周转时间
        PCBTable.remove(pcb);// 从PCB表中移除
        Ready.remove(pcb);// 从就绪队列中移除
        for(int i=0;i<Memory.PROCESS_PAGE_NUM;i++)
            PageTable.setItem(Memory.getBlockNo(pcb.getBaseAddress())+i,0,0);// 回收页表项
        StorageManage.ClearMemory(Memory.getBlockNo(pcb.getBaseAddress()),Memory.PROCESS_PAGE_NUM);// 回收内存
        StorageManage.ClearDisk(Disk.getBlockNo(pcb.getVirtualBaseAddress()),Disk.PROCESS_SWAP_PAGE);// 回收虚存
        Gui.DialogueUpdate(GUI.DialogueType.STOP,pcb);
    }

    /**
     * @description: 阻塞进程原语，运行态->阻塞态
     * @param pcb
     *            进程PCB
     * @param type
     *            指令类型
     * @return
     */
    public static void BlockProcess(PCB pcb,int type){
        pcb.setPSW(ProStates.BLOCKED);// 设置PCB状态字
        switch(type){// 根据指令类型加入到各自的队列中
            case 2:
                Block1.add(pcb);
                pcb.setQueue(type-1,Block1.indexOf(pcb),Clock.getTime());
                break;
            case 3:
                Block2.add(pcb);
                pcb.setQueue(type-1,Block2.indexOf(pcb),Clock.getTime());
                break;
            case 4:
                Block3.add(pcb);
                pcb.setQueue(type-1,Block3.indexOf(pcb),Clock.getTime());
                break;
            case 5:
                Block4.add(pcb);
                pcb.setQueue(type-1,Block4.indexOf(pcb),Clock.getTime());
                break;
            case 6:
                Block5.add(pcb);
                pcb.setQueue(type-1,Block5.indexOf(pcb),Clock.getTime());
                break;
        }
        CPU.ProtectScene();// CPU保护现场
        Gui.DialogueUpdate(GUI.DialogueType.BLOCK,pcb);
    }

    /**
     * @description: 唤醒进程原语，阻塞态->就绪态
     * @param pcb
     *            进程PCB
     * @param type
     *            指令类型
     * @return
     */
    public static void WakeupProcess(PCB pcb,int type){
        switch(type){// 根据指令类型从各自的队列中移除PCB
            case 2:
                Block1.remove(pcb);
                BlockQueue1Dialogue.add(new QueueDialogue(pcb,pcb.getQueueTimes(type-1),Clock.getTime()));
                break;
            case 3:
                Block2.remove(pcb);
                BlockQueue2Dialogue.add(new QueueDialogue(pcb,pcb.getQueueTimes(type-1),Clock.getTime()));
                break;
            case 4:
                Block3.remove(pcb);
                BlockQueue3Dialogue.add(new QueueDialogue(pcb,pcb.getQueueTimes(type-1),Clock.getTime()));
                break;
            case 5:
                Block4.remove(pcb);
                BlockQueue4Dialogue.add(new QueueDialogue(pcb,pcb.getQueueTimes(type-1),Clock.getTime()));
                break;
            case 6:
                Block5.remove(pcb);
                BlockQueue5Dialogue.add(new QueueDialogue(pcb,pcb.getQueueTimes(type-1),Clock.getTime()));
                break;
        }
        ReturnReady(pcb);// PCB返回就绪队列
    }

    /**
     * @description: 运行进程，将就绪队列中的进程交给CPU，就绪态->运行态
     * @param pcb
     * @return
     */
    public static void RunProcess(PCB pcb){
        Ready.remove(pcb);// 从就绪队列中移除PCB，加入到CPU
        pcb.setPSW(ProStates.RUNNING);// 设置状态字
        CPU.RestoreScene(pcb);// 恢复现场
        ReadyQueueDialogue.add(new QueueDialogue(pcb,pcb.getQueueTimes(0),Clock.getTime()));
        CPU.setPSW(CPUStates.USER);// 设置CPU状态为用户态
        CPU.setFree(false);// 置CPU为非空闲
    }

    /**
     * @description: 进程返回就绪队列
     * @param pcb
     * @return
     */
    public static void ReturnReady(PCB pcb){
        pcb.setPSW(ProStates.READY);// 设置状态字
        Ready.add(pcb);// 就绪队列加入PCB
        pcb.setQueue(0,Ready.indexOf(pcb),Clock.getTime());
        Gui.DialogueUpdate(GUI.DialogueType.WAKEUP,pcb);
    }

    /**
     * @description: 程序启动时的初始操作
     * @param
     * @return
     */
    public static void Init(){
        for(Job job:Jobs){// 对已经读取的作业加入到就绪队列并分配外存
            Backup.add(job);
            job.setDiskAddress(StorageManage.AllocateDisk(Disk.JOB_CAN_USE_BLOCK_NUM));
            Gui.DialogueUpdate(GUI.DialogueType.NEW,new PCB(job));
        }
    }

    public static ArrayList<PCB> getPCBTable(){
        return PCBTable;
    }

    public static void setPCBTable(ArrayList<PCB> pCBTable){
        PCBTable=pCBTable;
    }

    public static ArrayList<Job> getBackup(){
        return Backup;
    }

    public static void setBackup(ArrayList<Job> backup){
        Backup=backup;
    }

    public static ArrayList<PCB> getReady(){
        return Ready;
    }

    public static void setReady(ArrayList<PCB> ready){
        Ready=ready;
    }

    public static ArrayList<PCB> getBlock1(){
        return Block1;
    }

    public static void setBlock1(ArrayList<PCB> block1){
        Block1=block1;
    }

    public static ArrayList<PCB> getBlock2(){
        return Block2;
    }

    public static void setBlock2(ArrayList<PCB> block2){
        Block2=block2;
    }

    public static ArrayList<PCB> getBlock3(){
        return Block3;
    }

    public static void setBlock3(ArrayList<PCB> block3){
        Block3=block3;
    }

    public static ArrayList<PCB> getBlock4(){
        return Block4;
    }

    public static void setBlock4(ArrayList<PCB> block4){
        Block4=block4;
    }

    public static ArrayList<PCB> getBlock5(){
        return Block5;
    }

    public static void setBlock5(ArrayList<PCB> block5){
        Block5=block5;
    }

    public static ArrayList<Job> getJobs(){
        return Jobs;
    }

    public static void setJobs(ArrayList<Job> jobs){
        Jobs=jobs;
    }

    public static ArrayList<QueueDialogue> getReadyQueueDialogue(){
        return ReadyQueueDialogue;
    }

    public static void setReadyQueueDialogue(ArrayList<QueueDialogue> readyQueueDialogue){
        ReadyQueueDialogue=readyQueueDialogue;
    }

    public static ArrayList<QueueDialogue> getBlockQueue1Dialogue(){
        return BlockQueue1Dialogue;
    }

    public static void setBlockQueue1Dialogue(ArrayList<QueueDialogue> blockQueue1Dialogue){
        BlockQueue1Dialogue=blockQueue1Dialogue;
    }

    public static ArrayList<QueueDialogue> getBlockQueue2Dialogue(){
        return BlockQueue2Dialogue;
    }

    public static void setBlockQueue2Dialogue(ArrayList<QueueDialogue> blockQueue2Dialogue){
        BlockQueue2Dialogue=blockQueue2Dialogue;
    }

    public static ArrayList<QueueDialogue> getBlockQueue3Dialogue(){
        return BlockQueue3Dialogue;
    }

    public static void setBlockQueue3Dialogue(ArrayList<QueueDialogue> blockQueue3Dialogue){
        BlockQueue3Dialogue=blockQueue3Dialogue;
    }

    public static ArrayList<QueueDialogue> getBlockQueue4Dialogue(){
        return BlockQueue4Dialogue;
    }

    public static void setBlockQueue4Dialogue(ArrayList<QueueDialogue> blockQueue4Dialogue){
        BlockQueue4Dialogue=blockQueue4Dialogue;
    }

    public static ArrayList<QueueDialogue> getBlockQueue5Dialogue(){
        return BlockQueue5Dialogue;
    }

    public static void setBlockQueue5Dialogue(ArrayList<QueueDialogue> blockQueue5Dialogue){
        BlockQueue5Dialogue=blockQueue5Dialogue;
    }
}
