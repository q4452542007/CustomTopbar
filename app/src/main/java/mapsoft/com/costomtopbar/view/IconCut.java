package mapsoft.com.costomtopbar.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.secondbook.com.buttonfragment.R;


/**
 * @author djl
 * @function
 */

public class IconCut {

    private Context mContext;

    private static IconCut mIconCut;

    public IconCut(Context context) {
        this.mContext = context;
    }

    public static IconCut getInstance (Context context) {
        if (mIconCut == null) {
            mIconCut = new IconCut(context);
        }
        return mIconCut;
    }

    public  Bitmap cutWifi(Boolean flag) {

        if (flag) {
            return Bitmap.createBitmap(BitmapCut.ReadBitmapById(mContext, R.drawable.menu), 0, 0, 50, 50, null,
                    false);
        } else {
            return Bitmap.createBitmap(BitmapCut.ReadBitmapById(mContext, R.drawable.menu), 250, 0, 50, 50, null,
                    false);
        }

    }

    public  Bitmap cutGps(Boolean flag) {

        if (flag) {
            return Bitmap.createBitmap(BitmapCut.ReadBitmapById(mContext, R.drawable.menu), 300, 0, 50, 50, null,
                    false);
        } else {
            return Bitmap.createBitmap(BitmapCut.ReadBitmapById(mContext, R.drawable.menu), 350, 0, 50, 50, null,
                    false);
        }
    }

    public Bitmap cutHomeIcon() {
        return Bitmap.createBitmap(BitmapCut.ReadBitmapById(mContext, R.drawable.soundc), 720, 0, 50, 50, null,
                false);
    }

    public Bitmap cutLoginUser(){
        Bitmap bmp = Bitmap.createBitmap(BitmapCut.ReadBitmapById(mContext, R.drawable.login), 100, 150, 250, 60, null,
                false);

        return bmp;
    }
    public Bitmap cutLoginPwd(){
        Bitmap bmp = Bitmap.createBitmap(BitmapCut.ReadBitmapById(mContext, R.drawable.login), 100, 220, 250, 50, null,
                false);

        return bmp;
    }
}

