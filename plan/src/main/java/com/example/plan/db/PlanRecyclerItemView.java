package com.example.plan.db;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

public class PlanRecyclerItemView extends ViewGroup {

    public Boolean flag=true;
    private final String TAG="TAG";
    private int[] childWidth=new int[2];
    private float downX;
    private float lastX;
    private int status=1;
    private PlanRecyclerItemViewListener myInterface;
    private Scroller scroller=new Scroller(getContext());

    public PlanRecyclerItemView(Context context) {
        super(context,null,0);
    }

    public PlanRecyclerItemView(Context context, AttributeSet attrs) {
        super(context, attrs,0);
    }

    public PlanRecyclerItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int childCount=getChildCount();
        for(int i=0;i<childCount;i++){
            View childView=getChildAt(i);
            measureChild(childView,widthMeasureSpec,heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount=getChildCount();
        int width=0;
        int preWidth=0;
        for(int i=0;i<childCount;i++){
            View childView=getChildAt(i);
            width=width+childView.getMeasuredWidth();
            childWidth[i]=childView.getMeasuredWidth();
            childView.layout(preWidth,0,width,childView.getMeasuredHeight());
            preWidth=width;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {//问题：有时候在一个滑动事件中只能接收到down和move事件，不能接收到up和cancel事件，因此这里在move中进行滑动判断
        float x=event.getRawX();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                downX=x;
                lastX=x;
                return true;
            case MotionEvent.ACTION_MOVE:
                lastX=x;

                if(flag){
                    if(lastX-downX<-150&&status==1){
                        if(myInterface!=null){
                            myInterface.slideOut();
                        }
                        scroller.startScroll(getScrollX(),0,childWidth[1],0);
                        invalidate();
                        status=2;
                    }else if(lastX-downX>150&&status==2){
                        recover();
                    }
                }

                return true;
            case MotionEvent.ACTION_UP:
                if(Math.abs(lastX-downX)<20&&status==1){
                    myInterface.onClick();
                }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if(scroller.computeScrollOffset()){
            scrollTo(scroller.getCurrX(),scroller.getCurrY());
            invalidate();
        }
    }

    public void recover(){
        if(myInterface!=null){
            myInterface.slideIn();
        }
        scroller.startScroll(getScrollX(),0,-childWidth[1],0);
        status=1;
        invalidate();
    }

    public void setMyItemListener(PlanRecyclerItemViewListener myItemListener){
        myInterface=myItemListener;
    }

}
