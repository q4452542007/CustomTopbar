package mapsoft.com.costomtopbar.module;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/6.
 */

public class Path implements Serializable{
    private String num=null;
    private Station mStation;
    private List<Station> upStations;
    private List<Station> downStations;
    public Path() {
        upStations = new ArrayList<>();
        downStations = new ArrayList<>();
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public Station getStation(String name) {
        return new Station(name);
    }


    public void addUpStation(Station s) {
        upStations.add(s);
    }

    public void addDownStation(Station s) {
        downStations.add(s);
    }

    public List<Station> getUpStations() {
        return upStations;
    }

    public List<Station> getDownStations() {
        return downStations;
    }

}

