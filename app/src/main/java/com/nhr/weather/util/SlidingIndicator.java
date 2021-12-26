package com.nhr.weather.util;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;

import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.DrawableRes;

import com.nhr.weather.R;

public class SlidingIndicator extends HorizontalScrollView {
    private int mNumber = 1;
    private Context mContext;
    private LinearLayout mLinearLayout;
    private FrameLayout mFrameLayout;
    private ImageView imageView;
    float leftMargin = 0;
    private int ivWidth = 40;
    private int ivHeight = 20;
    private int dotBgId = R.drawable.ease_btn_blue_normal_shape;
    private int ivBgid = R.drawable.shape_iv_view_bg;

    public SlidingIndicator(Context context) {
        this(context,null);
    }

    public SlidingIndicator(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SlidingIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;

    }

    /**
     * 设置指示器点的个数
     * @param number
     */
    public void setNumber(int number){
        if(number > 0){
            mNumber = number;
        }

    }

    public int getNumber(){
        return mNumber;
    }

    /**
     * 设置指示器滚动条宽度
     * @param width
     */
    public void setIvWidth(int width) {
        if(width>0&& width<=100) {
            this.ivWidth = width;
        }
    }

    /**
     * 设置指示器滚动条高度
     * @param height
     */
    public void setIvHeight(int height) {
        if(height>0 && height<=100) {
            this.ivHeight = height;
        }
    }

    /**
     * 展示指示器
     */
    public void slidingIndicatorShow(){
        init(mContext,ivWidth,ivHeight);
    }

    /**
     * 设置底部小点背景图片或颜色
     * @param drawableId
     */
    public void setDotBg(@DrawableRes int drawableId){
        dotBgId = drawableId;
    }


    /**
     * 设置滑动滚动条背景
     * @param drawableId
     */
    public void setIvSlidingBg(@DrawableRes int drawableId){
        ivBgid = drawableId;
    }

    private void init(Context context, int width, int height) {
        mFrameLayout = new FrameLayout(context);
        mFrameLayout.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mLinearLayout = new LinearLayout(context);
        mLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        mLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        imageView = new ImageView(context);
        MarginLayoutParams vmi = new MarginLayoutParams(width,height);
        imageView.setLayoutParams(vmi);
        imageView.setBackgroundResource(ivBgid);
        for (int i = 0 ;i<mNumber;i++){
            ImageView imageView = new ImageView(context);
            imageView.setBackgroundResource(dotBgId);
            MarginLayoutParams vm = new MarginLayoutParams(width/2,height);
            vm.setMargins(height/2,0,height/2,0);
            imageView.setLayoutParams(vm);
            mLinearLayout.addView(imageView);

        }
        mFrameLayout.addView(mLinearLayout);
        mFrameLayout.addView(imageView);
        addView(mFrameLayout);

    }


    /**
     * 根据viewpager滑动而滑动的指示器
     * @param position
     * @param positionOffset
     */
    public void setViewLayoutParams(int position, float positionOffset){
        int viewWidth = ivWidth;
        //当posion == 0时算法 为1？
        if (positionOffset > 0 && positionOffset <= 0.5) {
            viewWidth = (int) (viewWidth * (1 - positionOffset));
            leftMargin = ((ivWidth + viewWidth) * positionOffset) + (ivWidth * position);

        } else if (positionOffset > 0.5 && positionOffset <= 1) {
            viewWidth = (int) (ivWidth * positionOffset);
            leftMargin = ((ivWidth + ivWidth - viewWidth) * positionOffset) + (ivWidth * position);
        }
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) imageView.getLayoutParams();
        params.width = viewWidth;
        params.leftMargin = Math.round(leftMargin);
        imageView.setLayoutParams(params);
    }


}
