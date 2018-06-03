package com.example.ysl.mywps.ui.view;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/12/22 0022.
 */

public class IconTextView extends TextView {
  private   Typeface iconfont = null;
    public IconTextView(Context context) {
        super(context);
        initTextivew(context);
    }

    public IconTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initTextivew(context);
    }

    private void initTextivew(Context context) {

      iconfont   = Typeface.createFromAsset(context.getAssets(), "iconfont/iconfont.ttf");
            setTypeface(iconfont);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);


    }
}
