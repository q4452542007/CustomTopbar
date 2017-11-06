package mapsoft.com.costomtopbar.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;



import android.support.v4.content.ContextCompat;

import mapsoft.com.costomtopbar.constant.Constant;


/**
 * Created by Administrator on 2017/10/10.
 * 主要是为所有Fragment提供公共的行为或者事件
 */

public class BaseFragment extends Fragment{

    protected Activity mContext;

    /**
     * 申请指定的权限
     * @param code
     * @param permissions
     */
    public void requestPermission(int code, String... permissions) {
        if (Build.VERSION.SDK_INT >= 23) {
            requestPermissions(permissions, code);
        }
    }

    /**
     * 判断是否有指定的权限
     * @param permissions
     * @return
     */
    public boolean hasPermission(String... permissions) {

        for (String permision : permissions) {
            if (ContextCompat.checkSelfPermission(getActivity(), permision) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions,  int[] grantResults) {
        switch (requestCode) {
            case Constant.HARDWEAR_CAMERA_CODE:
                if (grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    doOpenCamera();
                }
                break;
            case Constant.WRITE_READ_EXTERNAL_CODE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    doWriteSDCard();
                }
                break;
        }
    }
    public void doOpenCamera() {

    }

    public void doWriteSDCard() {

    }
}
