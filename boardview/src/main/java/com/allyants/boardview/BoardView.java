package com.allyants.boardview;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Scroller;

import java.util.ArrayList;

/**
 * Created by jbonk on 4/17/2017.
 */

public class BoardView extends FrameLayout {

    private static final float GLOBAL_SCALE = 0.6f;
    private static final int SCROLL_ANIMATION_DURATION = 325;
    private boolean mCellIsMobile = false;
    private BitmapDrawable mHoverCell;
    private Rect mHoverCellCurrentBounds;
    private Rect mHoverCellOriginalBounds;

    private HorizontalScrollView mRootLayout;
    private LinearLayout mParentLayout;

    private int originalPosition = -1;
    private int originalItemPosition = -1;

    private DoneListener mDoneCallback = new DoneListener() {
        @Override
        public void onDone() {

        }
    };

    private HeaderClickListener headerClickListener = new HeaderClickListener() {
        @Override
        public void onClick(View v, int column_pos) {

        }
    };

    private ItemClickListener itemClickListener = new ItemClickListener() {
        @Override
        public void onClick(View v, int column_pos, int item_pos) {

        }
    };

    private DragColumnStartCallback mDragColumnStartCallback = new DragColumnStartCallback() {
        @Override
        public void startDrag(View itemView, int originalPosition) {

        }

        @Override
        public void changedPosition(View itemView, int originalPosition, int newPosition) {

        }

        @Override
        public void endDrag(View itemView, int originalPosition, int newPosition) {

        }
    };

    private DragItemStartCallback mDragItemStartCallback = new DragItemStartCallback() {
        @Override
        public void startDrag(View itemView, int originalPosition,int originalColumn) {

        }

        @Override
        public void changedPosition(View itemView, int originalPosition,int originalColumn, int newPosition, int newColumn) {

        }

        @Override
        public void endDrag(View itemView, int originalPosition,int originalColumn, int newPosition, int newColumn) {

        }
    };

    public boolean constWidth = true;

    private final int LINE_THICKNESS = 15;

    private boolean can_scroll = false;
    private boolean created = false;

    private int mLastEventX = -1;
    private int mLastEventY = -1;
    private Scroller mScroller;

    public interface DragColumnStartCallback{
        void startDrag(View itemView, int originalPosition);
        void changedPosition(View itemView, int originalPosition, int newPosition);
        void endDrag(View itemView, int originalPosition, int newPosition);
    }

    public interface DragItemStartCallback{
        void startDrag(View itemView, int originalPosition,int originalColumn);
        void changedPosition(View itemView, int originalPosition,int originalColumn, int newPosition, int newColumn);
        void endDrag(View itemView, int originalPosition,int originalColumn, int newPosition, int newColumn);
    }

    public interface HeaderClickListener{
        void onClick(View v,int column_pos);
    }

    public interface ItemClickListener{
        void onClick(View v,int column_pos,int item_pos);
    }

    public void setOnHeaderClickListener(HeaderClickListener headerClickListener){
        this.headerClickListener = headerClickListener;
    }

    public void setOnItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    public void setOnDragColumnListener(DragColumnStartCallback dragStartCallback){
        mDragColumnStartCallback = dragStartCallback;
    }

    public void setOnDragItemListener(DragItemStartCallback dragStartCallback){
        mDragItemStartCallback = dragStartCallback;
    }

    public void setOnDoneListener(DoneListener onDoneListener){
        mDoneCallback = onDoneListener;
    }

    long last_swap = System.currentTimeMillis();
    long last_swap_item = System.currentTimeMillis();

    final long ANIM_TIME = 300;
    long mDelaySwap = 400;
    long mDelaySwapItem = 400;

    private int mLastSwap = -1;
    private int mDownY = -1;
    private int mDownX = -1;

    private boolean mSwapped = false;
    private boolean canDragHorizontal = true;
    private boolean canDragVertical = true;

    private boolean mCellSubIsMobile = false;

    private int mTotalOffsetX = 0;
    private int mTotalOffsetY = 0;

    BoardAdapter boardAdapter;

    private View mobileView;

    public BoardView(Context context) {
        super(context);
    }

    public BoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BoardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public interface DoneListener {
        void onDone();
    }

