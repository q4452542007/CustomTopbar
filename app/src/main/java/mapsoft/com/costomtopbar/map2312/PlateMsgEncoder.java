package mapsoft.com.costomtopbar.map2312;

import android.util.Log;

import java.util.Arrays;

import mapsoft.com.costomtopbar.constant.MapSoftConsts;
import mapsoft.com.costomtopbar.jt808.MsgDecoder;
import mapsoft.com.costomtopbar.jt808.TPMSConsts;
import mapsoft.com.costomtopbar.util.BCD8421Operater;
import mapsoft.com.costomtopbar.util.BitOperator;
import mapsoft.com.costomtopbar.util.Utils;

/**
 * @author djl
 * @function
 */

public class PlateMsgEncoder {

    private BitOperator bitOperator;
    private MapProtocolUtils mMapProtocolUtils;
    private MsgDecoder mMsgDecoder;
    private BCD8421Operater mBCD8421Operater;

    public PlateMsgEncoder() {
        this.bitOperator = new BitOperator();
        this.mMapProtocolUtils = new MapProtocolUtils();
        this.mMsgDecoder = new MsgDecoder();
        this.mBCD8421Operater = new BCD8421Operater();
    }

    public byte[] encode4PathPlateMsg(PathPlateMsg pathPlateMsg) throws Exception {
        // 消息体字节数组
        byte[] msgBody = null;
        msgBody = this.bitOperator.concatAll(Arrays.asList(//
                bitOperator.integerTo1Bytes(1), // 路牌编号
                bitOperator.integerTo1Bytes(pathPlateMsg.getPlateProps()), // 路牌属性
                bitOperator.integerTo3Bytes(3223857),// 线路号
                bitOperator.integerTo1Bytes(pathPlateMsg.getDisplayStyle()),// 显示风格
                bitOperator.integerTo1Bytes(pathPlateMsg.getWords()),// 几字路牌
                pathPlateMsg.getPathNum().getBytes(),
                pathPlateMsg.getContent(),                          // 显示内容

                bitOperator.integerTo1Bytes(2), // 路牌编号
                bitOperator.integerTo1Bytes(pathPlateMsg.getPlateProps()), // 路牌属性
                bitOperator.integerTo3Bytes(3223857),// 线路号
                bitOperator.integerTo1Bytes(pathPlateMsg.getDisplayStyle()),// 显示风格
                bitOperator.integerTo1Bytes(pathPlateMsg.getWords()),// 几字路牌
                pathPlateMsg.getPathNum().getBytes(),
                pathPlateMsg.getContent(),                          // 显示内容

                bitOperator.integerTo1Bytes(3), // 路牌编号
                bitOperator.integerTo1Bytes(pathPlateMsg.getPlateProps()), // 路牌属性
                bitOperator.integerTo3Bytes(3223857),// 线路号
                bitOperator.integerTo1Bytes(pathPlateMsg.getDisplayStyle()),// 显示风格
                bitOperator.integerTo1Bytes(pathPlateMsg.getWords()),// 几字路牌
                pathPlateMsg.getPathNum().getBytes(),
                pathPlateMsg.getContent()                       // 显示内容

        ));
        Log.d("msgBody: ", Utils.bytes2HexString(msgBody));
        byte[] msgBodyProps = this.mMapProtocolUtils.generateMsgBodyProps(MapSoftConsts.com_lectronic_display_board_info,
               0x0000,msgBody.length );

        byte[] msgHeader = this.mMapProtocolUtils.generateMsgHeader(0x00,
                0x02);

        byte[] headerAndBody = this.bitOperator.concatAll(msgHeader,msgBodyProps, msgBody);
        Log.e("headerAndBody: ",Utils.bytes2HexString(headerAndBody));

        // 校验码
        int checkSum = this.bitOperator.getCheckSum4JT808(headerAndBody, 0, headerAndBody.length);

        // 连接并且转义
        return this.doEncode(headerAndBody, checkSum);

    }

