/*
 * @Author: Meguri Ichinose
 * @Date: 2022-02-12 19:17:49
 * @Description: 数据类型转换(int,short,byte)
 */
package Operation;

public interface DataTypeOperation{
    /**
     * @description: int转short数组
     * @param data
     *            int类型数据
     * @return short类型数组[0]为低位[1]为高位
     */
    public static short[] IntToShort(int data){
        return new short[]{(short)(data&0xFFFF),(short)((data>>>16)&0xFFFF)};
    }

    /**
     * @description: short数组转int
     * @param data
     *            short数组[0]为低位[1]为高位
     * @return int类型数据
     */
    public static int ShortToInt(short[] data){
        return (data[0]&0xFFFF)|((data[1]&0xFFFF)<<16);
    }

    /**
     * @description: short转byte数组
     * @param data
     *            short类型数据
     * @return byte类型数组[0]为低位[1]为高位
     */
    public static byte[] ShortToByte(short data){
        return new byte[]{(byte)(data&0xFF),(byte)((data>>>16)&0xFF)};
    }

    /**
     * @description: short数组转int
     * @param data
     *            short数组[0]为低位[1]为高位
     * @return int类型数据
     */
    public static short ByteToShort(byte[] data){
        return (short)((data[0]&0xFF)|((data[1]&0xFF)<<8));
    }
}
