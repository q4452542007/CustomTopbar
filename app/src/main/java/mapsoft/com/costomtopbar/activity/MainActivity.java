package mapsoft.com.costomtopbar.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.secondbook.com.buttonfragment.R;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.ArrayList;
import mapsoft.com.costomtopbar.fragment.AffairFragment;
import mapsoft.com.costomtopbar.fragment.ChangePathFragment;
import mapsoft.com.costomtopbar.fragment.HomeFragment;
import mapsoft.com.costomtopbar.fragment.OptionFragment;
import mapsoft.com.costomtopbar.fragment.TalkFragment;
import mapsoft.com.costomtopbar.view.Topbar;


public class MainActivity extends BaseActivity implements OnClickListener {
    private final int SDK_PERMISSION_REQUEST = 127;
    /***
     * fragment相关
     */
    private HomeFragment mHomeFragment;
    private ChangePathFragment mChangePathFragment;
    private AffairFragment mAffairFragment;
    private OptionFragment mOptionFragment;
    private TalkFragment mTalkFragment;

    private RelativeLayout mHomeLayout;
    private RelativeLayout mChangePathLayout;
    private RelativeLayout mAffairLayout;
    private RelativeLayout mOptionLayout;
    private RelativeLayout mTalkLayout;
    private TextView mHomeView;
    private TextView mPathView;
    private TextView mAffairView;
    private TextView mOptionView;
    private FragmentManager fm;
    private RelativeLayout mMain;
    private FragmentTransaction fragmentTransaction;
    private String permissionInfo;

    /**
     * 获取网络状态
     */
    private IntentFilter mIntentFilter;
    private NetworkChangeReceiver mNetworkChangeReceiver;
    private Topbar mTopbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mMain = (RelativeLayout) findViewById(R.id.main);
        //mMain.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        mTopbar = findViewById(R.id.topbar);
        //获取网络状态
        /*mIntentFilter = new IntentFilter();
        mIntentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        mNetworkChangeReceiver = new NetworkChangeReceiver();
        registerReceiver(mNetworkChangeReceiver, mIntentFilter);*/

        findById();

