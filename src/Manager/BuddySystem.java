/*
 * @Author: Meguri Ichinose
 * @Date: 2022-02-16 20:17:24
 * @Description: 内存的伙伴系统
 */
package Manager;

import java.util.ArrayList;
import java.util.Comparator;

public class BuddySystem{
    private class BUDDY{// 伙伴链表
        private int BlockNum;// 该伙伴链表的元素的块数
        private ArrayList<Integer> BlockNo=new ArrayList<>();// 该队列中的元素的起始块号

        public int getBlockNum(){
            return BlockNum;
        }

        public void setBlockNum(int blockNum){
            BlockNum=blockNum;
        }

        public ArrayList<Integer> getBlockNo(){
            return BlockNo;
        }

        public void setBlockNo(ArrayList<Integer> blockNo){
            BlockNo=blockNo;
        }
    }

    // 伙伴队列，分别为1,2,4,8,16页的伙伴，队列保存大块的起始块号
    private BUDDY[] Buddy=new BUDDY[5];

    /**
     * @description: 构造函数，伙伴系统有块数为1,2,4,8,16的
     * @param
     * @return
     */
    public BuddySystem(){
        for(int i=0,t=1;i<Buddy.length;i++,t*=2)
            Buddy[i].setBlockNum(t);
    }

    /**
     * @description: 根据块数获取该块数对应的伙伴数组的序号
     * @param blockNum
     *            块数
     * @return 序号，若无，则返回-1
     */
    public int getIndex(int blockNum){
        for(int i=0;i<Buddy.length;i++){
            if(Buddy[i].getBlockNum()==blockNum)
                return i;
        }
        return -1;
    }

    /**
     * @description: 根据块号获取该块号所在的伙伴数组的序号
     * @param blockNo
     *            块号
     * @return 序号，若无，则返回-1
     */
    public int indexOf(int blockNo){
        for(int i=0;i<Buddy.length;i++)
            if(Buddy[i].getBlockNo().contains(blockNo))
                return i;
        return -1;
    }

    /**
     * @description: 对块进行分割
     * @param blockNo
     *            大块起始块号
     * @return
     */
    public void DivideBuddy(int blockNo){
        // 将一个大块分为等的二小块，其中一块的起始块号为起始块号，另一块的起始块号为中间的块号，从大块的队列中除去，分别加入到小的队列中
        int i=indexOf(blockNo);
        Buddy[i--].getBlockNo().remove(blockNo);
        Buddy[i].getBlockNo().add(blockNo);
        Buddy[i].getBlockNo().add(blockNo+Buddy[i].getBlockNum());
    }

    /**
     * @description: 将块进行合并，块号与下一个块的块号合并为一个大块
     * @param blockNo
     *            小块块号
     * @return
     */
    public void MergeBuddy(int blockNo){
        int i=indexOf(blockNo);
        Buddy[i].getBlockNo().remove(blockNo);
        Buddy[i].getBlockNo().remove(blockNo+Buddy[i].getBlockNum());
        Buddy[++i].getBlockNo().add(blockNo);
    }

    /**
     * @description: 根据块数查找是否有可用空间
     * @param blockNum
     *            块数
     * @return 最小的可用块的起始块号，若无，返回-1
     */
    public int getEmptySpace(int blockNum){
        for(int i=0;i<Buddy.length;i++){
            if(blockNum>=Buddy[i].getBlockNum())
                if(Buddy[i].getBlockNo().size()>0)
                    return Buddy[i].getBlockNo().remove(0);
        }
        return -1;
    }

    /**
     * @description: 对伙伴链表按块的起始块号进行排序
     * @param
     * @return
     */
    public void BuddySort(){
        for(int i=0;i<Buddy.length;i++)
            Buddy[i].getBlockNo().sort(Comparator.naturalOrder());
    }

    public BUDDY[] getBuddy(){
        return Buddy;
    }

    public void setBuddy(BUDDY[] buddy){
        this.Buddy=buddy;
    }

    public int getBlockNum(int index){
        return Buddy[index].getBlockNum();
    }

    public void setBlockNum(int index,int blockNum){
        Buddy[index].setBlockNum(blockNum);
    }

    public ArrayList<Integer> getBlockNo(int index){
        return Buddy[index].getBlockNo();
    }

    public void setBlockNo(int index,ArrayList<Integer> blockNo){
        Buddy[index].setBlockNo(blockNo);
    }
}
