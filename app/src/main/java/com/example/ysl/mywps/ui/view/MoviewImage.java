package com.example.ysl.mywps.ui.view;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.example.ysl.mywps.utils.CommonFun;
import com.example.ysl.mywps.utils.CommonUtil;

/**
 * Created by Administrator on 2018/1/15 0015.
 */
public class MoviewImage extends android.support.v7.widget.AppCompatImageView {

    private Context mContext;

    public MoviewImage(Context context) {
        super(context);
        this.mContext = context;
    }

    public MoviewImage(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        this.mContext = context;
    }

    public MoviewImage(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
    }

//    public void setLocation(int x, int y) {
//        this.setFrame(x, y - this.getHeight(), x + this.getWidth(), y);
//    }
//
//    // 移动
//    public boolean autoMouse(MotionEvent event) {
//
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_MOVE:
//                this.setLocation((int) event.getX() - this.getWidth() / 2, (int) event.getY() + this.getHeight() / 2);
//                break;
//        }
//
//        return false;
//
//    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHight = MeasureSpec.getSize(heightMeasureSpec);
        if (modeHeight == MeasureSpec.AT_MOST) {
            int height = CommonFun.dip2px(mContext, 65);
            int[] screenWH = CommonUtil.getScreenWH(mContext);
            double v = screenWH[0] / (screenWH[1] * 0.6);
            int width = (int) v * height;
            setMeasuredDimension(width, height);
        }
    }
}