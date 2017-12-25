package mapsoft.com.costomtopbar.module;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/6.
 */

public class Path {

    private String pathNum;

    private String direction;

    private ArrayList<Station> stations;

    public String getPathNum() {
        return pathNum;
    }

    public void setPathNum(String pathNum) {
        this.pathNum = pathNum;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public ArrayList<Station> getStations() {
        return stations;
    }

    public void setStations(ArrayList<Station> stations) {
        this.stations = stations;
    }
}

