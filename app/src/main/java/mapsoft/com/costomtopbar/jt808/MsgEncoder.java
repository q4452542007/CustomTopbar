package mapsoft.com.costomtopbar.jt808;


import android.util.Log;

import java.util.Arrays;

import mapsoft.com.costomtopbar.util.BCD8421Operater;
import mapsoft.com.costomtopbar.util.BitOperator;
import mapsoft.com.costomtopbar.util.Utils;

import static mapsoft.com.costomtopbar.fragment.HomeFragment.TAG;


public class MsgEncoder {
	private BitOperator bitOperator;
	private JT808ProtocolUtils jt808ProtocolUtils;
	private MsgDecoder mMsgDecoder;
	private BCD8421Operater mBCD8421Operater;

	public MsgEncoder() {
		this.bitOperator = new BitOperator();
		this.jt808ProtocolUtils = new JT808ProtocolUtils();
		this.mMsgDecoder = new MsgDecoder();
		this.mBCD8421Operater = new BCD8421Operater();
	}

	/*public byte[] encode4TerminalRegisterResp(TerminalRegisterMsg req, TerminalRegisterMsgRespBody respMsgBody,
											  int flowId) throws Exception {
		// 消息体字节数组
		byte[] msgBody = null;
		// 鉴权码(STRING) 只有在成功后才有该字段
		if (respMsgBody.getReplyCode() == TerminalRegisterMsgRespBody.success) {
			msgBody = this.bitOperator.concatAll(Arrays.asList(//
					bitOperator.integerTo2Bytes(respMsgBody.getReplyFlowId()), // 流水号(2)
					new byte[] { respMsgBody.getReplyCode() }, // 结果
					respMsgBody.getReplyToken().getBytes(TPMSConsts.string_charset)// 鉴权码(STRING)
			));
		} else {
			msgBody = this.bitOperator.concatAll(Arrays.asList(//
					bitOperator.integerTo2Bytes(respMsgBody.getReplyFlowId()), // 流水号(2)
					new byte[] { respMsgBody.getReplyCode() }// 错误代码
			));
		}

		// 消息头
		int msgBodyProps = this.jt808ProtocolUtils.generateMsgBodyProps(msgBody.length, 0b000, false, 0);
		byte[] msgHeader = this.jt808ProtocolUtils.generateMsgHeader(req.getMsgHeader().getTerminalPhone(),
				TPMSConsts.cmd_terminal_register_resp, msgBody, msgBodyProps, flowId);
		byte[] headerAndBody = this.bitOperator.concatAll(msgHeader, msgBody);

		// 校验码
		int checkSum = this.bitOperator.getCheckSum4JT808(headerAndBody, 0, headerAndBody.length - 1);
		// 连接并且转义
		return this.doEncode(headerAndBody, checkSum);
	}*/

	// public byte[] encode4ServerCommonRespMsg(TerminalAuthenticationMsg req,
	// ServerCommonRespMsgBody respMsgBody, int flowId) throws Exception {
	/*public byte[] encode4ServerCommonRespMsg(PackageData req, ServerCommonRespMsgBody respMsgBody, int flowId)
			throws Exception {
		byte[] msgBody = this.bitOperator.concatAll(Arrays.asList(//
				bitOperator.integerTo2Bytes(respMsgBody.getReplyFlowId()), // 应答流水号
				bitOperator.integerTo2Bytes(respMsgBody.getReplyId()), // 应答ID,对应的终端消息的ID
				new byte[] { respMsgBody.getReplyCode() }// 结果
		));

		// 消息头
		int msgBodyProps = this.jt808ProtocolUtils.generateMsgBodyProps(msgBody.length, 0b000, false, 0);
		byte[] msgHeader = this.jt808ProtocolUtils.generateMsgHeader(req.getMsgHeader().getTerminalPhone(),
				TPMSConsts.cmd_common_resp, msgBody, msgBodyProps, flowId);
		byte[] headerAndBody = this.bitOperator.concatAll(msgHeader, msgBody);
		// 校验码
		int checkSum = this.bitOperator.getCheckSum4JT808(headerAndBody, 0, headerAndBody.length - 1);
		// 连接并且转义
		return this.doEncode(headerAndBody, checkSum);
	}*/

