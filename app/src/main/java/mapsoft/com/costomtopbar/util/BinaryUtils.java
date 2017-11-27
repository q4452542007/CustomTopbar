package mapsoft.com.costomtopbar.util;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 多媒体中间件BinaryUtils类型转换器
 * @file  BinaryUtils.java
 * @author zoboy
 * @version 2.0.0
 * Copyright(C), 2017
 *			xi'an Coordinates Software Development Co., Ltd.
 */
public class BinaryUtils {

	private final static char[] mChars = "0123456789ABCDEF".toCharArray();
    private final static String mHexStr = "0123456789ABCDEF";

	/**
	 * 无符号byte转换整数
	 * 
	 * @param b
	 * @return [0~255]
	 */
	static public int unsignedByteToInt(byte b) {
		return b & 0xFF;
	}

	static public int unsignedByteToInt(ByteBuffer in) {
		return unsignedByteToInt(in.get());
	}

	/**
	 * 整数转换为无符号byte
	 * 
	 * @param i
	 *            [0~255]
	 * @return
	 */
	static public byte intToUnsignedByte(int i) {
		return (byte) i;
	}

	static public void intToUnsignedByte(int i, ByteBuffer in) {
		in.put(intToUnsignedByte(i));
	}

	/**
	 * bytes转换成十六进制字符串
	 * 
	 * @param b
	 *            byte[] byte数组
	 * @param iLen
	 *            int 取前N位处理 N=iLen
	 * @return String 每个Byte值之间空格分隔
	 */
	public static String byte2HexStr(byte[] b, int iLen, boolean split) {
		StringBuilder sb = new StringBuilder();
		for (int n = 0; n < iLen; n++) {
			sb.append(mChars[(b[n] & 0xFF) >> 4]);
			sb.append(mChars[b[n] & 0x0F]);
			if (split) {
				sb.append(' ');
			}
		}
		return sb.toString().trim().toUpperCase(Locale.US);
	}

	public static String byte2HexStr(byte[] b, int iLen) {
		return byte2HexStr(b, iLen, true);
	}

	public static String byte2HexStr(byte[] b) {
		return byte2HexStr(b, b.length);
	}

	public static String byte2HexStr(byte[] b, boolean split) {
		return byte2HexStr(b, b.length, true);
	}

	public static String byte2HexStr(byte b) {
		return byte2HexStr(new byte[] { b });
	}

	public static String byte2HexStr(ByteBuffer in, int length) {
		return byte2HexStr(in, length, true);
	}

	public static String byte2HexStr(ByteBuffer in, int length, boolean split) {
		byte[] bytes = new byte[length];
		in.get(bytes);
		return byte2HexStr(bytes, split);
	}

	/**
	 * bytes字符串转换为Byte值
	 * 
	 * @param src
	 *            String Byte字符串，每个Byte之间没有分隔符(字符范围:0-9 A-F)
	 * @return byte[]
	 */
	public static byte[] hexStr2Bytes(String src) {
		/* 对输入值进行规范化整理 */
		src = src.trim().replace(" ", "").toUpperCase(Locale.US);
		// 处理值初始化
		int m = 0, n = 0;
		int iLen = src.length() / 2; // 计算长度
		byte[] ret = new byte[iLen]; // 分配存储空间

		for (int i = 0; i < iLen; i++) {
			m = i * 2 + 1;
			n = m + 1;
			ret[i] = (byte) (Integer.decode("0x" + src.substring(i * 2, m) + src.substring(m, n)) & 0xFF);
		}
		return ret;
	}

	/**
	 * @函数功能: BCD码转为10进制串(阿拉伯数据)
	 * @输入参数: BCD码
	 * @输出结果: 10进制串
	 */
	public static String bcd2Str(byte[] bytes) {
		StringBuffer temp = new StringBuffer(bytes.length * 2);
		for (int i = 0; i < bytes.length; i++) {
			temp.append((byte) ((bytes[i] & 0xf0) >>> 4));
			temp.append((byte) (bytes[i] & 0x0f));
		}
		return temp.toString().substring(0, 1).equalsIgnoreCase("0") ? temp.toString().substring(1) : temp.toString();
	}

