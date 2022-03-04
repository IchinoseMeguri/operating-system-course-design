/*
 * @Author: Meguri Ichinose
 * @Date: 2022-02-16 20:42:33
 * @Description: 存储管理（通过操作位示图实现）
 */
package Manager;

import java.util.ArrayList;

import Hardware.Disk;
import Hardware.Memory;

public class StorageManage{
    private static BitMap MemoryBitMap=new BitMap(Memory.BLOCK_NUM);
    private static BitMap DiskBitMap=new BitMap(Disk.TOTAL_BLOCK_NUM);

    // 这些队列用于记录缓冲区中进入过的进程的进入时间离开时间
    private static ArrayList<QueueDialogue> BufferDialogue=new ArrayList<QueueDialogue>();

    /**
     * @description: 按块数分配内存（不含缓冲）
     * @param blockNum
     *            块数
     * @return 本次分配内存的首地址，若失败，返回-1
     */
    public static int AllocateMemory(int blockNum){
        // 遍历查询是否有可用空间，若有，则将空间分配，并修改位示图
        for(int i=Memory.PROCESS_START_BLOCK_NO;i<Memory.BUFFER_START_BLOCK_NO-blockNum;i++){
            if(MemoryBitMap.isFree(i,blockNum)){
                for(int j=0;j<blockNum;j++)
                    MemoryBitMap.setBit(j+i,true);
                return Memory.getAddress(i);
            }
        }
        return -1;
    }

    /**
     * @description: 回收内存
     * @param startNum
     *            起始块号
     * @param blockNum
     *            块数
     * @return
     */
    public static void ClearMemory(int startNum,int blockNum){
        for(int i=startNum;i<startNum+blockNum;i++){
            MemoryBitMap.setBit(i,false);
        }
    }

    /**
     * @description: 回收磁盘
     * @param startNum
     *            起始块号
     * @param blockNum
     *            块数
     * @return
     */
    public static void ClearDisk(int startNum,int blockNum){
        for(int i=startNum;i<startNum+blockNum;i++){
            DiskBitMap.setBit(i,false);
        }
    }

    /**
     * @description: 按块数分配磁盘（不含交换区）
     * @param blockNum
     *            块数
     * @return 本次分配内存的首地址，若失败，返回-1
     */
    public static int AllocateDisk(int blockNum){
        for(int i=Disk.STORAGE_START_NUM;i<Disk.TOTAL_BLOCK_NUM-blockNum;i++){
            if(DiskBitMap.isFree(i,blockNum)){
                for(int j=0;j<blockNum;j++)
                    DiskBitMap.setBit(j+i,true);
                return Disk.getAddress(i);
            }
        }
        return -1;
    }

    /**
     * @description: 按块数分配缓冲区
     * @param blockNum
     *            块数
     * @return 本次分配内存的首地址，若失败，返回-1
     */
    public static int AllocateBuffer(int blockNum){
        for(int i=Memory.BUFFER_START_BLOCK_NO;i<Memory.BLOCK_NUM;i++){
            if(MemoryBitMap.isFree(i,blockNum)){
                for(int j=0;j<blockNum;j++)
                    MemoryBitMap.setBit(j+i,true);
                return Memory.getAddress(i);
            }
        }
        return -1;
    }

    /**
     * @description: 按块数分配磁盘交换区
     * @param blockNum
     *            块数
     * @return 本次分配内存的首地址，若失败，返回-1
     */
    public static int AllocateSwap(int blockNum){
        for(int i=Disk.SWAP_START_NO;i<Disk.STORAGE_START_NUM-blockNum;i++){
            if(DiskBitMap.isFree(i,blockNum)){
                for(int j=0;j<blockNum;j++)
                    DiskBitMap.setBit(j+i,true);
                return Disk.getAddress(i);
            }
        }
        return -1;
    }

    /**
     * @description: 程序启动时的初始操作
     * @param
     * @return
     */
    public static void Init(){
        PageTable.Init();// 初始化页表
    }

    public static ArrayList<QueueDialogue> getBufferDialogue(){
        return BufferDialogue;
    }

    public static void setBufferDialogue(ArrayList<QueueDialogue> bufferDialogue){
        BufferDialogue=bufferDialogue;
    }

    public static void ClearMemory(int blockNo){

        MemoryBitMap.setBit(blockNo,false);
    }

    public static void ClearDisk(int blockNo){

        DiskBitMap.setBit(blockNo,false);
    }

    public static BitMap getMemoryBitMap(){
        return MemoryBitMap;
    }

    public static void setMemoryBitMap(BitMap memoryBitMap){
        MemoryBitMap=memoryBitMap;
    }

    public static BitMap getDiskBitMap(){
        return DiskBitMap;
    }

    public static void setDiskBitMap(BitMap diskBitMap){
        DiskBitMap=diskBitMap;
    }

}
