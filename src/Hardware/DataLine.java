/*
 * @Author: Meguri Ichinose
 * @Date: 2022-02-12 17:50:30
 * @Description: 数据线
 */
package Hardware;

public class DataLine{
    public static final int DATA_BIT=16;// 数据线数据16位
    private short Data;// 数据线数据

    public short getData(){
        return Data;
    }

    public void setData(short data){
        Data=data;
    }

}
