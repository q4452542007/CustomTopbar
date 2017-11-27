package mapsoft.com.costomtopbar.html;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;

/**
 * @author djl
 * @function
 */

public class ReadHtmlThread extends Thread {
    @Override
    public void run() {
        super.run();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //从一个URL加载一个Document对象。
                    File input = new File("/sdcard/22.html");
                    Document doc = Jsoup.parse(input, "UTF-8", "http://www.example.com/");
                    //选择“美食天下”所在节点
                    Elements elements = doc.select("table");
                    //打印 <a>标签里面的title
                    String a = elements.select("tr").get(3).select("td").get(1).text();
                    String b = elements.select("tr").get(3).select("td").get(3).text();
                    String c = elements.select("tr").get(3).select("td").get(4).text();
                    String d = elements.select("tr").get(3).select("td").get(5).text();
                    String e = elements.select("tr").get(3).select("td").get(6).text();
                    Log.e("站点序号", elements.select("li").select("a").attr("title"));
                    Log.e("站点名", elements.select("li").get(1).text());
                    Log.e("限速", elements.select("li").get(1).select("a").text());
                    Log.e("纬度", elements.select("li").get(1).select("i").text());
                    Log.e("经度", elements.select("li").get(1).select("j").text());

                } catch (Exception e) {
                    Log.e("mytag", e.toString());
                }
            }
        });
    }
}