package mapsoft.com.costomtopbar.jt808;

/**
 * @author djl
 * @function
 */

public class DriverLoginMsg {

    private LocationInfoUploadMsg mLocationInfoUploadMsg;
    private String companyId;
    private String driverId;
    private String platNum;
    private String pwd;

    public LocationInfoUploadMsg getLocationInfoUploadMsg() {
        return mLocationInfoUploadMsg;
    }

    public void setLocationInfoUploadMsg(LocationInfoUploadMsg locationInfoUploadMsg) {
        mLocationInfoUploadMsg = locationInfoUploadMsg;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getPlatNum() {
        return platNum;
    }

    public void setPlatNum(String platNum) {
        this.platNum = platNum;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
