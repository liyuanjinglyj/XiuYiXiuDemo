package com.example.liyuanjing.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import com.example.liyuanjing.utils.LYJUtils;

/**
 * Created by liyuanjing on 2016/2/5.
 */
public class LYJCircleView extends View {
    private Bitmap bitmap;
    private Paint paint;
    private Canvas canvas;
    private int screenWidth;
    private int screenHeight;
    private boolean isSpreadFlag=false;//标记是否发射完成

    public boolean isSpreadFlag() {
        return isSpreadFlag;
    }

    public void setIsSpreadFlag(boolean isSpreadFlag) {
        this.isSpreadFlag = isSpreadFlag;
    }

    public LYJCircleView(Context context,int width,int height,int statusHeight) {
        super(context);
        screenWidth= LYJUtils.getScreenWidth((Activity) context);
        screenHeight=LYJUtils.getScreenHeight((Activity) context);
        bitmap = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888); // 设置位图的宽高
        canvas = new Canvas();
        canvas.setBitmap(bitmap);
        paint=new Paint(Paint.DITHER_FLAG);
        paint.setAntiAlias(true);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        paint.setAlpha(100);
        paint.setShadowLayer(10, 0, 0, Color.RED);
        int[] mColors = new int[] {
                Color.TRANSPARENT,Color.RED
        };
        float[] mPositions = new float[] {
                0f, 0.1f
        };
        Shader shader=new RadialGradient(screenWidth / 2,screenHeight / 2,width / 2 + 10,mColors, mPositions,
                Shader.TileMode.MIRROR);
        paint.setShader(shader);
        canvas.drawCircle(screenWidth / 2, (screenHeight - statusHeight) / 2, width / 2 + 10, paint);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(bitmap,0,0,null);
    }
}
