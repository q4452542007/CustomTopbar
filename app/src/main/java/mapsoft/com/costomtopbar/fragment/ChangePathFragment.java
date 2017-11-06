package mapsoft.com.costomtopbar.fragment;



import android.net.Uri;
import android.os.Bundle;


import android.secondbook.com.buttonfragment.R;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by WangChang on 2016/5/15.
 */
public class ChangePathFragment extends BaseFragment {

    private static final String ARG_PATH = "path";

    private static final String TAG = "ChangePathFragment";


    WebView mWebView1,mWebView2;

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_path, container, false);
        mWebView1 = (WebView) view.findViewById(R.id.webview1);
        mWebView1.setBackgroundColor(0);
        mWebView2= (WebView) view.findViewById(R.id.webview2);
        mWebView2.setBackgroundColor(0); // 设置背景色
        //mWebView2.getBackground().setAlpha(0); // 设置填充透明度 范围：0-255
        WebSettings webSettings = mWebView1.getSettings();
        webSettings.setSupportZoom(true);

        // 设置与Js交互的权限
        webSettings.setJavaScriptEnabled(true);
        // 设置允许JS弹窗
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);

        // 步骤1：加载JS代码
        // 格式规定为:file:///android_asset/文件名.html
        mWebView1.loadUrl("file:///android_asset/123.html");
        mWebView2.loadUrl("file:///android_asset/1.html");
        mWebView1.setWebViewClient(new WebViewClient() {
                                      @Override
                                      public boolean shouldOverrideUrlLoading(WebView view, String url) {

                                          // 步骤2：根据协议的参数，判断是否是所需要的url
                                          // 一般根据scheme（协议格式） & authority（协议名）判断（前两个参数）
                                          //假定传入进来的 url = "js://webview?arg1=111&arg2=222"（同时也是约定好的需要拦截的）

                                          Uri uri = Uri.parse(url);
                                          // 如果url的协议 = 预先约定的 js 协议
                                          // 就解析往下解析参数
                                          if ( uri.getScheme().equals("js")) {
                                              // 如果 authority  = 预先约定协议里的 webview，即代表都符合约定的协议
                                              // 所以拦截url,下面JS开始调用Android需要的方法
                                              if (uri.getAuthority().equals("webview")) {

                                                  //步骤3：
                                                  //执行JS所需要调用的逻辑
                                                  //可以在协议上带有参数并传递到Android上

                                                  System.out.println("js调用了Android的方法");
                                                  String ss = uri.getQueryParameter("filename");
                                                  String pathNum = uri.getQueryParameter("pathname");
                                                  if (pathNum!=null) {
                                                      /*HomeFragment homeFragment = HomeFragment.newInstance(pathNum);
                                                      FragmentTransaction transaction = getActivity().getFragmentManager().beginTransaction();
                                                      transaction.replace(R.id.fragment_content, homeFragment);
                                                      transaction.commit();
                                                      MainActivity parentActivity = (MainActivity ) getActivity();
                                                      parentActivity.setBackgroundColorById(R.id.home_btn);*/
                                                  }
                                                  mWebView2.loadUrl("file:///android_asset/"+ss);
                                              }

                                              return true;
                                          }
                                          return super.shouldOverrideUrlLoading(view, url);
                                      }
                                  }
        );
        return view;

    }

    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public static ChangePathFragment newInstance(String path) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_PATH,path);

        ChangePathFragment fragment = new ChangePathFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Destroy");
    }
}
