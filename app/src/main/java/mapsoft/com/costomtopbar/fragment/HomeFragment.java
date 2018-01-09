package mapsoft.com.costomtopbar.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.ImageFormat;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.hardware.SerialManager;
import android.hardware.SerialPort;
import android.media.MediaRecorder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.secondbook.com.buttonfragment.R;
import android.support.annotation.DrawableRes;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.AMapLocationQualityReport;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import mapsoft.com.costomtopbar.IBackService;
import mapsoft.com.costomtopbar.activity.MainActivity;
import mapsoft.com.costomtopbar.constant.Constant;
import mapsoft.com.costomtopbar.jt808.LocationInfoUploadMsg;
import mapsoft.com.costomtopbar.map2312.InBusPlateMsg;
import mapsoft.com.costomtopbar.module.Path;
import mapsoft.com.costomtopbar.module.Station;
import mapsoft.com.costomtopbar.serial.MySerialManager;
import mapsoft.com.costomtopbar.util.BCD8421Operater;
import mapsoft.com.costomtopbar.view.IconCut;
import mapsoft.com.costomtopbar.view.Topbar;



/**
 * Created by WangChang on 2016/5/15.
 */
public class HomeFragment extends BaseFragment {

    public static final String TAG = "HomeFragment";
    private TextView tv_time;
    private static final int msgKey1 = 1;

    private IntentFilter mIntentFilter;
    private NetworkChangeReceiver mNetworkChangeReceiver;

    private ImageView wifiImage,gpsImage;

    private static Context context = null;

    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private Button okButton, photoButton, videoButton, browseButton;
    private Spinner systemSpinner, channelSpinner;

    private boolean isOnPause = false;
    // 功能相关
    private MediaRecorder mediaRecorder;
    private String videoFile = null;
    private boolean isRecord = false; // 判断是否录像

    private String dateString = null; // 保存文件
    public static String extsd_path = null;
    private int height = 0; // 制式通道的改变
    private int width = 0;

    private Camera camera = null;
    private Camera.Parameters param = null;
    private boolean previewRunning = false;
    private boolean isOpen = false;

    private TextView LocationResult,speedResult,pathView,directionView,speedView,currentStation,nextStation;

    private float mSpeed;
    private String mTime;
    private StringBuffer stringBuilder;
    int   scale  =   2;//设置位数
    int   roundingMode  =  4;//表示四舍五入，可以选择其他舍值方式，例如去尾，等等.
    private AppCompatActivity activity;
    private MainActivity mMainActivity;
    private Topbar mTopbar;

    private Intent mServiceIntent;
    private IBackService iBackService;

    private BCD8421Operater mBCD8421Operater;
    private FragmentInteraction listterner;

    private static LocationInfoUploadMsg mLocationInfoUploadMsg;

    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;

    private String time;

    private InBusPlateMsg mInBusPlateMsg;

    private MySerialManager mMySerialManager;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocationInfoUploadMsg = new LocationInfoUploadMsg();
        mBCD8421Operater = new BCD8421Operater();

