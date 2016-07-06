package com.singit.shays.singit.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by shays on 06/06/2016.
 * MyGridView is a custom GridView component.
 */
public class MyGridView extends GridView {
    /**
     * C'tor
     * @param context
     */
    public MyGridView(Context context) {
        super(context);
    }

    /**
     * C'tor
     * @param context
     * @param attrs
     */
    public MyGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * C'tor
     * @param context
     * @param attrs
     * @param defStyle
     */
    public MyGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightSpec;

        if (getLayoutParams().height == LayoutParams.WRAP_CONTENT) {
            heightSpec = MeasureSpec.makeMeasureSpec(
                    107374821 , MeasureSpec.AT_MOST);
        }
        else {
            heightSpec = heightMeasureSpec;
        }

        super.onMeasure(widthMeasureSpec, heightSpec);
    }
}
