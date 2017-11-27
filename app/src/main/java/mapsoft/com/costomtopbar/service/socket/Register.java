package mapsoft.com.costomtopbar.service.socket;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import mapsoft.com.costomtopbar.IBackService;
import mapsoft.com.costomtopbar.constant.Constant;

/**
 * @author djl
 * @function
 */

public class Register extends Service {

    private Intent mServiceIntent;
    private IBackService iBackService;
    public Register() {
        try {
            boolean isSend = iBackService.sendMessage(Constant.REGISTER);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
