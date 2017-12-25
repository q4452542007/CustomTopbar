package mapsoft.com.costomtopbar.application;

import android.app.Application;
import android.app.Service;
import android.os.Vibrator;
import android.secondbook.com.buttonfragment.R;

import com.amap.api.location.AMapLocationClient;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

import mapsoft.com.costomtopbar.activity.MainActivity;
import mapsoft.com.costomtopbar.report.TTsService;


/**
 * Created by Administrator on 2017/10/10.
 *
 */

public class MapsoftApplication extends Application{
    private static MapsoftApplication mApplication = null;
    public Vibrator mVibrator;
    private static AMapLocationClient locationClient = null;
    private static TTsService mTTsService = null;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        //initShareSDK();
        /***
         * 初始化定位sdk，建议在Application中创建
         */
        locationClient = new AMapLocationClient(MapsoftApplication.this);
        StringBuffer param = new StringBuffer();
        param.append("appid="+getString(R.string.app_id));
        param.append(",");
        // 设置使用v5+
        param.append(SpeechConstant.ENGINE_MODE+"="+SpeechConstant.MODE_MSC);
        SpeechUtility.createUtility(MapsoftApplication.this, param.toString());

    }
    public static AMapLocationClient getAMapInstance() {
        if (locationClient == null)
        {
            locationClient = new AMapLocationClient(mApplication);
        }
        return  locationClient;
    }

    public static MapsoftApplication getInstance() {

        return mApplication;
    }

    public static TTsService getTTsInstance() {
        return mTTsService;
    }

    /*public void initShareSDK() {
        ShareManager.init(this);
    }*/
}
