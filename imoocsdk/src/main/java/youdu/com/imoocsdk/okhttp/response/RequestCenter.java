package youdu.com.imoocsdk.okhttp.response;


import youdu.com.imoocsdk.module.AdInstance;
import youdu.com.imoocsdk.okhttp.CommonOkHttpClient;
import youdu.com.imoocsdk.okhttp.listener.DisposeDataHandle;
import youdu.com.imoocsdk.okhttp.listener.DisposeDataListener;
import youdu.com.imoocsdk.okhttp.request.CommonRequest;

/**
 * Created by renzhiqiang on 16/10/27.
 *
 * @function sdk请求发送中心
 */
public class RequestCenter {

    /**
     * 发送广告请求
     */
    public static void sendImageAdRequest(String url, DisposeDataListener listener) {

        CommonOkHttpClient.post(CommonRequest.createPostRequest(url, null),
                new DisposeDataHandle(listener, AdInstance.class));
    }
}
