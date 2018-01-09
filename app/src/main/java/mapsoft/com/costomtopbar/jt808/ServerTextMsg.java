package mapsoft.com.costomtopbar.jt808;

/**
 * @author djl
 * @function
 */
public class ServerTextMsg {
    private String text;
    private String flag;
    // 终端手机号
    protected String terminalPhone;
    private int replyFlowId;
    private byte[] msgBodyBytes;
    private int checkSum;
    public ServerTextMsg() {
    }

   /* public ServerTextMsg(PackageData packageData) {
        this();
        //this.channel = packageData.getChannel();
        this.checkSum = packageData.getCheckSum();
        this.msgBodyBytes = packageData.getMsgBodyBytes();
        this.msgHeader = packageData.getMsgHeader();
    }*/

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
    public int getReplyFlowId() {
        return replyFlowId;
    }

    public void setReplyFlowId(int replyFlowId) {
        this.replyFlowId = replyFlowId;
    }

    public String getTerminalPhone() {
        return terminalPhone;
    }

    public void setTerminalPhone(String terminalPhone) {
        this.terminalPhone = terminalPhone;
    }
}