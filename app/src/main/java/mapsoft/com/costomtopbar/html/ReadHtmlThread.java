package mapsoft.com.costomtopbar.html;

import android.content.Context;
import android.os.Message;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Handler;

import mapsoft.com.costomtopbar.activity.MainActivity;
import mapsoft.com.costomtopbar.module.Path;
import mapsoft.com.costomtopbar.module.Station;
import mapsoft.com.costomtopbar.report.GeofenceService;


/**
 * @author djl
 * @function
 */

public class ReadHtmlThread extends Thread {

    private Context mContext;
    private String pathNum;
    private Station mStation;
    private Path mPath;
    private ArrayList<Station> mStations;
    private String direction;
    private Element elements;

    private MainActivity.MyHandler mHandler;


    public ReadHtmlThread(MainActivity.MyHandler handler, String pathNum, String direction) {
        mHandler = handler;
        this.pathNum = pathNum;
        mStation = new Station();
        mPath = new Path();
        mStations = new ArrayList<>();
        this.direction = direction;
    }

    @Override
    public void run() {
        super.run();
        try {
            //从一个URL加载一个Document对象。
            File input = new File("/sdcard/path/src/"+pathNum+"src.html");
            Document doc = Jsoup.parse(input, "UTF-8", "http://www.example.com/");
            //选择“美食天下”所在节点
            if (direction == "up"){
                elements  = doc.select("div").get(0);
            } else {
                elements = doc.select("div").get(1);
            }
            //打印 <a>标签里面的title
            Elements a = elements.select("table").select("tr");
            mStations.clear();
            mPath.setPathNum(pathNum);
            mPath.setDirection(direction);

            for (int i = 3; i < a.size(); i++) {
                // 获取一个tr
                Element tr = a.get(i);
                // 获取该行的所有td节点
                Elements tds = tr.select("td");
                // 选择某一个td节点
                mStation = new Station();
                for (int j = 0; j < tds.size(); j++) {
                    String td = tds.get(j).text();
                    if (j==0){
                        mStation.setOrder(Integer.valueOf(td));
                    }
                    if (j==1){
                        mStation.setNum(Integer.valueOf(td));
                    }
                    if (j==2){
                        mStation.setType(Integer.valueOf(td));
                    }
                    if (j==3){
                        mStation.setName(td);
                    }
                    if (j==4){
                        mStation.setLatitude(Double.parseDouble(td));
                    }
                    if (j==5){
                        mStation.setLongitude(Double.parseDouble(td));
                    }
                    if (j==6){
                        mStation.setInRadius(Integer.valueOf(td));
                        continue;
                    }
                }

                mStations.add(mStation);
                Log.e("Station :",  mStation.toString());
            }
            mPath.setStations(mStations);
            Message msg=new Message();
            msg.what=0x001;
            msg.obj=mPath;//把一个自定义的类传出去
            mHandler.sendMessage(msg);
            Log.e("Path :",  mPath.toString());
        }catch (Exception e) {
            Log.e("mytag", e.toString());
        }
    }
}
