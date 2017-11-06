package com.imooc.app;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author djl
 * @function
 */

public class Topbar extends RelativeLayout {

    private Button leftButton, rightButton;
    private TextView tvTitle;

    private int leftTextColor;
    private Drawable leftBackfround;
    private String leftText;

    private int rightTextColor;
    private Drawable rightBackfround;
    private String rightText;

    private String title;
    private float titleTextSize;
    private int titleTextColor;

    private LayoutParams leftParams,rightParams,titleParams;


    public Topbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.Topbar);

        leftTextColor = ta.getColor(R.styleable.Topbar_leftTextColor, 0);
        leftBackfround = ta.getDrawable(R.styleable.Topbar_leftBackground);
        leftText = ta.getString(R.styleable.Topbar_leftText);
        rightTextColor = ta.getColor(R.styleable.Topbar_rightTextColor, 0);
        rightBackfround = ta.getDrawable(R.styleable.Topbar_rightBackground);
        rightText = ta.getString(R.styleable.Topbar_rightText);
        title = ta.getString(R.styleable.Topbar_title);
        titleTextSize = ta.getDimension(R.styleable.Topbar_titleTextSize, 0);
        titleTextColor = ta.getColor(R.styleable.Topbar_titleTextColor, 0);

        ta.recycle();

        leftButton = new Button(context);
        rightButton = new Button(context);
        tvTitle = new TextView(context);

        leftButton.setTextColor(leftTextColor);
        leftButton.setText(leftText);
        leftButton.setBackground(leftBackfround);

        rightButton.setTextColor(rightTextColor);
        rightButton.setText(rightText);
        rightButton.setBackground(rightBackfround);

        tvTitle.setText(title);
        tvTitle.setTextColor(titleTextColor);
        tvTitle.setTextSize(titleTextSize);
        tvTitle.setGravity(Gravity.CENTER);

        //setBackgroundColor(0xFFF59563);

        leftParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        leftParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, TRUE);
        addView(leftButton, leftParams);

        rightParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        rightParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, TRUE);
        addView(rightButton, rightParams);

        titleParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        titleParams.addRule(RelativeLayout.CENTER_IN_PARENT, TRUE);
        addView(tvTitle, titleParams);
    }
}

