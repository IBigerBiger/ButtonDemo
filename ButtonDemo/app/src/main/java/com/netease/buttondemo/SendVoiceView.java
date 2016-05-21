package com.netease.buttondemo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.view.animation.BounceInterpolator;
import android.widget.Button;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by xinyuanhxy on 16/4/20.
 */
public class SendVoiceView extends Button{

    private int width, height;// 控件宽高

    private Paint paint;

    private PathMeasure tickPathMeasure;

    private boolean isLoad = false;

    private Context context;

    public SendVoiceView(Context context) {
        this(context,null);
    }

    public SendVoiceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SendVoiceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        paint = new Paint();


    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int center = getWidth()/2; //获取圆心的x坐标
        int radius = (int) (getWidth()/4) - raduisPrecent;  //圆环的半径

        if (!isLoad){
            firstroundradius = getWidth()/4 + getWidth()/4 * 3 / 4;
            secondroundradius = getWidth()/4 + getWidth()/4 * 3 / 8;
            alpha = (double)230/(double)(getWidth()/4);
            firstalpha = 255 - (firstroundradius - getWidth()/4) * alpha;
            secondalpha = 255 - (secondroundradius - getWidth()/4) * alpha;
            isLoad = true;

        }

        Path tickPath = new Path();
        tickPath.moveTo(1.5f * radius , 2 * radius + 10 * getDensity(context));
        tickPath.lineTo(1.5f * radius + 0.3f * radius , 2 * radius + 0.3f * radius + 10 * getDensity(context));
        tickPath.lineTo(2 * radius + 0.5f * radius , 2 * radius - 0.3f * radius + 10 * getDensity(context));
        tickPathMeasure = new PathMeasure(tickPath,false);

        paint.setColor(Color.parseColor("#" + Integer.toHexString(Math.abs((int)firstalpha)) + "c7fcd8")); //设置外圆的颜色
        paint.setStyle(Paint.Style.FILL); //设置空心
        paint.setAntiAlias(true);  //消除锯齿
        canvas.drawCircle(center, center, firstroundradius, paint); //画出圆

        paint.setColor(Color.parseColor("#" + Integer.toHexString(Math.abs((int)secondalpha)) + "c7fcd8")); //设置外圆的颜色
        paint.setStyle(Paint.Style.FILL); //设置空心
        paint.setAntiAlias(true);  //消除锯齿
        canvas.drawCircle(center, center, secondroundradius, paint); //画出圆

        paint.setColor(Color.parseColor("#12A86B")); //设置圆的颜色
        paint.setStyle(Paint.Style.FILL); //设置空心
        paint.setAntiAlias(true);  //消除锯齿
        canvas.drawCircle(center, center, radius, paint); //画出圆

        paint.setColor(Color.parseColor("#3CBD8D")); //设置圆环的颜色
        paint.setStyle(Paint.Style.STROKE); //设置空心
        paint.setStrokeWidth(10 * getDensity(context) ); //设置圆环的宽度
        paint.setAntiAlias(true);  //消除锯齿
        canvas.drawCircle(center, center, radius - 15 * getDensity(context), paint); //画出圆环

        paint.setColor(Color.parseColor("#" + alphaPrecent + "FFFFFF")); //设置文字的颜色
        paint.setStyle(Paint.Style.FILL); //设置空心
        paint.setAntiAlias(true);  //消除锯齿
        paint.setTextSize(25 * getDensity(context) );
        // 计算Baseline绘制的起点X轴坐标
        int baseX = (int) (canvas.getWidth() / 2 - paint.measureText("点击") / 2);
        // 计算Baseline绘制的Y坐标
        int baseY = (int) ((canvas.getWidth() / 2) + ((paint.descent() + paint.ascent()) / 2) - 10);

        canvas.drawText("点击", baseX, baseY, paint);
        // 计算Baseline绘制的起点X轴坐标
         baseX = (int) (canvas.getWidth() / 2 - paint.measureText("发送音波") / 2);
        // 计算Baseline绘制的Y坐标
         baseY = (int) ((canvas.getWidth() / 2) - 2 * ((paint.descent() + paint.ascent()) / 2) + 10);

