package mapsoft.com.costomtopbar.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.secondbook.com.buttonfragment.R;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author djl
 * @function
 */

public class Topbar extends RelativeLayout {

    private Button wifiButton, gpsButton, rightButton;
    private TextView tvTitle;

    private int leftTextColor;
    private Drawable leftBackground;
    private Drawable gpsBackground;
    private String leftText;

    private int rightTextColor;
    private Drawable rightBackground;
    private String rightText;

    private String title;
    private float titleTextSize;
    private int titleTextColor;

    private LayoutParams wifiParams,gpsParamas,rightParams,titleParams;


    public Topbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.Topbar);

        leftTextColor = ta.getColor(R.styleable.Topbar_leftTextColor, 0);
        leftBackground = ta.getDrawable(R.styleable.Topbar_leftBackground);
        leftText = ta.getString(R.styleable.Topbar_leftText);
        gpsBackground = ta.getDrawable(R.styleable.Topbar_gpsBackground);
        rightTextColor = ta.getColor(R.styleable.Topbar_rightTextColor, 0);
        rightBackground = ta.getDrawable(R.styleable.Topbar_rightBackground);
        rightText = ta.getString(R.styleable.Topbar_rightText);
        title = ta.getString(R.styleable.Topbar_title);
        titleTextSize = ta.getDimension(R.styleable.Topbar_titleTextSize, 0);
        titleTextColor = ta.getColor(R.styleable.Topbar_titleTextColor, 0);

        ta.recycle();

        wifiButton = new Button(context);
        gpsButton = new Button(context);
        rightButton = new Button(context);
        tvTitle = new TextView(context);

        wifiButton.setTextColor(leftTextColor);
        wifiButton.setText(leftText);
        wifiButton.setBackground(leftBackground);
        wifiButton.setId(R.id.wifiButton);

        gpsButton.setBackground(gpsBackground);


        rightButton.setTextColor(rightTextColor);
        rightButton.setText(rightText);
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

        rightParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        rightParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, TRUE);
        rightParams.addRule(RelativeLayout.CENTER_VERTICAL, TRUE);
        addView(rightButton, rightParams);

        titleParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        titleParams.addRule(RelativeLayout.CENTER_IN_PARENT, TRUE);
        addView(tvTitle, titleParams);

    }
    public void setWifiButton(boolean flag) {
        if (flag) {
            wifiButton.setBackgroundResource(R.drawable.wifi);
        } else {
            wifiButton.setBackgroundResource(R.drawable.nowifi);
        }
    }
    public void setGpsButton(boolean flag) {
        if (flag) {
            gpsButton.setBackgroundResource(R.drawable.gps);
        } else {
            gpsButton.setBackgroundResource(R.drawable.nogps);
        }
    }
    public void updateTime(String time) {
        tvTitle.setText(time);
    }
}

