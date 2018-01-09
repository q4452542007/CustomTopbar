package youdu.com.imoocsdk.report;


import java.util.ArrayList;

import youdu.com.imoocsdk.module.monitor.Monitor;
import youdu.com.imoocsdk.okhttp.CommonOkHttpClient;
import youdu.com.imoocsdk.okhttp.HttpConstant;
import youdu.com.imoocsdk.okhttp.HttpConstant.Params;
import youdu.com.imoocsdk.okhttp.listener.DisposeDataHandle;
import youdu.com.imoocsdk.okhttp.listener.DisposeDataListener;
import youdu.com.imoocsdk.okhttp.request.CommonRequest;
import youdu.com.imoocsdk.okhttp.request.RequestParams;
import youdu.com.imoocsdk.util.Utils;

/**
 * @author: vision
 * @function: 负责所有监测请求的发送
 * @date: 16/6/13
 */
public class ReportManager {

    /**
     * 默认的事件回调处理
     */
    private static DisposeDataHandle handle = new DisposeDataHandle(
        new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
            }

            @Override
            public void onFailure(Object reasonObj) {
            }
        });

    /**
     * send the sus monitor
     */
    public static void susReport(ArrayList<Monitor> monitors, boolean isAuto) {
        if (monitors != null && monitors.size() > 0) {
            for (Monitor monitor : monitors) {
                RequestParams params = new RequestParams();
                if (Utils.containString(monitor.url, HttpConstant.ATM_PRE)) {
                    params.put("ve", "0");
                    if (isAuto) {
                        params.put("auto", "1");
                    }
                }
                CommonOkHttpClient.get(
                    CommonRequest.createMonitorRequest(monitor.url, params), handle);
            }
        }
    }

    /**
     * 发送视频播放完成监测
     */
    public static void sueReport(ArrayList<Monitor> monitors, boolean isFull, long playTime) {
        if (monitors != null && monitors.size() > 0) {
            for (Monitor monitor : monitors) {
                RequestParams params = new RequestParams();
                if (Utils.containString(monitor.url, HttpConstant.ATM_PRE)) {
                    if (isFull) {
                        params.put("fu", "1");
                    }
                    params.put("ve", String.valueOf(playTime));
                }
                /**
                 * 调用封装好的网络请求组件发送监测请求
                 */
                CommonOkHttpClient.get(
                    CommonRequest.createMonitorRequest(monitor.url, params), handle);
            }
        }
    }

    /**
     * send the su report
     */
    public static void suReport(ArrayList<Monitor> monitors, long playTime) {
        if (monitors != null && monitors.size() > 0) {
            for (Monitor monitor : monitors) {
                RequestParams params = new RequestParams();
                if (monitor.time == playTime) {
                    if (Utils.containString(monitor.url, HttpConstant.ATM_PRE)) {
                        params.put("ve", String.valueOf(playTime));
                    }
                    CommonOkHttpClient.get(
                        CommonRequest.createMonitorRequest(monitor.url, params), handle);
                }
            }
        }
    }

    /**
     * send the clicl full btn monitor
     *
     * @param monitors urls
     * @param playTime player time
     */
    public static void fullScreenReport(ArrayList<Monitor> monitors, long playTime) {
        if (monitors != null && monitors.size() > 0) {
            for (Monitor monitor : monitors) {
                RequestParams params = new RequestParams();
                if (Utils.containString(monitor.url, HttpConstant.ATM_PRE)) {
                    params.put("ve", String.valueOf(playTime));
                }
                CommonOkHttpClient.get(
                    CommonRequest.createMonitorRequest(monitor.url, params), handle);
            }
        }
    }

    /**
     * send the click back full btn monitor
     *
     * @param monitors urls
     * @param playTime player time
     */
    public static void exitfullScreenReport(ArrayList<Monitor> monitors, long playTime) {
        if (monitors != null && monitors.size() > 0) {
            for (Monitor monitor : monitors) {
                RequestParams params = new RequestParams();
                if (Utils.containString(monitor.url, HttpConstant.ATM_PRE)) {
                    params.put("ve", String.valueOf(playTime));
                }
                CommonOkHttpClient.get(
                    CommonRequest.createMonitorRequest(monitor.url, params), handle);
            }
        }
    }


    /**
     * send the video pause monitor
     *
     * @param monitors urls
     * @param playTime player time
     */
    public static void pauseVideoReport(ArrayList<Monitor> monitors, long playTime) {
        if (monitors != null && monitors.size() > 0) {
            for (Monitor monitor : monitors) {
                RequestParams params = new RequestParams();
                if (Utils.containString(monitor.url, HttpConstant.ATM_PRE)) {
                    params.put("ve", String.valueOf(playTime));
                }
                CommonOkHttpClient.get(
                    CommonRequest.createMonitorRequest(monitor.url, params), handle);
            }
        }
    }

    /**
     * 发送广告是否正常解析及展示监测
     */
    public static void sendAdMonitor(boolean isPad, String sid, String ie, String appVersion, Params step, String result) {
        RequestParams params = new RequestParams();
        params.put(Params.lvs.getKey(), HttpConstant.Params.lvs.getValue());
        params.put(Params.st.getKey(), Params.st.getValue());
        params.put(Params.os.getKey(), Params.os.getValue());
        params.put(Params.p.getKey(), Params.p.getValue());
        params.put(Params.appid.getKey(), Params.appid.getValue());
        if (isPad) {
            params.put(Params.bt_pad.getKey(), Params.bt_pad.getValue());
        } else {
            params.put(Params.bt_phone.getKey(), Params.bt_phone.getValue());
        }
        params.put(step.getKey(),
            step.getValue());
        params.put(HttpConstant.STEP_CD, result);
        params.put(HttpConstant.SID, sid);
        params.put(HttpConstant.IE, ie);
        params.put(HttpConstant.AVS, appVersion);

        CommonOkHttpClient.get(CommonRequest.createGetRequest(HttpConstant.ATM_MONITOR, params), handle);
    }
}