	/*public byte[] encode4ParamSetting(byte[] msgBodyBytes, Session session) throws Exception {
		// 消息头
		int msgBodyProps = this.jt808ProtocolUtils.generateMsgBodyProps(msgBodyBytes.length, 0b000, false, 0);
		byte[] msgHeader = this.jt808ProtocolUtils.generateMsgHeader(session.getTerminalPhone(),
				TPMSConsts.cmd_terminal_param_settings, msgBodyBytes, msgBodyProps, session.currentFlowId());
		// 连接消息头和消息体
		byte[] headerAndBody = this.bitOperator.concatAll(msgHeader, msgBodyBytes);
		// 校验码
		int checkSum = this.bitOperator.getCheckSum4JT808(headerAndBody, 0, headerAndBody.length - 1);
		// 连接并且转义
		return this.doEncode(headerAndBody, checkSum);
	}*/

	public byte[] encode4TerminalCheckPower(PackageData packageData,
											 int flowId) throws Exception {
		byte[] msgBody = null;
		byte[] powerCode = null;

		powerCode = mMsgDecoder.toTerminalRegisterMsgRespBody(packageData).getReplyCode();
		msgBody = mMsgDecoder.toTerminalRegisterMsgRespBody(packageData).getReplyCode();

		// 消息头
		int msgBodyProps = this.jt808ProtocolUtils.generateMsgBodyProps(msgBody.length, 0b000, false, 0);

		byte[] msgHeader = this.jt808ProtocolUtils.generateMsgHeader(packageData.getMsgHeader().getTerminalPhone(),
				TPMSConsts.msg_id_terminal_authentication, msgBody, msgBodyProps, flowId);

		byte[] headerAndBody = this.bitOperator.concatAll(msgHeader, msgBody);

		// 校验码
		int checkSum = this.bitOperator.getCheckSum4JT808(headerAndBody, 0, headerAndBody.length);

		// 连接并且转义
		return this.doEncode(headerAndBody, checkSum);
	}


	/**
	 *	终端发送注册
	 * @param req
	 * @param flowId
	 * @return
	 * @throws Exception
	 */
	public byte[] encode4TerminalRegisterMsg(TerminalRegisterMsg req,
											 int flowId) throws Exception {
		// 消息体字节数组
		byte[] msgBody = null;
		msgBody = this.bitOperator.concatAll(Arrays.asList(//
				bitOperator.integerTo2Bytes(req.getTerminalRegInfo().getProvinceId()), // 省
				bitOperator.integerTo2Bytes(req.getTerminalRegInfo().getCityId()), // 市
				req.getTerminalRegInfo().getManufacturerId().getBytes(TPMSConsts.string_charset),// 制造商ID
				req.getTerminalRegInfo().getTerminalType().getBytes(TPMSConsts.string_charset),// 终端型号
				req.getTerminalRegInfo().getTerminalId().getBytes(TPMSConsts.string_charset),// 终端ID
				bitOperator.integerTo1Bytes(req.getTerminalRegInfo().getLicensePlateColor()) // 车牌颜色
		));
		// 消息头
		int msgBodyProps = this.jt808ProtocolUtils.generateMsgBodyProps(msgBody.length, 0b000, false, 0);

		byte[] msgHeader = this.jt808ProtocolUtils.generateMsgHeader(req.getMsgHeader().getTerminalPhone(),
				TPMSConsts.msg_id_terminal_register, msgBody, msgBodyProps, flowId);

		byte[] headerAndBody = this.bitOperator.concatAll(msgHeader, msgBody);

		// 校验码
		int checkSum = this.bitOperator.getCheckSum4JT808(headerAndBody, 0, headerAndBody.length);

		// 连接并且转义
		return this.doEncode(headerAndBody, checkSum);
	}

