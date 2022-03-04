/*
 * @Author: Meguri Ichinose
 * @Date: 2022-02-21 17:46:33
 * @Description: 日志等字符串生成
 */
package GUI;

import java.util.ArrayList;

import Hardware.CPU;
import Hardware.Clock;
import Manager.ProcessManage;
import Manager.QueueDialogue;
import Manager.StorageManage;
import Process.Job;
import Process.PCB;
import Threads.BufferThread;
import Threads.JobRequestThread;
import Threads.ProcessSchedulingThread;

public class Log{
    private static String ProcessDialogue="";
    private static String BufferDialogue="";

    /**
     * @description: 获取CPU状态
     * @param
     * @return
     */
    public static String getCPUState(){
        if(CPU.isFree())
            return "空闲";
        switch(CPU.getPSW()){
            case KERNEL:
                return "核心态";
            case USER:
                return "用户态";
        }
        return "";
    }

    public static String getBufferDialogue(){
        return BufferDialogue;
    }

    public static void setBufferDialogue(String bufferDialogue){
        Log.BufferDialogue=bufferDialogue;
    }

    public static String getProcessDialogue(){
        return ProcessDialogue;
    }

    public static void setProcessDialogue(String processDialogue){
        Log.ProcessDialogue=processDialogue;
    }

    public static void ProcessDialogueAppend(String str){
        ProcessDialogue+=str;
    }

    public static void BufferDialogueAppend(String str){
        BufferDialogue+=str;
    }

    /**
     * @description: 获取后备作业队列信息字符串
     * @param
     * @return 字符串
     */
    public static String getBackupQueue(){
        String str="[";
        for(int i=0;i<ProcessManage.getBackup().size();i++){
            if(i!=0)
                str+=",";
            str+=ProcessManage.getBackup().get(i).getJobsID();
        }
        return str+"]";
    }

    /**
     * @description: 获取就绪队列信息字符串
     * @param
     * @return 字符串
     */
    public static String getReadyQueue(){
        String str="[";
        for(int i=0;i<ProcessManage.getReady().size();i++){
            if(i!=0)
                str+=",";
            str+=ProcessManage.getReady().get(i).getProID();
        }
        return str+"]";
    }

    /**
     * @description: 获取阻塞队列1字符串
     * @param
     * @return 字符串
     */
    public static String getBlock1Queue(){
        String str="[";
        for(int i=0;i<ProcessManage.getBlock1().size();i++){
            if(i!=0)
                str+=",";
            str+=ProcessManage.getBlock1().get(i).getProID();
        }
        return str+"]";
    }

    /**
     * @description: 获取阻塞队列2字符串
     * @param
     * @return 字符串
     */
    public static String getBlock2Queue(){
        String str="[";
        for(int i=0;i<ProcessManage.getBlock2().size();i++){
            if(i!=0)
                str+=",";
            str+=ProcessManage.getBlock2().get(i).getProID();
        }
        return str+"]";
    }

    /**
     * @description: 获取阻塞队列3字符串
     * @param
     * @return 字符串
     */
    public static String getBlock3Queue(){
        String str="[";
        for(int i=0;i<ProcessManage.getBlock3().size();i++){
            if(i!=0)
                str+=",";
            str+=ProcessManage.getBlock3().get(i).getProID();
        }
        return str+"]";
    }

    /**
     * @description: 获取阻塞队列4字符串
     * @param
     * @return 字符串
     */
    public static String getBlock4Queue(){
        String str="[";
        for(int i=0;i<ProcessManage.getBlock4().size();i++){
            if(i!=0)
                str+=",";
            str+=ProcessManage.getBlock4().get(i).getProID();
        }
        return str+"]";
    }

    /**
     * @description: 获取阻塞队列5字符串
     * @param
     * @return 字符串
     */
    public static String getBlock5Queue(){
        String str="[";
        for(int i=0;i<ProcessManage.getBlock5().size();i++){
            if(i!=0)
                str+=",";
            str+=ProcessManage.getBlock5().get(i).getProID();
        }
        return str+"]";
    }

    /**
     * @description: 获取作业信息字符串
     * @param
     * @return 字符串
     */
    public static String getJobsString(){
        String str="作业序号\t优先级\t请求时间\t指令数目\n";
        ArrayList<Job> Jobs=ProcessManage.getJobs();
        for(int i=0;i<Jobs.size();i++){
            str+=Jobs.get(i).getJobsID()+"\t"+Jobs.get(i).getPriority()
                    +"\t"+Jobs.get(i).getInTimes()+"\t"+Jobs.get(i).getInstrucNum()+"\n";
        }
        return str;
    }

