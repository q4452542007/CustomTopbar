package mapsoft.com.costomtopbar.map2312;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Arrays;

import mapsoft.com.costomtopbar.jt808.PackageData;

/**
 * @author djl
 * @function
 */

public class PlateMsg {

    /**
     * 16byte 消息头
     */
    protected MsgHeader msgHeader;

    // 消息体字节数组
    @JSONField(serialize=false)
    protected byte[] msgBodyBytes;

    /**
     * 校验码 1byte
     */
    protected int checkSum;



    public MsgHeader getMsgHeader() {
        return msgHeader;
    }

    public void setMsgHeader(MsgHeader msgHeader) {
        this.msgHeader = msgHeader;
    }

    public byte[] getMsgBodyBytes() {
        return msgBodyBytes;
    }

    public void setMsgBodyBytes(byte[] msgBodyBytes) {
        this.msgBodyBytes = msgBodyBytes;
    }

    public int getCheckSum() {
        return checkSum;
    }

    public void setCheckSum(int checkSum) {
        this.checkSum = checkSum;
    }



    public PlateMsg() {
    }

    public PlateMsg(MsgHeader msgHeader, int checkSum) {
        this.msgHeader = msgHeader;
        this.checkSum = checkSum;
    }

    @Override
    public String toString() {
        return "PackageData [msgHeader=" + msgHeader + ", msgBodyBytes=" + Arrays.toString(msgBodyBytes) + ", checkSum="
                + checkSum  + "]";
    }

    public static class MsgHeader {

        /** 地址
         * 0x00 单机通信
         * 0xFF 多机通信广播
         * 0x01—0xFE 通信设备地址
         */
        protected int adress;

        /** 协议
         * 0x01 终端和显示屏之间的通信
         * 0x02 报站器与电子路牌（包括站节牌）之间的通信
         * 0x03 PC 与路牌控制盒之间的通信
         * 0x04 简易型电子站牌的通信协议
         */
        protected int protocol;

        /////// ========消息体属性
        //通讯命令字
        protected int commandWords;
        //ID号
        protected int msgId;
        //消息长度
        protected int length;
        /////// ========消息体属性

        public MsgHeader() {
        }

        public int getAdress() {
            return adress;
        }

        public void setAdress(int adress) {
            this.adress = adress;
        }

        public int getProtocol() {
            return protocol;
        }

        public void setProtocol(int protocol) {
            this.protocol = protocol;
        }

        public int getMsgId() {
            return msgId;
        }

        public void setMsgId(int msgId) {
            this.msgId = msgId;
        }

        public int getLength() {
            return length;
        }

        public void setLength(int length) {
            this.length = length;
        }

        public int getCommandWords() {
            return commandWords;
        }

        public void setCommandWords(int commandWords) {
            this.commandWords = commandWords;
        }
    }

}
