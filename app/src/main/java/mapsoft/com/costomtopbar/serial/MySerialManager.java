package mapsoft.com.costomtopbar.serial;

import android.content.Context;
import android.hardware.SerialManager;
import android.hardware.SerialPort;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.nio.ByteBuffer;

import mapsoft.com.costomtopbar.activity.MainActivity;
import mapsoft.com.costomtopbar.constant.MapSoftConsts;
import mapsoft.com.costomtopbar.map2312.InBusPlateMsg;
import mapsoft.com.costomtopbar.map2312.PathPlateMsg;
import mapsoft.com.costomtopbar.map2312.PlateMsg;
import mapsoft.com.costomtopbar.map2312.PlateMsgEncoder;
import mapsoft.com.costomtopbar.util.Utils;


/**
 * @author djl
 * @function
 */

public class MySerialManager {

    private static final String TAG = "MySerialManager";

    private Context mContext = null;

    private static MySerialManager mMySerialManager = null;

    private static SerialManager mSerialManager = null;

    private SerialPort mSerialPort;

    private SerialPort mTransparentPort;

    private boolean isOpened = false;

    private ByteBuffer mOutputBuffer;

    private ByteBuffer mInputBuffer;

    private TransparentThread mTransparentThread;

    private ReadThread mReadThread;

    byte[] buffer = new byte[1024];

    private PlateMsgEncoder mPlateMsgEncoder;

    private PathPlateMsg mPathPlateMsg;

    private MainActivity mMainActivity;

    private MySerialManager(Context context) {
        mContext = context;
        mMainActivity = (MainActivity) context;
        mOutputBuffer = ByteBuffer.allocate(1024);
        mInputBuffer = ByteBuffer.allocate(1024);
        mSerialManager = (SerialManager)context.getSystemService("serial");
        //openTransparentPort();
        openPathPlatePort();
        mPlateMsgEncoder = new PlateMsgEncoder();
        mPathPlateMsg = new PathPlateMsg();
    }

    public static MySerialManager getInstance(Context context) {
        if (mMySerialManager == null)
        {
            mMySerialManager = new MySerialManager(context);
        }
        return  mMySerialManager;
    }

    public SerialManager getSerialManager() {
        return mSerialManager;
    }

    private SerialPort openSerialPort(String port, int speed) {

        if(isOpened) {
            Log.e(TAG,"serial already opened");
        }else{
            OpenPort(port,speed);
            isOpened = true;
        }

        return mSerialPort;
    }

    private void OpenPort(String port, int speed) {
        closeSerial();
        try {
            /*if(port.equals("/dev/mcu2ttyS1")){
                mTransparentPort = mSerialManager.openSerialPort(port,speed);
                if (mTransparentPort != null) {
                    mTransparentThread = new TransparentThread();
                    mTransparentThread.start();
                }
                return;
            }*/
            mSerialPort = mSerialManager.openSerialPort(port,speed);
            if (mSerialPort != null) {
                mReadThread = new ReadThread();
                mReadThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendCmd(byte[] _cmd) {
        if (mSerialPort != null) {
            try {
                mOutputBuffer.clear();
                mOutputBuffer.put(_cmd);
                mSerialPort.write(mOutputBuffer, _cmd.length);
            } catch (IOException e) {
                Log.e(TAG, "write failed", e);
            }
        }
    }
    public void sendPathPlateMsg(int pathNum,byte[] content) {
        mPathPlateMsg.setContent(content);
        mPathPlateMsg.setDisplayStyle(MapSoftConsts.left_right_display);
        mPathPlateMsg.setPathNum(pathNum);
        mPathPlateMsg.setPlateProps(128);
        mPathPlateMsg.setWords(26);
        byte[] sss = null;
        try {
            sss = mPlateMsgEncoder.encode4PathPlateMsg(mPathPlateMsg);
            Log.e(TAG, Utils.bytes2HexString(sss));
            sendCmd(sss);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void sendAdMsg(byte[] content) {
        InBusPlateMsg mInBusPlateMsg = new InBusPlateMsg();
        mInBusPlateMsg.setAdNum(1);
        mInBusPlateMsg.setAdress(0x5B);
        mInBusPlateMsg.setContent(content);
        byte[] sss = null;
        try {
            sss = mPlateMsgEncoder.encode4AdMsg(mInBusPlateMsg);
            Log.e(TAG, Utils.bytes2HexString(sss));
            sendCmd(sss);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void sendClearAdMsg() {

        byte[] sss = null;
        try {
            sendCmd(MapSoftConsts.com_clear_ad);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void sendLcdChangePathMsg(byte[] content) {
        InBusPlateMsg mInBusPlateMsg = new InBusPlateMsg();
        mInBusPlateMsg.setContent(content);
        byte[] sss = null;
        try {
            sss = mPlateMsgEncoder.encode4LcdChangePathMsg(mInBusPlateMsg);
            Log.e(TAG, Utils.bytes2HexString(sss));
            sendCmd(sss);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendLcdStationMsg(InBusPlateMsg mInBusPlateMsg) {
        byte[] sss = null;
        try {
            sss = mPlateMsgEncoder.encode4LcdStationMsg(mInBusPlateMsg);
            Log.e("Staions: ", Utils.bytes2HexString(sss));
            sendCmd(sss);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openPathPlatePort() {

        openSerialPort("/dev/mcu2ttyS2", 9600);

    }

    public void openTransparentPort() {

        OpenPort("/dev/mcu2ttyS1", 9600);

    }

    public void openLcdGuidePlatePort() {
       // openSerialPort("")
    }

    private void closeSerial() {

        if (mSerialPort != null) {
            try {
                mSerialPort.close();
            } catch (IOException e) {
            }
            mSerialPort = null;
        }
    }

    private void onDataReceived(final byte[] buf, final int size) {

    }

    private class ReadThread extends Thread {
        int ret = 0;

        @Override
        public void run() {
            Log.d(TAG, "ReadThread run");
            super.run();

            while (!isInterrupted()) {
                try {
                    Thread.sleep(100);

                    ret = 0;
                    try {
                        ret = mSerialPort.read(mInputBuffer);
                        if (ret > 0) {
                            mInputBuffer.get(buffer, 0, ret);
                            if(mContext == null) {
                                break;
                            }else {
                                Log.e(TAG,Utils.bytes2HexString(buffer));
                                mMainActivity.logMsg(Utils.bytes2HexString(buffer));
                            }
                            mInputBuffer.clear();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "read failed", e);
                        break;
                    }
                }catch (InterruptedException e){
                    e.printStackTrace();
                    break;
                }
            }

            Log.d(TAG, "ReadThread exit");
        }
    }
    private class TransparentThread extends Thread {
        int ret = 0;

        @Override
        public void run() {
            Log.d(TAG, "ReadThread run");
            super.run();

            while (!isInterrupted()) {
                try {
                    Thread.sleep(100);

                    ret = 0;
                    try {
                        ret = mTransparentPort.read(mInputBuffer);
                        if (ret > 0) {
                            mInputBuffer.get(buffer, 0, ret);
                            if(mContext == null) {
                                break;
                            }else {
                                onDataReceived(buffer, ret);
                            }
                            mInputBuffer.clear();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "read failed", e);
                        break;
                    }
                }catch (InterruptedException e){
                    e.printStackTrace();
                    break;
                }
            }

            Log.d(TAG, "ReadThread exit");
        }
    }


}
