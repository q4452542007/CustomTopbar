/*
package mapsoft.com.costomtopbar.network.http;


import java.io.File;
import java.io.FileNotFoundException;

import youdu.com.imoocsdk.okhttp.CommonOkHttpClient;

import youdu.com.imoocsdk.okhttp.listener.DisposeDataHandle;
import youdu.com.imoocsdk.okhttp.listener.DisposeDataListener;
import youdu.com.imoocsdk.okhttp.listener.DisposeDownloadListener;
import youdu.com.imoocsdk.okhttp.request.CommonRequest;
import youdu.com.imoocsdk.okhttp.request.RequestParams;

*/
/**
 * @author djl
 * @function 存放应用中的所有请求
 *//*


public class RequestCenter {

    //根据参数发送所有post请求
    private static void postRequest(String url, RequestParams params, DisposeDataListener listener, Class<?> clazz) {

        CommonOkHttpClient.get(CommonRequest.createGetRequest(url, params), new DisposeDataHandle(listener, clazz
        ));
    }

    */
/*public static void requestRecommandData(DisposeDataListener listener) {

        RequestCenter.postRequest(HttpConstants.HOME_RECOMMAND, null, listener, BaseRecommandModel.class);
    }*//*


    public static void downloadFile(String url, String path, DisposeDownloadListener listener) {
        CommonOkHttpClient.downloadFile(CommonRequest.createGetRequest(url, null),
                new DisposeDataHandle(listener, path));
    }


    //文件上传   暂定用于上传语音
    public static void uploadFile(String url, String path, DisposeDownloadListener listener,File file) throws FileNotFoundException {
        RequestParams params = new RequestParams();
        params.put("wav",file);
        CommonOkHttpClient.uploadFile(CommonRequest.createMultiPostRequest(url, params),
                new DisposeDataHandle(listener, path));

    }


  */
/*  *//*
*/
/**
     * 检查版本更新
     * @param listener
     *//*
*/
/*
    public static void checkVersion(DisposeDataListener listener) {

        RequestCenter.postRequest(HttpConstants.CHECK_UPDATE, null, listener, UpdateModel.class);
    }

    public static void login(String userName, String passwd, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("mb", userName);
        params.put("pwd", passwd);
        RequestCenter.postRequest(HttpConstants.LOGIN, params, listener, User.class);

    }*//*

}
*/