	public static String bcd2Str(ByteBuffer in, int length) {
		StringBuffer temp = new StringBuffer(length * 2);
		for (int i = 0; i < length; i++) {
			byte b = in.get();
			temp.append((byte) ((b & 0xf0) >>> 4));
			temp.append((byte) (b & 0x0f));
		}
		return temp.toString().substring(0, 1).equalsIgnoreCase("0") ? temp.toString().substring(1) : temp.toString();
	}
	public static byte[] str2Bcd(String asc,int len) {
		int mod = len % 2;
		if (mod != 0) {
			asc = "0" + asc;
			len = asc.length();
		}
		byte abt[] = new byte[len];
		if (len >= 2) {
			len = len / 2;
		}
		byte bbt[] = new byte[len];
		abt = asc.getBytes();
		int j, k;
		for (int p = 0; p < asc.length() / 2; p++) {
			if ((abt[2 * p] >= '0') && (abt[2 * p] <= '9')) {
				j = abt[2 * p] - '0';
			} else if ((abt[2 * p] >= 'a') && (abt[2 * p] <= 'z')) {
				j = abt[2 * p] - 'a' + 0x0a;
			} else {
				j = abt[2 * p] - 'A' + 0x0a;
			}
			if ((abt[2 * p + 1] >= '0') && (abt[2 * p + 1] <= '9')) {
				k = abt[2 * p + 1] - '0';
			} else if ((abt[2 * p + 1] >= 'a') && (abt[2 * p + 1] <= 'z')) {
				k = abt[2 * p + 1] - 'a' + 0x0a;
			} else {
				k = abt[2 * p + 1] - 'A' + 0x0a;
			}
			int a = (j << 4) + k;
			byte b = (byte) a;
			bbt[p] = b;
		}
		return bbt;
	}
	
	
	/**
	 * @函数功能: 10进制串转为BCD码
	 * @输入参数: 10进制串
	 * @输出结果: BCD码
	 */
	public static byte[] str2Bcd(String asc) {
		int len = asc.length();
		int mod = len % 2;
		if (mod != 0) {
			asc = "0" + asc;
			len = asc.length();
		}
		byte abt[] = new byte[len];
		if (len >= 2) {
			len = len / 2;
		}
		byte bbt[] = new byte[len];
		abt = asc.getBytes();
		int j, k;
		for (int p = 0; p < asc.length() / 2; p++) {
			if ((abt[2 * p] >= '0') && (abt[2 * p] <= '9')) {
				j = abt[2 * p] - '0';
			} else if ((abt[2 * p] >= 'a') && (abt[2 * p] <= 'z')) {
				j = abt[2 * p] - 'a' + 0x0a;
			} else {
				j = abt[2 * p] - 'A' + 0x0a;
			}
			if ((abt[2 * p + 1] >= '0') && (abt[2 * p + 1] <= '9')) {
				k = abt[2 * p + 1] - '0';
			} else if ((abt[2 * p + 1] >= 'a') && (abt[2 * p + 1] <= 'z')) {
				k = abt[2 * p + 1] - 'a' + 0x0a;
			} else {
				k = abt[2 * p + 1] - 'A' + 0x0a;
			}
			int a = (j << 4) + k;
			byte b = (byte) a;
			bbt[p] = b;
		}
		return bbt;
	}

	/**
	 * 基于位移的int转化成byte[]
	 * 
	 * @param number
	 * @return byte[]
	 */

	public static byte[] longToDword(long number) {
		byte[] abyte = new byte[4];
		// "&" 与（AND），对两个整型操作数中对应位执行布尔代数，两个位都为1时输出1，否则0。
		abyte[3] = (byte) (0xff & number);
		// ">>"右移位，若为正数则高位补0，若为负数则高位补1
		abyte[2] = (byte) ((0xff00 & number) >> 8);
		abyte[1] = (byte) ((0xff0000 & number) >> 16);
		abyte[0] = (byte) ((0xff000000 & number) >> 24);
		return abyte;
	}

	/**
	 * 三个字节的数组转换成Integer类型
	 * 
	 * @param bytes
	 * @return
	 */
	public static int threeBytesToInt(byte[] bytes) {
		int number = bytes[2] & 0xFF;
		number |= ((bytes[1] << 8) & 0xFF0000);
		number |= ((bytes[0] << 16) & 0xFF000000);
		return number;
	}

