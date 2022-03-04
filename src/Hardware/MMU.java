/*
 * @Author: Meguri Ichinose
 * @Date: 2022-02-11 17:13:35
 * @Description: MMU地址变换机构
 */
package Hardware;

import Manager.PageTable;
import Process.PCB;
import Threads.PageFaultThread;

public class MMU{
    private static int BaseAddress;// 基地址
    private static int P_Address;// 物理地址

    /**
     * @description: 根据逻辑地址找到物理地址
     * @param l_Address
     *            逻辑地址
     * @return 物理地址，若越界，则返回-1
     */
    public static int getPhysicalAddress(PCB pcb,int l_Address){
        // 置基址寄存器值
        setBaseAddress(pcb.getBaseAddress());
        // 页表的每一项的访问位自增1
        PageTable.incVisit();
        if(!isOverBounds(l_Address)){
            // 如果未越界，直到找到页，进行遍历和换页
            while(true){
                for(int i=0;i<Memory.MAX_CONCURRENCY*Memory.PROCESS_PAGE_NUM;i++){
                    if(PageTable.getProID(i)==pcb.getProID())
                        if(PageTable.getBlockNo(i)==l_Address)
                            return i;
                }
                // 向缺页中断线程发送信号，进行换页
                PageFaultThread.PageFault(pcb,l_Address);
            }
        }
        return -1;
    }

    /**
     * @description: 判断逻辑地址是否越界
     * @param l_Address
     *            逻辑地址
     * @return 是否越界
     */
    public static boolean isOverBounds(int l_Address){
        return l_Address>Disk.JOB_CAN_USE_BLOCK_NUM;
    }

    public static int getP_Address(){
        return P_Address;
    }

    public static void setP_Address(int p_Address){
        P_Address=p_Address;
    }

    public static int getBaseAddress(){
        return BaseAddress;
    }

    public static void setBaseAddress(int baseAddress){
        BaseAddress=baseAddress;
    }
}
