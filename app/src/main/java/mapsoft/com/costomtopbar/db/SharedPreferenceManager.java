package mapsoft.com.costomtopbar.db;

import android.content.Context;
import android.content.SharedPreferences;

import mapsoft.com.costomtopbar.application.MapsoftApplication;



/**
 * @author djl
 * @function
 */

public class SharedPreferenceManager {

    //当前类实例
    private static SharedPreferenceManager mInstance;

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    private static final String SHARE_PREFERENCE_NAME = "mapsoft.pre";  //文件名

    public static final String IP_SETTING = "ip";    //IP设置
    public static final String PORT_SETTING = "port";   //端口设置
    public static final String ID_SETTING = "id";

    public static SharedPreferenceManager getInstance() {
        if ( mInstance == null) {
            mInstance = new SharedPreferenceManager();
        }
        return mInstance;
    }

    private SharedPreferenceManager() {

        sp = MapsoftApplication.getInstance().getSharedPreferences(SHARE_PREFERENCE_NAME,
                Context.MODE_PRIVATE);
        editor = sp.edit();

    }

    //int类型写入
    public void putString(String key , String value) {
        editor.putString(key, value);
        editor.commit();
    }

    //int类型读取
    public String getString(String key , String defaultValue) {

        return sp.getString(key, defaultValue);
    }
}
