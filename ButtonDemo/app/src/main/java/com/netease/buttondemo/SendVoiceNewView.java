package com.netease.buttondemo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by xinyuanhxy on 16/5/17.
 */
public class SendVoiceNewView extends View{

    private int width, height;

    private Paint greenPaint,whitePaint,gradientPaint,textPaint;

    //旋转参数
    private int rotationPos;

    //椭圆大小改变参数
    private int transPos;

    //椭圆的宽高
    private int ovalWidth,ovalHeight;

    //椭圆旋转与椭圆大小改变的Animator
    private ValueAnimator ovalrvalueAnimator,ovaltvalueAnimator,itemminvalueAnimator,itemmaxvalueAnimator;

    private int itemWidth = (int)(4 * getDensity(getContext()));
    private int itemDis;

    private List<Item> leftList,rightList,centerList;

    private List<ValueAnimator> leftAnimList,rightAnimList,centerAniList;

    private boolean doAnim = false;

    private boolean isInitAnim = false;

    private boolean isTouchable = true;

    private String alphaPrecent = "FF";

    public SendVoiceNewView(Context context) {
        this(context, null);
    }

    public SendVoiceNewView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SendVoiceNewView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        if (Build.VERSION.SDK_INT < 18) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        initItemList();

        initPaint();
        initNormAnim();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getX() > getWidth() / 2 - ovalWidth / 2 && event.getX() < getWidth() / 2 + ovalWidth / 2 &&
                        event.getY() > getHeight() / 2 - ovalHeight / 2 && event.getY() < getHeight() / 2 + ovalHeight / 2 )
                {
                    if (isTouchable) {
//                        setIsTouchable(false);
                        onStart();
                        mSendVoiceListener.onSendClick();
                    }
                }
        return super.onTouchEvent(event);
    }

    //初始化Item对象
    private void initItemList(){
        leftList = new ArrayList<>();
        rightList = new ArrayList<>();
        centerList = new ArrayList<>();
        for (int i = 0; i < 7;i++){
            leftList.add(new Item());
            rightList.add(new Item());
        }

        for (int i = 0; i < 4;i++){
            centerList.add(new Item());
        }

        initItemAnimList();
    }

    //初始化Item动画
    private void initItemAnimList(){

        centerAniList = new ArrayList<>();

        for (int i=0; i < centerList.size(); i++){

            final int finalI = i;



            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0,centerList.get(i).type == 1 ? centerList.get(i).maxHeight : centerList.get(i).minHeight);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    centerList.get(finalI).currentHeight = (float) animation.getAnimatedValue();
                }
            });

            valueAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    centerList.get(finalI).isAnimEnd = true;
                    if (!isInitAnim) {
                        initItemAnim();
                        isInitAnim = true;
                    }
                }
            });
            valueAnimator.setInterpolator(new LinearInterpolator());
            valueAnimator.setDuration(100);

            centerAniList.add(valueAnimator);
        }

        leftAnimList = new ArrayList<>();
        rightAnimList = new ArrayList<>();

        for (int i=0; i < leftList.size(); i++){

            final int finalI = i;

            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0,leftList.get(i).type == 1 ? leftList.get(i).maxHeight : leftList.get(i).minHeight);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    leftList.get(finalI).currentHeight = (float) animation.getAnimatedValue();
                }
            });

            valueAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    leftList.get(finalI).isAnimEnd = true;


                }
            });
            valueAnimator.setInterpolator(new LinearInterpolator());
            valueAnimator.setDuration(100);

            leftAnimList.add(valueAnimator);

            valueAnimator = ValueAnimator.ofFloat(0,rightList.get(i).type == 1 ? rightList.get(i).maxHeight : rightList.get(i).minHeight);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    rightList.get(finalI).currentHeight = (float) animation.getAnimatedValue();
                }
            });

            valueAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    rightList.get(finalI).isAnimEnd = true;


                }
            });
            valueAnimator.setInterpolator(new LinearInterpolator());
            valueAnimator.setDuration(100);

            rightAnimList.add(valueAnimator);
        }

    }

    //初始画笔
    private void initPaint(){
        greenPaint = new Paint();
        greenPaint.setAntiAlias(true);
        greenPaint.setStyle(Paint.Style.FILL);
        greenPaint.setColor(Color.parseColor("#7f12A675"));

        whitePaint = new Paint();
        whitePaint.setAntiAlias(true);
        whitePaint.setStyle(Paint.Style.FILL);
        whitePaint.setColor(Color.WHITE);

        gradientPaint = new Paint();
        gradientPaint.setAntiAlias(true);
        gradientPaint.setStyle(Paint.Style.FILL);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setStyle(Paint.Style.FILL);
    }


    private void initNormAnim(){
        ovalrvalueAnimator = ValueAnimator.ofInt(0, 361);
        ovalrvalueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                rotationPos = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        ovalrvalueAnimator.setRepeatCount(-1);
        ovalrvalueAnimator.setInterpolator(new LinearInterpolator());
        ovalrvalueAnimator.setDuration(8000);
        ovalrvalueAnimator.setRepeatMode(1);
        ovalrvalueAnimator.start();

        ovaltvalueAnimator = ValueAnimator.ofInt(0,10,0);
        ovaltvalueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                transPos = (int) animation.getAnimatedValue();
            }
        });
        ovaltvalueAnimator.setRepeatCount(-1);
        ovaltvalueAnimator.setInterpolator(new LinearInterpolator());
        ovaltvalueAnimator.setDuration(2000);
        ovaltvalueAnimator.setRepeatMode(1);
        ovaltvalueAnimator.start();

    }

    private void initItemAnim(){

        itemminvalueAnimator = ValueAnimator.ofInt(0,120);
        itemminvalueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int update = (int) animation.getAnimatedValue();
                for (int i = 0; i < 7; i++) {

                    if (leftList.get(i).type == 0 && leftList.get(i).isAnimEnd) {

                        float x = update * (leftList.get(i).maxHeight - leftList.get(i).minHeight) * 2 / 120;
                        if (update > 60) {
                            leftList.get(i).currentHeight = leftList.get(i).maxHeight - (x - (leftList.get(i).maxHeight - leftList.get(i).minHeight));
                        } else {
                            leftList.get(i).currentHeight = leftList.get(i).minHeight + x;
                        }
                    }

                    if (rightList.get(i).type == 0 && rightList.get(i).isAnimEnd) {

                        float x = update * (rightList.get(i).maxHeight - rightList.get(i).minHeight) * 2 / 120;
                        if (update > 60) {
                            rightList.get(i).currentHeight = rightList.get(i).maxHeight - (x - (rightList.get(i).maxHeight - rightList.get(i).minHeight));
                        } else {
                            rightList.get(i).currentHeight = rightList.get(i).minHeight + x;
                        }
                    }
                }

                for (int i = 0; i < 4; i++) {

                    if (centerList.get(i).type == 0 && centerList.get(i).isAnimEnd) {

                        float x = update * (centerList.get(i).maxHeight - centerList.get(i).minHeight) * 2 / 120;
                        if (update > 60) {
                            centerList.get(i).currentHeight = centerList.get(i).maxHeight - (x - (centerList.get(i).maxHeight - centerList.get(i).minHeight));
                        } else {
                            centerList.get(i).currentHeight = centerList.get(i).minHeight + x;
                        }
                    }
                }
                postInvalidate();
            }
        });
        itemminvalueAnimator.setRepeatCount(-1);
        itemminvalueAnimator.setInterpolator(new LinearInterpolator());
        itemminvalueAnimator.setDuration(800);
        itemminvalueAnimator.setRepeatMode(1);
        itemminvalueAnimator.start();

        itemmaxvalueAnimator = ValueAnimator.ofInt(120,0);
        itemmaxvalueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int update = (int) animation.getAnimatedValue();
                for (int i = 0; i < 7; i++) {

                    if (leftList.get(i).type == 1 && leftList.get(i).isAnimEnd) {

                        float x = update * (leftList.get(i).maxHeight - leftList.get(i).minHeight) * 2 / 120;
                        if (update > 60) {
                            leftList.get(i).currentHeight = leftList.get(i).maxHeight - (2 * (leftList.get(i).maxHeight - leftList.get(i).minHeight) - x);
                        } else {
                            leftList.get(i).currentHeight = leftList.get(i).minHeight + ((leftList.get(i).maxHeight - leftList.get(i).minHeight) - x);
                        }
                    }

                    if (rightList.get(i).type == 1 && rightList.get(i).isAnimEnd) {

                        float x = update * (rightList.get(i).maxHeight - rightList.get(i).minHeight) * 2 / 120;
                        if (update > 60) {
                            rightList.get(i).currentHeight = rightList.get(i).maxHeight - (2 * (rightList.get(i).maxHeight - rightList.get(i).minHeight) - x);
                        } else {
                            rightList.get(i).currentHeight = rightList.get(i).minHeight + ((rightList.get(i).maxHeight - rightList.get(i).minHeight) - x);
                        }
                    }
                }

                for (int i = 0; i < 4; i++) {

                    if (centerList.get(i).type == 1 && centerList.get(i).isAnimEnd) {


                        float x = update * (centerList.get(i).maxHeight - centerList.get(i).minHeight) * 2 / 120;
                        if (update > 60) {
                            centerList.get(i).currentHeight = centerList.get(i).maxHeight - (2 * (centerList.get(i).maxHeight - centerList.get(i).minHeight) - x);
                        } else {
                            centerList.get(i).currentHeight = centerList.get(i).minHeight + ((centerList.get(i).maxHeight - centerList.get(i).minHeight) - x);
                        }
                    }
                }
                postInvalidate();
            }
        });
        itemmaxvalueAnimator.setRepeatCount(-1);
        itemmaxvalueAnimator.setInterpolator(new LinearInterpolator());
        itemmaxvalueAnimator.setDuration(800);
        itemmaxvalueAnimator.setRepeatMode(1);
        itemmaxvalueAnimator.start();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawOval(canvas);

        drawGradient(canvas);

        drawText(canvas);

        if (doAnim) {
            drawItem(canvas);
        }


