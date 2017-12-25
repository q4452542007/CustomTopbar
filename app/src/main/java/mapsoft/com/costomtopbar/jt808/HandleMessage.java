package mapsoft.com.costomtopbar.jt808;

import android.util.Log;

import mapsoft.com.costomtopbar.util.BitOperator;

/**
 * @author djl
 * @function
 */

public class HandleMessage {

    private static final String TAG = "HandleMessage";

    private MsgDecoder mMsgDecoder;
    private MsgEncoder mMsgEncoder;
    private ServerCommonRespMsgBody mServerCommonRespMsgBody;
    private TerminalRegisterMsgRespBody mTerminalRegisterMsgRespBody;
    private ServerTextMsg mTextMsg;
    private BitOperator mBitOperator;

    public HandleMessage() {
        this.mMsgDecoder = new MsgDecoder();
        this.mMsgEncoder = new MsgEncoder();
        this.mServerCommonRespMsgBody = new ServerCommonRespMsgBody();
        this.mTerminalRegisterMsgRespBody = new TerminalRegisterMsgRespBody();
        mTextMsg = new ServerTextMsg();
        this.mBitOperator = new BitOperator();
    }

    public int handleAllMessage(PackageData packageData) {
        int a = 0;
        int s = packageData.getMsgHeader().getMsgId();
        switch (packageData.getMsgHeader().getMsgId()) {
            case TPMSConsts.cmd_terminal_register_resp:
                mTerminalRegisterMsgRespBody = mMsgDecoder.toTerminalRegisterMsgRespBody(packageData);
                a = handleTerminalRegisterResponse(mTerminalRegisterMsgRespBody);
                break;

            case TPMSConsts.cmd_common_resp:
                mServerCommonRespMsgBody = mMsgDecoder.toServerCommonRespMsgBody(packageData);
                a = handleServerCommonRespMsg(mServerCommonRespMsgBody);
                break;

            case TPMSConsts.msg_id_terminal_text_msg_issue:
                mTextMsg = mMsgDecoder.toServerTextMsgBody(packageData);
                a = handleServerTextMsgIssue(mTextMsg);
                break;
            default:
                break;
        }
        return a;
    }

    private int handleServerTextMsgIssue(ServerTextMsg textMsg) {

        return 60;
    }

   /* private int handleServerTextMsgIssue() {

    }*/

    private int handleServerCommonRespMsg(ServerCommonRespMsgBody serverCommonRespMsgBody){
        int s = 0;
        switch (serverCommonRespMsgBody.getReplyId()) {

            //平台通用应答

            //鉴权
            case TPMSConsts.msg_id_terminal_authentication:
                s = handleTerminalAuthenticationResponse(serverCommonRespMsgBody);
                break;

            //心跳
            case TPMSConsts.msg_id_terminal_heart_beat:
                s = handleTerminalHeatBeatResponse(serverCommonRespMsgBody);
                break;

            //位置信息上传
            case TPMSConsts.msg_id_terminal_location_info_upload:
                s = handleLocationUploadResponse(serverCommonRespMsgBody);
                break;

            //终端注销
            case TPMSConsts.msg_id_terminal_log_out:
                break;

            case TPMSConsts.cmd_terminal_sign_in_resp:
                s = handleTerminalSignInResponse(serverCommonRespMsgBody);
                break;


            default:
                break;
        }
        return s;

    }

    private int handleTerminalSignInResponse(ServerCommonRespMsgBody serverCommonRespMsgBody) {
        int a = 0;
        int s = serverCommonRespMsgBody.getReplyId();
        switch (s) {
            case 0:
                Log.e(TAG,"考勤登入成功");
                a = 50;
                break;
            case 1:
                Log.e(TAG,"考勤登入失败");
                a = 51;
                break;
            default:
                break;
        }
        return a;
    }

    private int handleTerminalRegisterResponse(TerminalRegisterMsgRespBody terminalRegisterMsgRespBody) {
        int a = 0;
        int s = terminalRegisterMsgRespBody.getReplyToken();
        switch (s) {
            case 0:
                Log.e(TAG,"注册成功");
                a = 10;
                break;
            case 1:
                Log.e(TAG,"车辆已被注册");
                a = 11;
                break;
            case 2:
                Log.e(TAG,"数据库中无该车辆");
                break;
            case 3:
                Log.e(TAG,"终端已被注册");
                break;
            case 4:
                Log.e(TAG,"数据库中无该终端");
                break;
            default:
                break;
        }
        return a;
    }
    private int handleTerminalAuthenticationResponse(ServerCommonRespMsgBody serverCommonRespMsgBody){
        int s = serverCommonRespMsgBody.getReplyCode();
        switch (s){
            case 0:
                Log.e(TAG,"鉴权成功");
                return 20;
            case 1:
                Log.e(TAG,"鉴权失败");
                break;
            case 2:
                Log.e(TAG,"消息有误");
                break;
            case 3:
                Log.e(TAG,"不支持");
                break;
            default:
                break;
        }
        return 21;

    }
  /*  private int handleTerminalRegisterResponse(ServerCommonRespMsgBody serverCommonRespMsgBody) {

        switch (serverCommonRespMsgBody.getReplyCode()){
            case 0:
                Log.e(TAG,"注册成功");
                return 10;
            case 1:
                Log.e(TAG,"车辆已被注册");
                break;
            case 2:
                Log.e(TAG,"数据库中无该车辆");
                break;
            case 3:
                Log.e(TAG,"终端已被注册");
                break;
            case 4:
                Log.e(TAG,"数据库中无该终端");
                break;
            default:
                break;
        }
        return 11;
    }*/
    private int handleTerminalHeatBeatResponse(ServerCommonRespMsgBody serverCommonRespMsgBody) {

        switch (serverCommonRespMsgBody.getReplyCode()){
            case 0:
                Log.e(TAG,"正常心跳");
                return 30;
            case 1:
                Log.e(TAG,"心跳应答失败");
                break;
            default:
                break;
        }
        return 31;
    }

    private int handleLocationUploadResponse(ServerCommonRespMsgBody serverCommonRespMsgBody) {
        switch (serverCommonRespMsgBody.getReplyCode()) {
            case 0:
                Log.e(TAG,"位置信息汇报成功");
                return 40;
            case 1:
                Log.e(TAG,"位置信息汇报失败");
                break;
            default:
                break;
        }
        return 41;
    }


}
