/*
 * @Author: Meguri Ichinose
 * @Date: 2022-02-12 17:47:35
 * @Description: 地址线
 */
package Hardware;

public class AddressLine{
    public static final int ADDRESS_BIT=16;// 地址线16位
    private short Address;// 地址线数据

    public short getAddress(){
        return Address;
    }

    public void setAddress(short address){
        Address=address;
    }

}
