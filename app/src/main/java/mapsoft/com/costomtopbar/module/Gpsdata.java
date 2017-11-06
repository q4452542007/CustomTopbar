package mapsoft.com.costomtopbar.module;

public class Gpsdata {
	public int InfoType; // 数据类型
	public int Latitude; // 纬度
	public int Longitude; // 经度
	public double High; // 海拔
	public double Direct; // 方向
	public double Speed; // 速度
	public String GpsTime; // GPS时间
	float signalintensity;// 信号强度
	int statenumber;// 编号

	public Gpsdata() {

	}

	public Gpsdata(float signalintensity, int statenumber) {
		super();
		this.signalintensity = signalintensity;
		this.statenumber = statenumber;
	}

	public float getSignalintensity() {
		return signalintensity;
	}

	public void setSignalintensity(float signalintensity) {
		this.signalintensity = signalintensity;
	}

	public int getStatenumber() {
		return statenumber;
	}

	public void setStatenumber(int statenumber) {
		this.statenumber = statenumber;
	}

}