	/**
	 *	简单终端发送注册
	 * @param req
	 * @param flowId
	 * @return
	 * @throws Exception
	 */
	public byte[] encode4SimpleTerminalRegisterMsg(TerminalRegisterMsg req,
											 int flowId) throws Exception {
		// 消息体字节数组
		byte[] msgBody = null;
		msgBody = this.bitOperator.concatAll(Arrays.asList(//
				bitOperator.integerTo2Bytes(req.getTerminalRegInfo().getProvinceId()), // 省
				bitOperator.integerTo2Bytes(req.getTerminalRegInfo().getCityId()), // 市
				req.getTerminalRegInfo().getManufacturerId().getBytes(TPMSConsts.string_charset),// 制造商ID
				req.getTerminalRegInfo().getTerminalType().getBytes(TPMSConsts.string_charset),// 终端型号
				req.getTerminalRegInfo().getTerminalId().getBytes(TPMSConsts.string_charset),// 终端ID
				bitOperator.integerTo1Bytes(req.getTerminalRegInfo().getLicensePlateColor()) // 车牌颜色
		));
		// 消息头
		int msgBodyProps = this.jt808ProtocolUtils.generateMsgBodyProps(msgBody.length, 0b000, false, 0);

		byte[] msgHeader = this.jt808ProtocolUtils.generateMsgHeader(req.getMsgHeader().getTerminalPhone(),
				TPMSConsts.msg_id_terminal_register, msgBody, msgBodyProps, flowId);

		byte[] headerAndBody = this.bitOperator.concatAll(msgHeader, msgBody);

		// 校验码
		int checkSum = this.bitOperator.getCheckSum4JT808(headerAndBody, 0, headerAndBody.length);

		// 连接并且转义
		return this.doEncode(headerAndBody, checkSum);
	}

	public byte[] encode4HeartBeatMsg(int flowId) throws Exception {
		// 消息体为空
		byte[] msgBody = null;
		// 消息头
		int msgBodyProps = this.jt808ProtocolUtils.generateMsgBodyProps(0, 0b000, false, 0);

		byte[] msgHeader = this.jt808ProtocolUtils.generateMsgHeader(TPMSConsts.phoneNum,
				TPMSConsts.msg_id_terminal_heart_beat, msgBody, msgBodyProps, flowId);

		byte[] headerAndBody = msgHeader;

		// 校验码
		int checkSum = this.bitOperator.getCheckSum4JT808(headerAndBody, 0, headerAndBody.length);

		// 连接并且转义
		return this.doEncode(headerAndBody, checkSum);
	}

	public byte[] encode4LocationInfoUploadMsg(LocationInfoUploadMsg lim,int flow) throws Exception {
		// 消息体字节数组
		byte[] msgBody = null;
		String time = lim.getTime();
		time = time.replaceAll("-","").replaceAll(" ","").replaceAll(":","").substring(2);
		Log.e(TAG,"time : " + time);
		msgBody = this.bitOperator.concatAll(Arrays.asList(//
				bitOperator.integerTo4Bytes(lim.getWarningFlagField()), // 报警标志
				bitOperator.integerTo4Bytes(lim.getStatusField()), // 状态
				bitOperator.integerTo4Bytes((int) (lim.getLatitude()*1000000)),// 纬度
				bitOperator.integerTo4Bytes((int) (lim.getLongitude()*1000000)),// 经度
				bitOperator.integerTo2Bytes(lim.getElevation()),// 终端ID
				bitOperator.integerTo2Bytes(Float.floatToIntBits(lim.getSpeed())),
				bitOperator.integerTo2Bytes(lim.getDirection()), // 车牌颜色
				mBCD8421Operater.string2Bcd(time)
		));
		String message = Utils.bytes2HexString(msgBody);

		// 消息头
		int msgBodyProps = this.jt808ProtocolUtils.generateMsgBodyProps(msgBody.length, 0b000, false, 0);

		byte[] msgHeader = this.jt808ProtocolUtils.generateMsgHeader(TPMSConsts.phoneNum,
				TPMSConsts.msg_id_terminal_location_info_upload, msgBody, msgBodyProps, flow);

		byte[] headerAndBody = this.bitOperator.concatAll(msgHeader, msgBody);

		// 校验码
		int checkSum = this.bitOperator.getCheckSum4JT808(headerAndBody, 0, headerAndBody.length);

		// 连接并且转义
		return this.doEncode(headerAndBody, checkSum);
	}



