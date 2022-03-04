/*
 * @Author: Meguri Ichinose
 * @Date: 2022-02-10 15:27:21
 * @Description:
 * 硬盘：10 个柱面（Cylinder），1 个柱面有 32 个磁道（Track），1 个磁道中有 64 个扇区（Sector）。假设 1 个扇
 * 区为 1 个物理块（Block），每个物理块大小 1024B。
 * 从 0 开始排序
 */
package Hardware;

public class Disk{
    public static final int BLOCK_SIZE=1024;// 每个物理块大小 1024B
    public static final int SECTOR_NUM_PER_TRACK=64;// 1 个磁道中有 64 个扇区（Sector）
    public static final int TRACK_NUM_PER_CYLINDER=32;// 1 个柱面有 32 个磁道（Track）
    public static final int CYLINDER_NUM=10;// 10 个柱面（Cylinder）
    public static final int PAGE_BLOCK_NUM=1;// 1 个扇区为 1 个物理块（Block）
    public static final int TOTAL_BLOCK_NUM=CYLINDER_NUM*TRACK_NUM_PER_CYLINDER*SECTOR_NUM_PER_TRACK;// 总块数 20480
    public static final int TOTAL_SIZE=TOTAL_BLOCK_NUM*BLOCK_SIZE;// 总大小 20971520B
    public static final int TOTAL_PAGE_NUM=TOTAL_BLOCK_NUM/PAGE_BLOCK_NUM;// 总页数 20480
    public static final int PROCESS_SWAP_PAGE=3;// 磁盘交换区大小每个用户进程 3 页（块）
    public static final int MAX_CONCURRENCY=8;// 用户进程最高并发度为 8
    public static final int SWAP_START_NO=0;// 交换区起始块号 0
    public static final int SWAP_BLOCK_NUM=MAX_CONCURRENCY*PROCESS_SWAP_PAGE;// 交换区大小为 24 块
    public static final int STORAGE_START_NUM=SWAP_START_NO+SWAP_BLOCK_NUM;// 存储区起始块号 24
    public static final int STORAGE_BLOCK_NUM=TOTAL_BLOCK_NUM-SWAP_BLOCK_NUM;// 存储区块数 20456
    public static final int JOB_CAN_USE_BLOCK_NUM=24;// 可用物理块数 24

    private static final byte[][][][] Data=new byte[CYLINDER_NUM][TRACK_NUM_PER_CYLINDER][SECTOR_NUM_PER_TRACK][BLOCK_SIZE];

    /**
     * @description: 向磁盘指定块写数据
     * @param cylinder
     *            柱面号
     * @param track
     *            磁道号
     * @param sector
     *            扇区号
     * @param data
     *            数据（1024B）
     * @return
     */
    public static void Write(int cylinder,int track,int sector,byte[] data){
        Data[cylinder][track][sector]=data;
    }

    /**
     * @description: 从磁盘指定块读数据
     * @param cylinder
     *            柱面号
     * @param track
     *            磁道号
     * @param sector
     *            扇区号
     * @return Data[] 数据（1024B）
     */
    public static byte[] Read(int cylinder,int track,int sector){
        return Data[cylinder][track][sector];
    }

    /**
     * @description: 向磁盘指定块写数据
     * @param block
     *            块号
     * @param data
     *            数据（1024B）
     * @return
     */
    public static void Write(int block,byte[] data){
        Write(getCylinderNo(block),getTrackNo(block),getSectorNo(block),data);
    }

    /**
     * @description: 从磁盘指定块读数据
     * @param block
     *            块号
     * @return Data[] 数据（1024B）
     */
    public static byte[] Read(int block){
        return Data[getCylinderNo(block)][getTrackNo(block)][getSectorNo(block)];
    }

    /**
     * @description: 根据块号获取柱面号
     * @param block
     *            块号
     * @return 柱面号
     */
    public static int getCylinderNo(int block){
        return block/(SECTOR_NUM_PER_TRACK*TRACK_NUM_PER_CYLINDER);
    }

    /**
     * @description: 根据块号获取磁道号
     * @param block
     *            块号
     * @return 磁道号
     */
    public static int getTrackNo(int block){
        return (block%(SECTOR_NUM_PER_TRACK*TRACK_NUM_PER_CYLINDER))/SECTOR_NUM_PER_TRACK;
    }

    /**
     * @description: 根据块号获取扇区号
     * @param block
     *            块号
     * @return 扇区号
     */
    public static int getSectorNo(int block){
        return block%SECTOR_NUM_PER_TRACK;
    }

    /**
     * @description: 根据块号获取首地址
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

    public static byte[][][][] getData(){
        return Data;
    }
}
