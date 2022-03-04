/*
 * @Author: Meguri Ichinose
 * @Date: 2022-02-13 11:23:48
 * @Description: 图形界面设计
 */
package GUI;

import Hardware.CPU;
import Hardware.Clock;
import Hardware.Memory;
import Manager.ProcessManage;
import Manager.StorageManage;
import Operation.TXTOperation;
import Process.PCB;
import Threads.BufferThread;
import Threads.IOOperationThread;
import Threads.JobRequestThread;
import Threads.PageFaultThread;
import Threads.ProcessSchedulingThread;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.FlowLayout;
import java.io.IOException;

public class Gui extends JFrame{
    private static JButton ReadInJobs;// 按钮：读入作业
    private static JButton CreateNewJob;// 按钮：新建作业（实时作业请求）
    private static JButton Start;// 按钮：开始
    private static JButton Suspend;// 按钮：暂停或者继续
    private static JButton SpeedUp;// 按钮：速度（x1、x2、x4、x8）
    private static JButton OutputDialogue;// 按钮：输入日志文件
    private static JPanel ButtonAreaPanel;

    private static int SpeedFlag=1;// 标志界面运行速度，值只能为1/2/4/8
    private static boolean isRunning=false;// 标志程序是否正在运行

    private static JScrollPane DialogueScrollPane;
    private static JTextArea Dialogue;
    private static JScrollPane JobInformationScrollPane;
    private static JTextArea JobInformation;
    private static JScrollPane processInformationScrollPane;
    private static JTextArea ProcessInformation;

    private static JPanel MemoryBitMapPanel;
    private static JTextField[] MemoryBitMap;

    private static JPanel RunningInformationPanel;
    private static JPanel ClockTimePanel;
    private static JTextField ClockTime;
    private static JPanel CPUStatePanel;
    private static JTextField CPUState;
    private static JPanel RunningProcessPanel;
    private static JTextField RunningProcess;
    private static JPanel RunningCommandPanel;
    private static JTextField RunningCommand;
    private static JPanel RunningCommandStatePanel;
    private static JTextField RunningCommandState;
    private static JPanel RunningCommandLAddressPanel;
    private static JTextField RunningCommandLAddress;
    private static JPanel RunningCommandPAddressPanel;
    private static JTextField RunningCommandPAddress;

    private static JTextField BufferNowUsing;
    private static JPanel BufferNowUsingPanel;
    private static JTextField BufferWaitUse;
    private static JPanel BufferWaitUsePanel;
    private static JPanel BufferMessagePanel;

    private static JPanel QueuesPanel;
    private static JPanel BackupQueuePanel;
    private static JPanel ReadyQueuePanel;
    private static JPanel BlockQueue1Panel;
    private static JPanel BlockQueue2Panel;
    private static JPanel BlockQueue3Panel;
    private static JPanel BlockQueue4Panel;
    private static JPanel BlockQueue5Panel;
    private static JTextField BackupQueue;
    private static JTextField ReadyQueue;
    private static JTextField BlockQueue1;
    private static JTextField BlockQueue2;
    private static JTextField BlockQueue3;
    private static JTextField BlockQueue4;
    private static JTextField BlockQueue5;

    private static JPanel Up;
    private static JPanel Down;
    private static JPanel DownLeft;
    private static JPanel DownMid;
    private static JPanel DownRight;
    private static JPanel DownMidUp;
    private static JPanel DownMidMid;
    private static JPanel DownMidDown;

