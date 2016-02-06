package com.example.liyuanjing.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.Shader;
import android.os.Handler;
import android.os.Message;
import android.support.v7.graphics.Palette;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.liyuanjing.model.LYJCircle;
import com.example.liyuanjing.utils.LYJUtils;
import com.example.liyuanjing.xiuyixiudemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liyuanjing on 2016/2/3.
 */
public class XiuYiXiuView extends View {
    /***
     * 中心图片画笔
     */
    private Paint paint;
    /***
     * 水波圆圈画笔
     */
    private Paint circlePaint;
    /***
     * 用bitmap创建画布
     */
    private Bitmap bitmap;
    /***
     * 中心图片
     */
    private Bitmap imageBit;
    /***
     * 画布
     */
    private Canvas canvas;
    /***
     * 屏幕的宽
     */
    private int screenWidth;
    /***
     * 屏幕的高
     */
    private int screenHeight;
    /***
     * 图片右上角坐标
     */
    private Point pointLeftTop;
    /***
     * 图片右下角坐标
     */
    private Point pointRightBottom;
    /***
     * 记录圆圈
     */
    private List<LYJCircle> lyjCircleList;
    /***
     * 标记是否按下按钮，并且源泉是否扩散消失
     */
    private boolean isSpread=false;
    /***
     * 默认没有按动时候的圆圈
     */
    private LYJCircle defaultCircle;
    public XiuYiXiuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.lyjCircleList=new ArrayList<>();
        screenWidth=LYJUtils.getScreenWidth((Activity) context);
        screenHeight=LYJUtils.getScreenHeight((Activity) context);
        bitmap = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888); // 设置位图的宽高
        canvas = new Canvas();
        canvas.setBitmap(bitmap);
        paint=new Paint(Paint.DITHER_FLAG);
        paint.setAntiAlias(true);
        circlePaint=new Paint(Paint.DITHER_FLAG);
        circlePaint.setAntiAlias(true);
        imageBit= BitmapFactory.decodeResource(getResources(), R.drawable.bwa_homepage_yuyin);
        pointLeftTop=new Point((screenWidth/2)-(imageBit.getWidth()/2),(screenHeight/2)-(imageBit.getHeight()/2));
        pointRightBottom=new Point(pointLeftTop.x+imageBit.getWidth(),pointLeftTop.y+imageBit.getHeight());
        canvas.drawBitmap(imageBit,pointLeftTop.x,pointLeftTop.y,paint);
        //取图片上的颜色
        Palette.generateAsync(imageBit, new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                Palette.Swatch swatch1 = palette.getVibrantSwatch(); //充满活力的色板
                circlePaint.setColor(swatch1.getRgb());
                circlePaint.setStyle(Paint.Style.STROKE);
                circlePaint.setStrokeWidth(10);
                circlePaint.setAlpha(100);
                paint.setShadowLayer(15, 0, 0, swatch1.getRgb());//设置阴影效果
                int[] mColors = new int[] {//渲染颜色
                        Color.TRANSPARENT,swatch1.getRgb()
                };
                //范围，这里可以微调，实现你想要的渐变
                float[] mPositions = new float[] {
                        0f, 0.1f
                };
                Shader shader=new RadialGradient(screenWidth / 2,screenHeight / 2,imageBit.getWidth() / 2 + 10,mColors, mPositions,
                        Shader.TileMode.MIRROR);
                circlePaint.setShader(shader);
                defaultCircle=new LYJCircle(screenWidth / 2, screenHeight / 2, imageBit.getWidth() / 2 + 10);
                clearScreenAndDrawList();
                Message message = handler.obtainMessage(1);
                handler.sendMessageDelayed(message, 1000); //发送message

            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                isSpread=true;//是否按下图片
                lyjCircleList.add(new LYJCircle(screenWidth / 2, screenHeight / 2, imageBit.getWidth() / 2 + 10));
                clearScreenAndDrawList();
                invalidate();
                break;
            default:
                break;
        }
        return true;
    }

    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what) {
                case 1:
                    //定时更新界面
                    clearScreenAndDrawList();
                    invalidate();
                    Message message = handler.obtainMessage(1);
                    handler.sendMessageDelayed(message, 200);
            }
            super.handleMessage(msg);
        }
    };


    /**
     * 清掉屏幕上所有的线，然后画出集合里面的线
     */
    private void clearScreenAndDrawList() {
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        //判断是否按下图片，并且外圈执行完成没有。
        if(!isSpread){
                circlePaint.setMaskFilter(null);
                canvas.drawCircle(defaultCircle.getRoundX(), defaultCircle.getRoundY(),defaultCircle.getRadiuLoop(), circlePaint);// 画线
        }else{
            for (LYJCircle lyjCircle : lyjCircleList) {
                if(lyjCircle.getSpreadRadiu()==0){

                }else if(lyjCircle.getSpreadRadiu()>(lyjCircle.getRadiu()+99)){
                    //如果圆圈扩散半径大于图片半径+99，那么设置边缘模糊，也就是淡出的效果
                    circlePaint.setMaskFilter(new BlurMaskFilter(5, BlurMaskFilter.Blur.OUTER));
                    canvas.drawCircle(lyjCircle.getRoundX(), lyjCircle.getRoundY(),lyjCircle.getSpreadRadiu(), circlePaint);// 画线
                }else{
                    //不是则按正常的环形渲染来
                    circlePaint.setMaskFilter(null);
                    canvas.drawCircle(lyjCircle.getRoundX(), lyjCircle.getRoundY(),lyjCircle.getSpreadRadiu(), circlePaint);// 画线
                }
            }
        }
        canvas.drawBitmap(imageBit,pointLeftTop.x,pointLeftTop.y,paint);
        //释放小时了的圆圈
        for(int i=0;i<lyjCircleList.size();i++){
            if(lyjCircleList.get(i).getSpreadRadiu()==0){
                lyjCircleList.remove(i);
            }
        }
        //如果没有点击图片发射出去的圆圈，那么就恢复默认缩放。
        if(lyjCircleList.size()<=0){
            isSpread=false;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(bitmap, 0, 0, null);
    }
}