	public byte[] encode4DriverLoginMsg(DriverLoginMsg driverLoginMsg) throws Exception{
		// 消息体字节数组
		byte[] msgBody = null;
		String time = driverLoginMsg.getLocationInfoUploadMsg().getTime();
		time = time.replaceAll("-","").replaceAll(" ","").replaceAll(":","").substring(2);
		byte[] platNum = {0x00,0x00,0x00,0x00,0x00,0x00};
		//platNum = driverLoginMsg.getPlatNum().getBytes();
		msgBody = this.bitOperator.concatAll(Arrays.asList(//
				bitOperator.integerTo4Bytes(driverLoginMsg.getLocationInfoUploadMsg().getWarningFlagField()), // 报警标志
				bitOperator.integerTo4Bytes(driverLoginMsg.getLocationInfoUploadMsg().getStatusField()), // 状态
				bitOperator.integerTo4Bytes((int) (driverLoginMsg.getLocationInfoUploadMsg().getLatitude()*1000000)),// 纬度
				bitOperator.integerTo4Bytes((int) (driverLoginMsg.getLocationInfoUploadMsg().getLongitude()*1000000)),// 经度
				bitOperator.integerTo2Bytes(driverLoginMsg.getLocationInfoUploadMsg().getElevation()),// 海拔
				bitOperator.integerTo2Bytes(Float.floatToIntBits(driverLoginMsg.getLocationInfoUploadMsg().getSpeed())), //速度
				bitOperator.integerTo2Bytes(driverLoginMsg.getLocationInfoUploadMsg().getDirection()), // 方向
				mBCD8421Operater.string2Bcd(time), //时间
				mBCD8421Operater.string2Bcd(driverLoginMsg.getCompanyId()), //单位代码
				mBCD8421Operater.string2Bcd(driverLoginMsg.getDriverId()), //司机ID
				platNum,	//车牌号
				mBCD8421Operater.string2Bcd(driverLoginMsg.getPwd())  //密码
		));
		//String message = Utils.bytes2HexString(msgBody);

		// 消息头
		int msgBodyProps = this.jt808ProtocolUtils.generateMsgBodyProps(msgBody.length, 0b000, false, 0);

		byte[] msgHeader = this.jt808ProtocolUtils.generateMsgHeader(TPMSConsts.phoneNum,
				TPMSConsts.msg_id_terminal_driver_sign_in, msgBody, msgBodyProps, 81);

		byte[] headerAndBody = this.bitOperator.concatAll(msgHeader, msgBody);

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
		// 转义
		return jt808ProtocolUtils.doEscape4Send(noEscapedBytes, 1, noEscapedBytes.length - 2);
	}

    public byte[] encode4TerminalCommonRespMsg(ServerTextMsg textMsg) throws Exception {
		byte[] msgBody = null;


		msgBody = this.bitOperator.concatAll(Arrays.asList(//
				bitOperator.integerTo2Bytes(textMsg.getReplyFlowId()),
				bitOperator.integerTo2Bytes(TPMSConsts.msg_id_terminal_text_msg_issue),
				bitOperator.integerTo1Bytes(0)//
		));

		// 消息头
		int msgBodyProps = this.jt808ProtocolUtils.generateMsgBodyProps(msgBody.length, 0b000, false, 0);

		byte[] msgHeader = this.jt808ProtocolUtils.generateMsgHeader(textMsg.getTerminalPhone(),
				TPMSConsts.msg_id_terminal_common_resp, msgBody, msgBodyProps, 0);

		byte[] headerAndBody = this.bitOperator.concatAll(msgHeader, msgBody);

		// 校验码
		int checkSum = this.bitOperator.getCheckSum4JT808(headerAndBody, 0, headerAndBody.length);

		// 连接并且转义
		return this.doEncode(headerAndBody, checkSum);
    }
}
