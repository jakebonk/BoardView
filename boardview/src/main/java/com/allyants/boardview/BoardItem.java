package com.allyants.boardview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by jbonk on 4/19/2017.
 */

public class BoardItem extends LinearLayout {
    int originalWidth = 0;
    public float scale = 1f;
    public BoardItem(Context context) {
        super(context);
    }

    public BoardItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BoardItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        if(getWidth() > 0 && scale != 1f) {
            if(originalWidth == 0){
                originalWidth = getWidth();
            }
            this.setMeasuredDimension((int) (originalWidth * scale), (int) (originalWidth * scale));
        }else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
