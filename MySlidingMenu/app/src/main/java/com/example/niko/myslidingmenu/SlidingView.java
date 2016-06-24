package com.example.niko.myslidingmenu;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

/**
 * Created by Niko on 2016/3/6.
 */
public class SlidingView extends HorizontalScrollView {

    private LinearLayout mWapper;
    private ViewGroup mMenu;
    private ViewGroup mContent;
    private  int mScreenWidth;
    private int mMenuRightPadding=50;
    private boolean once;
    private LinearLayout mMenuList;




    public SlidingView(Context context, AttributeSet attrs) {
        super(context, attrs);


        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);

        DisplayMetrics md = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(md);
        mScreenWidth = md.widthPixels;

        mMenuRightPadding= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,mMenuRightPadding,context.getResources().getDisplayMetrics());

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        if(!once) {

            mWapper = (LinearLayout) getChildAt(0);
            mMenu = (ViewGroup) mWapper.getChildAt(0);
            mMenuList = (LinearLayout) mMenu.getChildAt(0);

            mContent = (ViewGroup) mWapper.getChildAt(1);
            mMenu.getLayoutParams().width = mScreenWidth - mMenuRightPadding;
            mContent.getLayoutParams().width = mScreenWidth;

            once =true;
        }


        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        if(changed) {
            this.scrollTo(mScreenWidth - mMenuRightPadding, 0);
        }

    }
    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        switch (ev.getAction()){

            case MotionEvent.ACTION_UP:


               int x = (int) getScrollX();
                if(x>=(mScreenWidth-mMenuRightPadding)/3*2){
                    this.smoothScrollTo(mScreenWidth-mMenuRightPadding,0);
                }else{
                  //  this.scrollTo(0,0);

                    this.smoothScrollTo(0,0);
                }

                return true;

        }

        return super.onTouchEvent(ev);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        float scale = l*1.0f/(mScreenWidth - mMenuRightPadding);
        float scaleContent = 1.0f-0.6f*scale;
        float mMenuListScale = 1.0f-0.4f*scale;

        Log.d("TAG===>", "fds" + mMenuListScale);


       ObjectAnimator mMenuListLeftX =  ObjectAnimator.ofFloat( mMenuList,TRANSLATION_X ,(1.0f-scaleContent)*-mScreenWidth/3);
       ObjectAnimator mMenuListScaleX = ObjectAnimator.ofFloat(mMenuList,SCALE_X,mMenuListScale);
        ObjectAnimator mMenuListScaleY = ObjectAnimator.ofFloat(mMenuList,SCALE_Y,mMenuListScale);
        ObjectAnimator mMenuListAp = ObjectAnimator.ofFloat(mMenuList,ALPHA,mMenuListScale);


        ObjectAnimator anim=  ObjectAnimator.ofFloat(mMenu, TRANSLATION_X, ((mScreenWidth - mMenuRightPadding) *scale));
        AnimatorSet set = new AnimatorSet();
        set.playTogether(anim,mMenuListLeftX,mMenuListScaleX,mMenuListScaleY,mMenuListAp);

        set.setDuration(0);
        set.start();


    }


}