    /**
     * @description: 构造函数，统筹布局
     * @param
     * @return
     */
    public Gui(){
        setTitle("并发环境下作业管理与连续动态内存管理的模拟");

        // 窗口关闭时结束本程序
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        ButtonAreaPanel=new JPanel();
        ReadInJobs=new JButton("读入作业信息");
        ReadInJobs.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                try{
                    System.out.println("Read in jobs:");
                    ProcessManage.setJobs(TXTOperation.GetJobsQueue());
                }catch(IOException e1){
                    e1.printStackTrace();
                }
                JobInformation.setText(Log.getJobsString());
                JobInformation.setCaretPosition(JobInformation.getDocument().getLength());
                ReadInJobs.setEnabled(false);
            }
        });
        CreateNewJob=new JButton("实时作业请求");
        CreateNewJob.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                System.out.println("Create new job.");
                ProcessManage.CreateNewJob();
                JobInformation.setText(Log.getJobsString());
                JobInformation.setCaretPosition(JobInformation.getDocument().getLength());
            }
        });
        Start=new JButton("开始");
        Start.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                // 正在运行标志
                isRunning=true;
                // 初始化
                ProcessManage.Init();
                StorageManage.Init();
                Clock.setStop(false);
                // 开启6个线程
                Clock clock=new Clock();
                JobRequestThread jobRequestThread=new JobRequestThread();
                ProcessSchedulingThread processSchedulingThread=new ProcessSchedulingThread();
                PageFaultThread pageFaultThread=new PageFaultThread();
                IOOperationThread iOOperationThread=new IOOperationThread();
                BufferThread bufferThread=new BufferThread();
                clock.start();
                jobRequestThread.start();
                processSchedulingThread.start();
                pageFaultThread.start();
                iOOperationThread.start();
                bufferThread.start();
                CreateNewJob.setEnabled(true);
                Suspend.setEnabled(true);
                Start.setEnabled(false);
            }
        });
        Suspend=new JButton("暂停");
        Suspend.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                if(isRunning){
                    Clock.setStop(true);
                    isRunning=false;
                    Suspend.setText("继续");
                    System.out.println("Suspend.");
                }else{
                    Clock.setStop(false);
                    isRunning=true;
                    Suspend.setText("暂停");
                    System.out.println("Continue.");
                }
            }
        });
        SpeedUp=new JButton("速度x"+SpeedFlag);
        // 加速功能
        SpeedUp.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                switch(SpeedFlag){
                    case 1:
                        SpeedFlag=2;
                        break;
                    case 2:
                        SpeedFlag=4;
                        break;
                    case 4:
                        SpeedFlag=8;
                        break;
                    case 8:
                        SpeedFlag=1;
                        break;
                }
                Clock.setSpeed(SpeedFlag);
                SpeedUp.setText("速度x"+SpeedFlag);
                System.out.println("Speed: "+SpeedFlag);
            }
        });
        OutputDialogue=new JButton("输出日志文件");
        OutputDialogue.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                System.out.print("Output dialogue to file: ");
                try{
                    TXTOperation.SaveProcessResults(Log.getOutputString(),Clock.getTime());
                }catch(IOException e1){
                    e1.printStackTrace();
                }
                OutputDialogue.setEnabled(false);
            }
        });
        ButtonAreaPanel.add(ReadInJobs);
        ButtonAreaPanel.add(CreateNewJob);
        ButtonAreaPanel.add(Start);
        ButtonAreaPanel.add(Suspend);
        ButtonAreaPanel.add(SpeedUp);
        ButtonAreaPanel.add(OutputDialogue);
        CreateNewJob.setEnabled(false);
        Suspend.setEnabled(false);

        Dialogue=new JTextArea("",33,30);
        DialogueScrollPane=new JScrollPane(Dialogue);

        JobInformation=new JTextArea("",10,30);
        JobInformationScrollPane=new JScrollPane(JobInformation);

        ProcessInformation=new JTextArea("",10,30);
        processInformationScrollPane=new JScrollPane(ProcessInformation);

        MemoryBitMapPanel=new JPanel();
        MemoryBitMapPanel.setLayout(new GridLayout(5,9));
        MemoryBitMap=new JTextField[Memory.BLOCK_NUM];
        for(int i=-1;i<4;i++){
            for(int j=-1;j<8;j++){
                if(i==-1){
                    if(j==-1){
                        MemoryBitMapPanel.add(new JLabel(""));
                    }else{
                        MemoryBitMapPanel.add(new JLabel(""+j));
                    }
                }else{
                    if(j==-1){
                        MemoryBitMapPanel.add(new JLabel(""+8*i));
                    }else{
                        MemoryBitMap[8*i+j]=new JTextField("0");
                        MemoryBitMapPanel.add(MemoryBitMap[8*i+j]);
                    }
                }
            }
        }

        RunningInformationPanel=new JPanel();
        RunningInformationPanel.setLayout(new GridLayout(8,1));
        ClockTimePanel=new JPanel();
        CPUStatePanel=new JPanel();
        RunningProcessPanel=new JPanel();
        RunningCommandPanel=new JPanel();
        RunningCommandStatePanel=new JPanel();
        RunningCommandLAddressPanel=new JPanel();
        RunningCommandPAddressPanel=new JPanel();
        ClockTimePanel.setLayout(new FlowLayout(FlowLayout.TRAILING));
        CPUStatePanel.setLayout(new FlowLayout(FlowLayout.TRAILING));
        RunningProcessPanel.setLayout(new FlowLayout(FlowLayout.TRAILING));
        RunningCommandPanel.setLayout(new FlowLayout(FlowLayout.TRAILING));
        RunningCommandStatePanel.setLayout(new FlowLayout(FlowLayout.TRAILING));
        RunningCommandLAddressPanel.setLayout(new FlowLayout(FlowLayout.TRAILING));
        RunningCommandPAddressPanel.setLayout(new FlowLayout(FlowLayout.TRAILING));
        ClockTime=new JTextField("0",15);
        CPUState=new JTextField("",15);
        RunningProcess=new JTextField("",15);
        RunningCommand=new JTextField("",15);
        RunningCommandState=new JTextField("",15);
        RunningCommandLAddress=new JTextField("",15);
        RunningCommandPAddress=new JTextField("",15);
        ClockTimePanel.add(new JLabel("时间"));
        ClockTimePanel.add(ClockTime);
        CPUStatePanel.add(new JLabel("CPU状态"));
        CPUStatePanel.add(CPUState);
        RunningProcessPanel.add(new JLabel("正在执行进程"));
        RunningProcessPanel.add(RunningProcess);
        RunningCommandPanel.add(new JLabel("正在执行指令"));
        RunningCommandPanel.add(RunningCommand);
        RunningCommandStatePanel.add(new JLabel("指令类型"));
        RunningCommandStatePanel.add(RunningCommandState);
        RunningCommandLAddressPanel.add(new JLabel("访问逻辑地址"));
        RunningCommandLAddressPanel.add(RunningCommandLAddress);
        RunningCommandPAddressPanel.add(new JLabel("访问物理地址"));
        RunningCommandPAddressPanel.add(RunningCommandPAddress);
        RunningInformationPanel.add(new JLabel("运行信息"));
        RunningInformationPanel.add(ClockTimePanel);
        RunningInformationPanel.add(CPUStatePanel);
        RunningInformationPanel.add(RunningProcessPanel);
        RunningInformationPanel.add(RunningCommandPanel);
        RunningInformationPanel.add(RunningCommandStatePanel);
        RunningInformationPanel.add(RunningCommandLAddressPanel);
        RunningInformationPanel.add(RunningCommandPAddressPanel);

        BufferMessagePanel=new JPanel();
        BufferMessagePanel.setLayout(new GridLayout(3,1));
        BufferNowUsingPanel=new JPanel();
        BufferWaitUsePanel=new JPanel();
        BufferNowUsingPanel.setLayout(new FlowLayout(FlowLayout.TRAILING));
        BufferWaitUsePanel.setLayout(new FlowLayout(FlowLayout.TRAILING));
        BufferNowUsing=new JTextField("",15);
        BufferWaitUse=new JTextField("",15);
        BufferNowUsingPanel.add(new JLabel("正在使用"));
        BufferNowUsingPanel.add(BufferNowUsing);
        BufferWaitUsePanel.add(new JLabel("等待使用"));
        BufferWaitUsePanel.add(BufferWaitUse);
        BufferMessagePanel.add(new JLabel("缓冲区"));
        BufferMessagePanel.add(BufferNowUsingPanel);
        BufferMessagePanel.add(BufferWaitUsePanel);

        QueuesPanel=new JPanel();
        QueuesPanel.setLayout(new GridLayout(8,1));
        BackupQueuePanel=new JPanel();
        ReadyQueuePanel=new JPanel();
        BlockQueue1Panel=new JPanel();
        BlockQueue2Panel=new JPanel();
        BlockQueue3Panel=new JPanel();
        BlockQueue4Panel=new JPanel();
        BlockQueue5Panel=new JPanel();
        BackupQueuePanel.setLayout(new FlowLayout(FlowLayout.TRAILING));
        ReadyQueuePanel.setLayout(new FlowLayout(FlowLayout.TRAILING));
        BlockQueue1Panel.setLayout(new FlowLayout(FlowLayout.TRAILING));
        BlockQueue2Panel.setLayout(new FlowLayout(FlowLayout.TRAILING));
        BlockQueue3Panel.setLayout(new FlowLayout(FlowLayout.TRAILING));
        BlockQueue4Panel.setLayout(new FlowLayout(FlowLayout.TRAILING));
        BlockQueue5Panel.setLayout(new FlowLayout(FlowLayout.TRAILING));
        BackupQueue=new JTextField("",15);
        ReadyQueue=new JTextField("",15);
        BlockQueue1=new JTextField("",15);
        BlockQueue2=new JTextField("",15);
        BlockQueue3=new JTextField("",15);
        BlockQueue4=new JTextField("",15);
        BlockQueue5=new JTextField("",15);
        BackupQueuePanel.add(new JLabel("后备队列"));
        BackupQueuePanel.add(BackupQueue);
        ReadyQueuePanel.add(new JLabel("就绪队列"));
        ReadyQueuePanel.add(ReadyQueue);
        BlockQueue1Panel.add(new JLabel("阻塞队列1"));
        BlockQueue1Panel.add(BlockQueue1);
        BlockQueue2Panel.add(new JLabel("阻塞队列2"));
        BlockQueue2Panel.add(BlockQueue2);
        BlockQueue3Panel.add(new JLabel("阻塞队列3"));
        BlockQueue3Panel.add(BlockQueue3);
        BlockQueue4Panel.add(new JLabel("阻塞队列4"));
        BlockQueue4Panel.add(BlockQueue4);
        BlockQueue5Panel.add(new JLabel("阻塞队列5"));
        BlockQueue5Panel.add(BlockQueue5);
        QueuesPanel.add(new JLabel("队列信息"));
        QueuesPanel.add(BackupQueuePanel);
        QueuesPanel.add(ReadyQueuePanel);
        QueuesPanel.add(BlockQueue1Panel);
        QueuesPanel.add(BlockQueue2Panel);
        QueuesPanel.add(BlockQueue3Panel);
        QueuesPanel.add(BlockQueue4Panel);
        QueuesPanel.add(BlockQueue5Panel);

        Up=new JPanel();
        Down=new JPanel();
        DownLeft=new JPanel();
        DownMid=new JPanel();
        DownRight=new JPanel();
        DownMidUp=new JPanel();
        DownMidMid=new JPanel();
        DownMidDown=new JPanel();
        Down.setLayout(new BorderLayout());
        DownLeft.setLayout(new BorderLayout());
        DownMid.setLayout(new BorderLayout());
        DownRight.setLayout(new BorderLayout());
        DownMidUp.setLayout(new BorderLayout());
        DownMidMid.setLayout(new BorderLayout());
        DownMidDown.setLayout(new BorderLayout());
        add(Up,BorderLayout.NORTH);
        add(Down,BorderLayout.SOUTH);
        Up.add(ButtonAreaPanel);
        Down.add(DownLeft,BorderLayout.WEST);
        Down.add(DownMid,BorderLayout.CENTER);
        Down.add(DownRight,BorderLayout.EAST);
        DownLeft.add(new JLabel("运行日志"),BorderLayout.NORTH);
        DownLeft.add(DialogueScrollPane,BorderLayout.SOUTH);
        DownMid.add(DownMidUp,BorderLayout.NORTH);
        DownMid.add(DownMidMid,BorderLayout.CENTER);
        DownMid.add(DownMidDown,BorderLayout.SOUTH);
        DownMidUp.add(new JLabel("作业信息列表"),BorderLayout.NORTH);
        DownMidUp.add(JobInformationScrollPane,BorderLayout.CENTER);
        DownMidMid.add(new JLabel("进程信息列表"),BorderLayout.NORTH);
        DownMidMid.add(processInformationScrollPane,BorderLayout.CENTER);
        DownMidDown.add(new JLabel("内存分配位示图"),BorderLayout.NORTH);
        DownMidDown.add(MemoryBitMapPanel,BorderLayout.CENTER);
        DownRight.add(RunningInformationPanel,BorderLayout.NORTH);
        DownRight.add(BufferMessagePanel);
        DownRight.add(QueuesPanel,BorderLayout.SOUTH);

        // 窗口自适应
        pack();
        // 固定可视化界面窗口大小
        setResizable(false);
        // 窗口居中
        setLocationRelativeTo(null);
    }

    /**
     * @description: 窗口显示信息，每秒刷新一次
     * @param
     * @return
     */
    public static void Show(){
        if(ProcessManage.getPCBTable().isEmpty()&&ProcessManage.getBackup().isEmpty())
            Suspend.doClick();

        ProcessInformation.setText(Log.getProcessString());
        ProcessInformation.setCaretPosition(ProcessInformation.getDocument().getLength());
        if(CPU.isFree())
            DialogueUpdate(DialogueType.FREE,null);

        for(int i=0;i<MemoryBitMap.length;i++){
            MemoryBitMap[i].setText(StorageManage.getMemoryBitMap().getBit(i)?"1":"0");
        }

        ClockTime.setText(""+Clock.getTime());
        CPUState.setText(Log.getCPUState());
        if(CPU.getPcb()!=null){
            RunningProcess.setText(""+CPU.getPcb().getProID());
            RunningCommand.setText(""+CPU.getCmd().getInstruc_ID());
            RunningCommandState.setText(""+CPU.getCmd().getInstruc_State());
            RunningCommandLAddress.setText(""+CPU.getCmd().getL_Address());
            RunningCommandPAddress.setText(""+CPU.getPcb().getP_Address());
        }else{
            RunningProcess.setText("");
            RunningCommand.setText("");
            RunningCommandState.setText("");
            RunningCommandLAddress.setText("");
            RunningCommandPAddress.setText("");
        }

        BufferNowUsing.setText(Log.getBufferNowUsing());
        BufferWaitUse.setText(Log.getBufferQueue());

        BackupQueue.setText(Log.getBackupQueue());
        ReadyQueue.setText(Log.getReadyQueue());
        BlockQueue1.setText(Log.getBlock1Queue());
        BlockQueue2.setText(Log.getBlock2Queue());
        BlockQueue3.setText(Log.getBlock3Queue());
        BlockQueue4.setText(Log.getBlock4Queue());
        BlockQueue5.setText(Log.getBlock5Queue());
    }

    /**
     * @description: 运行日志拼接字符串
     * @param dialogueType
     *            日志类型
     * @param pcb
     *            进程PCB
     * @return
     */
    public static void DialogueUpdate(DialogueType dialogueType,PCB pcb){
        String str=Log.getDialogue(dialogueType,pcb);
        Dialogue.append(str);
        Dialogue.setCaretPosition(Dialogue.getDocument().getLength());
        switch(dialogueType){
            case NEW, CREATE, RUN, BLOCK, WAKEUP, FREE, STOP:
                Log.ProcessDialogueAppend(str);
                break;
            case BUFFER_IN, BUFFER_OUT, P, V, BUFFER_FREE, COPY_IN, COPY_OUT:
                Log.BufferDialogueAppend(str);
                break;
            default:
        }
    }
}