//        canvas.drawRect(getWidth() / 2 - ovalWidth / 2,getHeight()/2 - ovalWidth/2,getWidth() / 2 + ovalWidth / 2,getHeight()/2 + ovalWidth/2,greenPaint);
    }


    //绘制椭圆
    private void drawOval(Canvas canvas){

        canvas.save();

        canvas.translate(getWidth() / 2, getHeight() / 2);

        canvas.rotate(rotationPos);

        canvas.drawOval(new RectF(-ovalWidth / 2 - transPos / 2, -ovalHeight / 2, ovalWidth / 2 + transPos / 2, ovalHeight / 2), greenPaint);

        canvas.save();
        canvas.rotate(45);
        canvas.drawOval(new RectF(-ovalWidth / 2 - transPos / 3, -ovalHeight / 2, ovalWidth / 2 + transPos / 3, ovalHeight / 2), greenPaint);
        canvas.restore();

        canvas.save();
        canvas.rotate(90);
        canvas.drawOval(new RectF(-ovalWidth / 2 - transPos / 3, -ovalHeight / 2, ovalWidth / 2 + transPos / 3, ovalHeight / 2), greenPaint);
        canvas.restore();

        canvas.save();
        canvas.rotate(135);
        canvas.drawOval(new RectF(-ovalWidth / 2 - transPos / 2, -ovalHeight / 2, ovalWidth / 2 + transPos / 2, ovalHeight / 2), greenPaint);
        canvas.restore();

        canvas.restore();

    }

    //绘制线性渐变
    private void drawGradient(Canvas canvas){

        canvas.save();

        canvas.translate(getWidth() / 2, getHeight() / 2);

        canvas.rotate(45);

        gradientPaint.setShader(new LinearGradient( - ovalWidth / 2,  - ovalWidth / 2,  ovalWidth / 2, ovalHeight/2, Color.parseColor("#7f27ff69"), Color.parseColor("#7f91f9ff"), Shader.TileMode.REPEAT));

        canvas.drawCircle(0, 0, ovalWidth / 2 + 2, gradientPaint);

        canvas.restore();

    }

    private void drawText(Canvas canvas){

        textPaint.setColor(Color.parseColor("#" + alphaPrecent + "FFFFFF")); //设置文字的颜色
        textPaint.setTextSize(17 * getDensity(getContext()));
        // 计算Baseline绘制的起点X轴坐标
        int baseX = (int) (getWidth() / 2 - textPaint.measureText("发送声波") / 2);
        // 计算Baseline绘制的Y坐标
        int baseY = (int) ((getHeight() / 2) - ((textPaint.descent() + textPaint.ascent()) / 2));
        canvas.drawText("发送声波", baseX, baseY, textPaint);

    }

    //画竖向的item条
    private void drawItem(Canvas canvas){

        for(int i = 0 ; i < 7 ; i++){

            float left = (itemDis + itemWidth) * i;
            RectF rectF = new RectF(left, getHeight()/2 - leftList.get(i).currentHeight/2,left + itemWidth , getHeight()/2 + leftList.get(i).currentHeight/2 );

            canvas.drawRoundRect(rectF,10,10,greenPaint);

            float right = (getWidth() - (itemDis + itemWidth)*i);
            rectF = new RectF(right - itemWidth, getHeight()/2 - rightList.get(i).currentHeight/2,right , getHeight()/2 + rightList.get(i).currentHeight/2 );

            canvas.drawRoundRect(rectF,10,10,greenPaint);
        }


        for (int i = 0; i < 4; i ++){

            float left = getWidth() / 2 +  itemDis * ( (float)- 1.5 + i) + itemWidth * (-2 + i);

            RectF rectF = new RectF(left, getHeight()/2 - centerList.get(i).currentHeight/2,left + itemWidth , getHeight()/2 + centerList.get(i).currentHeight/2 );

            canvas.drawRoundRect(rectF, 10, 10, whitePaint);
        }

    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 获得宽高测量模式和大小
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        // 保存测量结果
        int width, height;

        if (widthMode == MeasureSpec.EXACTLY) {
            // 宽度
            width = widthSize;
        } else {
            // 宽度加左右内边距
            width = this.width + getPaddingLeft() + getPaddingRight();
            ;
            if (widthMode == MeasureSpec.AT_MOST) {
                // 取小的那个
                width = Math.min(width, widthSize);
            }

        }

        if (heightMode == MeasureSpec.EXACTLY) {
            // 高度
            height = heightSize;
        } else {
            // 高度加左右内边距
            height = this.height + getPaddingTop() + getPaddingBottom();
            ;
            if (heightMode == MeasureSpec.AT_MOST) {
                // 取小的那个
                height = Math.min(height, heightSize);
            }

        }
        // 设置高度宽度为logo宽度和高度,实际开发中应该判断MeasureSpec的模式，进行对应的逻辑处理,这里做了简单的判断测量
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        ovalWidth = getWidth() / 3;
        ovalHeight = ovalWidth * 15 / 19;

        itemDis = (getWidth() / 3 - itemWidth * 7 )/7;
    }

    public void onStart(){

        doAnim = true;

        final AnimatorSet centerAnimatorSet = new AnimatorSet();
        final AnimatorSet leftAnimatorSet = new AnimatorSet();
        final AnimatorSet rightAnimatorSet = new AnimatorSet();

        centerAnimatorSet.play(centerAniList.get(2)).with(centerAniList.get(0));
        centerAnimatorSet.play(centerAniList.get(0)).before(centerAniList.get(1));
        centerAnimatorSet.play(centerAniList.get(1)).before(centerAniList.get(3));


        rightAnimatorSet.play(rightAnimList.get(6)).before(rightAnimList.get(5));
        rightAnimatorSet.play(rightAnimList.get(5)).before(rightAnimList.get(4));
        rightAnimatorSet.play(rightAnimList.get(4)).before(rightAnimList.get(3));
        rightAnimatorSet.play(rightAnimList.get(3)).before(rightAnimList.get(2));
        rightAnimatorSet.play(rightAnimList.get(2)).before(rightAnimList.get(1));
        rightAnimatorSet.play(rightAnimList.get(1)).before(rightAnimList.get(0));


        leftAnimatorSet.play(leftAnimList.get(6)).before(leftAnimList.get(5));
        leftAnimatorSet.play(leftAnimList.get(5)).before(leftAnimList.get(4));
        leftAnimatorSet.play(leftAnimList.get(4)).before(leftAnimList.get(3));
        leftAnimatorSet.play(leftAnimList.get(3)).before(leftAnimList.get(2));
        leftAnimatorSet.play(leftAnimList.get(2)).before(leftAnimList.get(1));
        leftAnimatorSet.play(leftAnimList.get(1)).before(leftAnimList.get(0));

        ValueAnimator alphavalueAnimator = ValueAnimator.ofInt(255,0);
        alphavalueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int alpha = (int) animation.getAnimatedValue();
                if (alpha <= 25) {
                    alphaPrecent = "00";
                } else {
                    alphaPrecent = Integer.toHexString(alpha);
                }
                invalidate();
            }
        });
        alphavalueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                centerAnimatorSet.start();
                centerAnimatorSet.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        leftAnimatorSet.start();
                        rightAnimatorSet.start();
                    }
                });
            }
        });
        alphavalueAnimator.setDuration(300);
        alphavalueAnimator.start();

    }

    private class Item{

        public Item() {
            maxHeight = new Random().nextInt((int)(17 * getDensity(getContext()))) + (int)(17 * getDensity(getContext()));
            minHeight = new Random().nextInt((int)(10 * getDensity(getContext()))) + (int)(4 * getDensity(getContext()));
            int random = new Random().nextInt(101);
            if (random <= 25){
                type = 0;
            }else if (random <= 50){
                type = 1;
            }else if (random <= 75){
                type = 0;
            }else if (random <= 100){
                type = 1;
            }
        }

        float maxHeight,minHeight,currentHeight,type;
        boolean isAnimEnd = false;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (ovalrvalueAnimator != null) {
            ovalrvalueAnimator.cancel();
            ovalrvalueAnimator = null;
        }
        if (ovaltvalueAnimator != null){
            ovaltvalueAnimator.cancel();
            ovaltvalueAnimator = null;
        }

        if (itemminvalueAnimator != null){
            itemminvalueAnimator.cancel();
            itemminvalueAnimator = null;
        }

        if (itemmaxvalueAnimator != null){
            itemmaxvalueAnimator.cancel();
            itemmaxvalueAnimator = null;
        }
    }

    public static float getDensity(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        android.view.Display display = wm.getDefaultDisplay();
        display.getMetrics(dm);
        return dm.density;
    }

    private SendVoiceListener mSendVoiceListener;

    public void setmSendVoiceListener(SendVoiceListener mSendVoiceListener) {
        this.mSendVoiceListener = mSendVoiceListener;
    }

    public void setIsTouchable(boolean isTouchable) {
        this.isTouchable = isTouchable;
    }

    public interface SendVoiceListener{
        void onSendClick();
    }
}
