package mapsoft.com.costomtopbar.service.socket;

import mapsoft.com.costomtopbar.jt808.MsgDecoder;
import mapsoft.com.costomtopbar.jt808.MsgEncoder;
import mapsoft.com.costomtopbar.jt808.PackageData;
import mapsoft.com.costomtopbar.jt808.TerminalRegisterMsg;

/**
 * @author djl
 * @function
 */

public class RigsterMessage {

    private MsgDecoder mMsgDecoder;
    private MsgEncoder mMsgEncoder;
    private TerminalRegisterMsg req;
    private PackageData packageData;
    private static byte[] message;
    private static int MsgId = 256;
    private static int MsgBodyLength = 37;
    public RigsterMessage(String phoneNum, int flowId) {
        this.mMsgDecoder = new MsgDecoder();
        this.mMsgEncoder = new MsgEncoder();
        this.packageData = new PackageData();
        initPackageData(phoneNum,flowId);
    }



    private void initPackageData(String terminalPhone,int flowid) {

        PackageData.MsgHeader msgHeader = new PackageData.MsgHeader(MsgId,MsgBodyLength,terminalPhone,flowid);
        msgHeader.setMsgId(256);
        msgHeader.setMsgBodyPropsField(37);
        //msgHeader.setTerminalPhone("00000077777");
        //msgHeader.setEncryptionType(0);
        msgHeader.setFlowId(1);
        //msgHeader.setHasSubPackage(false);
        msgHeader.setMsgBodyLength(37);
        //msgHeader.setReservedBit("0");
        //msgHeader.setSubPackageSeq(0);
        //msgHeader.setTotalSubPackage(0);
        //msgHeader.setPackageInfoField(0);
        packageData.setMsgHeader(msgHeader);
        //packageData.setMsgBodyBytes(req.getMsgBodyBytes());
        packageData.setCheckSum(31);
        /*TerminalRegisterMsg terminalRegisterMsg = new TerminalRegisterMsg(packageData);
		terminalRegisterMsg = msgDecoder.toTerminalRegisterMsg(packageData);*/

    }

    public byte[] send(byte[] terminalType,byte[] terminalId , byte[] manufacturerId){
        req = new TerminalRegisterMsg(packageData);
        TerminalRegisterMsg.TerminalRegInfo body = new TerminalRegisterMsg.TerminalRegInfo();

        body.setProvinceId(33);
        body.setCityId(108);
        body.setLicensePlateColor(1);
        body.setTerminalType(mMsgDecoder.parseStringFromBytes(terminalType,0,terminalType.length));
        body.setTerminalId(mMsgDecoder.parseStringFromBytes(terminalId,0,terminalId.length));
        body.setManufacturerId(mMsgDecoder.parseStringFromBytes(manufacturerId,0,manufacturerId.length));
        //packageData = msgDecoder.bytes2PackageData(TPMSConsts.rigster_header);
		/*byte[] msgbody = {0x00,0x21,0x00,0x6C,0x37,0x31,0x31,0x30,0x32,0x4D,0x44,0x4A,0x36,0x31,0x30,0x30,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00}*/
        req.setTerminalRegInfo(body);
        MsgEncoder msgEncoder = new MsgEncoder();
        try {
            message = msgEncoder.encode4TerminalRegisterMsg(req,1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }
    public byte[] simpleSend(){
        req = new TerminalRegisterMsg(packageData);
        TerminalRegisterMsg.TerminalRegInfo body = new TerminalRegisterMsg.TerminalRegInfo();
        //packageData = msgDecoder.bytes2PackageData(TPMSConsts.rigster_header);
		/*byte[] msgbody = {0x00,0x21,0x00,0x6C,0x37,0x31,0x31,0x30,0x32,0x4D,0x44,0x4A,0x36,0x31,0x30,0x30,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00}*/
        req.setTerminalRegInfo(body);
        MsgEncoder msgEncoder = new MsgEncoder();
        try {
            message = msgEncoder.encode4TerminalRegisterMsg(req,1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }

}