	/**
	 * 基于位移的int转化成byte[]
	 * 
	 * @param in
	 * @return byte[]
	 */

	public static int[] dwordToTypeData(ByteBuffer in) {

		int number = in.get() & 0xFF;
		number = (number << 8) | (in.get() & 0xFF);
		number = (number << 8) | (in.get() & 0xFF);
		number = (number << 8) | (in.get() & 0xFF);

		String str = Integer.toBinaryString(number);

		StringBuffer sb = new StringBuffer();

		if (str.length() < 32) {
			int zeroLength = 32 - str.length();

			for (int i = 0; i < zeroLength; i++) {
				sb.append("0");
			}
			sb.append(str);

		}

		char[] charData = sb.toString().toCharArray();

		int[] result = new int[charData.length];

		for (int i = 0; i < charData.length; i++) {
			result[charData.length - i - 1] = Integer.parseInt(String.valueOf(charData[i]));
		}

		return result;
	}

	/**
	 * 基于位移的 byte[]转化成int
	 * 
	 * @param bytes
	 * @return int number
	 */

	public static long dwordToLong(byte[] bytes) {
		long number = bytes[3] & 0xFF;
		// "|="按位或赋值。
		number |= ((bytes[2] << 8) & 0xFF00);
		number |= ((bytes[1] << 16) & 0xFF0000);
		number |= ((bytes[0] << 24) & 0xFF000000);
		return number;
	}

	public static long dwordToLong(ByteBuffer in) {
		long number = in.get() & 0xFF;
		number = (number << 8) | (in.get() & 0xFF);
		number = (number << 8) | (in.get() & 0xFF);
		number = (number << 8) | (in.get() & 0xFF);

		return number;
	}

	public static int dwordToInt(ByteBuffer in) {
		int number = in.get() & 0xFF;
		number = (number << 8) | (in.get() & 0xFF);
		number = (number << 8) | (in.get() & 0xFF);
		number = (number << 8) | (in.get() & 0xFF);

		return number;
	}

	public static String dwordToICNumber(ByteBuffer in) {

		long number = in.get() & 0xFF;

		number = ((long) (in.get() & 0xFF) << 8) | number;
		number = ((long) (in.get() & 0xFF) << 16) | number;
		number = ((long) (in.get() & 0xFF) << 24) | number;

		StringBuffer sb = new StringBuffer(String.valueOf(number));

		while (sb.length() < 10) {
			sb.insert(0, '0');
		}

		return sb.toString();
	}

	/**
	 * 双字节数组转换成int，bytes[0]为高八位，bytes[1]为低八位。
	 * 
	 * @param bytes
	 *            两个字节的数组
	 * @return
	 */
	public static int wordToInt(byte[] bytes) {
		int number = bytes[0] & 0xFF;
		number = (number << 8) | (bytes[1] & 0xFF);
		return number;
	}

	public static int byteToInt(byte[] bytes) {
		Integer result = 0;
		for (int i = 0; i != 8; ++i) {
			result |= bytes[i] << i;
		}
		return result;
	}

	public static int wordToInt(byte[] bytes, int p) {
		int number = bytes[p] & 0xFF;
		number = (number << 8) | (bytes[p + 1] & 0xFF);
		return number;
	}

	public static int wordToInt(ByteBuffer in) {
		int number = in.get() & 0xFF;
		number = (number << 8) | (in.get() & 0xFF);
		return number;
	}

	/**
	 * int型整数转换成双字节数组。bytes[0]为高八位，bytes[1]为低八位。
	 * 
	 * @param number
	 * @return
	 */
	public static byte[] intToWord(int number) {
		byte[] bytes = new byte[2];
		bytes[1] = (byte) (0xff & number);
		// ">>"右移位，若为正数则高位补0，若为负数则高位补1
		bytes[0] = (byte) ((0xff00 & number) >> 8);
		return bytes;
	}

