package mapsoft.com.costomtopbar.application;

import android.app.Application;
import android.app.Service;
import android.os.Vibrator;


/**
 * Created by Administrator on 2017/10/10.
 *
 */

public class MapsoftApplication extends Application{
    private static MapsoftApplication mApplication = null;
    public Vibrator mVibrator;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        //initShareSDK();
        /***
         * 初始化定位sdk，建议在Application中创建
         */
        mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
    }

    public static MapsoftApplication getInstance() {
        return mApplication;
    }

    /*public void initShareSDK() {
        ShareManager.init(this);
    }*/
}