        canvas.drawText("发送音波", baseX, baseY, paint);


        paint.setStrokeWidth(10 * getDensity(context)); //设置圆弧的宽度
        paint.setColor(Color.parseColor("#D5FFF0"));  //设置进度的颜色
        RectF oval = new RectF(center - radius  + 15 * getDensity(context), center - radius  +15 * getDensity(context), center
                + radius - 15 * getDensity(context), center + radius - 15 * getDensity(context));  //用于定义的圆弧的形状和大小的界限
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawArc(oval, 270, -(360 * progress / 100), false, paint);  //根据进度画圆弧


        paint.setStrokeWidth(10 * getDensity(context));
        Path path = new Path();
        tickPathMeasure.getSegment(0, tickPrecent / 100 * tickPathMeasure.getLength(), path, true);//该方法，可以获得整个路径的一部分
        path.rLineTo(0, 0);//解决Android本身的一个bug
        canvas.drawPath(path, paint);//绘制出这一部分
    }

    private float progress = 0;
    private String alphaPrecent = "FF";
    private float tickPrecent = 0;
    private int raduisPrecent = 0;

    private int firstroundradius ;
    private int secondroundradius ;
    private double firstalpha;
    private double secondalpha;
    private double alpha;

    public void doSendAnim(){

        //外圈两个圆的半径和透明度变化Animator
        ValueAnimator roundvalueAnimator = ValueAnimator.ofInt(0,3000);
        roundvalueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (firstroundradius > secondroundradius) {
                    firstroundradius = firstroundradius + 4;
                    secondroundradius = secondroundradius + 3;
                } else {
                    firstroundradius = firstroundradius + 3;
                    secondroundradius = secondroundradius + 4;
                }
                firstalpha = 255 - (firstroundradius - getWidth() / 4) * alpha;
                secondalpha = 255 - (secondroundradius - getWidth() / 4) * alpha;
                if (firstroundradius >= getWidth() / 2) {
                    firstroundradius = getWidth() / 4;
                    firstalpha = 255;
                }
                if (secondroundradius >= getWidth() / 2) {
                    secondroundradius = getWidth() / 4;
                    secondalpha = 255;
                }
                invalidate();
            }
        });
        roundvalueAnimator.setDuration(3000);

        //实心圆半径变化，从大到小在到大的Animator
        ValueAnimator radiusvalueAnimator = ValueAnimator.ofInt(0,30,0);
        radiusvalueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                raduisPrecent = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        radiusvalueAnimator.setInterpolator(new BounceInterpolator());
        radiusvalueAnimator.setDuration(1200);

        //文字消失透明度变化的Animator
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
        alphavalueAnimator.setDuration(300);

        //圆环运动的Animator
        ValueAnimator progressvalueAnimator = ValueAnimator.ofInt(0, 110);
        progressvalueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()

                                        {
                                            @Override
                                            public void onAnimationUpdate(ValueAnimator animation) {
                                                progress = (int) animation.getAnimatedValue();
                                                invalidate();
                                            }
                                        }

        );
        progressvalueAnimator.setDuration(2400);

        //最后
        ValueAnimator tickvalueAnimator = ValueAnimator.ofInt(0, 100);
        tickvalueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                tickPrecent = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        tickvalueAnimator.setDuration(300);
        tickvalueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        progress = 0;
                        alphaPrecent = "FF";
                        tickPrecent = 0;
                        raduisPrecent = 0;
                        Message msg = new Message();
                        msg.what = 1;
                        resetHandler.sendMessage(msg);
                    }
                }, 1000);
            }
        });


        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(roundvalueAnimator).with(radiusvalueAnimator);
        animatorSet.play(radiusvalueAnimator).with(alphavalueAnimator);
        animatorSet.play(alphavalueAnimator).before(progressvalueAnimator);
        animatorSet.play(progressvalueAnimator).before(tickvalueAnimator);
        animatorSet.start();
        }

    Handler resetHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                invalidate();
            }

        }
    };

        @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
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


    private static float getDensity(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        android.view.Display display = wm.getDefaultDisplay();
        display.getMetrics(dm);
        return dm.density;
    }

}