	/**
	 * int型整数转换成双字节数组。bytes[0]为高八位，bytes[1]为低八位。
	 * 
	 * @param number
	 * @return
	 */
	public static byte[] intToDword(int number) {
		byte[] bytes = new byte[4];

		bytes[3] = (byte) (0xff & number);
		// ">>"右移位，若为正数则高位补0，若为负数则高位补1
		bytes[2] = (byte) ((0xff00 & number) >> 8);

		bytes[1] = (byte) ((0xff0000 & number) >> 16);

		bytes[0] = (byte) ((0xff000000 & number) >> 24);

		return bytes;
	}

	public static void intToWord(int number, ByteBuffer bb) {
		bb.put(intToWord(number));
	}

	public static void intToDword(int number, ByteBuffer bb) {
		bb.put(intToDword(number));
	}

	/**
	 * 获取一个byte数组的校验码
	 * 
	 * @param bytes
	 * @return
	 */
	public static int validate(int v, byte[] bytes, int offset, int length) {
		int valicode = v;
		for (int i = offset; i < length; i++) {
			valicode = valicode ^ (bytes[i] & 0xFF);
		}
		return valicode;
	}

	public static int validate(int v, byte[] bytes) {
		return validate(v, bytes, 0, bytes.length);
	}

	public static int validate(int v, byte b) {
		byte[] bytes = new byte[1];
		bytes[0] = b;
		return validate(v, bytes, 0, 1);
	}

	public static int validate(byte[] bytes) {
		int valicode = bytes[0] & 0xFF;
		for (int i = 1; i < bytes.length; i++) {
			valicode = valicode ^ (bytes[i] & 0xFF);
		}
		return valicode;
	}

	/**
	 * 锐明4字节数组转时间
	 * 
	 * @param bytes
	 * @return
	 */
	public static Date dwordToDateRM(byte[] bytes) {
		long m = bytes[0] & 0xFF;
		// "|="按位或赋值。
		m = m << 8 | (bytes[1] & 0xFF);
		m = m << 8 | (bytes[2] & 0xFF);
		m = m << 8 | (bytes[3] & 0xFF);

		return new Date(m * 1000);
	}

	public static Date dwordToDateRM(ByteBuffer in) {
		byte[] bytes = new byte[4];
		in.get(bytes);
		return dwordToDateRM(bytes);
	}

	public static byte[] dateToDwordRM(Date date) {

		long m = date.getTime() / 1000;
		byte[] bytes = new byte[4];

		bytes[3] = intToUnsignedByte((int) (0xff & m));
		bytes[2] = intToUnsignedByte((int) ((0xff00 & m) >> 8));
		bytes[1] = intToUnsignedByte((int) ((0xff0000 & m) >> 16));
		bytes[0] = intToUnsignedByte((int) ((0xff000000 & m) >> 24));

		return bytes;
	}

	public static void dateToDwordRM(Date date, ByteBuffer in) {
		in.put(dateToDwordRM(date));
	}

	public static void dateToBcd6(Date date, ByteBuffer in) {
		SimpleDateFormat formatter = new SimpleDateFormat("yy MM dd HH mm ss");
		String dateString[] = formatter.format(date).split(" ");
		for (String str : dateString) {
			in.put(intToUnsignedByte(Integer.parseInt(str, 16)));
		}
	}

	public static void dateToBcd3(Date date, ByteBuffer in) {
		SimpleDateFormat formatter = new SimpleDateFormat("yy MM dd");
		String dateString[] = formatter.format(date).split(" ");
		for (String str : dateString) {
			in.put(intToUnsignedByte(Integer.parseInt(str, 16)));
		}
	}

	/**
	 * 四字节转经纬度，针对锐明设备。 第一个字节度，第二个字节分，第三个字节分的前两个小数位，第四个字节分的后两个小数位。
	 * 
	 * @param bytes
	 * @return
	 */
	public static double dwordToLatLngRM(byte[] bytes) {
		int latLng = unsignedByteToInt(bytes[0]);
		int latLngMinute = unsignedByteToInt(bytes[1]);
		int latLngMinuteFract = wordToInt(bytes, 2);
		StringBuffer latLngMinuteFractStr = new StringBuffer(String.valueOf(latLngMinuteFract));
		while (latLngMinuteFractStr.length() < 4) {
			latLngMinuteFractStr.insert(0, '0');
		}
		return latLng + Double.parseDouble(String.valueOf(latLngMinute) + "." + latLngMinuteFractStr) / 60;
	}