    /**
     * @description: 获取进程信息字符串
     * @param
     * @return 字符串
     */
    public static String getProcessString(){
        String str="进程\t指令\t类型\t状态\n";
        ArrayList<PCB> PCBs=ProcessManage.getPCBTable();
        for(int i=0;i<PCBs.size();i++){
            str+=PCBs.get(i).getProID()+"\t"+PCBs.get(i).getIR()+"\t";
            if(PCBs.get(i).getIR()>PCBs.get(i).getInstrucNum())
                str+="-";
            else
                str+=PCBs.get(i).getJob().getCommandQueue().get(PCBs.get(i).getIR()-1).getInstruc_State();
            str+="\t";
            switch(PCBs.get(i).getPSW()){
                case BLOCKED:
                    str+="阻塞态";
                    break;
                case EXIT:
                    str+="撤销态";
                    break;
                case NEW:
                    str+="新建态";
                    break;
                case READY:
                    str+="就绪态";
                    break;
                case RUNNING:
                    str+="运行态";
                    break;
            }
            str+="\n";
        }
        return str;
    }

    /**
     * @description: 运行日志拼接字符串
     * @param dialogueType
     *            日志类型
     * @param pcb
     *            进程PCB
     * @return 字符串
     */
    public static String getDialogue(DialogueType dialogueType,PCB pcb){
        String str="";
        switch(dialogueType){
            case BLOCK:
                str+=ProcessSchedulingThread.getTime()+":[";
                str+="阻塞进程:"+pcb.getProID()+":"+ProcessManage.ProcessWhereQueue(pcb);
                break;
            case BUFFER_IN:
                str+=BufferThread.getTime()+":[";
                str+="入缓冲区:"+pcb.getProID()+":"+pcb.getIR()+",类型4,磁盘文件读操作函数:"
                        +pcb.getJob().getCommandQueue().get(pcb.getIR()-2).getL_Address()+","
                        +pcb.getP_Address()+","+pcb.getBufferAddress()+","+pcb.getDiskAddress();
                break;
            case BUFFER_OUT:
                str+=BufferThread.getTime()+":[";
                str+="入缓冲区:"+pcb.getProID()+":"+pcb.getIR()+",类型5,磁盘文件写操作函数:"
                        +pcb.getJob().getCommandQueue().get(pcb.getIR()-2).getL_Address()+","
                        +pcb.getP_Address()+","+pcb.getBufferAddress()+","+pcb.getDiskAddress();
                break;
            case CREATE:
                str+=JobRequestThread.getTime()+":[";
                str+="创建进程:"+pcb.getProID()+":"+pcb.getBaseAddress();
                break;
            case FREE:
                str+=ProcessSchedulingThread.getTime()+":[";
                str+="CPU空闲";
                break;
            case NEW:
                str+=Clock.getTime()+":[";
                str+="新增作业:"+pcb.getProID();
                break;
            case PRINT:
                str+=ProcessSchedulingThread.getTime()+":[";
                str+="打印请求:"+pcb.getProID()+":"+pcb.getIR()+","+pcb.getJob().getCommandQueue().get(pcb.getIR()-1)
                        .getInstruc_State()+","+pcb.getJob().getCommandQueue().get(pcb.getIR()-1).getL_Address()+","
                        +pcb.getP_Address();
                break;
            case RUN:
                str+=ProcessSchedulingThread.getTime()+":[";
                str+="运行进程:"+pcb.getProID()+":"+pcb.getIR()+",";
                int state=pcb.getJob().getCommandQueue().get(pcb.getIR()-1).getInstruc_State();
                switch(state){
                    case 0:
                        str+="用户态计算操作语句";
                        break;
                    case 1:
                        str+="用户态计算操作函数";
                        break;
                    case 2:
                        str+="键盘输入变量语句";
                        break;
                    case 3:
                        str+="屏幕显示输出变量语句";
                        break;
                    case 4:
                        str+="磁盘文件读操作函数";
                        break;
                    case 5:
                        str+="磁盘文件写操作函数";
                        break;
                    case 6:
                        str+="打印操作语句";
                        break;
                }
                str+=":"+pcb.getJob().getCommandQueue().get(pcb.getIR()-1).getL_Address()+","+pcb.getP_Address()
                        +","+pcb.getJob().getCommandQueue().get(pcb.getIR()-1).getInRunTimes();
                break;
            case STOP:
                str+=ProcessSchedulingThread.getTime()+":[";
                str+="终止进程:"+pcb.getProID();
                break;
            case WAKEUP:
                str+=ProcessSchedulingThread.getTime()+":[";
                str+="重新进入就绪队列:"+pcb.getProID();
                break;
            case BUFFER_FREE:
                str+=BufferThread.getTime()+":[";
                str+="缓冲区无进程";
                break;
            case P:
                str+=BufferThread.getTime()+":[";
                str+="P操作";
                break;
            case V:
                str+=BufferThread.getTime()+":[";
                str+="V操作";
                break;
            case COPY_IN:
                str+=BufferThread.getTime()+":[";
                str+="拷贝入缓冲区";
                break;
            case COPY_OUT:
                str+=BufferThread.getTime()+":[";
                str+="拷贝出缓冲区";
                break;
            case PRINT_BUFFER:
                str+=BufferThread.getTime()+":[";
                str+="入缓冲区:"+pcb.getProID()+":"+pcb.getIR()+",类型6,打印操作语句:"
                        +pcb.getJob().getCommandQueue().get(pcb.getIR()-2).getL_Address()+","
                        +pcb.getP_Address()+","+pcb.getBufferAddress()+","+pcb.getDiskAddress();
                break;
            default:
                break;
        }
        return str+"]\n";
    }

