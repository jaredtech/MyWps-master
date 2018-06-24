package com.example.ysl.mywps.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.widget.LinearLayout;

public class DragViewGroup extends LinearLayout {

    public DragViewGroup(Context context) {
        this(context, null);
    }

    public DragViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    //定位
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        //可以在这里确定这个viewGroup的：宽 = r-l.高 = b - t
    }
    public void setLocation(int x, int y) {
        this.layout(x, y - this.getHeight(), x + this.getWidth(), y);
    }

    // 移动
    public boolean autoMouse(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                this.setLocation((int) event.getX() - this.getWidth() / 2, (int) event.getY() + this.getHeight() / 2);
                break;
        }

        return false;

    }
}
