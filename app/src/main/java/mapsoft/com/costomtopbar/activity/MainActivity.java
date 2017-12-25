package mapsoft.com.costomtopbar.activity;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;


import android.content.res.Configuration;
import android.content.res.Resources;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.kpocom.Gpioctljni;
import android.os.Bundle;

import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.RemoteException;
import android.secondbook.com.buttonfragment.R;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.iflytek.cloud.SpeechSynthesizer;
import java.util.Timer;
import java.util.TimerTask;

import mapsoft.com.costomtopbar.IBackService;

import mapsoft.com.costomtopbar.ITTsService;
import mapsoft.com.costomtopbar.constant.Constant;
import mapsoft.com.costomtopbar.db.ChatSQLiteHelper;
import mapsoft.com.costomtopbar.db.SharedPreferenceManager;
import mapsoft.com.costomtopbar.fragment.AffairFragment;
import mapsoft.com.costomtopbar.fragment.ChangePathFragment;
import mapsoft.com.costomtopbar.fragment.HomeFragment;
import mapsoft.com.costomtopbar.fragment.LoginFragment;
import mapsoft.com.costomtopbar.fragment.DVRFragment;
import mapsoft.com.costomtopbar.fragment.ManualReportFragment;
import mapsoft.com.costomtopbar.fragment.TalkFragment;
import mapsoft.com.costomtopbar.html.ReadHtmlThread;
import mapsoft.com.costomtopbar.jt808.DriverLoginMsg;
import mapsoft.com.costomtopbar.jt808.JT808ProtocolUtils;
import mapsoft.com.costomtopbar.jt808.LocationInfoUploadMsg;
import mapsoft.com.costomtopbar.jt808.MsgEncoder;
import mapsoft.com.costomtopbar.module.Path;
import mapsoft.com.costomtopbar.report.GeofenceService;
import mapsoft.com.costomtopbar.report.TTsService;
import mapsoft.com.costomtopbar.service.socket.BackService;
import mapsoft.com.costomtopbar.util.BitOperator;
import mapsoft.com.costomtopbar.util.Utils;
import mapsoft.com.costomtopbar.view.IconCut;
import mapsoft.com.costomtopbar.view.Topbar;


public class MainActivity extends BaseActivity implements OnClickListener, HomeFragment.FragmentInteraction,LoginFragment.FragmentInteraction1,ChangePathFragment.FragmentInteraction2{
    /***
     * fragment相关
     */
    private static HomeFragment mHomeFragment;
    private LoginFragment mLoginFragment;
    private ManualReportFragment mManualReportFragment;
    private ChangePathFragment mChangePathFragment;
    private AffairFragment mAffairFragment;
    private DVRFragment mDVRFragment;
    private TalkFragment mTalkFragment;
    private RelativeLayout mHomeLayout;
    private RelativeLayout mChangePathLayout;
    private RelativeLayout mAffairLayout;
    private RelativeLayout mDvrLayout;
    private RelativeLayout mTalkLayout;
    private FragmentManager fm;
    private RelativeLayout mMain;
    private FragmentTransaction fragmentTransaction;

    //socket相关
    private Intent mSocketIntent;
    private Intent mTtsIntent;
    private IBackService iBackService;
    private ITTsService iTtsService;


    //自定义顶部栏
    private static Topbar mTopbar;

    private static byte[] message;

    private BitOperator bitOperator;
    private JT808ProtocolUtils jt808ProtocolUtils;
    private MsgEncoder mMsgEncoder;

    private LocationInfoUploadMsg mLocationInfoUploadMsg;
    private static int count = 0;
    private int flow = 0;
    private static Boolean isChecked = false;

    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    //private ReportStation mReportStation;

    private ReadHtmlThread mReadHtmlThread;
    private static Path mPath = null;
    private static GeofenceService mGeofenceService = null;

    private TTsService mTTsService = null;
    // 语音合成对象
    private SpeechSynthesizer mTts;

    public static MyHandler mMyHandler;
    private TimeHandler handler;

