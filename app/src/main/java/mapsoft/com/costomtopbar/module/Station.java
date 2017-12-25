package mapsoft.com.costomtopbar.module;

/**
 * Created by Administrator on 2017/9/6.
 */

public class Station {

    //站点名
    private String mName;
    //站点编号
    private int num;
    // 纬度
    private double latitude;
    //经度
    private double longitude;
    //进站半径
    private int inRadius;
    //站点序号
    private int order;
    //站点类型
    private int type;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getInRadius() {
        return inRadius;
    }

    public void setInRadius(int inRadius) {
        this.inRadius = inRadius;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
