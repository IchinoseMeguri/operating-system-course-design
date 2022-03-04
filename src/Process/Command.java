/*
 * @Author: Meguri Ichinose
 * @Date: 2022-02-10 15:10:53
 * @Description: 指令类
 */
package Process;

public class Command{

    private int Instruc_ID;// 指令编号作业，创建进程以后，进程所执行用户程序段指令序号，从 1 开始计数。
    private int Instruc_State;// 用户程序指令的类型
    /*
     * 每条指令的类型标志为 Instruc_State：
     * 0 表示用户态计算操作语句。进程正常调度，当时间片到时，进程切换；InRunTimes=1s；
     * 1 表示用户态计算操作函数。进程正常调度，当时间片到时，进程切换；InRunTimes=2s；
     * 2 表示键盘输入变量语句。发生系统调用，CPU 进行模式切换，运行进程进入阻塞态；
     * 值守的键盘操作输入模块接收输入变量或输出变量内容，InRunTimes=2s 后完成输入，产生
     * 硬件终端信息号，阻塞队列 1 的队头节点出对，进入就绪队列；
     * 3 屏幕显示输出变量语句。发生系统调用，CPU 进行模式切换，运行进程进入阻塞态；
     * 值守的屏幕显示模块输出变量内容，InRunTimes=1s 后完成显示，产生硬件终端信息号，阻
     * 塞队列 2 的队头节点出队，进入就绪队列；
     * 4 磁盘文件读操作函数。发生进程调度，进程进入阻塞态；同时关中断，假设
     * InRunTimes=3s 以后读完文件内容到内存缓冲区，操作系统的读文件模块负责将缓冲区数据
     * 拷贝到用户进程 PCB 区域，产生唤醒信号，阻塞队列队 3 的头节点出队，进程进入就绪队
     * 列；
     * 5 磁盘文件写操作函数。进程进入阻塞态；同时关中断，假设 InRunTimes=4s 以后写文
     * 件数据到内存缓冲区，操作系统的写文件模块负责将缓冲区数据写入磁盘物理块，产生进程
     * 唤醒信号，阻塞队列队 4 的头节点出队，进程进入就绪队列；
     * 6 打印操作语句。假设 InRunTimes=4s 以后拷贝打印数据到内存缓冲区，Spooling 系统
     * 完成打印任务。发出打印请求的用户作业，在打印数据拷贝到内存缓冲区以后，阻塞队列队
     * 5 头节点出队，进入就绪队列；
     */
    private int L_Address;// 用户程序指令访问的逻辑地址
    /*
     * 用户程序指令访问的逻辑地址（L_Address）的生成：物理上一共有 24 块物理块可用，
     * Instruc_State InRunTimes L_Address
     * 0 1s 顺序计数，与下一个语句段地址可以相同或者相隔 2 个地址
     * 2 2s 顺序计数，与下一个语句段地址可以相同或者相隔 2 个地址
     * 3 3s 顺序计数，与下一个语句段地址可以相同或者相隔 2 个地址
     * 6 4s 顺序计数，与下一个语句段地址可以相同或者相隔 2 个地址
     * 1 2s 跳跃，在 10-20 之间产生随机整数，作为逻辑地址
     * 4 3s 跳跃，在 10-20 之间产生随机整数，作为逻辑地址
     * 5 4s 跳跃，在 10-20 之间产生随机整数，作为逻辑地址
     */
    private int InRunTimes;// 每条指令运行时间

    public Command(int instruc_ID,int instruc_State,int l_Address,int inRunTimes){
        Instruc_ID=instruc_ID;
        Instruc_State=instruc_State;
        L_Address=l_Address;
        InRunTimes=inRunTimes;
    }

    public int getInstruc_ID(){
        return Instruc_ID;
    }

    public void setInstruc_ID(int instruc_ID){
        Instruc_ID=instruc_ID;
    }

    public int getInstruc_State(){
        return Instruc_State;
    }

    public void setInstruc_State(int instruc_State){
        Instruc_State=instruc_State;
    }

    public int getL_Address(){
        return L_Address;
    }

    public void setL_Address(int l_Address){
        L_Address=l_Address;
    }

    public int getInRunTimes(){
        return InRunTimes;
    }

    public void setInRunTimes(int inRunTimes){
        InRunTimes=inRunTimes;
    }

    @Override
    public String toString(){
        return "Command [Instruc_ID="+Instruc_ID+", Instruc_State="+Instruc_State
                +", L_Address="+L_Address+", InRunTimes="+InRunTimes+"]";
    }

}
