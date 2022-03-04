/*
 * @Author: Meguri Ichinose
 * @Date: 2022-02-28 13:15:21
 * @Description: 页表
 */
package Manager;

import Hardware.Memory;

public class PageTable{
    private class ITEM{
        private int PageNo;// 页号
        private int ProID;// 进程号
        private int BlockNo;// 所对应的物理块号
        private int Visit;// 访问位

        public ITEM(int pageNo,int proID,int blockNo,int visit){
            PageNo=pageNo;
            ProID=proID;
            BlockNo=blockNo;
            Visit=visit;
        }

        public int getPageNo(){
            return PageNo;
        }

        public int getVisit(){
            return Visit;
        }

        public void setVisit(int visit){
            this.Visit=visit;
        }

        public void setPageNo(int pageNo){
            PageNo=pageNo;
        }

        public int getProID(){
            return ProID;
        }

        public void setProID(int proID){
            ProID=proID;
        }

        public int getBlockNo(){
            return BlockNo;
        }

        public void setBlockNo(int blockNo){
            BlockNo=blockNo;
        }

        public void incVisit(){
            Visit++;
        }
    }

    private static ITEM[] Item=new ITEM[Memory.PROCESS_PAGE_NUM*Memory.MAX_CONCURRENCY];// 24 条页表项

    public static ITEM[] getItem(){
        return Item;
    }

    public static void setItem(ITEM[] item){
        Item=item;
    }

    public static void setProID(int pageNo,int proID){
        Item[pageNo].setProID(proID);
    }

    public static int getProID(int pageNo){
        return Item[pageNo].getProID();
    }

    public static void setBlockNo(int pageNo,int blockNo){
        Item[pageNo].setBlockNo(blockNo);
    }

    public static int getBlockNo(int pageNo){
        return Item[pageNo].getBlockNo();
    }

    public static int getVisit(int pageNo){
        return Item[pageNo].getVisit();
    }

    /**
     * @description: 所有的页表项自增
     * @param
     * @return
     */
    public static void incVisit(){
        for(ITEM item:Item){
            item.incVisit();
        }
    }

    public static void ResetVisit(int pageNo){
        Item[pageNo].setVisit(0);
    }

    @Deprecated
    public static void setPageNo(int index,int pageNo){
        Item[index].setPageNo(pageNo);
    }

    @Deprecated
    public static void setVisit(int pageNo,int visit){
        Item[pageNo].setVisit(visit);
    }

    public static int getPageNo(int index){
        return Item[index].getPageNo();
    }

    public static void setItem(int pageNo,int proID,int blockNo){
        Item[pageNo].setProID(proID);
        Item[pageNo].setBlockNo(blockNo);
    }

    /**
     * @description: 初始化页表
     * @param
     * @return
     */
    public static void Init(){
        for(int i=0;i<Item.length;i++){
            Item[i]=new PageTable().new ITEM(i,0,0,0);
        }
    }
}