        mMySerialManager = MySerialManager.getInstance(getActivity());
        mInBusPlateMsg = new InBusPlateMsg();
    }

    @Override
    public void onStart() {
        super.onStart();
        initLocation();
        startLocation();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homepage, container, false);

        if (getArguments() != null) {
            String mParam1 = getArguments().getString("param");
            TextView busPath =  (TextView)view.findViewById(R.id.bus_path);
            busPath.setText(mParam1);
        }
        activity = (AppCompatActivity) getActivity();
        mMainActivity = (MainActivity) getActivity();
        mTopbar = (Topbar) activity.findViewById(R.id.topbar);
        mTopbar.setWifiButton(IconCut.getInstance(context).cutWifi(false));
        wifiImage = (ImageView) view.findViewById(R.id.wifiView);
        gpsImage = (ImageView) view.findViewById(R.id.gpsView);
        tv_time = (TextView) view.findViewById(R.id.mytime);
        speedResult = (TextView) view.findViewById(R.id.bus_speed);
        LocationResult = (TextView) view.findViewById(R.id.latitude);
        LocationResult.setMovementMethod(ScrollingMovementMethod.getInstance());
        pathView = (TextView) view.findViewById(R.id.bus_path);
        directionView = (TextView) view.findViewById(R.id.direction_view);
        currentStation = (TextView) view.findViewById(R.id.current_station);
        nextStation = (TextView) view.findViewById(R.id.next_station);
        //wifiImage.setImageResource(R.drawable.nowifi1);
        //gpsImage.setImageResource(R.drawable.nogps);

        // 初始化surfaceView
        /* surfaceView = (SurfaceView) view.findViewById(R.id.surface);
         surfaceHolder = surfaceView.getHolder();
         surfaceHolder.addCallback(new MySurfaceViewCallback());
         surfaceHolder.setType(surfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);*/

        //获取网络状态
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        mNetworkChangeReceiver = new NetworkChangeReceiver();
        getActivity().registerReceiver(mNetworkChangeReceiver, mIntentFilter);

        return view;
    }


    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public static HomeFragment newInstance(String content) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString("param", content);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * 网络状态广播
     */
    class NetworkChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isAvailable()) {
                /*BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 5;//图片宽高都为原来的二分之一，即图片为原来的四分之一
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.wifi, options);
                wifiImage.setImageBitmap(bitmap);*/
                //wifiImage.setImageResource(R.drawable.wifi);

                //mTopbar.setWifiButton(IconCut.getInstance(context).cutWifi(true));
                mTopbar.setWifiButton(R.drawable.signalbar);
            } else {
                //wifiImage.setImageResource(R.drawable.nowifi);
                //mTopbar.setWifiButton(IconCut.getInstance(context).cutWifi(false));
                mTopbar.setWifiButton(R.drawable.nosignalbar);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        CloseCamera();
        getActivity().unregisterReceiver(mNetworkChangeReceiver);
        Log.d(TAG,"destroy");
    }

    @Override
    public void onStop() {
        Log.d(TAG,"stop");
        super.onStop();
        destroyLocation();
    }

    private class MySurfaceViewCallback implements SurfaceHolder.Callback {

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width,
                int height) {
            // TODO Auto-generated method stub
            System.out.println("------surfaceChanged------");
            surfaceHolder = holder;
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            // TODO Auto-generated method stub
            System.out.println("------surfaceCreated------");
            surfaceHolder = holder;

            ok_choice();
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            // TODO Auto-generated method stub
            System.out.println("------surfaceDestroyed------");
            surfaceView = null;
            surfaceHolder = null;
        }
    }

    private void ok_choice() {
        // TODO Auto-generated method stub
        System.out.println("------ok button down------");
        height = 290;
        width = 503;

        if (hasPermission(Constant.HARDWEAR_CAMERA_PERMISSION)) {
            InitCamera();
        } else {
            requestPermission(Constant.HARDWEAR_CAMERA_CODE, Constant.HARDWEAR_CAMERA_PERMISSION);
        }

    }

    // 初始化camera
    private void InitCamera() {
        System.out.println("------InitCamera------");

        if (!isOpen) {
            camera = Camera.open(); // 取得第一个摄像头
            param = camera.getParameters();// 获取param
            param.setPreviewSize(width, height);// 设置预览大小
            param.setPreviewFpsRange(4, 10);// 预览照片时每秒显示多少帧的范围张
            param.setPictureFormat(ImageFormat.JPEG);// 图片形式
            param.set("jpeg-quality", 95);
            param.setPictureSize(1600, 900);
            camera.setParameters(param);
            try {
                camera.setPreviewDisplay(surfaceHolder);// 设置预览显示
            } catch (IOException e) {
            }
            // 进行预览
            if (!previewRunning) {
                camera.startPreview(); // 进行预览
                previewRunning = true; // 已经开始预览
            }

            isOpen = true;
        }
    }

    // 关闭摄像头
    private void CloseCamera() {
        if (camera != null) {
            System.out.println("------CloseCamera------");
            if (previewRunning) {
                camera.stopPreview(); // 停止预览
                previewRunning = false;
            }
            camera.release();
            camera = null;
            isOpen = false;
        }
    }

    public static String formatTimeEight(String time) throws Exception {
        Date d = null;
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        d = sd.parse(time);
        long rightTime = (long) (d.getTime() + 8 * 60 * 60 * 1000); //把当前得到的时间用date.getTime()的方法写成时间戳的形式，再加上8小时对应的毫秒数
        String newtime = sd.format(rightTime);//把得到的新的时间戳再次格式化成时间的格式
        return newtime;
    }

    public static Date stringToDate(String time) {
        Date d = null;
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            d = sd.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d;
    }

    /**
     * 定义了所有activity必须实现的接口
     */
    public interface FragmentInteraction
    {
        /**
         * Fragment 向Activity传递指令，这个方法可以根据需求来定义
         * @param locationInfoUploadMsg
         */
        void process(LocationInfoUploadMsg locationInfoUploadMsg);


    }

    /**
     * 当FRagmen被加载到activity的时候会被回调
     * @param activity
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if(activity instanceof FragmentInteraction)
        {
            listterner = (FragmentInteraction)activity;
        }
        else{
            throw new IllegalArgumentException("activity must implements FragmentInteraction");
        }

    }


    @Override
    public void onDetach() {
        super.onDetach();

        listterner = null;
    }

    /**
     * 初始化定位
     *
     * @since 2.8.0
     * @author hongming.wang
     *
     */
    private void initLocation(){
        //初始化client
        locationClient = new AMapLocationClient(getActivity().getApplicationContext());
        locationOption = getDefaultOption();
        //设置定位参数
        locationClient.setLocationOption(locationOption);
        // 设置定位监听
        locationClient.setLocationListener(locationListener);
    }

    /**
     * 默认的定位参数
     * @since 2.8.0
     * @author hongming.wang
     *
     */
    private AMapLocationClientOption getDefaultOption(){
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(true);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(10000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(1000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(true);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(false); //可选，设置是否使用缓存定位，默认为true
        return mOption;
    }

    /**
     * 定位监听
     */
    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation location) {
            if (null != location) {

                StringBuffer sb = new StringBuffer();
                //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
                if(location.getErrorCode() == 0){
                    //mTopbar.setGpsButton(IconCut.getInstance(context).cutGps(true));
                    mTopbar.setGpsButton(R.drawable.gpsbar);
                    sb.append("经    度    : " + location.getLongitude() + "\n");
                    sb.append("纬    度    : " + location.getLatitude() + "\n");
                    sb.append("精    度    : " + location.getAccuracy() + "米" + "\n");
                    mLocationInfoUploadMsg.setLatitude(location.getLatitude());
                    mLocationInfoUploadMsg.setLongitude(location.getLongitude());
                    mLocationInfoUploadMsg.setElevation((int) location.getAltitude());
                    mLocationInfoUploadMsg.setSpeed(location.getSpeed());
                    mLocationInfoUploadMsg.setDirection((int) location.getBearing());
                    mLocationInfoUploadMsg.setTime(AMapUtils.formatUTC(location.getTime(),"yyyy-MM-dd HH:mm:ss"));
                    listterner.process(mLocationInfoUploadMsg);
                    time = AMapUtils.formatUTC(location.getTime(),"yyyy-MM-dd HH:mm");

                } else {
                    //定位失败
                    //mTopbar.setGpsButton(IconCut.getInstance(context).cutGps(false));
                    mTopbar.setGpsButton(R.drawable.nogpsbar);
                    sb.append("定位失败" + "\n");
                    sb.append("错误码:" + location.getErrorCode() + "\n");
                    sb.append("错误信息:" + location.getErrorInfo() + "\n");
                    sb.append("错误描述:" + location.getLocationDetail() + "\n");
                }
                //解析定位结果，
                String result = sb.toString();
                LocationResult.setText(result);
                speedResult.setText(location.getSpeed()+"");
                mTopbar.updateTime(time);
            } else {
                LocationResult.setText("定位失败，loc is null");
                //mTopbar.setGpsButton(IconCut.getInstance(context).cutGps(false));
                mTopbar.setGpsButton(R.drawable.nogpsbar);
            }
        }
    };


    /**
     * 获取GPS状态的字符串
     * @param statusCode GPS状态码
     * @return
     */
    private String getGPSStatusString(int statusCode){
        String str = "";
        switch (statusCode){
            case AMapLocationQualityReport.GPS_STATUS_OK:
            str = "GPS状态正常";
            break;
            case AMapLocationQualityReport.GPS_STATUS_NOGPSPROVIDER:
            str = "手机中没有GPS Provider，无法进行GPS定位";
            break;
            case AMapLocationQualityReport.GPS_STATUS_OFF:
            str = "GPS关闭，建议开启GPS，提高定位质量";
            break;
            case AMapLocationQualityReport.GPS_STATUS_MODE_SAVING:
            str = "选择的定位模式中不包含GPS定位，建议选择包含GPS定位的模式，提高定位质量";
            break;
            case AMapLocationQualityReport.GPS_STATUS_NOGPSPERMISSION:
            str = "没有GPS定位权限，建议开启gps定位权限";
            break;
        }
        return str;
    }
    /*// 根据控件的选择，重新设置定位参数
    private void resetOption() {
        // 设置是否需要显示地址信息
        locationOption.setNeedAddress(cbAddress.isChecked());
        *//**
     * 设置是否优先返回GPS定位结果，如果30秒内GPS没有返回定位结果则进行网络定位
     * 注意：只有在高精度模式下的单次定位有效，其他方式无效
     *//*
        locationOption.setGpsFirst(cbGpsFirst.isChecked());
        // 设置是否开启缓存
        locationOption.setLocationCacheEnable(cbCacheAble.isChecked());
        // 设置是否单次定位
        locationOption.setOnceLocation(cbOnceLocation.isChecked());
        //设置是否等待设备wifi刷新，如果设置为true,会自动变为单次定位，持续定位时不要使用
        locationOption.setOnceLocationLatest(cbOnceLastest.isChecked());
        //设置是否使用传感器
        locationOption.setSensorEnable(cbSensorAble.isChecked());
        //设置是否开启wifi扫描，如果设置为false时同时会停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        String strInterval = etInterval.getText().toString();
        if (!TextUtils.isEmpty(strInterval)) {
            try{
                // 设置发送定位请求的时间间隔,最小值为1000，如果小于1000，按照1000算
                locationOption.setInterval(Long.valueOf(strInterval));
            }catch(Throwable e){
                e.printStackTrace();
            }
        }

        String strTimeout = etHttpTimeout.getText().toString();
        if(!TextUtils.isEmpty(strTimeout)){
            try{
                // 设置网络请求超时时间
                locationOption.setHttpTimeOut(Long.valueOf(strTimeout));
            }catch(Throwable e){
                e.printStackTrace();
            }
        }
    }*/

    /**
     * 开始定位
     *
     * @since 2.8.0
     * @author hongming.wang
     *
     */
    private void startLocation(){
        //根据控件的选择，重新设置定位参数
        //resetOption();
        // 设置定位参数
        locationClient.setLocationOption(locationOption);
        // 启动定位
        locationClient.startLocation();
    }

    /**
     * 停止定位
     *
     * @since 2.8.0
     * @author hongming.wang
     *
     */
    private void stopLocation(){
        // 停止定位
        locationClient.stopLocation();
    }

    /**
     * 销毁定位
     *
     * @since 2.8.0
     * @author hongming.wang
     *
     */
    private void destroyLocation(){
        if (null != locationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            locationClient.onDestroy();
            locationClient = null;
            locationOption = null;
        }
    }
    private static SimpleDateFormat sdf = null;
    public  static String formatUTC(long l, String strPattern) {
        if (TextUtils.isEmpty(strPattern)) {
            strPattern = "yyyy-MM-dd HH:mm:ss";
        }
        if (sdf == null) {
            try {
                sdf = new SimpleDateFormat(strPattern, Locale.CHINA);
            } catch (Throwable e) {
            }
        } else {
            sdf.applyPattern(strPattern);
        }
        return sdf == null ? "NULL" : sdf.format(l);
    }

    public static LocationInfoUploadMsg getLocationMsg() {
        return mLocationInfoUploadMsg;
    }

    public void changePath(Path path) {
        pathView.setText(path.getPathNum());
        directionView.setText(path.getDirection().equals("up")?"上行":"下行");
        /*mMySerialManager.openPathPlatePort();
        try {
            mMySerialManager.sendPathPlateMsg(Integer.valueOf(path.getPathNum()),
                    (path.getStations().get(0).getName()+" → "
                            + path.getStations().get(path.getStations().size()-1).getName()+"\0").getBytes("GB2312"));
            mMySerialManager.sendLcdChangePathMsg("342".getBytes("GB2312"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }*/
    }

    public String changeStation(Path path,String order) {
        ArrayList<Station> stations = path.getStations();
        String msg = null;
        if (order.equals("-1")) {
            currentStation.setText(stations.get(0).getName());
            nextStation.setText(stations.get(1).getName());
            /*mInBusPlateMsg.setDirection_flag(path.getDirection().equals("up")?0:1);
            mInBusPlateMsg.setIn_out_flag(0);
            mInBusPlateMsg.setCustom_flag(1);
            mMySerialManager.sendLcdStationMsg(mInBusPlateMsg);*/
            return "-1";
        }
        if (order.equals(Constant.IN_STATION)) {
                for (int i = 0; i < stations.size()-2; i++) {
                if (currentStation.getText().equals(stations.get(i).getName())) {
                    msg = stations.get(i).getName();
                    currentStation.setText(stations.get(i+1).getName());
                    nextStation.setText(stations.get(i+2).getName());
                    /*mInBusPlateMsg.setDirection_flag(path.getDirection().equals("up")?0:1);
                    mInBusPlateMsg.setIn_out_flag(0);
                    mInBusPlateMsg.setCustom_flag(i+1);
                    mMySerialManager.sendLcdStationMsg(mInBusPlateMsg);*/
                    return msg;
                }
            }
        }
        if (order.equals(Constant.OUT_STATION)) {
            for (int i = 0; i < stations.size()-2; i++) {
                if (currentStation.getText().equals(stations.get(i).getName())) {
                    msg = stations.get(i+1).getName();
                    /*mInBusPlateMsg.setDirection_flag(path.getDirection().equals("up")?0:1);
                    mInBusPlateMsg.setIn_out_flag(1);
                    mInBusPlateMsg.setCustom_flag(i+1);
                    mMySerialManager.sendLcdStationMsg(mInBusPlateMsg);*/
                    return msg;
                }
            }
        }
        /*if (order.equals(Constant.NEXT_STATION)) {
            msg = currentStation.getText()+"";
            return Constant.IN_STATION;
        }*/


        for (int i = 0; i < stations.size()-2; i++){
            if (order.equals(String.valueOf(stations.get(i).getOrder()))) {
                currentStation.setText(stations.get(i).getName());
                nextStation.setText(stations.get(i+1).getName());
            }
        }
        return msg;


    }

}
