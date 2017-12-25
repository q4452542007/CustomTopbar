package mapsoft.com.costomtopbar.report;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Message;
import android.os.RemoteException;
import android.secondbook.com.buttonfragment.R;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.amap.api.fence.GeoFence;
import com.amap.api.fence.GeoFenceClient;
import com.amap.api.fence.GeoFenceListener;
import com.amap.api.location.CoordinateConverter;
import com.amap.api.location.DPoint;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mapsoft.com.costomtopbar.ITTsService;

import mapsoft.com.costomtopbar.activity.MainActivity;
import mapsoft.com.costomtopbar.module.Path;
import mapsoft.com.costomtopbar.module.Station;

import static com.amap.api.fence.GeoFenceClient.GEOFENCE_IN;
import static com.amap.api.fence.GeoFenceClient.GEOFENCE_OUT;
import static com.amap.api.fence.GeoFenceClient.GEOFENCE_STAYED;

/**
 * @author djl
 * @function
 */

public class GeofenceService implements View.OnClickListener,
        GeoFenceListener{
    // 地理围栏客户端
    private GeoFenceClient mGeoFenceClient = null;
    // 中心点坐标
    public double latitude;
    public double longitude;
    // 要创建的围栏半径
    private float fenceRadius = 0.0F;
    // 触发地理围栏的行为，默认为进入提醒
    private int activatesAction = GEOFENCE_IN;
    // 地理围栏的广播action
    private static final String GEOFENCE_BROADCAST_ACTION = "com.example.geofence.round";

    // 记录已经添加成功的围栏
    private HashMap<String, GeoFence> fenceMap = new HashMap<String, GeoFence>();

    //记录一条线路的围栏中心点集合
    private ArrayList<DPoint> mDPoints;


    private Context mContext;
    // 语音合成对象
    private ITTsService iTtsService = null;
    private Toast mToast;
    private MainActivity.MyHandler mHandler;

    private Message mMessage;
    //缓冲进度
    private int mPercentForBuffering = 0;
    //播放进度
    private int mPercentForPlaying = 0;
    private ArrayList<String> flag;

    public GeofenceService(Context context, ITTsService iTtsService) {
        this.mGeoFenceClient = new GeoFenceClient(context);
        this.mDPoints = new ArrayList<>();
        /**
         * 创建pendingIntent
         */
        mGeoFenceClient.createPendingIntent(GEOFENCE_BROADCAST_ACTION);
        mGeoFenceClient.setGeoFenceListener(this);
        /**
         * 设置地理围栏的触发行为,默认为进入
         */
        mGeoFenceClient.setActivateAction(GEOFENCE_IN| GEOFENCE_OUT |GEOFENCE_STAYED);
        this.iTtsService = iTtsService;
        mHandler = new MainActivity.MyHandler((MainActivity) context);
        flag = new ArrayList<>();
        mContext = context;
    }

    //创建围栏中心点
    public void creatPoints(Path path) {
        ArrayList<Station> stations = path.getStations();
        int i = 0;
        mDPoints.clear();
        for (i = 0; i < stations.size(); i++){
            //创建一个中心点坐标
            DPoint centerPoint = new DPoint();
            //设置中心点纬度
            CoordinateConverter converter = new CoordinateConverter(mContext);
            converter.from(CoordinateConverter.CoordType.GPS);
            try {
                converter.coord(new DPoint(stations.get(i).getLatitude(), stations.get(i).getLongitude()));
                DPoint desLatLng = converter.convert();
                centerPoint.setLatitude(desLatLng.getLatitude());
                //设置中心点经度
                centerPoint.setLongitude(desLatLng.getLongitude());
            } catch (Exception e) {
                e.printStackTrace();
            }
            mDPoints.add(centerPoint);
        }
        mGeoFenceClient.removeGeoFence();
        flag.clear();
        int num = mGeoFenceClient.getAllGeoFence().size();
        addfence(mDPoints,stations);

    }

    private void addfence(ArrayList<DPoint> dPoints,ArrayList<Station> stations) {
        for (int i =0 ;i < dPoints.size(); i++ ){
            if (i == dPoints.size()-2) {
                mGeoFenceClient.addGeoFence(dPoints.get(i),stations.get(i).getInRadius(), stations.get(i).getName()+"," + String.valueOf(stations.get(i).getOrder()) + "," + stations.get(i+1).getName() + "," + "终点站" );
            }
            else if (i == dPoints.size()-1) {
                mGeoFenceClient.addGeoFence(dPoints.get(i),stations.get(i).getInRadius(), stations.get(i).getName()+"," + String.valueOf(stations.get(i).getOrder()));
            }
            else {
                mGeoFenceClient.addGeoFence(dPoints.get(i),stations.get(i).getInRadius(), stations.get(i).getName()+"," + String.valueOf(stations.get(i).getOrder()) + "," + stations.get(i+1).getName());
            }
            flag.add("-1");
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onGeoFenceCreateFinished(List<GeoFence> list, int errorCode, String s) {
        if(errorCode == GeoFence.ADDGEOFENCE_SUCCESS){//判断围栏是否创建成功
            Log.e("GeoFence : ","添加围栏成功!!");
            //geoFenceList就是已经添加的围栏列表，可据此查看创建的围栏
        } else {
            //geoFenceList就是已经添加的围栏列表
            Log.e("GeoFence : ","添加围栏失败!!");
        }
    }
    // 注册广播
    public void registerReceiver() {
        IntentFilter filter = new IntentFilter(
                ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(GEOFENCE_BROADCAST_ACTION);
        mContext.registerReceiver(mGeoFenceReceiver, filter);
    }

    public void unRegisterReceiver() {
        mContext.unregisterReceiver(mGeoFenceReceiver);
    }

    private BroadcastReceiver mGeoFenceReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(GEOFENCE_BROADCAST_ACTION)) {
                //解析广播内容
                //获取Bundle
                Bundle bundle = intent.getExtras();
                //获取自定义的围栏标识：
                String customId = bundle.getString(GeoFence.BUNDLE_KEY_CUSTOMID);
                int status = bundle.getInt(GeoFence.BUNDLE_KEY_FENCESTATUS);
                String fenceId = bundle.getString(GeoFence.BUNDLE_KEY_FENCEID);
                GeoFence fence = bundle.getParcelable(GeoFence.BUNDLE_KEY_FENCE);
                String order = customId.split(",")[1];
                if (flag.get(Integer.valueOf(order)).equals("-1")) {
                    //mTts.startSpeaking("围栏初始化", mTtsListener);
                    flag.set(Integer.valueOf(order),status+"");
                    mMessage =new Message();
                    mMessage.what=0x002;
                    mMessage.obj = "-1";
                    mHandler.sendMessage(mMessage);
                } else {

                    if (null == iTtsService) {
                        // 创建单例失败，与 21001 错误为同样原因，参考 http://bbs.xfyun.cn/forum.php?mod=viewthread&tid=9688
                        showTip("创建对象失败，请确认 libmsc.so 放置正确，\n 且有调用 createUtility 进行初始化");
                        return;
                    }
                    int code;
                    try {
                    switch (status) {
                        case 1:
                            Log.e("报站:", customId.split(",")[0] + "到了");
                            flag.set(Integer.valueOf(order),"0");

                                if (customId.split(",").length == 2) {
                                    iTtsService.report("终点站," + customId.split(",")[0]);

                                } else {
                                    iTtsService.report(customId.split(",")[0] + ",到了");
                                }


                            mMessage =new Message();
                            mMessage.what=0x002;
                            mMessage.obj = order;
                            mHandler.sendMessage(mMessage);
                            break;
                        case 2:
                            Log.e("报站:", customId.split(",")[0] + "离开");
                            flag.set(Integer.valueOf(order),"1");
                            if (customId.split(",").length == 4) {
                                iTtsService.report("下一站," + "终点站" + customId.split(",")[2]);
                            }
                            else if (customId.split(",").length == 2) {
                                //切换上下行
                            }
                            else {
                                iTtsService.report("下一站," + customId.split(",")[2]);
                            }
                            break;
                        case 4:
                            Log.e("报站:", customId.split(",")[0] + "停留过久");
                            iTtsService.report(customId.split(",")[0]);
                            break;
                    }
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    };
    /**
     * 初始化监听。
     */
    private InitListener mTtsInitListener = new InitListener() {
        @Override
        public void onInit(int code) {

        }
    };
    /**
     * 合成回调监听。
     */
    private SynthesizerListener mTtsListener = new SynthesizerListener() {

        @Override
        public void onSpeakBegin() {
            showTip("开始播放");
        }

        @Override
        public void onSpeakPaused() {
            showTip("暂停播放");
        }

        @Override
        public void onSpeakResumed() {
            showTip("继续播放");
        }

        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos,
                                     String info) {
            // 合成进度
            mPercentForBuffering = percent;
            showTip(String.format(mContext.getString(R.string.tts_toast_format),
                    mPercentForBuffering, mPercentForPlaying));
        }

        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
            // 播放进度
            mPercentForPlaying = percent;
            showTip(String.format(mContext.getString(R.string.tts_toast_format),
                    mPercentForBuffering, mPercentForPlaying));
        }

        @Override
        public void onCompleted(SpeechError error) {
            if (error == null) {
                showTip("播放完成");
            } else if (error != null) {
                showTip(error.getPlainDescription(true));
            }
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
        }
    };
    private void showTip(final String str) {
        mToast.setText(str);
        mToast.show();
    }
}