    public void setAdapter(BoardAdapter boardAdapter){
        this.boardAdapter = boardAdapter;
        mParentLayout.removeAllViews();
        boardAdapter.createColumns();
        for(int i = 0;i < boardAdapter.columns.size();i++){
            BoardAdapter.Column column = boardAdapter.columns.get(i);
            addColumnList(column.header,column.views,null);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mRootLayout = new HorizontalScrollView(getContext());
        mRootLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
        mParentLayout = new LinearLayout(getContext());
        mParentLayout.setOrientation(LinearLayout.HORIZONTAL);
        mScroller = new Scroller(mRootLayout.getContext(), new DecelerateInterpolator(1.2f));
        mParentLayout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
        mRootLayout.addView(mParentLayout);
        addView(mRootLayout);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if(!created){
            created = true;
            mDoneCallback.onDone();
        }
        if(mHoverCell != null){
            mHoverCell.draw(canvas);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        boolean colValue = handleColumnDragEvent(event);
        return colValue || super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean colValue = handleColumnDragEvent(event);
        return colValue || super.onTouchEvent(event);
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public void scrollToColumn(int column,boolean animate){
        if(column >= 0) {
            View childView = mParentLayout.getChildAt(column);
            if(childView != null) {
                int newX = childView.getLeft() - (int) (((getMeasuredWidth() - childView.getMeasuredWidth()) / 2));
                if (animate) {
                    mRootLayout.smoothScrollTo(newX, 0);
                } else {
                    mRootLayout.scrollTo(newX, 0);
                }
            }
        }
    }

    public boolean handleColumnDragEvent(MotionEvent event){
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mDownX = (int)event.getX();
                mDownY = (int)event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                if(mDownY == -1){
                    mDownY = (int)event.getRawY();

                }
                if(mDownX == -1){
                    mDownX = (int)event.getX();
                }

                mLastEventX = (int) event.getX();
                mLastEventY = (int) event.getRawY();
                int deltaX = mLastEventX - mDownX;
                int deltaY = mLastEventY - mDownY;

                if(mCellSubIsMobile){
                    int offsetX = 0;
                    if(canDragHorizontal){
                        offsetX =  deltaX;
                    }
                    int offsetY = mHoverCellOriginalBounds.top;
                    if(canDragVertical){
                        offsetY = mHoverCellOriginalBounds.top + deltaY;
                    }
                    mHoverCellCurrentBounds.offsetTo(offsetX,
                            offsetY);
                    mHoverCell.setBounds(rotatedBounds(mHoverCellCurrentBounds,0.0523599f));
                    invalidate();
                    handleItemSwitchHorizontal();
                    return true;
                }else if (mCellIsMobile) {
                    int offsetX = 0;
                    if(canDragHorizontal){
                        offsetX =  deltaX;
                    }
                    int offsetY = mHoverCellOriginalBounds.top;
                    if(canDragVertical){
                        offsetY = mHoverCellOriginalBounds.top + deltaY + mTotalOffsetY;
                    }
                    mHoverCellCurrentBounds.offsetTo(offsetX,
                            offsetY);
                    mHoverCell.setBounds(rotatedBounds(mHoverCellCurrentBounds,0.0523599f));
                    invalidate();
                    handleColumnSwitchHorizontal();

                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                touchEventsCancelled();
                break;
            case MotionEvent.ACTION_CANCEL:
                touchEventsCancelled();
                break;
            case MotionEvent.ACTION_POINTER_UP:
                /* If a multitouch event took place and the original touch dictating
                 * the movement of the hover cell has ended, then the dragging event
                 * ends and the hover cell is animated to its corresponding position
                 * in the listview. */
//                pointerIndex = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >>
//                        MotionEvent.ACTION_POINTER_INDEX_SHIFT;
//                final int pointerId = event.getPointerId(pointerIndex);
//                if (pointerId == mActivePointerId) {
//
//                }
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public void computeScroll() {
        if (!mScroller.isFinished() && mScroller.computeScrollOffset()) {
            int x = mScroller.getCurrX();
            int y = mScroller.getCurrY();
            if (getScrollX() != x || getScrollY() != y) {
                scrollTo(x, y);
            }

            ViewCompat.postInvalidateOnAnimation(this);
        } else {
            super.computeScroll();
        }
    }

    private void handleItemSwitchHorizontal(){
        int itemPos = ((LinearLayout)(mobileView.getParent())).indexOfChild(mobileView);
        View aboveView = ((LinearLayout)(mobileView.getParent())).getChildAt(itemPos - 1);
        View belowView = ((LinearLayout)(mobileView.getParent())).getChildAt(itemPos + 1);
        //swapping above
        int[] location = new int[2];
        mobileView.getLocationOnScreen(location);
        int[] parentLocation = new int[2];
        ScrollView parent = ((ScrollView)((LinearLayout)mobileView.getParent()).getParent());
        parent.getLocationOnScreen(parentLocation);
        if(location[1]-mobileView.getHeight() < parentLocation[1]){
            parent.smoothScrollBy(0,-10);
        }
        if(location[1]+mobileView.getHeight()+mobileView.getHeight() > parentLocation[1]+parent.getHeight()){
            parent.smoothScrollBy(0,10);
        }
        if(aboveView != null){
            int[] locationAbove = new int[2];
            aboveView.getLocationInWindow(locationAbove);

            if(locationAbove[1]+aboveView.getHeight() > mLastEventY){
                switchItemFromPosition(-1,mobileView);
            }
        }
        //swapping below
        if(belowView != null){
            int[] locationBelow = new int[2];
            belowView.getLocationOnScreen(locationBelow);
            if(locationBelow[1] < mLastEventY){
                switchItemFromPosition(1,mobileView);
            }
        }
        int columnPos = mParentLayout.indexOfChild((View)(mobileView.getParent().getParent().getParent()));
        View leftView = mParentLayout.getChildAt(columnPos - 1);
        View currentView = mParentLayout.getChildAt(columnPos);
        View rightView = mParentLayout.getChildAt(columnPos + 1);

        if(leftView != null){
            int[] locationLeft = new int[2];
            leftView.getLocationOnScreen(locationLeft);
            if (locationLeft[0] + leftView.getWidth() > mLastEventX) {
                int pos = ((LinearLayout)mobileView.getParent()).indexOfChild(mobileView);
                if(last_swap_item <= System.currentTimeMillis() - mDelaySwapItem) {
                    last_swap_item = System.currentTimeMillis();
                    if(((LinearLayout)mobileView.getParent()) != null) {
                        ((LinearLayout) mobileView.getParent()).removeViewAt(pos);
                        ((LinearLayout)((ScrollView)((ViewGroup)leftView).getChildAt(1)).getChildAt(0)).addView(mobileView);
                        scrollToColumn(columnPos-1,true);
                        int newItemPos = ((LinearLayout)((ViewGroup)leftView).getChildAt(0)).indexOfChild(mobileView)-1;
                        int newColumnPos = ((LinearLayout)mobileView.getParent().getParent().getParent()).indexOfChild((View)(mobileView.getParent().getParent()));
                        mDragItemStartCallback.changedPosition(mobileView,originalItemPosition,originalPosition,newItemPos,newColumnPos);
                    }
                }
            }
        }
        if(rightView != null){
            int[] locationRight = new int[2];
            rightView.getLocationOnScreen(locationRight);
            if (locationRight[0] < mLastEventX) {
                int pos = ((LinearLayout)mobileView.getParent()).indexOfChild(mobileView);
                if(last_swap_item <= System.currentTimeMillis() - mDelaySwapItem) {
                    last_swap_item = System.currentTimeMillis();
                    if(((LinearLayout)mobileView.getParent()) != null) {
                        ((LinearLayout) mobileView.getParent()).removeViewAt(pos);
                        ((LinearLayout)((ScrollView)((ViewGroup)rightView).getChildAt(1)).getChildAt(0)).addView(mobileView);
                        scrollToColumn(columnPos+1,true);
                        int newItemPos = ((LinearLayout)((ViewGroup)rightView).getChildAt(0)).indexOfChild(mobileView)-1;
                        int newColumnPos = ((LinearLayout)mobileView.getParent().getParent().getParent()).indexOfChild((View)(mobileView.getParent().getParent()));
                        mDragItemStartCallback.changedPosition(mobileView,originalItemPosition,originalPosition,newItemPos,newColumnPos);
                    }
                }
            }
        }

    }

    private void switchItemFromPosition(int change,View view){
        LinearLayout parentLayout = (LinearLayout)(view.getParent());
        int columnPos = parentLayout.indexOfChild(view);
        if(columnPos+change >=  0 && columnPos+change < parentLayout.getChildCount()) {
            parentLayout.removeView(view);
            parentLayout.addView(view, columnPos + change);
            if(mDragItemStartCallback != null){
                int newPos = parentLayout.indexOfChild(view);
                last_swap = System.currentTimeMillis();
                mLastSwap = newPos;
                int newColumnPos = ((LinearLayout)mobileView.getParent().getParent().getParent().getParent()).indexOfChild((View)(mobileView.getParent().getParent().getParent()));
                mDragItemStartCallback.changedPosition(view,originalItemPosition,originalPosition,newPos,newColumnPos);
            }
        }
    }

    private void handleColumnSwitchHorizontal(){
        if(can_scroll && last_swap <= System.currentTimeMillis()-mDelaySwap) {
            int columnPos = mParentLayout.indexOfChild(mobileView);
            View leftView = mParentLayout.getChildAt(columnPos - 1);
            View rightView = mParentLayout.getChildAt(columnPos + 1);

            int[] locationRight = new int[2];
            if (rightView != null) {
                rightView.getLocationOnScreen(locationRight);
                if (locationRight[0] < mLastEventX) {
                    //Scroll to the right
                    switchColumnFromPosition(1,mobileView);
                    if (locationRight[0] + (rightView.getWidth() / 2) < mLastEventX) {

                    }
                }
            }

            int[] locationLeft = new int[2];
            if (leftView != null) {
                leftView.getLocationOnScreen(locationLeft);
                if (locationLeft[0] + leftView.getWidth() > mLastEventX) {
                    //Scroll to the right
                    switchColumnFromPosition(-1,mobileView);
                    //mRootLayout.scrollBy(-1 * 2, 0);
                    if (locationLeft[0] + (leftView.getWidth() / 2) > mLastEventX) {

                    }
                }
            }
        }
    }

    private void switchColumnFromPosition(int change,View view){
        int columnPos = mParentLayout.indexOfChild(view);
        if(columnPos+change >=  0 && last_swap <= System.currentTimeMillis()-mDelaySwap) {
            mParentLayout.removeView(view);
            mParentLayout.addView(view, columnPos + change);
            if(mDragColumnStartCallback != null){
                int newPos = mParentLayout.indexOfChild(view);
                last_swap = System.currentTimeMillis();
                mLastSwap = newPos;
                Handler handlerTimer = new Handler();
                handlerTimer.postDelayed(new Runnable(){
                    public void run() {
                        scrollToColumn(mLastSwap,true);
                    }}, 0);
                mDragColumnStartCallback.changedPosition(((LinearLayout)view).getChildAt(0),originalPosition,newPos);
            }
        }
    }

    private void touchEventsCancelled() {
        if(mCellSubIsMobile){
            mobileView.setVisibility(VISIBLE);
            mHoverCell = null;
            invalidate();
            if(mDragItemStartCallback != null){
                LinearLayout parentLayout = (LinearLayout)(mobileView.getParent().getParent().getParent().getParent());
                int columnPos = parentLayout.indexOfChild((View)(mobileView.getParent().getParent().getParent()));
                //Subtract one because the first view is the header
                int pos = ((LinearLayout)mobileView.getParent()).indexOfChild(mobileView);
                View tmpView = boardAdapter.columns.get(originalPosition).views.get(originalItemPosition);
                boardAdapter.columns.get(originalPosition).views.remove(originalItemPosition);
                boardAdapter.columns.get(columnPos).views.add(pos,tmpView);
                Object tmpObject = boardAdapter.columns.get(originalPosition).objects.get(originalItemPosition);
                boardAdapter.columns.get(originalPosition).objects.remove(originalItemPosition);
                boardAdapter.columns.get(columnPos).objects.add(pos,tmpObject);
                mDragItemStartCallback.endDrag(mobileView,originalPosition,originalItemPosition,pos,columnPos);
            }
        }else if(mCellIsMobile){
            for(int i = 0;i < mParentLayout.getChildCount();i++){
                BoardItem parentView = (BoardItem)mParentLayout.getChildAt(i);//Gets the parent layout
                for(int j = 0;j < parentView.getChildCount();j++) {
                    View childView = ((LinearLayout) parentView).getChildAt(j);
                    scrollToColumn(originalPosition, true);
                    scaleView(childView, parentView, GLOBAL_SCALE, 1f);
                }
            }
            mobileView.setVisibility(VISIBLE);
            mHoverCell = null;
            invalidate();
            if(mDragColumnStartCallback != null){
                int columnPos = mParentLayout.indexOfChild(mobileView);
                scrollToColumn(columnPos,true);
                BoardAdapter.Column column = boardAdapter.columns.get(originalPosition);
                boardAdapter.columns.remove(originalPosition);
                boardAdapter.columns.add(columnPos,column);
                mDragColumnStartCallback.endDrag(((LinearLayout)mobileView).getChildAt(0),originalPosition,columnPos);
            }
        }

        mDownX = -1;
        mDownY = -1;
        mCellSubIsMobile = false;
        mCellIsMobile = false;
    }

    Handler handler = new Handler();

    public void scaleView(final View v, final BoardItem parent, final float startScale, final float endScale) {
        final Animation anim = new ScaleAnimation(
                startScale, endScale, // Start and end values for the X axis scaling
                startScale, endScale, // Start and end values for the Y axis scaling
                Animation.RELATIVE_TO_SELF,0f, // Pivot point of X scaling
                Animation.RELATIVE_TO_SELF, 0f); // Pivot point of Y scaling
        anim.setFillAfter(true); // Needed to keep the result of the animation
        anim.setFillBefore(false);
        anim.setFillEnabled(true);
        anim.setDuration(ANIM_TIME);
        v.startAnimation(anim);
        final long startTime = System.currentTimeMillis();
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                handler.post(createRunnable(parent,startTime,startScale,endScale));
                can_scroll = false;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                parent.scale = endScale;
                scrollToColumn(mLastSwap,true);
                parent.requestLayout();
                parent.invalidate();
                can_scroll = true;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private Runnable createRunnable(final BoardItem parent, final long startTime, final float startScale, final float endScale){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                long time = System.currentTimeMillis()-startTime;
                float scale_time = time/(float)ANIM_TIME;
                if(scale_time > 1){
                    scale_time = 1;
                }
                scrollToColumn(mLastSwap,true);
                parent.scale = startScale + (endScale - startScale)*scale_time;
                parent.requestLayout();
                parent.invalidate();
                if(scale_time != 1) {
                    handler.postDelayed(this,10);
                }
            }
        };
        return runnable;
    }

    private void removeParent(View view){
        ViewGroup viewGroup = ((ViewGroup)view.getParent());
        if(viewGroup != null){
            viewGroup.removeView(view);
        }
    }

    private void addColumnList(@Nullable View header, ArrayList<View> items, @Nullable View footer){
        final BoardItem parent_layout = new BoardItem(getContext());
        final LinearLayout layout = new LinearLayout(getContext());
        final ScrollView scroll_view = new ScrollView(getContext());
        final LinearLayout layout_children = new LinearLayout(getContext());
        layout_children.setOrientation(LinearLayout.VERTICAL);
        layout.setOrientation(LinearLayout.VERTICAL);
        parent_layout.setOrientation(LinearLayout.VERTICAL);
        if(constWidth) {
            int margin = calculatePixelFromDensity(5);
            LayoutParams params = new LayoutParams(calculatePixelFromDensity(240), LayoutParams.WRAP_CONTENT);
            params.setMargins(margin,margin,margin,margin);
            layout.setLayoutParams(params);
            parent_layout.setLayoutParams(params);
        }else {
            int margin = calculatePixelFromDensity(5);
            LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            params.setMargins(margin,margin,margin,margin);
            layout.setLayoutParams(params);
            parent_layout.setLayoutParams(params);
        }
        parent_layout.addView(layout);
        if(header != null){
            removeParent(header);
            layout.addView(header);
            header.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = mParentLayout.indexOfChild(parent_layout);
                    scrollToColumn(pos,true);
                    headerClickListener.onClick(v,pos);
                }
            });
            header.setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mDragColumnStartCallback == null) {
                        return false;
                    }
                    originalPosition = mParentLayout.indexOfChild(parent_layout);
                    mDragColumnStartCallback.startDrag(layout,originalPosition);
                    mLastSwap = originalPosition;
                    for(int i = 0;i < mParentLayout.getChildCount();i++){
                        BoardItem parentView = (BoardItem)mParentLayout.getChildAt(i);//Gets the parent layout
                        for(int j = 0;j < parentView.getChildCount();j++) {
                            View childView = ((LinearLayout) parentView).getChildAt(j);
                            scrollToColumn(originalPosition, true);
                            scaleView(childView, parentView, 1f, GLOBAL_SCALE);
                        }
                    }

                    scrollToColumn(originalPosition,false);
                    mCellIsMobile = true;
                    mobileView = (View)(parent_layout);
                    mHoverCell = getAndAddHoverView(mobileView,GLOBAL_SCALE);
                    mobileView.setVisibility(INVISIBLE);
                    return false;
                }
            });
        }
        if(items.size() > 0){
            parent_layout.addView(scroll_view);
            scroll_view.addView(layout_children);
            for(int i = 0;i < items.size();i++){
                final View view = items.get(i);
                removeParent(view);
                layout_children.addView(view);
                final int finalI = i;
                view.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = mParentLayout.indexOfChild(parent_layout);
                        itemClickListener.onClick(v,pos, finalI);
                    }
                });
                view.setOnLongClickListener(new OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if (mDragItemStartCallback == null) {
                            return false;
                        }
                        originalPosition = mParentLayout.indexOfChild(parent_layout);
                        originalItemPosition = ((LinearLayout)view.getParent()).indexOfChild(view);
                        mDragItemStartCallback.startDrag(view,originalPosition,originalItemPosition);
                        mCellSubIsMobile = true;
                        mobileView = (View)(view);
                        mHoverCell = getAndAddHoverView(mobileView,1);
                        mobileView.setVisibility(INVISIBLE);
                        return false;
                    }
                });
            }
        }
        if(footer != null) {
            removeParent(footer);
            layout.addView(footer);
        }
        mParentLayout.addView(parent_layout);
    }

    private int calculatePixelFromDensity(float dp){
        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        float fpixels = metrics.density * dp;
        int pixels = (int) (fpixels + 0.5f);
        return pixels;
    }

    private BitmapDrawable getAndAddHoverView(View v, float scale){
        int w = v.getWidth();
        int h = v.getHeight();
        int top = v.getTop();
        int left = v.getLeft();

        Bitmap b = getBitmapWithBorder(v,scale);
        BitmapDrawable drawable = new BitmapDrawable(getResources(),b);
        mHoverCellOriginalBounds = new Rect(left,top,left+w,top+h);
        mHoverCellCurrentBounds = new Rect(mHoverCellOriginalBounds);
        drawable.setBounds(mHoverCellCurrentBounds);
        return drawable;
    }

    private Bitmap getBitmapWithBorder(View v, float scale) {
        Bitmap bitmap = getBitmapFromView(v,0);
        Bitmap b = getBitmapFromView(v,1);
        Canvas can = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setAlpha(150);
        can.scale(scale,scale,mDownX,mDownY);
        can.rotate(3);
        can.drawBitmap(b,0,0,paint);
        return bitmap;
    }

    private Bitmap getBitmapFromView(View v, float scale){
        double radians = 0.0523599f;
        double s = Math.abs(Math.sin(radians));
        double c = Math.abs(Math.cos(radians));
        int width = (int)(v.getHeight()*s + v.getWidth()*c);
        int height = (int)(v.getWidth()*s + v.getHeight()*c);
        Bitmap bitmap = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.scale(scale,scale);
        v.draw(canvas);
        return bitmap;
    }

    private Rect rotatedBounds(Rect tmp,double radians){
        double s = Math.abs(Math.sin(radians));
        double c = Math.abs(Math.cos(radians));
        int width = (int)(tmp.height()*s + tmp.width()*c);
        int height = (int)(tmp.width()*s + tmp.height()*c);

        return new Rect(tmp.left,tmp.top,tmp.left+width,tmp.top+height);
    }

}