    /**
     * @description: 获取各个队列和缓冲区状态事件的字符串
     * @param
     * @return 字符串
     */
    public static String getStateDialogue(){
        String[] str=new String[7];
        str[0]="FFFF:[就绪队列:";
        for(QueueDialogue log:ProcessManage.getReadyQueueDialogue()){
            str[0]+=log.getInTime()+"="+log.getPcb().getProID();
            if(ProcessManage.getReadyQueueDialogue().indexOf(log)!=(ProcessManage.getReadyQueueDialogue().size()-1))
                str[0]+=";";
        }
        str[1]="]\nFFFF:[阻塞队列1:";
        for(QueueDialogue log:ProcessManage.getBlockQueue1Dialogue()){
            str[1]+=log.getInTime()+"/"+log.getOutTime()+"="+log.getPcb().getProID();
            if(ProcessManage.getBlockQueue1Dialogue().indexOf(log)!=(ProcessManage.getBlockQueue1Dialogue().size()-1))
                str[1]+=";";
        }
        str[2]="]\nFFFF:[阻塞队列2:";
        for(QueueDialogue log:ProcessManage.getBlockQueue2Dialogue()){
            str[2]+=log.getInTime()+"/"+log.getOutTime()+"="+log.getPcb().getProID();
            if(ProcessManage.getBlockQueue2Dialogue().indexOf(log)!=(ProcessManage.getBlockQueue2Dialogue().size()-1))
                str[2]+=";";
        }
        str[3]="]\nFFFF:[阻塞队列3:";
        for(QueueDialogue log:ProcessManage.getBlockQueue3Dialogue()){
            str[3]+=log.getInTime()+"/"+log.getOutTime()+"="+log.getPcb().getProID();
            if(ProcessManage.getBlockQueue3Dialogue().indexOf(log)!=(ProcessManage.getBlockQueue3Dialogue().size()-1))
                str[3]+=";";
        }
        str[4]="]\nFFFF:[阻塞队列4:";
        for(QueueDialogue log:ProcessManage.getBlockQueue4Dialogue()){
            str[4]+=log.getInTime()+"/"+log.getOutTime()+"="+log.getPcb().getProID();
            if(ProcessManage.getBlockQueue4Dialogue().indexOf(log)!=(ProcessManage.getBlockQueue4Dialogue().size()-1))
                str[4]+=";";
        }
        str[5]="]\nFFFF:[阻塞队列5:";
        for(QueueDialogue log:ProcessManage.getBlockQueue5Dialogue()){
            str[5]+=log.getInTime()+"/"+log.getOutTime()+"="+log.getPcb().getProID();
            if(ProcessManage.getBlockQueue5Dialogue().indexOf(log)!=(ProcessManage.getBlockQueue5Dialogue().size()-1))
                str[5]+=";";
        }
        str[6]="]\n\nMMMM:[缓冲区:";
        for(QueueDialogue log:StorageManage.getBufferDialogue()){
            str[6]+=log.getInTime()+"="+log.getPcb().getProID();
            if(StorageManage.getBufferDialogue().indexOf(log)!=(StorageManage.getBufferDialogue().size()-1))
                str[6]+=";";
        }
        str[6]+="]\n";

        String string="";
        for(int i=0;i<str.length;i++){
            string+=str[i];
        }
        return string;
    }

    /**
     * @description: 获取输出的字符串
     * @param
     * @return 字符串
     */
    public static String getOutputString(){
        return "作业/进程调度事件:\n"+ProcessDialogue+"\n缓冲区处理事件:\n"+BufferDialogue+"\n状态统计:\n"+getStateDialogue();
    }

    /**
     * @description: 获取缓冲区的进程
     * @param
     * @return 进程号的字符串
     */
    public static String getBufferNowUsing(){
        return BufferThread.getBuffer()==null?"":""+BufferThread.getBuffer().getProID();
    }

    /**
     * @description: 获取等待进入缓冲区的进程
     * @param
     * @return 字符串
     */
    public static String getBufferQueue(){
        String str="[";
        for(int i=1;i<BufferThread.getBufferQueue().size();i++){
            if(i!=1)
                str+=",";
            str+=BufferThread.getBufferQueue().get(i).getProID();
        }
        return str+"]";
    }
}
