package youdu.com.imoocsdk.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;


/**
 * @author djl
 * @function    初始化UniversalImageLoader,并加载网络图片
 */

public class ImageLoaderManager {

    private static final int THREAD_COUNT = 4;  //标明我们UIL最多可以有几条线程
    private static final int PROPRITY = 2;  //标明我们图片加载优先级
    private static final int DISK_CHACHE_SIZE = 50 * 1024;  //标明UIL最多可以加载的图片
    private static final int CONNECT_TIME_OUT = 5 * 1000;  //连接超时时间
    private static final int READ_TIME_OUT = 30 * 1000;  //读取超时时间

    private static ImageLoader mImageLoader = null;
    private static ImageLoaderManager mIstance = null;

    public static ImageLoaderManager getInstance(Context context) {

        if (mIstance == null) {
            synchronized (ImageLoaderManager.class) {
                if (mIstance == null) {

                    mIstance = new ImageLoaderManager(context);
                }
            }
        }
        return mIstance;
    }

    /**
     * 单例模式的私有构造方法
     *
     * @param context
     */
    public ImageLoaderManager(Context context) {

        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(context)
                .threadPoolSize(THREAD_COUNT)   //配置图片下载线程的最大数量
                .threadPriority(Thread.NORM_PRIORITY - PROPRITY)
                .denyCacheImageMultipleSizesInMemory()  //防止缓存多套尺寸图片
                .memoryCache(new WeakMemoryCache())     //使用弱引用内存缓存 系统会在内存不足的时候回收图片
                .diskCacheSize(DISK_CHACHE_SIZE)        //分配硬盘缓存大小
                .diskCacheFileNameGenerator(new Md5FileNameGenerator()) //使用MD5命名文件
                .tasksProcessingOrder(QueueProcessingType.LIFO) //图片下载顺序
                .defaultDisplayImageOptions(getDefaultOptions())  //默认的图片加载Option
                .imageDownloader(new BaseImageDownloader(context, CONNECT_TIME_OUT, READ_TIME_OUT)) //设置图片下载器
                .writeDebugLogs() //debug模式会输出日志
                .build();

        ImageLoader.getInstance().init(configuration);
        mImageLoader = ImageLoader.getInstance();
    }

    /**
     *  实现默认的Options
     * @return
     */
    private DisplayImageOptions getDefaultOptions() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(null)     //在我们图片地址为空的时候加载的图片
                .showImageOnFail(null)          //图片下载失败的时候显示的图片
                .cacheInMemory(true)        //设置图片可以缓存在内存
                .cacheOnDisk(true)          //设置图片可以缓存在硬盘
                .bitmapConfig(Bitmap.Config.RGB_565)    //设置图片的解码类型
                .build();

        return options;
    }

    /**
     * 加载图片API
     * @param imageView
     * @param url
     * @param options
     * @param listener
     */

    public void displayImage(ImageView imageView, String url, DisplayImageOptions options, ImageLoadingListener listener){

        if (mImageLoader != null) {

            mImageLoader.displayImage(url, imageView, options, listener);
        }
    }

    public void displayImage(ImageView imageView, String url, ImageLoadingListener listener) {

        displayImage(imageView, url, null, listener);
    }
    public void displayImage(ImageView imageView, String url) {
        displayImage(imageView, url, null);
    }



}

