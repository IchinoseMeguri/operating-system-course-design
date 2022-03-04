/*
 * @Author: Meguri Ichinose
 * @Date: 2022-02-10 14:21:13
 * @Description: PCB类
 */
package Process;

import java.util.ArrayList;

import Hardware.Memory;

public class PCB{
    private int ProID;// 进程编号，整数
    private int Priority;// 进程优先级，随机生成[1-5]整数优先数，优先数越小，优先级越大；
    private int InTimes;// 进程创建时间，由仿真时钟开始计时，整数，假设每条指令执行时间 1s；
    private int EndTimes;// 进程结束时间，显示仿真时钟的时间，整数；
    private ProStates PSW;// 进程状态
    // 进程运行时间列表，统计记录进程开始运行时间、时长，时间由仿真时钟提供
    private ArrayList<RunTime> RunTimes=new ArrayList<RunTime>();

    private class RunTime{
        private int StartTime;// 开始运行时间
        private int TimeLong;// 时长

        public RunTime(int startTime,int timeLong){
            StartTime=startTime;
            TimeLong=timeLong;
        }

        public int getTimeLong(){
            return TimeLong;
        }

        public int getStartTime(){
            return StartTime;
        }

        public void setStartTime(int startTime){
            this.StartTime=startTime;
        }

        public void setTimeLong(int timeLong){
            this.TimeLong=timeLong;
        }
    }

    private int TurnTimes;// 进程周转时间统计

    private int InstrucNum;// 进程包含的指令数目
    private int PC;// 程序计数器信息，下一条将执行的指令编号
    private int IR;// 指令寄存器信息，正在执行的指令编号

    private int MemoryBaseAddress;// 内存基地址
    private int VirtualBaseAddress;// 虚存基地址

    private int[] PageNo=new int[Memory.PROCESS_PAGE_NUM];// 页号

    // 最新在就绪队列QueuePos[0]、5条阻塞队列QueuePos[1:5]的位置
    private QueuePos[] Queue=new QueuePos[6];

    private class QueuePos{
        private int QueueNum;// 位置编号
        private int QueueTimes;// 进入队列时间

        public int getQueueNum(){
            return QueueNum;
        }

        public void setQueueNum(int queueNum){
            QueueNum=queueNum;
        }

        public int getQueueTimes(){
            return QueueTimes;
        }

        public void setQueueTimes(int queueTimes){
            QueueTimes=queueTimes;
        }
    }

    private Job job;// 作业

    private int P_Address;// 物理地址
    private int BufferAddress;// 缓冲区地址
    private int DiskAddress;// 外存物理块地址
    private int InBufferTime;// 进入缓冲区时间

    public PCB(Job job){
        this.job=job;
        setProID(job.getJobsID());
        setPriority(job.getPriority());
        setInTimes(job.getInTimes());
        setInstrucNum(job.getInstrucNum());
        setTurnTimes(0);
        setPC(1);
        setIR(1);
        for(int i=0;i<this.Queue.length;i++){
            Queue[i]=new QueuePos();
        }
    }

    public int[] getPageNo(){
        return PageNo;
    }

    public void setPageNo(int[] pageNo){
        this.PageNo=pageNo;
    }

    public int getInBufferTime(){
        return InBufferTime;
    }

    public void setInBufferTime(int inBufferTime){
        this.InBufferTime=inBufferTime;
    }

    public int getVirtualBaseAddress(){
        return VirtualBaseAddress;
    }

    public void setVirtualBaseAddress(int virtualBaseAddress){
        this.VirtualBaseAddress=virtualBaseAddress;
    }

    public QueuePos[] getQueue(){
        return Queue;
    }

    public int getQueueNum(int index){
        return Queue[index].getQueueNum();
    }

    public int getQueueTimes(int index){
        return Queue[index].getQueueTimes();
    }

    public void setQueue(QueuePos[] queue){
        this.Queue=queue;
    }

    public void setQueue(int index,int queueNum,int queueTimes){
        Queue[index].setQueueNum(queueNum);
        Queue[index].setQueueTimes(queueTimes);
    }

    public Job getJob(){
        return job;
    }

    public void setJob(Job job){
        this.job=job;
    }

    public ArrayList<RunTime> getRunTimes(){
        return RunTimes;
    }

    public int getRunTimesStartTime(int index){
        return RunTimes.get(index).getStartTime();
    }

    public int getRunTimesTimeLong(int index){
        return RunTimes.get(index).getTimeLong();
    }

    public void setRunTimes(ArrayList<RunTime> runTimes){
        RunTimes=runTimes;
    }

    public void setRunTimesStartTime(int index,int startTime){
        RunTimes.get(index).setStartTime(startTime);
    }

    public void setRunTimesTimeLong(int index,int timeLong){
        RunTimes.get(index).setTimeLong(timeLong);
    }

    public void AddRunTime(int startTime,int timeLong){
        RunTimes.add(new RunTime(startTime,timeLong));
    }

    public int getTurnTimes(){
        return TurnTimes;
    }

    public void setTurnTimes(int turnTimes){
        this.TurnTimes=turnTimes;
    }

    public int getProID(){
        return ProID;
    }

    public void setProID(int proID){
        ProID=proID;
    }

    public int getPriority(){
        return Priority;
    }

    public void setPriority(int priority){
        Priority=priority;
    }

    public int getInTimes(){
        return InTimes;
    }

    public void setInTimes(int inTimes){
        InTimes=inTimes;
    }

    public int getEndTimes(){
        return EndTimes;
    }

    public void setEndTimes(int endTimes){
        EndTimes=endTimes;
    }

    public ProStates getPSW(){
        return PSW;
    }

    public void setPSW(ProStates pSW){
        PSW=pSW;
    }

    public int getInstrucNum(){
        return InstrucNum;
    }

    public void setInstrucNum(int instrucNum){
        InstrucNum=instrucNum;
    }

    public int getPC(){
        return PC;
    }

    public void setPC(int pC){
        PC=pC;
    }

    public int getIR(){
        return IR;
    }

    public void setIR(int iR){
        IR=iR;
    }

    public int getBaseAddress(){
        return MemoryBaseAddress;
    }

    public void setBaseAddress(int baseAddress){
        MemoryBaseAddress=baseAddress;
    }

    public int getMemoryBaseAddress(){
        return MemoryBaseAddress;
    }

    public void setMemoryBaseAddress(int memoryBaseAddress){
        MemoryBaseAddress=memoryBaseAddress;
    }

    public int getP_Address(){
        return P_Address;
    }

    public void setP_Address(int p_Address){
        P_Address=p_Address;
    }

    public int getBufferAddress(){
        return BufferAddress;
    }

    public void setBufferAddress(int bufferAddress){
        BufferAddress=bufferAddress;
    }

    public int getDiskAddress(){
        return DiskAddress;
    }

    public void setDiskAddress(int diskAddress){
        DiskAddress=diskAddress;
    }

}
