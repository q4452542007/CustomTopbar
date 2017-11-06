package mapsoft.com.costomtopbar.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import android.secondbook.com.buttonfragment.R;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import mapsoft.com.costomtopbar.baidu.LocationApplication;
import mapsoft.com.costomtopbar.baidu.service.LocationService;
import mapsoft.com.costomtopbar.constant.Constant;
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

    private TextView LocationResult,speedResult;
    private LocationService locationService;
    private float mSpeed;
    private String mTime;
    private StringBuffer stringBuilder;
    int   scale  =   2;//设置位数
    int   roundingMode  =  4;//表示四舍五入，可以选择其他舍值方式，例如去尾，等等.

    private Topbar mTopbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
    }

    @Override
    public void onStart() {
        super.onStart();
        locationService = ((LocationApplication) getActivity().getApplication()).locationService;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(mListener);
        //注册监听
        int type = getActivity().getIntent().getIntExtra("from", 0);
        if (type == 0) {
            locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        } else if (type == 1) {
            locationService.setLocationOption(locationService.getOption());
        }
            locationService.start();// 定位SDK
            // start之后会默认发起一次定位请求，开发者无须判断isstart并主动调用request
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homepage, container, false);

        if (getArguments() != null) {
            String mParam1 = getArguments().getString("param");
            TextView busPath =  (TextView)view.findViewById(R.id.bus_path);
            busPath.setText(mParam1);
        }
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        mTopbar = activity.findViewById(R.id.topbar);

        wifiImage = (ImageView) view.findViewById(R.id.wifiView);
        gpsImage = (ImageView) view.findViewById(R.id.gpsView);
        tv_time = (TextView) view.findViewById(R.id.mytime);
        speedResult = (TextView) view.findViewById(R.id.bus_speed);
        LocationResult = (TextView) view.findViewById(R.id.latitude);
        LocationResult.setMovementMethod(ScrollingMovementMethod.getInstance());
        //wifiImage.setImageResource(R.drawable.nowifi1);
        //gpsImage.setImageResource(R.drawable.nogps);




        // 初始化surfaceView
        /*surfaceView = (SurfaceView) view.findViewById(R.id.surface);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(new MySurfaceViewCallback());
        surfaceHolder.setType(surfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);*/

        //获取网络状态
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        mNetworkChangeReceiver = new NetworkChangeReceiver();
        getActivity().registerReceiver(mNetworkChangeReceiver, mIntentFilter);
        /*new TimeThread().start();*/

/*        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String [] permission = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(getActivity(), permission, 1);
        } else {
            requestLocation();
        }*/

        return view;
    }
    /*private void requestLocation() {
        initLocation();
        mLocationClient.start();
    }
    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setScanSpan(1000);
        option.setLocationMode(LocationClientOption.LocationMode.Device_Sensors);
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
    }*/

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




    /*public class TimeThread extends  Thread{
        @Override
        public void run() {
            super.run();
            do{
                try {
                    Message msg = new Message();
                    msg.what = msgKey1;
                    mHandler.sendMessage(msg);
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }while (true);
        }
    }*/
/*    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case msgKey1:
                    long time = System.currentTimeMillis();
                    Date date = new Date(time);
                    SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒 EEE");
                    tv_time.setText(format.format(date));
                    break;
                default:
                    break;
            }
        }
    };*/

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
                mTopbar.setWifiButton(true);
            } else {
                //wifiImage.setImageResource(R.drawable.nowifi);
                mTopbar.setWifiButton(false);
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
        locationService.unregisterListener(mListener); //注销掉监听
        locationService.stop(); //停止定位服务
        Log.d(TAG,"stop");
        super.onStop();
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
/*    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions,  int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(getActivity(), "必须同意所有权限才能使用本程序", Toast.LENGTH_SHORT).show();
                            getActivity().finish();
                            return;
                        }
                    }
                    requestLocation();
                } else {
                    Toast.makeText(getActivity(), "发生位置错误", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
                break;
            default:
        }
    }*/

    /**
     * 显示请求字符串
     *
     * @param str
     */
    public void logMsg(String str, String time, float speed) {
        final String s = str;


        /*try {
            if (LocationResult != null){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        LocationResult.post(new Runnable() {
                            @Override
                            public void run() {
                                LocationResult.setText(s);
                            }
                        });

                    }
                }).start();
            }
            //LocationResult.setText(str);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }
    /*****
     *
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     *
     */
    private BDAbstractLocationListener mListener = new BDAbstractLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // TODO Auto-generated method stub
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                StringBuffer sb = new StringBuffer(256);
                /**
                 * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
                 * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
                 */
                mTime = location.getTime();
                mSpeed = location.getSpeed();
                sb.append("latitude : ");// 纬度
                sb.append(location.getLatitude());
                sb.append("\nlontitude : ");// 经度
                sb.append(location.getLongitude());
                sb.append("\nDirection: ");
                sb.append(location.getDirection());// 方向
                if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                    mTopbar.setGpsButton(true);
                    try {
                        String newtime = formatTimeEight(location.getTime()) ;//mytime 是原来的时间，newtime是新时间
                        mTime = newtime;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                    sb.append("\ndescribe : ");
                    sb.append("网络定位成功");
                }else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                    sb.append("\ndescribe : ");
                    sb.append("离线定位成功");
                    try {
                        String newtime = formatTimeEight(location.getTime()) ;//mytime 是原来的时间，newtime是新时间
                        mTime = newtime;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else if (location.getLocType() == BDLocation.TypeServerError) {
                    mTopbar.setGpsButton(false);
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    mTopbar.setGpsButton(false);
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    mTopbar.setGpsButton(false);
                }

                stringBuilder = sb;
                mTopbar.updateTime(mTime);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        speedResult.setText(String.valueOf(mSpeed));
                        LocationResult.setText(stringBuilder);
                        //tv_time.setText(mTime);
                    }
                });
                //logMsg(sb.toString(), mTime, mSpeed);
            }
        }

    };
    public static String formatTimeEight(String time) throws Exception {
        Date d = null;
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        d = sd.parse(time);
        long rightTime = (long) (d.getTime() + 8 * 60 * 60 * 1000); //把当前得到的时间用date.getTime()的方法写成时间戳的形式，再加上8小时对应的毫秒数
        String newtime = sd.format(rightTime);//把得到的新的时间戳再次格式化成时间的格式
        return newtime;
    }


}