        // 進入系統默認為movie
        mHomeFragment = new HomeFragment();
        fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_content, mHomeFragment);
        fragmentTransaction.commit();
        
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    private void findById() {
        mHomeLayout = (RelativeLayout) findViewById(R.id.home_layout_view);
        mHomeLayout.setOnClickListener(this);
        mChangePathLayout = (RelativeLayout) findViewById(R.id.path_layout_view);
        mChangePathLayout.setOnClickListener(this);
        mAffairLayout = (RelativeLayout) findViewById(R.id.affair_layout_view);
        mAffairLayout.setOnClickListener(this);
        mOptionLayout = (RelativeLayout) findViewById(R.id.dvr_layout_view);
        mOptionLayout.setOnClickListener(this);
        mTalkLayout = (RelativeLayout) findViewById(R.id.talk_layout_view);
        mTalkLayout.setOnClickListener(this);
        /*mHomeView = (TextView) this.findViewById(R.id.home_image_view);
        mPathView = (TextView) this.findViewById(R.id.path_image_view);
        mAffairView = (TextView) this.findViewById(R.id.affair_image_view);
        mOptionView = (TextView) this.findViewById(R.id.dvr_image_view);*/
        mHomeLayout.setOnClickListener(this);
        mChangePathLayout.setOnClickListener(this);
        mAffairLayout.setOnClickListener(this);
        mOptionLayout.setOnClickListener(this);

        mHomeLayout.setBackgroundResource(R.drawable.kaoqinc);


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
                mHomeLayout.setBackgroundResource(R.drawable.kaoqinc);
                mChangePathLayout.setBackgroundResource(R.drawable.xianlu);
                mAffairLayout.setBackgroundResource(R.drawable.shijian);
                mOptionLayout.setBackgroundResource(R.drawable.dvr);
                mTalkLayout.setBackgroundResource(R.drawable.hanhua);
                //显示Homefragment
                hideFragment(mChangePathFragment,fragmentTransaction);
                hideFragment(mAffairFragment,fragmentTransaction);
                hideFragment(mOptionFragment,fragmentTransaction);
                hideFragment(mTalkFragment, fragmentTransaction);
                if (mHomeFragment == null){
                    mHomeFragment = new HomeFragment();
                    fragmentTransaction.add(R.id.fragment_content,mHomeFragment);
                } else {
                    fragmentTransaction.show(mHomeFragment);
                }
                break;

            case R.id.path_layout_view:
                mHomeLayout.setBackgroundResource(R.drawable.kaoqin);
                mChangePathLayout.setBackgroundResource(R.drawable.xianluc);
                mAffairLayout.setBackgroundResource(R.drawable.shijian);
                mOptionLayout.setBackgroundResource(R.drawable.dvr);
                mTalkLayout.setBackgroundResource(R.drawable.hanhua);
                //显示Homefragment
                hideFragment(mHomeFragment,fragmentTransaction);
                hideFragment(mAffairFragment,fragmentTransaction);
                hideFragment(mOptionFragment,fragmentTransaction);
                hideFragment(mTalkFragment, fragmentTransaction);
                if (mChangePathFragment == null){
                    mChangePathFragment = new ChangePathFragment();
                    fragmentTransaction.add(R.id.fragment_content,mChangePathFragment);
                } else {
                    fragmentTransaction.show(mChangePathFragment);
                }
                break;

            case R.id.affair_layout_view:
                mHomeLayout.setBackgroundResource(R.drawable.kaoqin);
                mChangePathLayout.setBackgroundResource(R.drawable.xianlu);
                mAffairLayout.setBackgroundResource(R.drawable.shijianc);
                mOptionLayout.setBackgroundResource(R.drawable.dvr);
                mTalkLayout.setBackgroundResource(R.drawable.hanhua);
                //显示Homefragment
                hideFragment(mChangePathFragment,fragmentTransaction);
                hideFragment(mHomeFragment,fragmentTransaction);
                hideFragment(mOptionFragment,fragmentTransaction);
                hideFragment(mTalkFragment, fragmentTransaction);
                if (mAffairFragment == null){
                    mAffairFragment = new AffairFragment();
                    fragmentTransaction.add(R.id.fragment_content,mAffairFragment);
                } else {
                    fragmentTransaction.show(mAffairFragment);
                }
                break;

            case R.id.dvr_layout_view:
                mHomeLayout.setBackgroundResource(R.drawable.kaoqin);
                mChangePathLayout.setBackgroundResource(R.drawable.xianlu);
                mAffairLayout.setBackgroundResource(R.drawable.shijian);
                mOptionLayout.setBackgroundResource(R.drawable.dvrc);
                mTalkLayout.setBackgroundResource(R.drawable.hanhua);
                //显示Homefragment
                hideFragment(mChangePathFragment,fragmentTransaction);
                hideFragment(mHomeFragment,fragmentTransaction);
                hideFragment(mAffairFragment,fragmentTransaction);
                hideFragment(mTalkFragment, fragmentTransaction);
                if (mOptionFragment == null){
                    mOptionFragment = new OptionFragment();
                    fragmentTransaction.add(R.id.fragment_content,mOptionFragment);
                } else {
                    fragmentTransaction.show(mOptionFragment);
                }
                break;
            case R.id.talk_layout_view:
                mHomeLayout.setBackgroundResource(R.drawable.kaoqin);
                mChangePathLayout.setBackgroundResource(R.drawable.xianlu);
                mAffairLayout.setBackgroundResource(R.drawable.shijian);
                mOptionLayout.setBackgroundResource(R.drawable.dvr);
                mTalkLayout.setBackgroundResource(R.drawable.hanhuac);
                //显示Homefragment
                hideFragment(mChangePathFragment,fragmentTransaction);
                hideFragment(mHomeFragment,fragmentTransaction);
                hideFragment(mAffairFragment,fragmentTransaction);
                hideFragment(mOptionFragment,fragmentTransaction);
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
    @TargetApi(23)
    private void getPersimmions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissions = new ArrayList<String>();
            /***
             * 定位权限为必须权限，用户如果禁止，则每次进入都会申请
             */
            // 定位精确位置
            if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if(checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
			/*
			 * 读写权限和电话状态权限非必要权限(建议授予)只会申请一次，用户同意或者禁止，只会弹一次
			 */
            // 读写权限
            if (addPermission(permissions, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissionInfo += "Manifest.permission.WRITE_EXTERNAL_STORAGE Deny \n";
            }
            // 读取电话状态权限
            if (addPermission(permissions, Manifest.permission.READ_PHONE_STATE)) {
                permissionInfo += "Manifest.permission.READ_PHONE_STATE Deny \n";
            }

            if (permissions.size() > 0) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), SDK_PERMISSION_REQUEST);
            }
        }
    }
    @TargetApi(23)
    private boolean addPermission(ArrayList<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) { // 如果应用没有获得对应权限,则添加到列表中,准备批量申请
            if (shouldShowRequestPermissionRationale(permission)){
                return true;
            }else{
                permissionsList.add(permission);
                return false;
            }

        }else{
            return true;
        }
    }

    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // TODO Auto-generated method stub
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    /**
     * 网络状态广播
     */
    class NetworkChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(context.CONNECTIVITY_SERVICE);
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
    protected void onDestroy() {
        super.onDestroy();
        //unregisterReceiver(mNetworkChangeReceiver);
    }
}
