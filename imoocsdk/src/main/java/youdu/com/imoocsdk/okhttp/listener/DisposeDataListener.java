package youdu.com.imoocsdk.okhttp.listener;

/**
 * 业务逻辑层真正处理的地方，包括java层异常和业务层异常
 * @author djl
 * @function   自定义时间监听
 */
public interface DisposeDataListener {

    /**
     *  请求成功回调事件处理
     * @param responseObj
     */
    public void onSuccess(Object responseObj);

    /**
     *  请求失败回调时间处理
     * @param responseObj
     */
    public void onFailure(Object responseObj);

}