    //检测报警
    private TimerTask task = null;
    private Timer timer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        Log.e("TAG", "Max memory is " + maxMemory + "KB");
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mMain = (RelativeLayout) findViewById(R.id.main);
        mMain.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        mTopbar = (Topbar) findViewById(R.id.topbar);
        mTopbar.setHomeButton(IconCut.getInstance(this).cutHomeIcon());
        // 進入系統默認為movie
        mHomeFragment = new HomeFragment();
        fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_content, mHomeFragment);
        fragmentTransaction.commit();
        findById();
        initData();
    }

    private void initData() {
        mMyHandler = new MyHandler(this);
        handler = new TimeHandler();
        String pathAndDi = SharedPreferenceManager.getInstance().getString(SharedPreferenceManager.PATH_SETTING, "65,up");
        mReadHtmlThread = new ReadHtmlThread(mMyHandler, pathAndDi.split(",")[0],pathAndDi.split(",")[1]);
        mReadHtmlThread.run();
        task = new TimerTask() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                Message message =new Message();
                message.what = 1;
                handler.sendMessage(message);
            }
        };
        timer.schedule(task, 1000, 1000);
    }

    //后期收到回应清零
    class TimeHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            if (Gpioctljni.getAlarmValue() == 1) {
                try {
                    mLocationInfoUploadMsg.warningFlagField = 524289;
                    Log.i(TAG, "是否为空：" + iBackService);
                    if (iBackService == null) {
                        Toast.makeText(MainActivity.this,
                                "没有连接，可能是服务器已断开", Toast.LENGTH_SHORT).show();
                    } else {
                        byte[] alarmMsg = mMsgEncoder.encode4LocationInfoUploadMsg(mLocationInfoUploadMsg, 0);
                        String message = Utils.bytes2HexString(alarmMsg);
                        Log.e(TAG, "driverMsg: " + message);
                        boolean isSend = iBackService.sendMessage(alarmMsg);
                        if (isSend) {
                            flow++;
                        } else {
                            isChecked = false;
                        }
                        Toast.makeText(MainActivity.this,
                                isSend ?"发送报警" : "失败", Toast.LENGTH_SHORT)
                                .show();
                        //et.setText("");
                    }

                } catch (RemoteException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            super.handleMessage(msg);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        bindService(mSocketIntent, conn, BIND_AUTO_CREATE);
        bindService(mTtsIntent, conn1, BIND_AUTO_CREATE);
        mGeofenceService = new GeofenceService(this, iTtsService);
        // 开始服务
        registerReceiver();
        mGeofenceService.registerReceiver();
        /*IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        registerReceiver(usbReceiver, filter);*/
    }

    private void findById() {
        mSocketIntent = new Intent(this, BackService.class);
        mTtsIntent = new Intent(this, TTsService.class);
        mHomeLayout = (RelativeLayout) findViewById(R.id.home_layout_view);
        mHomeLayout.setOnClickListener(this);
        mChangePathLayout = (RelativeLayout) findViewById(R.id.path_layout_view);
        mChangePathLayout.setOnClickListener(this);
        mAffairLayout = (RelativeLayout) findViewById(R.id.affair_layout_view);
        mAffairLayout.setOnClickListener(this);
        mDvrLayout = (RelativeLayout) findViewById(R.id.dvr_layout_view);
        mDvrLayout.setOnClickListener(this);
        mTalkLayout = (RelativeLayout) findViewById(R.id.talk_layout_view);
        mTalkLayout.setOnClickListener(this);/*
        mHomeView = (TextView) this.findViewById(R.id.home_image_view);
        mPathView = (TextView) this.findViewById(R.id.path_image_view);
        mAffairView = (TextView) this.findViewById(R.id.affair_image_view);
        mOptionView = (TextView) this.findViewById(R.id.dvr_image_view);*/
        mHomeLayout.setOnClickListener(this);
        mChangePathLayout.setOnClickListener(this);
        mAffairLayout.setOnClickListener(this);
        mDvrLayout.setOnClickListener(this);


        bitOperator = new BitOperator();
        jt808ProtocolUtils = new JT808ProtocolUtils();
        mMsgEncoder = new MsgEncoder();
        //mReportStation = new ReportStation(getApplicationContext());
        mHomeLayout.setBackgroundResource(R.drawable.shouyec);
        mTopbar.setOnTopbarClickListener(new Topbar.topbarClickListener() {
            @Override
            public void leftClick() {

            }

            @Override
            public void homeClick() {

                try {
                    Log.i(TAG, "是否为空：" + iBackService);
                    if (iBackService == null) {
                        Toast.makeText(getApplicationContext(),
                                "没有连接，可能是服务器已断开", Toast.LENGTH_SHORT).show();
                    } else {
                        boolean isSend = iBackService.sendMessage(Constant.HEART);
                        Toast.makeText(MainActivity.this,
                                isSend ? "success" : "fail", Toast.LENGTH_SHORT)
                        .show();
                        //et.setText("");
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

    }
    private void hideFragment(Fragment fragment, FragmentTransaction ft){
        if(fragment != null){
            ft.hide(fragment);
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        fragmentTransaction = fm.beginTransaction();
        switch (v.getId()) {
            case R.id.home_layout_view:
                mHomeLayout.setBackgroundResource(R.drawable.shouyec);
                mChangePathLayout.setBackgroundResource(R.drawable.xianlu);
                mAffairLayout.setBackgroundResource(R.drawable.shijian);
                mDvrLayout.setBackgroundResource(R.drawable.dvr);
                mTalkLayout.setBackgroundResource(R.drawable.hanhua);
                //显示Homefragment
                hideFragment(mLoginFragment,fragmentTransaction);
                hideFragment(mManualReportFragment,fragmentTransaction);
                hideFragment(mChangePathFragment,fragmentTransaction);
                hideFragment(mAffairFragment,fragmentTransaction);
                hideFragment(mDVRFragment,fragmentTransaction);
                hideFragment(mTalkFragment, fragmentTransaction);
                if ( mHomeFragment == null){
                    mHomeFragment = new HomeFragment();
                    fragmentTransaction.add(R.id.fragment_content,mHomeFragment);
                } else {
                    fragmentTransaction.show(mHomeFragment);
                }
                break;

            case R.id.path_layout_view:
                mHomeLayout.setBackgroundResource(R.drawable.shouye);
                mChangePathLayout.setBackgroundResource(R.drawable.xianluc);
                mAffairLayout.setBackgroundResource(R.drawable.shijian);
                mDvrLayout.setBackgroundResource(R.drawable.dvr);
                mTalkLayout.setBackgroundResource(R.drawable.hanhua);
                hideFragment(mLoginFragment,fragmentTransaction);
                hideFragment(mManualReportFragment,fragmentTransaction);
                hideFragment(mHomeFragment,fragmentTransaction);
                hideFragment(mAffairFragment,fragmentTransaction);
                hideFragment(mDVRFragment,fragmentTransaction);
                hideFragment(mTalkFragment, fragmentTransaction);
                if (mChangePathFragment == null){
                    mChangePathFragment = new ChangePathFragment();
                    fragmentTransaction.add(R.id.fragment_content,mChangePathFragment);
                } else {
                    fragmentTransaction.show(mChangePathFragment);
                }
                break;

            case R.id.affair_layout_view:
                mHomeLayout.setBackgroundResource(R.drawable.shouye);
                mChangePathLayout.setBackgroundResource(R.drawable.xianlu);
                mAffairLayout.setBackgroundResource(R.drawable.shijianc);
                mDvrLayout.setBackgroundResource(R.drawable.dvr);
                mTalkLayout.setBackgroundResource(R.drawable.hanhua);
                //显示AffairFragment
                hideFragment(mLoginFragment,fragmentTransaction);
                hideFragment(mManualReportFragment,fragmentTransaction);
                hideFragment(mChangePathFragment,fragmentTransaction);
                hideFragment(mHomeFragment,fragmentTransaction);
                hideFragment(mDVRFragment,fragmentTransaction);
                hideFragment(mTalkFragment, fragmentTransaction);
                if (mAffairFragment == null){
                    mAffairFragment = new AffairFragment();
                    fragmentTransaction.add(R.id.fragment_content,mAffairFragment);
                } else {
                    fragmentTransaction.show(mAffairFragment);
                }
                break;

            case R.id.dvr_layout_view:
                mHomeLayout.setBackgroundResource(R.drawable.shouye);
                hideFragment(mManualReportFragment,fragmentTransaction);
                mChangePathLayout.setBackgroundResource(R.drawable.xianlu);
                mAffairLayout.setBackgroundResource(R.drawable.shijian);
                mDvrLayout.setBackgroundResource(R.drawable.dvrc);
                mTalkLayout.setBackgroundResource(R.drawable.hanhua);
                hideFragment(mLoginFragment,fragmentTransaction);
                hideFragment(mChangePathFragment,fragmentTransaction);
                hideFragment(mHomeFragment,fragmentTransaction);
                hideFragment(mAffairFragment,fragmentTransaction);
                hideFragment(mTalkFragment, fragmentTransaction);
                if (mDVRFragment == null){
                    mDVRFragment = new DVRFragment();
                    fragmentTransaction.add(R.id.fragment_content, mDVRFragment);
                } else {
                    fragmentTransaction.show(mDVRFragment);
                }
                break;
            case R.id.talk_layout_view:
                mHomeLayout.setBackgroundResource(R.drawable.shouye);
                mChangePathLayout.setBackgroundResource(R.drawable.xianlu);
                mAffairLayout.setBackgroundResource(R.drawable.shijian);
                mDvrLayout.setBackgroundResource(R.drawable.dvr);
                mTalkLayout.setBackgroundResource(R.drawable.hanhuac);
                hideFragment(mManualReportFragment,fragmentTransaction);
                hideFragment(mLoginFragment,fragmentTransaction);
                hideFragment(mChangePathFragment,fragmentTransaction);
                hideFragment(mHomeFragment,fragmentTransaction);
                hideFragment(mAffairFragment,fragmentTransaction);
                hideFragment(mDVRFragment,fragmentTransaction);
                if (mTalkFragment == null){
                    mTalkFragment = new TalkFragment();
                    fragmentTransaction.add(R.id.fragment_content,mTalkFragment);
                } else {
                    fragmentTransaction.show(mTalkFragment);
                }
                break;

            default:
                break;
        }
        // 不要忘记提交
        fragmentTransaction.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void toLoginFragment() {
        fragmentTransaction = fm.beginTransaction();
        mHomeLayout.setBackgroundResource(R.drawable.shouye);
        mChangePathLayout.setBackgroundResource(R.drawable.xianlu);
        mAffairLayout.setBackgroundResource(R.drawable.shijian);
        mDvrLayout.setBackgroundResource(R.drawable.dvr);
        mTalkLayout.setBackgroundResource(R.drawable.hanhua);
        //显示Homefragment
        hideFragment(mChangePathFragment,fragmentTransaction);
        hideFragment(mAffairFragment,fragmentTransaction);
        hideFragment(mDVRFragment,fragmentTransaction);
        hideFragment(mTalkFragment, fragmentTransaction);
        if (mLoginFragment == null){
            mLoginFragment = new LoginFragment();
            fragmentTransaction.add(R.id.fragment_content,mLoginFragment);
        } else {
            fragmentTransaction.show(mLoginFragment);
        }
        fragmentTransaction.commit();
    }
    public void toReportFragment() {
        fragmentTransaction = fm.beginTransaction();
        mHomeLayout.setBackgroundResource(R.drawable.shouye);
        mChangePathLayout.setBackgroundResource(R.drawable.xianlu);
        mAffairLayout.setBackgroundResource(R.drawable.shijian);
        mDvrLayout.setBackgroundResource(R.drawable.dvr);
        mTalkLayout.setBackgroundResource(R.drawable.hanhua);
        //显示Homefragment
        hideFragment(mChangePathFragment,fragmentTransaction);
        hideFragment(mAffairFragment,fragmentTransaction);
        hideFragment(mDVRFragment,fragmentTransaction);
        hideFragment(mTalkFragment, fragmentTransaction);
        if (mManualReportFragment == null){
            mManualReportFragment = new ManualReportFragment();
            fragmentTransaction.add(R.id.fragment_content,mManualReportFragment);
        } else {
            fragmentTransaction.show(mManualReportFragment);
        }
        fragmentTransaction.commit();
    }

    public void manulReport(String msg) {
        if (iTtsService != null) {
            try {
                if (msg.equals(Constant.NEXT_STATION)) {
                    mHomeFragment.changeStation(mPath, Constant.NEXT_STATION);
                }
                if (msg.equals(Constant.IN_STATION)) {
                    String ss = mHomeFragment.changeStation(mPath, Constant.IN_STATION);
                    iTtsService.report(ss + ",到了");
                }
                if (msg.equals(Constant.OUT_STATION)) {
                    String ss = mHomeFragment.changeStation(mPath, Constant.OUT_STATION);
                    iTtsService.report("下一站," + ss);
                }

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    // 注册广播
    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BackService.HEART_BEAT_ACTION);
        intentFilter.addAction(BackService.MESSAGE_ACTION);
        intentFilter.addAction(BackService.RIGSTER_BEAT_ACTION);
        intentFilter.addAction(BackService.CHECK_BEAT_ACTION);
        intentFilter.addAction(BackService.LOCATION_UPLOAD_ACTION);
        intentFilter.addAction(BackService.TEXT_ISSUE_ACTION);
        registerReceiver(mReceiver, intentFilter);
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // 消息广播
            if (action.equals(BackService.MESSAGE_ACTION)) {
                String stringExtra = intent.getStringExtra("message");
                Log.e(TAG,stringExtra);
            } else if (action.equals(BackService.HEART_BEAT_ACTION)) {// 心跳广播
                //等待添加UI更新

            }
            else if (action.equals(BackService.RIGSTER_BEAT_ACTION)) {// 注册广播
                switch (Integer.valueOf(intent.getStringExtra("result"))) {
                    case 0:
                    //Log.e(TAG,"注册成功");
                    //等待添加UI更新
                    break;
                    case 1:
                    //Log.e(TAG,"注册失败");
                    //等待添加UI更新
                    break;
                }

            }
            else if (action.equals(BackService.CHECK_BEAT_ACTION)) { //鉴权广播
                isChecked = true;
            }
            else if (action.equals(BackService.LOCATION_UPLOAD_ACTION)) {
                //等待添加UI
            }
            else if (action.equals(BackService.LOCATION_UPLOAD_ACTION)) {
                //等待添加UI
            }
            else if (action.equals(BackService.TEXT_ISSUE_ACTION)) {
                    try {
                        String stringExtra = intent.getStringExtra("result");
                        iTtsService.report("收到平台短信," + stringExtra);
                        mTopbar.setMessageButton(R.drawable.messagebar);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }

            }
        }
    };

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            // 未连接为空
            iBackService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // 已连接
            iBackService = IBackService.Stub.asInterface(service);
        }
    };
    private ServiceConnection conn1 = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            // 未连接为空
            iTtsService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // 已连接
            iTtsService = ITTsService.Stub.asInterface(service);
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        // 注销广播 最好在onPause上注销
        unregisterReceiver(mReceiver);
        // 注销服务
        unbindService(conn);
        unbindService(conn1);
        mGeofenceService.unRegisterReceiver();
        isChecked = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        bindService(mSocketIntent, conn, Context.BIND_AUTO_CREATE);
        bindService(mTtsIntent, conn1, Context.BIND_AUTO_CREATE);
        registerReceiver();
        mGeofenceService.registerReceiver();
    }

    @Override
    public void process(LocationInfoUploadMsg locationInfo) {
        if (isChecked) {
            count++;
            if (count == 8) {
                mLocationInfoUploadMsg = locationInfo;

                try {
                    Log.i(TAG, "是否为空：" + iBackService);
                    if (iBackService == null) {
                        Toast.makeText(getApplicationContext(),
                                "没有连接，可能是服务器已断开", Toast.LENGTH_SHORT).show();
                    } else {
                        byte[] locationMsg = mMsgEncoder.encode4LocationInfoUploadMsg(mLocationInfoUploadMsg, flow);
                        String message = Utils.bytes2HexString(locationMsg);
                        boolean isSend = iBackService.sendMessage(locationMsg);
                        if (isSend) {
                            Log.e(TAG, "locationMsg:" + message);
                            flow++;
                        }
                        else {
                            isChecked = false;
                        }
                        Toast.makeText(MainActivity.this,
                                isSend ? "发送位置信息" : "失败", Toast.LENGTH_SHORT)
                        .show();
                        //et.setText("");
                    }

                } catch (RemoteException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                count = 0;
            }

        }
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config=new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config,res.getDisplayMetrics() );
        return res;
    }

    @Override
    public void sendDriverMsg(DriverLoginMsg driverLoginMsg) {
        if (mLocationInfoUploadMsg == null) {
            Toast.makeText(this,"稍等片刻,定位信息尚未上传",Toast.LENGTH_SHORT).show();
            return;
        }
        driverLoginMsg.setLocationInfoUploadMsg(mLocationInfoUploadMsg);
        try {
            Log.i(TAG, "是否为空：" + iBackService);
            if (iBackService == null) {
                Toast.makeText(getApplicationContext(),
                        "没有连接，可能是服务器已断开", Toast.LENGTH_SHORT).show();
            } else {
                byte[] driverMsg = mMsgEncoder.encode4DriverLoginMsg(driverLoginMsg);
                String message = Utils.bytes2HexString(driverMsg);
                Log.e(TAG,"driverMsg: " + message);
                boolean isSend = iBackService.sendMessage(driverMsg);
                if (isSend) {
                    flow ++;
                }
                else {
                    isChecked = false;
                }
                Toast.makeText(MainActivity.this,
                        isSend ? "考勤签到" : "失败", Toast.LENGTH_SHORT)
                .show();
                //et.setText("");
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendPath(String pathNum, String direction) {
        if (direction.equals("up")) {
            mReadHtmlThread = new ReadHtmlThread(mMyHandler, pathNum, "up");
            mReadHtmlThread.run();
            SharedPreferenceManager.getInstance().putString(SharedPreferenceManager.PATH_SETTING, pathNum + "," + "up");

        } else {
            mReadHtmlThread = new ReadHtmlThread(mMyHandler, pathNum, "down");
            mReadHtmlThread.run();
            SharedPreferenceManager.getInstance().putString(SharedPreferenceManager.PATH_SETTING, pathNum + "," + "down");

        }
    }


    public static class MyHandler extends Handler {

        private Context mContext;

        public MyHandler(Service service){
            super();
            mContext = service;
        }

        public MyHandler(MainActivity activity) {
            super();
            mContext = activity;
        }

        @Override
        public void handleMessage(Message msg) {
            if (mContext == null) {
                super.handleMessage(msg);
                return;
            }
            switch (msg.what) {
                case 0x001:
                    mPath = (Path) msg.obj;
                    mGeofenceService.creatPoints(mPath);
                    mHomeFragment.changePath(mPath);
                    break;
                case 0x002:
                    mHomeFragment.changeStation(mPath,(String) msg.obj);
                    break;
                case 0x003:
                    isChecked = false;
                    count = 0;
                    break;
                case 0x004:
                    mTopbar.cancleMessageButton();
                    break;
            }
        }
    }
    private static final String ACTION_USB_PERMISSION = "com.github.mjdev.libaums.USB_PERMISSION";
    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int COPY_STORAGE_PROVIDER_RESULT = 0;
    private static final int OPEN_STORAGE_PROVIDER_RESULT = 1;
    private static final int OPEN_DOCUMENT_TREE_RESULT = 2;

    private static final int REQUEST_EXT_STORAGE_WRITE_PERM = 0;

    private final BroadcastReceiver usbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {

                UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {

                    if (device != null) {
                        //setupDevice();
                    }
                }

            } else if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action)) {
                UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);

                Log.d(TAG, "USB device attached");

                // determine if connected device is a mass storage devuce
                if (device != null) {
                    //discoverDevice();
                }
            } else if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
                UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);

                Log.d(TAG, "USB device detached");

                // determine if connected device is a mass storage devuce
                if (device != null) {
                    /*if (MainActivity.this.currentDevice != -1) {
                        MainActivity.this.massStorageDevices[currentDevice].close();
                    }
                    // check if there are other devices or set action bar title
                    // to no device if not
                    discoverDevice();*/

                }
            }

        }
    };




}
