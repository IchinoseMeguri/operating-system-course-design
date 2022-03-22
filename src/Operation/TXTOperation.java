/*
 * @Author: Meguri Ichinose
 * @Date: 2022-02-10 14:26:54
 * @Description: 文件操作，通过该类的函数将文件信息读入到程序以及将信息写出到文件
 */
package Operation;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import Process.Command;
import Process.Job;

public interface TXTOperation{

    /**
     * @description: 将字符串text输出为文件
     * @param filename
     *            文件名
     * @param text
     *            字符串
     * @return
     */
    public static void OutputToFile(String filename,String text) throws IOException{
        FileOutputStream out=new FileOutputStream(filename);
        out.write(text.getBytes());
        out.close();
    }

    /**
     * @description:从文件输入，暂时处理为字符串
     * @param filename
     *            文件名
     * @return 字符串
     */
    public static String InputFromFile(String filename) throws IOException{
        FileInputStream in=new FileInputStream(filename);
        BufferedReader reader=new BufferedReader(new InputStreamReader(in));
        String str="";
        String line="";
        while((line=reader.readLine())!=null){
            str+=line;
            str+="\n";
        }
        in.close();
        return str;
    }

    /**
     * @description: 将字符串整理为指令序列
     * @param str
     *            字符串
     * @return 指令序列
     */
    public static ArrayList<Command> GetCommandQueue(String str) throws IOException{
        String[] arr=str.split(",|\n");
        int[] array=new int[arr.length];
        for(int i=0;i<arr.length;i++){
            array[i]=Integer.parseInt(arr[i]);
        }
        ArrayList<Command> commandqueue=new ArrayList<Command>();
        for(int i=0;i<array.length;){
            commandqueue.add(new Command(array[i++],array[i++],array[i++],array[i++]));
        }
        return commandqueue;
    }

    /**
     * @description: 仿真程序开始运行时按秒计时，将xxxxxx-jobs-input.txt文件一次性读入一个临时数组。
     * @param
     * @return 作业序列
     */
    public static ArrayList<Job> GetJobsQueue() throws IOException{
        String[] arr=InputFromFile(".\\input\\19319117-jobs-input.txt").split(",|\n");
        int[] array=new int[arr.length-4];
        for(int i=4;i<arr.length;i++){
            array[i-4]=Integer.parseInt(arr[i]);
        }
        ArrayList<Job> jobqueue=new ArrayList<Job>();
        for(int i=0;i<array.length;){
            ArrayList<Command> commandqueue=GetCommandQueue(InputFromFile(".\\input\\"+array[i]+".txt"));
            jobqueue.add(new Job(array[i++],array[i++],array[i++],array[i++],commandqueue));
        }
        return jobqueue;
    }

    /**
     * @description: 每次运行记录字符串保存到 ProcessResults-？？？.txt 文件
     * @param text
     *            字符串
     * @param EndTime
     *            表示数字，每次运行完成所有作业的总秒数
     * @return
     */
    public static void SaveProcessResults(String text,int EndTime) throws IOException{
        String filename="ProcessResults-"+EndTime+".txt";
        OutputToFile(filename,text);
    }
}