    public byte[] encode4AdMsg(InBusPlateMsg inBusPlateMsg) throws Exception {
        // 消息体字节数组
        byte[] msgBody = null;
        msgBody = this.bitOperator.concatAll(Arrays.asList(//
                this.bitOperator.integerTo2Bytes(MapSoftConsts.ad_frame_header_screen_in_bus),
                this.bitOperator.integerTo1Bytes(inBusPlateMsg.getAdress()),
                this.bitOperator.integerTo1Bytes(inBusPlateMsg.getAdNum()),
                inBusPlateMsg.getContent(),
                this.bitOperator.integerTo1Bytes(MapSoftConsts.end_flag_screen_in_bus)
        ));
        Log.d("msgBody: ", Utils.bytes2HexString(msgBody));
        // 校验码
        int checkSum = this.bitOperator.getCheckSum4JT808(msgBody, 0, msgBody.length);

        return this.bitOperator.concatAll(msgBody,this.bitOperator.integerTo1Bytes(checkSum));
    }

    public byte[] encode4LcdChangePathMsg(InBusPlateMsg inBusPlateMsg) throws Exception {
        // 消息体字节数组
        byte[] msgBody = null;
        msgBody = this.bitOperator.concatAll(Arrays.asList(//
                inBusPlateMsg.getContent()
        ));
        Log.d("msgBody: ", Utils.bytes2HexString(msgBody));
        byte[] msgBodyProps = this.mMapProtocolUtils.generateLCDMsgBodyProps(MapSoftConsts.com_lectronic_display_board_programme,
                0x0000,msgBody.length );

        byte[] msgHeader = this.mMapProtocolUtils.generateMsgHeader(0x00,
                0x02);

        byte[] headerAndBody = this.bitOperator.concatAll(msgHeader,msgBodyProps, msgBody);
        Log.e("headerAndBody: ",Utils.bytes2HexString(headerAndBody));

        // 校验码
        int checkSum = this.bitOperator.getCheckSum4JT808(headerAndBody, 0, headerAndBody.length);

        // 连接并且转义
        return this.doEncode(headerAndBody, checkSum);

    }
    public byte[] encode4LcdStationMsg(InBusPlateMsg inBusPlateMsg) throws Exception {
        // 消息体字节数组
        byte[] msgBody = null;
        msgBody = this.bitOperator.concatAll(Arrays.asList(//
                this.bitOperator.integerTo1Bytes(inBusPlateMsg.getDirection_flag()),
                this.bitOperator.integerTo1Bytes(inBusPlateMsg.getIn_out_flag()),
                this.bitOperator.integerTo1Bytes(inBusPlateMsg.custom_flag)
        ));
        Log.d("msgBody: ", Utils.bytes2HexString(msgBody));
        byte[] msgBodyProps = this.mMapProtocolUtils.generateLCDMsgBodyProps(MapSoftConsts.com_report,
                0x0000,0x0003 );

        byte[] msgHeader = this.mMapProtocolUtils.generateMsgHeader(0x00,
                0x02);

        byte[] headerAndBody = this.bitOperator.concatAll(msgHeader,msgBodyProps, msgBody);
        Log.e("headerAndBody: ",Utils.bytes2HexString(headerAndBody));

        // 校验码
        int checkSum = this.bitOperator.getCheckSum4JT808(headerAndBody, 0, headerAndBody.length);

        // 连接并且转义
        return this.doEncode(headerAndBody, checkSum);

    }





    private byte[] doEncode(byte[] headerAndBody, int checkSum) throws Exception {
        byte[] noEscapedBytes = this.bitOperator.concatAll(Arrays.asList(//
                new byte[] { TPMSConsts.pkg_delimiter }, // 0x7e
                headerAndBody, // 消息头+ 消息体
                bitOperator.integerTo1Bytes(checkSum), // 校验码
                new byte[] { TPMSConsts.pkg_delimiter }// 0x7e
        ));
        //Log.e("noEscapedBytes:",Utils.bytes2HexString(noEscapedBytes));
        // 转义
        return mMapProtocolUtils.doEscape4Send(noEscapedBytes, 1, noEscapedBytes.length - 2);
    }

}
