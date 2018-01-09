package mapsoft.com.costomtopbar.constant;


import java.nio.charset.Charset;

/**
 * Created by jinglong.Dong on 2017/12/29.
 */

public class MapSoftConsts {
	public static final String string_encoding = "GB2312";

	public static final Charset string_charset = Charset.forName(string_encoding);

	// 车内屏广告帧头
	public static final int ad_frame_header_screen_in_bus = 0x55AA;

	//车内屏结束标志
	public static final int end_flag_screen_in_bus = 0xFF;

	// 报站器与车内屏旧通信方式使用索引码
	public static final int com_old_screen_in_bus = 0x5A;

	// 广告语软件与车内屏通信
	public static final int com_ad_screen_in_bus = 0x5B;

	// 软件与车内屏时间通信
	public static final int com_time_screen_in_bus = 0x5C;

	// 软件与车内屏温度通信
	public static final int com_temp_screen_in_bus = 0x5D;

	// 清楚车内屏广告等信息
	public static final int com_empty_screen_in_bus = 0x5E;

	//路牌编号
	public static final int the_header_plate = 0x01;
	public static final int the_waist_plate = 0x02;
	public static final int the_end_plate = 0x03;

	//路牌属性
	public static final int attribute_display_plate = 0x01;
	public static final int attribute_notdisplay_plate = 0x00;

	//显示风格
	public static final int up_down_display = 0x01;
	public static final int left_right_display = 0x02;

	//通讯命令字
	public static final int com_report = 0x01;  //报站器
	public static final int com_lectronic_display_board_programme = 0x02;  //电子路牌编程
	public static final int com_lectronic_display_board_info = 0x03;

	public static final byte[] com_clear_ad = {0x55, (byte) 0xAA,0x5E, (byte) 0xFE,0x00, (byte) 0xFF, (byte) 0xA0};
}
