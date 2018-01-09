package mapsoft.com.costomtopbar.map2312;

/**
 * @author djl
 * @function
 */

public class InBusPlateMsg {

    //通讯命令字
    protected int commandWords;

    //-------------广告软件与车内屏的通讯

    /**
     *  0x5A:报站器与车内屏旧通信方式使用索引码；
     *  0x5B:广告语软件与车内屏通信；
     *  0x5C:软件与车内屏时间通信;
     *  0x5D:软件与车内屏温度通信;
     *  0x5E:清楚车内屏广告等信息;
     */
    int adress;
    //广告语数目编号,0x00~0x2E，共 47 条（每条字数不超过 120）
    int adNum;
    //GB2312 编码，以'\0'结尾的字符串
    byte[] content;

    //-------------广告软件与车内屏的通讯


    //---------------LCD导乘牌联动报站通讯协议
    //上下行标志   0 表示上行，1 表示下行
    int direction_flag;
    //进出站标志   0 表示进站，1 表示出站
    int in_out_flag;
    /**
     * 每个站点根据需要可以为 00~FF 任意数字。
     * 比如 第 一 站 可 以 编 辑 成01 、 第 二 站 编 辑 成02……
     */
    int custom_flag;

    /**
     * 数据长度
     * 高 8 位（对于 LCD 导乘牌，固定为 00）
     * 低 8 位（对于 LCD 导乘牌，固定为 03）
     */
    int length = 0x0003;
    //---------------LCD导乘牌联动报站通讯协议


    //-------------------LCD导乘牌线路切换协议

    /**
     * 线路名称标识
     * 可自定义的线路名称标识，可与实际线路名称不同(不支持线路号有中文)
     */
    byte[] pathNameFlag;

    //-------------------LCD导乘牌线路切换协议


    public int getAdress() {
        return adress;
    }

    public void setAdress(int adress) {
        this.adress = adress;
    }

    public int getAdNum() {
        return adNum;
    }

    public void setAdNum(int adNum) {
        this.adNum = adNum;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public int getDirection_flag() {
        return direction_flag;
    }

    public void setDirection_flag(int direction_flag) {
        this.direction_flag = direction_flag;
    }

    public int getIn_out_flag() {
        return in_out_flag;
    }

    public void setIn_out_flag(int in_out_flag) {
        this.in_out_flag = in_out_flag;
    }

    public int getCustom_flag() {
        return custom_flag;
    }

    public void setCustom_flag(int custom_flag) {
        this.custom_flag = custom_flag;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public byte[] getPathNameFlag() {
        return pathNameFlag;
    }

    public void setPathNameFlag(byte[] pathNameFlag) {
        this.pathNameFlag = pathNameFlag;
    }

    public int getCommandWords() {
        return commandWords;
    }

    public void setCommandWords(int commandWords) {
        this.commandWords = commandWords;
    }
}