	public static double dwordToLatLngRM(ByteBuffer in) {
		byte[] bytes = new byte[4];
		in.get(bytes);
		return dwordToLatLngRM(bytes);
	}

	public static byte[] dataToBusDataRM(ByteBuffer in, int length) {
		byte[] bytes = new byte[length];
		in.get(bytes);
		return bytes;

	}

	/**
	 * 数据下传回拨电话返回byte数组
	 * 
	 * @return
	 */
	public static void stringToBytes(String str, ByteBuffer in) {
		in.put(stringToBytes(str));
	}

	public static byte[] stringToBytes(String str) {
		return str.getBytes(Charset.forName("GBK"));
	}

	public static String bytesToString(byte[] bytes) {
		return new String(bytes, Charset.forName("GB2312"));
	}

	public static String bytesToString(ByteBuffer in, int length) {
		byte[] bytes = new byte[length];
		in.get(bytes);
		return bytesToString(bytes);
	}

	/**
	 * 字节流转换成IC卡字符串
	 * 
	 * @param in
	 * @param length
	 * @return
	 */
	public static String bytesToICStr(ByteBuffer in, int length) {
		byte[] bytes = new byte[length];
		in.get(bytes);
		StringBuilder temp = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			// 高四位
			// temp.append((bytes[i] & 0xf0) >>> 4);
			// 低四位
			temp.append(bytes[i] & 0x0f);
		}
		return temp.toString();
	}

	public static Date bcd6ToDate(ByteBuffer in, int length) {
		StringBuffer str = new StringBuffer(length);

		Date date = null;

		// 年
		str.append("20");

		for (int i = 0; i < length; i++) {

			str.append(BinaryUtils.bcd2Str(in, 1) + " ");

		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy MM dd HH mm ss ");
		try {

			date = sdf.parse(str.toString());

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static Date bcd3ToDate(ByteBuffer in, int length) {
		StringBuffer str = new StringBuffer(length);

		Date date = null;

		// 年
		str.append("20");

		for (int i = 0; i < length; i++) {

			str.append(BinaryUtils.bcd2Str(in, 1) + " ");

		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy MM dd");
		try {

			date = sdf.parse(str.toString());

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * （length/4)个Dword转换位int封装到List中
	 * 
	 * @param in
	 * @param length
	 * @return
	 */
	public static List<Integer> getIntArrayForDword(ByteBuffer in, int length) {
		List<Integer> result = new ArrayList<Integer>();
		int start = in.position();

		if (in.limit() - start >= length) {
			for (int i = 0; i < length / 4; i++) {
				result.add(BinaryUtils.dwordToInt(in));
			}

		}
		return result;

	}

	/**
	 * （length/4)个Dword转换位int封装到List中
	 * 
	 * @param in
	 * @param length
	 * @return
	 */
	public static List<Integer> getIntArrayForWord(ByteBuffer in, int length) {
		List<Integer> result = new ArrayList<Integer>();
		int start = in.position();

		if (in.limit() - start >= length) {
			for (int i = 0; i < length / 2; i++) {
				result.add(BinaryUtils.wordToInt(in));
			}

		}
		return result;
	}

	public static char[] convertIntTo32Char(int value) {
		char[] arrayChar = new char[32];
		Arrays.fill(arrayChar, '0');
		char[] chatArr = Integer.toBinaryString(value).toCharArray();
		System.arraycopy(chatArr, 0, arrayChar, 0, chatArr.length);
		return arrayChar;
	}

	/**
	 * （length/4)个Dword转换位int*num封装到List中
	 * 
	 * @param in
	 * @param length
	 * @return
	 */
	public static List<Integer> getIntArrayForWord(ByteBuffer in, int length, int num) {
		List<Integer> result = new ArrayList<Integer>();
		int start = in.position();

		if (in.limit() - start >= length) {
			for (int i = 0; i < length / 2; i++) {
				result.add(BinaryUtils.wordToInt(in) * num);
			}

		}
		return result;

	}

	/**
	 * 数据校验
	 * 
	 * @return
	 */
	private static int checkValidity(String str) {

		byte[] bytes = hexStr2Bytes(str);

		ByteBuffer in = ByteBuffer.wrap(bytes);

		int xorResult = 0;

		for (int i = in.position(); i < in.limit(); i++) {
			xorResult = xorResult ^ in.get();
		}

		return xorResult;
	}

	/**
	 * Main For Test
	 * 
	 * @param args
	 */
//	static public void main(String[] args) {
//
//		// byte[] bytes = hexStr2Bytes("F9 AA");
//		//
//		// ByteBuffer bb = ByteBuffer.wrap(bytes);
//
//		byte bytes = intToUnsignedByte(Integer.parseInt("17", 16));
//
//		// BinaryUtils.dwordToLong(bb,6);
//		//
//		// System.out.println(date);
//
//		// String str = "02 00 00 9E 01 38 88 88 88 88 00 02 20 80 00 80 00 00
//		// 10 01 02 09 25 E7 06 78 8B 3C 01 F4 00 0A 00 00 16 10 11 14 20 30 01
//		// 04 00 01 86 A0 02 02 03 E8 25 04 00 01 80 05 E1 04 00 00 03 E8 E2 04
//		// 00 00 00 01 E3 04 00 00 00 01 E4 04 00 00 03 E8 E5 04 00 00 00 01 E6
//		// 04 00 00 00 01 E7 04 00 00 00 0A E8 04 00 00 00 0A E9 04 00 00 00 00
//		// EA 04 00 00 03 E8 EB 04 00 00 03 E8 EC 04 00 00 03 E8 ED 04 00 00 03
//		// E8 EE 04 00 00 00 03 EF 04 00 00 00 03 F0 04 00 00 00 C8 F1 04 00 00
//		// 00 28 F2 04 00 00 00 03 F3 04 00 00 00 03";
//		// System.out.println("检验码:"+BinaryUtils.checkValidity(str));
//
//		/*
//		 * byte[] bytes = hexStr2Bytes("C3 3C C7 C4");
//		 * 
//		 * ByteBuffer bb = ByteBuffer.wrap(bytes);
//		 * 
//		 * System.out.println(dwordToICNumber(bb));
//		 */
//
//		// byte[] bytes = hexStr2Bytes("00 01 80 05");
//		//
//		// ByteBuffer bb = ByteBuffer.wrap(bytes);
//		//
//		// long a = BinaryUtils.dwordToLong(bb);
//		//
//		// byte[] m =BinaryUtils.longToDword(a);
//
//		// String a = BinaryUtils.bcd2Str(bb,2);
//		//
//		// System.out.print(a);
//		/*
//		 * byte[] bytes = hexStr2Bytes("00 D6 38 01 00 00  21"); //7E 01 A0 34
//		 * 14 00 06 62 00 03 08 01 00 98 7F 7E 01 A2 34 14 00 06 62 00 03 02 05
//		 * 00 9A 7F byte[] noMes = null; // String string =
//		 * "00 D6 38 01 00 00 7E 01 A0 34 14 00 06 62 00 03 00 00 00 98 7F 7E 01 A2 34 14 00 06 62 00 03 00 00 00 9A 7F 21"
//		 * ; // byte[] bytes1 = new byte[]{00, D6, 38, 01, 00, 00, 7E, 01, A0,
//		 * 34, 14, 00, 06, 62, 00, 03, 00, 00, 00, 98, 7F, 7E, 01, A2, 34, 14,
//		 * 00, 06, 62, 00, 03, 00, 00, 00, 9A, 7F, 21}; ByteBuffer buffer =
//		 * ByteBuffer.wrap(bytes); byte[] devIdByte = new byte[3];
//		 * buffer.get(devIdByte); int devInt =
//		 * BinaryUtils.threeBytesToInt(devIdByte); byte[] lineTypebyte = new
//		 * byte[1]; buffer.get(lineTypebyte); String lineTyte =
//		 * String.valueOf(BinaryUtils.unsignedByteToInt(lineTypebyte[0]));
//		 * byte[] stationSqn = new byte[2]; buffer.get(stationSqn); int
//		 * stationSeq = BinaryUtils.wordToInt(stationSqn); int endFlagInt = 0;
//		 * int packHead= 0; Integer fdoorInt = null; Integer fdoorInInt = null;
//		 * Integer fdoorOffInt = null; Integer cdoorInInt = null; Integer
//		 * cdoorOffInt = null; Integer edoorInInt = null; Integer edoorOffInt =
//		 * null; byte[] pfrData = new byte[1]; buffer.get(pfrData); packHead =
//		 * BinaryUtils.unsignedByteToInt(pfrData[0]); if(packHead==126){ do{
//		 * noMes = new byte[1]; buffer.get(noMes); byte[] fdoorByte = new
//		 * byte[1]; buffer.get(fdoorByte); fdoorInt =
//		 * BinaryUtils.unsignedByteToInt(fdoorByte[0]); //前门 if(fdoorInt==160){
//		 * noMes = new byte[7]; buffer.get(noMes); byte[] fdoorInByte = new
//		 * byte[1]; buffer.get(fdoorInByte); fdoorInInt =
//		 * BinaryUtils.unsignedByteToInt(fdoorInByte[0]); //
//		 * passFR.setGetFOnNumber(BinaryUtils.unsignedByteToInt(fdoorInByte[0]))
//		 * ; byte[] fdoorOffByte = new byte[1]; buffer.get(fdoorOffByte);
//		 * fdoorOffInt = BinaryUtils.unsignedByteToInt(fdoorOffByte[0]); //
//		 * passFR.setGetFOffNumber(); }else if(fdoorInt==161){//中门 noMes = new
//		 * byte[7]; buffer.get(noMes); byte[] fdoorInByte = new byte[1];
//		 * buffer.get(fdoorInByte); cdoorInInt =
//		 * BinaryUtils.unsignedByteToInt(fdoorInByte[0]); //
//		 * passFR.setGetCOnNumber(BinaryUtils.unsignedByteToInt(fdoorInByte[0]))
//		 * ; byte[] fdoorOffByte = new byte[1]; buffer.get(fdoorOffByte);
//		 * cdoorOffInt = BinaryUtils.unsignedByteToInt(fdoorOffByte[0]); //
//		 * passFR.setGetCOffNumber(BinaryUtils.unsignedByteToInt(fdoorOffByte[0]
//		 * )); }else if(fdoorInt==162){//后门 noMes = new byte[7];
//		 * buffer.get(noMes); byte[] fdoorInByte = new byte[1];
//		 * buffer.get(fdoorInByte); edoorInInt =
//		 * BinaryUtils.unsignedByteToInt(fdoorInByte[0]); //
//		 * passFR.setGetEOnNumber(BinaryUtils.unsignedByteToInt(fdoorInByte[0]))
//		 * ; byte[] fdoorOffByte = new byte[1]; buffer.get(fdoorOffByte);
//		 * edoorOffInt = BinaryUtils.unsignedByteToInt(fdoorOffByte[0]); //
//		 * passFR.setGetEOffNumber(BinaryUtils.unsignedByteToInt(fdoorOffByte[0]
//		 * )); }
//		 * 
//		 * 
//		 * noMes = new byte[3]; buffer.get(noMes); byte[] endFlag = new byte[1];
//		 * buffer.get(endFlag); endFlagInt =
//		 * BinaryUtils.unsignedByteToInt(endFlag[0]);
//		 * 
//		 * }while(endFlagInt!=33); }
//		 * 
//		 * 
//		 * System.out.println("devInt="+devInt);
//		 * System.out.println("lineTyte="+lineTyte);
//		 * System.out.println("stationSeq="+stationSeq);
//		 * System.out.println("packHead="+packHead);
//		 * System.out.println("fdoorInt="+fdoorInt);
//		 * System.out.println("fdoorInInt="+fdoorInInt);
//		 * System.out.println("fdoorOffInt="+fdoorOffInt);
//		 * System.out.println("cdoorInInt="+cdoorInInt);
//		 * System.out.println("cdoorOffInt="+cdoorOffInt);
//		 * System.out.println("edoorInInt="+edoorInInt);
//		 * System.out.println("edoorOffInt="+edoorOffInt);
//		 * System.out.println("endFlagInt="+endFlagInt); System.out.println(new
//		 * Date());
//		 * 
//		 * // buffer.get(pfrData); //
//		 * System.out.println(BinaryUtils.unsignedByteToInt(buffer.get(pfrData))
//		 * );
//		 */
//	}

}