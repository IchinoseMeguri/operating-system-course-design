/*
 * @Author: Meguri Ichinose
 * @Date: 2022-02-19 18:41:44
 * @Description: 位示图
 */
package Manager;

public class BitMap{
    private boolean[] BitMap;// 每块的占用标志
    private int Num;// 块数量

    public BitMap(int num){
        Num=num;
        setBitMap(new boolean[Num]);
    }

    /**
     * @description: 查询第startNum开始的blockNum块是否为空
     * @param startNum
     *            开始块号
     * @param blockNum
     *            块数
     * @return 是否为空
     */
    public boolean isFree(int startNum,int blockNum){
        if(startNum+blockNum>Num)
            return false;
        for(int i=0;i<blockNum;i++){
            if(getBit(startNum+i))
                return false;
        }
        return true;
    }

    public boolean getBit(int index){
        return BitMap[index];
    }

    public void setBit(int index,boolean data){
        BitMap[index]=data;
    }

    public int getNum(){
        return Num;
    }

    public void setNum(int num){
        Num=num;
    }

    public boolean[] getBitMap(){
        return BitMap;
    }

    public void setBitMap(boolean[] bitMap){
        this.BitMap=bitMap;
    }
}
