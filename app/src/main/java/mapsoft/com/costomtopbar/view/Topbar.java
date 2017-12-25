package mapsoft.com.costomtopbar.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import android.secondbook.com.buttonfragment.R;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;

import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author djl
 * @function
 */

public class Topbar extends RelativeLayout {

    private ImageView wifiButton,gpsButton, rightButton, messageButton;
    private TextView tvTitle;

    private int leftTextColor;
    private Drawable leftBackground;
    private Drawable gpsBackground;
    private Drawable messageBackgeound;
    private String leftText;

    private int rightTextColor;
    private Drawable rightBackground;
    private String rightText;

    private String title;
    private float titleTextSize;
    private int titleTextColor;

    private LayoutParams wifiParams,gpsParamas,rightParams,titleParams,messageParams;

    public topbarClickListener mListener;

    public interface topbarClickListener {
        public void leftClick();
        public void homeClick();

    }

    public void setOnTopbarClickListener(topbarClickListener listener) {
        this.mListener = listener;
    }

    public Topbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.Topbar);

        leftTextColor = ta.getColor(R.styleable.Topbar_leftTextColor, 0);
        leftBackground = ta.getDrawable(R.styleable.Topbar_leftBackground);
        leftText = ta.getString(R.styleable.Topbar_leftText);
        gpsBackground = ta.getDrawable(R.styleable.Topbar_gpsBackground);
        messageBackgeound = ta.getDrawable(R.styleable.Topbar_messageBackground);
        rightTextColor = ta.getColor(R.styleable.Topbar_rightTextColor, 0);
        rightBackground = ta.getDrawable(R.styleable.Topbar_rightBackground);
        rightText = ta.getString(R.styleable.Topbar_rightText);
        title = ta.getString(R.styleable.Topbar_title);
        titleTextSize = ta.getDimension(R.styleable.Topbar_titleTextSize, 0);
        titleTextColor = ta.getColor(R.styleable.Topbar_titleTextColor, 0);

        ta.recycle();

        wifiButton = new ImageView(context);
        gpsButton = new ImageView(context);
        rightButton = new ImageView(context);
        messageButton = new ImageView(context);
        tvTitle = new TextView(context);

        //wifiButton.setTextColor(leftTextColor);
        //wifiButton.setText(leftText);
        wifiButton.setBackground(leftBackground);
        wifiButton.setId(R.id.wifiButton);
        gpsButton.setBackground(gpsBackground);
        gpsButton.setId(R.id.gpsButton);
        messageButton.setBackground(messageBackgeound);

        /*rightButton.setTextColor(rightTextColor);
        rightButton.setText(rightText);*/
        rightButton.setBackground(rightBackground);

        tvTitle.setText(title);
        tvTitle.setTextColor(titleTextColor);
        tvTitle.setTextSize(titleTextSize);
        tvTitle.setGravity(Gravity.CENTER);


        wifiParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        wifiParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, TRUE);
        wifiParams.addRule(RelativeLayout.CENTER_VERTICAL, TRUE);
        addView(wifiButton, wifiParams);

        gpsParamas =  new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        gpsParamas.addRule(RelativeLayout.RIGHT_OF,wifiButton.getId());
        gpsParamas.addRule(RelativeLayout.CENTER_VERTICAL, TRUE);
        gpsParamas.setMargins(10, 0, 0 ,0);
        addView(gpsButton, gpsParamas);

        messageParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        messageParams.addRule(RelativeLayout.RIGHT_OF,gpsButton.getId());
        messageParams.addRule(RelativeLayout.CENTER_VERTICAL, TRUE);
        messageParams.setMargins(10, 0, 0 ,0);
        addView(messageButton, messageParams);

        //gpsButton.setImageBitmap(IconCut.getInstance(context).cutMap(300));
        rightParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        rightParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, TRUE);
        rightParams.addRule(RelativeLayout.CENTER_VERTICAL, TRUE);
        addView(rightButton, rightParams);

        titleParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        titleParams.addRule(RelativeLayout.CENTER_IN_PARENT, TRUE);
        addView(tvTitle, titleParams);

        rightButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.homeClick();
            }
        });

    }
    public void setWifiButton(Bitmap bitmap) {

            wifiButton.setImageBitmap(bitmap);
    }
    public void setWifiButton(int drawble) {

        wifiButton.setImageResource(drawble);
    }
    public void setGpsButton(Bitmap bitmap) {

       gpsButton.setImageBitmap(bitmap);
    }
    public void setGpsButton(int drawble) {

        gpsButton.setImageResource(drawble);
    }
    public void setMessageButton(int drawble) {
        if (messageButton.getVisibility() == View.INVISIBLE)
        {
            messageButton.setVisibility(View.VISIBLE);
            messageButton.setWillNotDraw(true);
        }
        messageButton.setImageResource(drawble);
        setFlickerAnimation(messageButton);
    }

    public void cancleMessageButton() {
        if (messageButton.getVisibility() == View.VISIBLE)
        {
            messageButton.setWillNotDraw(false);
            setFlickerAnimationNull(messageButton);
            messageButton.setVisibility(View.INVISIBLE);
        }
    }
    public void setHomeButton(Bitmap bitmap) {

        rightButton.setImageBitmap(bitmap);
    }
    public void updateTime(String time) {
        tvTitle.setText(time);
    }

    private void setFlickerAnimation(ImageView iv_chat_head) {
        final Animation animation = new AlphaAnimation(1, 0);
        animation.setDuration(750);//闪烁时间间隔
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(Animation.REVERSE);
        iv_chat_head.setAnimation(animation);
    }
    private void setFlickerAnimationNull(ImageView iv_chat_head) {

        iv_chat_head.setAnimation(null);
    }

}

