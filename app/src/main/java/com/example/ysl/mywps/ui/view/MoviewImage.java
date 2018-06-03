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

/**
 * Created by Administrator on 2018/1/15 0015.
 */
public class MoviewImage extends ImageView {

    private float startX;
    private float startY;
    float moveX;
    float moveY;

    public MoviewImage(Context context) {
        super(context);
    }

    public MoviewImage(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public MoviewImage(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setLocation(int x, int y) {
        this.setFrame(x, y - this.getHeight(), x + this.getWidth(), y);
    }

    // 移动
    public boolean autoMouse(MotionEvent event) {
        boolean rb = false;

        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                this.setLocation((int) event.getX()-this.getWidth()/2, (int) event.getY()+this.getHeight()/2);
                rb = true;
                break;
        }

        return false;

    }


}