package mapsoft.com.costomtopbar.jt808;

import java.nio.charset.Charset;

public class TPMSConsts {

	public static final String string_encoding = "GBK";

	public static final Charset string_charset = Charset.forName(string_encoding);
	// 标识位
	public static final int pkg_delimiter = 0x7e;
	// 客户端发呆15分钟后,服务器主动断开连接
	public static int tcp_client_idle_minutes = 30;

	// 终端通用应答
	public static final int msg_id_terminal_common_resp = 0x0001;
	// 终端心跳
	public static final int msg_id_terminal_heart_beat = 0x0002;
	// 终端注册
	public static final int msg_id_terminal_register = 0x0100;
	// 终端注销
	public static final int msg_id_terminal_log_out = 0x0003;
	// 终端鉴权
	public static final int msg_id_terminal_authentication = 0x0102;
	// 位置信息汇报
	public static final int msg_id_terminal_location_info_upload = 0x0200;
	// 胎压数据透传
	public static final int msg_id_terminal_transmission_tyre_pressure = 0x0600;
	// 查询终端参数应答
	public static final int msg_id_terminal_param_query_resp = 0x0104;
	//事件报告
	public static final int msg_id_terminal_affair_upload = 0x0301;
	//驾驶员签到
	public static final int msg_id_terminal_driver_sign_in = 0x0f03;
	// 平台通用应答
	public static final int cmd_common_resp = 0x8001;
	// 终端注册应答
	public static final int cmd_terminal_register_resp = 0x8100;
	// 设置终端参数
	public static final int cmd_terminal_param_settings = 0x8103;
	// 查询终端参数
	public static final int cmd_terminal_param_query = 0x8104;

	public static final String phoneNum = "000000077777";
	public static final byte[] terminalType = {0x4D,0x44,0x4A,0x36,0x31,0x30,0x30,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
	public static final byte[] terminalId = {0x00,0x00,0x00,0x00,0x00,0x00,0x00};
	public static final byte[] manufacturerId = {0x37,0x31,0x31,0x30,0x32};
	public static final byte[]  rigster_header = {0x01,0x00,0x00,0x25,0x00,0x00,0x00,0x07,0x77,0x77,0x00,0x01,0x00,0x21,0x00,0x6C,0x37,0x31,0x31,0x30,0x32,0x4D,0x44,0x4A,0x36,0x31,0x30,0x30,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x01,0x1F};
}
