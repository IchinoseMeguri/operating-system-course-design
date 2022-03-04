/*
 * @Author: Meguri Ichinose
 * @Date: 2022-02-10 14:11:54
 * @Description:
 * 内存：共 32KB，每个物理块大小 1024B，共 32 个物理块（Block）。将每个物理块假设为 1
 * 页帧（Page），共 32 页。
 * 用位示图是实现内存管理。
 * 采用连续动态分配方式，逻辑地址、物理地址均连续。
 */
package Hardware;

import Operation.DataTypeOperation;

public class Memory{
    public static final int BLOCK_NUM=32;// 共 32 个物理块（Block）
    public static final int BLOCK_SIZE=1024;// 每个物理块大小 1024B
    public static final int TOTAL_SIZE=BLOCK_NUM*BLOCK_SIZE;// 内存总大小 32768B
    public static final int PAGE_BLOCK_NUM=1;// 将每个物理块假设为 1 页帧（Page）
    public static final int PAGE_NUM=32;// 共 32 页帧。
    public static final int PAGE_SIZE=PAGE_BLOCK_NUM*BLOCK_SIZE;// 每个页大小 1024B
    public static final int PROCESS_PAGE_NUM=3;// 用户进程的内存大小为 3 页
    public static final int MAX_CONCURRENCY=8;// 用户进程最高并发度为 8
    public static final int PROCESS_START_BLOCK_NO=0;// 用户区起始块号 0
    public static final int BUFFER_PAGE_NUM=PAGE_NUM-PROCESS_PAGE_NUM*MAX_CONCURRENCY;// 其他内存空间可用于缓冲区 8
    public static final int BUFFER_START_BLOCK_NO=PROCESS_START_BLOCK_NO+PROCESS_PAGE_NUM*MAX_CONCURRENCY;// 缓冲区起始块号 24

    private static byte[] Data=new byte[TOTAL_SIZE];// 数据

    /**
     * @description: 向内存指定单元写入数据
     * @param address
     *            地址
     * @param data
     *            数据
     * @return
     */
    public static void Write(short address,short data){
        byte[] Byte=DataTypeOperation.ShortToByte(data);
        Data[address]=Byte[0];
        Data[address+1]=Byte[1];
    }

    /**
     * @description: 从内存指定单元读出数据
     * @param address
     *            地址
     * @return 数据
     */
    public static short Read(short address){
        byte[] Byte=new byte[]{Data[address],Data[address+1]};
        return DataTypeOperation.ByteToShort(Byte);
    }

    /**
     * @description: 根据块号向内存指定单元写入数据
     * @param block
     *            块号
     * @param data
     *            数据(1024B)
     * @return
     */
    public static void Write(int block,byte[] data){
        for(int i=block*BLOCK_SIZE;i<(block+1)*BLOCK_SIZE;i+=2){
            Write((short)i,data[i-block*BLOCK_SIZE]);
        }
    }

    /**
     * @description: 根据块号从内存读出数据
     * @param block
     *            块号
     * @return data 数据(1024B)
     */
    public static byte[] Read(int block){
        byte[] data=new byte[BLOCK_SIZE];
        for(int i=0;i<BLOCK_SIZE;i++){
            data[i]=Data[block*BLOCK_SIZE+i];
        }
        return data;
    }

    /**
     * @description: 清空内存数据
     * @param
     * @return
     */
    public static void ClearMemory(){
        for(int i=0;i<Data.length;i++)
            Data[i]=0;
    }

    /**
     * @description: 根据块号获取起始地址
     * @param block
     *            块号
     * @return 首地址
     */
    public static int getAddress(int block){
        return block*BLOCK_SIZE;
    }

    /**
     * @description: 根据地址得到块号
     * @param address
     *            首地址
     * @return 块号
     */
    public static int getBlockNo(int address){
        return address/BLOCK_SIZE;
    }

    public static byte[] getData(){
        return Data;
    }

    public static void setData(byte[] data){
        Data=data;
    }
}
