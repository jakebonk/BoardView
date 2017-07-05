package com.allyants.boardview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by jbonk on 4/19/2017.
 */

public class BoardItem extends LinearLayout {
    int originalWidth = 0;
    int originalHeight = 0;
    private float scale = 1f;
    public BoardItem(Context context) {
        super(context);
    }

    public BoardItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BoardItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void init(){
//        originalHeight = 0;
//        originalWidth = 0;
    }

    public void setScale(float scale){
        this.scale = scale;
    }

    public float getScale(){
        return scale;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        if(getHeight() > 0 && getWidth() > 0 && scale != 1f) {
            if(originalWidth == 0){
                originalWidth = getWidth();
            }
            if(originalHeight == 0){
                originalHeight = getHeight();
            }
            this.setMeasuredDimension((int) (originalWidth * scale), (int) (originalHeight * scale));
        }else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
