/*
 * @Author: Meguri Ichinose
 * @Date: 2022-02-10 14:52:35
 * @Description: 作业类
 */
package Process;

import java.util.ArrayList;

public class Job{

    private int JobsID;// 作业序号
    private int Priority;// 作业优先级
    private int InTimes;// 作业请求时间
    private int InstrucNum;// 作业包含的程序指令数目

    private ArrayList<Command> CommandQueue;// 指令队列

    private int DiskAddress;// 外存物理块地址

    public Job(int jobsID,int priority,int inTimes,int instrucNum,ArrayList<Command> commandQueue){
        JobsID=jobsID;
        Priority=priority;
        InTimes=inTimes;
        InstrucNum=instrucNum;
        CommandQueue=commandQueue;
    }

    public int getDiskAddress(){
        return DiskAddress;
    }

    public void setDiskAddress(int diskAddress){
        this.DiskAddress=diskAddress;
    }

    public int getJobsID(){
        return JobsID;
    }

    public void setJobsID(int jobsID){
        JobsID=jobsID;
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

    public int getInstrucNum(){
        return InstrucNum;
    }

    public void setInstrucNum(int instrucNum){
        InstrucNum=instrucNum;
    }

    public ArrayList<Command> getCommandQueue(){
        return CommandQueue;
    }

    public void setCommandQueue(ArrayList<Command> commandQueue){
        CommandQueue=commandQueue;
    }

    @Override
    public String toString(){
        String commandQueue="";
        for(int i=0;i<CommandQueue.size();i++){
            commandQueue+="\n   "+CommandQueue.get(i).toString();
        }
        return "Job [JobsID="+JobsID+", Priority="+Priority+", InTimes="+InTimes
                +", InstrucNum="+InstrucNum+"]"+commandQueue;
    }

}
