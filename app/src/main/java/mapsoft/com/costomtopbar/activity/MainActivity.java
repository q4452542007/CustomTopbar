package mapsoft.com.costomtopbar.activity;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;


import android.content.res.Configuration;
import android.content.res.Resources;

import android.os.Bundle;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.os.RemoteException;
import android.secondbook.com.buttonfragment.R;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.AMapLocationQualityReport;


import mapsoft.com.costomtopbar.IBackService;
import mapsoft.com.costomtopbar.constant.Constant;
import mapsoft.com.costomtopbar.fragment.AMapUtils;
import mapsoft.com.costomtopbar.fragment.AffairFragment;
import mapsoft.com.costomtopbar.fragment.ChangePathFragment;
import mapsoft.com.costomtopbar.fragment.HomeFragment;
import mapsoft.com.costomtopbar.fragment.LoginFragment;
import mapsoft.com.costomtopbar.fragment.DVRFragment;
import mapsoft.com.costomtopbar.fragment.TalkFragment;
import mapsoft.com.costomtopbar.jt808.JT808ProtocolUtils;
import mapsoft.com.costomtopbar.jt808.LocationInfoUploadMsg;
import mapsoft.com.costomtopbar.jt808.MsgEncoder;
import mapsoft.com.costomtopbar.jt808.TPMSConsts;
import mapsoft.com.costomtopbar.service.socket.BackService;
import mapsoft.com.costomtopbar.service.socket.RigsterMessage;
import mapsoft.com.costomtopbar.util.BitOperator;
import mapsoft.com.costomtopbar.util.Utils;
import mapsoft.com.costomtopbar.view.IconCut;
import mapsoft.com.costomtopbar.view.Topbar;


public class MainActivity extends BaseActivity implements OnClickListener, HomeFragment.FragmentInteraction{
    private static final String TAG = "MainActivity";
    private final int SDK_PERMISSION_REQUEST = 127;
    /***
     * fragment相关
     */
    private HomeFragment mHomeFragment;
    private LoginFragment mLoginFragment;
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
    private Intent mServiceIntent;
    private IBackService iBackService;

    //自定义顶部栏
    private Topbar mTopbar;

    private static byte[] message;

    private BitOperator bitOperator;
    private JT808ProtocolUtils jt808ProtocolUtils;
    private MsgEncoder mMsgEncoder;

    private LocationInfoUploadMsg mLocationInfoUploadMsg;
    private int count = 0;
    private int flow = 0;
    private static Boolean isChecked = false;

    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        Log.e("TAG", "Max memory is " + maxMemory + "KB");
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mMain = (RelativeLayout) findViewById(R.id.main);
        //mMain.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        mTopbar = (Topbar) findViewById(R.id.topbar);
        //获取网络状态
        /*mIntentFilter = new IntentFilter();
        mIntentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        mNetworkChangeReceiver = new NetworkChangeReceiver();
        registerReceiver(mNetworkChangeReceiver, mIntentFilter);*/
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

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        bindService(mServiceIntent, conn, BIND_AUTO_CREATE);
        // 开始服务
        registerReceiver();
    }

    private void findById() {
        mServiceIntent = new Intent(this, BackService.class);
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

    // 注册广播
    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BackService.HEART_BEAT_ACTION);
        intentFilter.addAction(BackService.MESSAGE_ACTION);
        intentFilter.addAction(BackService.RIGSTER_BEAT_ACTION);
        intentFilter.addAction(BackService.CHECK_BEAT_ACTION);
        intentFilter.addAction(BackService.LOCATION_UPLOAD_ACTION);
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
                switch (intent.getIntExtra("result",1)) {
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

    @Override
    protected void onPause() {
        super.onPause();
        // 注销广播 最好在onPause上注销
        unregisterReceiver(mReceiver);
        // 注销服务
        unbindService(conn);
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

}
