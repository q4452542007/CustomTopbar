package mapsoft.com.costomtopbar.map2312;

import android.util.Log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.Util;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import mapsoft.com.costomtopbar.util.BCD8421Operater;
import mapsoft.com.costomtopbar.util.BitOperator;
import mapsoft.com.costomtopbar.util.Utils;

/**
 * @author djl
 * @function
 */

public class MapProtocolUtils {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private BitOperator bitOperator;
    private BCD8421Operater bcd8421Operater;

    public MapProtocolUtils() {
        this.bitOperator = new BitOperator();
        this.bcd8421Operater = new BCD8421Operater();
    }

    public byte[] generateMsgBodyProps(int commandWords, int msgId, int length) {
        // [ 0 ] 命令字
        // [ 1-2 ] ID号
        // [ 3-4 ] 消息体长度
        if (length >= 255)
            log.warn("The max value of msgLen is 255, but {} .", length);
        byte[] bodyProps = bitOperator.concatAll(Arrays.asList(
                bitOperator.integerTo1Bytes(commandWords),
                bitOperator.integerTo2Bytes(msgId),
                bitOperator.integerTo2Bytes(length)
        ));
        return bodyProps;
    }
    public byte[] generateLCDMsgBodyProps(int commandWords, int msgId, int length) {
        // [ 0 ] 命令字
        // [ 1-2 ] ID号
        // [ 3-4 ] 消息体长度
        if (length >= 255)
            log.warn("The max value of msgLen is 255, but {} .", length);
        byte[] bodyProps = bitOperator.concatAll(Arrays.asList(
                bitOperator.integerTo1Bytes(commandWords),
                bitOperator.integerTo2Bytes(msgId),
                bitOperator.integerTo1Bytes(length)
        ));
        return bodyProps;
    }



    public byte[] generateMsgHeader(int adress, int protocol)
            throws Exception {
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            // 1. 地址
            baos.write(bitOperator.integerTo1Bytes(adress));
            // 2. 协议
            baos.write(bitOperator.integerTo1Bytes(protocol));
            return baos.toByteArray();
        } finally {
            if (baos != null) {
                baos.close();
            }
        }
    }

    /**
     *
     * 发送消息时转义<br>
     *
     * <pre>
     *  0x7e <====> 0x7d5e
     *  0x7d <====> 0x7d5d
     * </pre>
     *
     * @param bs
     *            要转义的字节数组
     * @param start
     *            起始索引
     * @param end
     *            结束索引
     * @return 转义后的字节数组
     * @throws Exception
     */
    public byte[] doEscape4Send(byte[] bs, int start, int end) throws Exception {
        if (start < 0 || end > bs.length)
            throw new ArrayIndexOutOfBoundsException("doEscape4Send error : index out of bounds(start=" + start
                    + ",end=" + end + ",bytes length=" + bs.length + ")");
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            for (int i = 0; i < start; i++) {
                baos.write(bs[i]);
            }
            for (int i = start; i < end; i++) {
                if (bs[i] == 0x7e) {
                    baos.write(0x7d);
                    baos.write(0x5e);
                } else if (bs[i] == 0x7d) {
                    baos.write(0x7d);
                    baos.write(0x5d);
                } else {
                    baos.write(bs[i]);
                }
            }
            for (int i = end; i < bs.length; i++) {
                baos.write(bs[i]);
            }
            //Log.e("doEscape4Send: ", Utils.bytes2HexString(baos.toByteArray()));
            return baos.toByteArray();
        } catch (Exception e) {
            throw e;
        } finally {
            if (baos != null) {
                baos.close();
                baos = null;
            }
        }
    }
}
