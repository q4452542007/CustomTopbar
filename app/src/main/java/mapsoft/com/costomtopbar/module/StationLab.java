package mapsoft.com.costomtopbar.module;



import java.util.ArrayList;
import java.util.List;

public class StationLab {
    private static StationLab sStationLab;

    private ArrayList<Path> mPaths;

    public static StationLab get() {
        if (sStationLab == null) {
            sStationLab = new StationLab();
        }
        return sStationLab;
    }

    private StationLab() {
        mPaths = new ArrayList<>();
    }

    public void addPath(Path s) {
        mPaths.add(s);
    }

    public List<Path> getPaths() {
        return mPaths;
    }

    /*public Path getPath(String pathNum) {
        for (Path path : mPaths) {
            if (path.getNum().equals(pathNum)) {
                return path;
            }
        }
        return new Path();
    }*/
}

