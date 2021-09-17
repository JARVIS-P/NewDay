package com.example.more.db;

import android.animation.FloatEvaluator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

public class TinderCardView extends CardView{

    private final String TAG="TAG";
    private float downX;
    private float downY;
    private float newX;
    private float newY;
    private float dx;
    private float dy;
    private float rootX;
    private float rootY;
    private float sum;
    private CardViewInterface myInterface;

    public TinderCardView(@NonNull Context context) {
        super(context,null,0);
    }

    public TinderCardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs,0);
    }

    public TinderCardView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setListener(CardViewInterface in){
        myInterface=in;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //动画本身并不会改变控件的原本位置因此在滑动(图片跟随)时this.getX会变化而event.getX并不会发生变化
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                rootX=this.getLeft();
                rootY=this.getTop();
                downX=event.getX();
                downY=event.getY();
                this.clearAnimation();
                return true;
            case MotionEvent.ACTION_MOVE:
                newX=event.getX();
                newY=event.getY();
                dx=newX-downX;
                dy=newY-downY;

                float rotation=(getX()-getWidth()/3.5f)*0.05f;//根据移动距离计算旋转角度
                sum=rotation;

                //Log.e(TAG, "onTouchEvent: "+getX());
                this.setRotation(rotation);

                this.setX(this.getX()+dx);//随着手动移动而移动
                this.setY(this.getY()+dy);

                this.setAlpha(Math.abs(1/(rotation*0.1f)));

                return true;
            case MotionEvent.ACTION_UP:
                //Log.e(TAG, "onTouchEvent: "+(Math.abs(getX()-getWidth()/3.5)));
                if(Math.abs(getX()-getWidth()/3.5)<50){

                    performClick();//在自定义onTouchEvent时会与View本身的点击事件相冲突所以要在某些条件下显示的调用performClick方法
                }
                if(Math.abs(getX()-getWidth()/3.5)<400){
                    Log.e(TAG, "onTouchEvent: ");
                    recover();
                }else {
                    remove();
                    myInterface.myListener();
                }
                return true;
        }
        return super.onTouchEvent(event);
    }

    public void recover(){
        this.animate().y(rootY).x(rootX).alpha(1).rotation(0)
                .setInterpolator(new OvershootInterpolator())//添加插值器，先向前惯性滑动再返回
                .setDuration(350);
    }

    private void remove(){
        if(getX()-325<0){
            this.animate().alpha(0).x(0-this.getWidth()).setDuration(200).setInterpolator(new AccelerateInterpolator()).rotation(sum-25);
        }else{
            this.animate().alpha(0).x(1440).setDuration(200).setInterpolator(new AccelerateInterpolator()).rotation(sum+25);
        }
    }

}