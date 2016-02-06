package com.example.liyuanjing.xiuyixiudemo;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.example.liyuanjing.view.LYJCircleView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liyuanjing on 2016/2/5.
 */
public class XiuYiXiuActivity extends AppCompatActivity {
    private ImageButton mImageButton;
    private LYJCircleView lyjCircleView;
    private RelativeLayout relativeLayout;
    private List<LYJCircleView> lyjCircleViewList;
    private int statusBarHeight;
    private Animator anim;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xiuyixiu_activity_main);
        this.mImageButton=(ImageButton)findViewById(R.id.xiuyixiu_imagebutton);
        this.relativeLayout=(RelativeLayout)findViewById(R.id.xiuyixiu_relativelayout);
        this.lyjCircleViewList=new ArrayList<>();
        this.mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lyjCircleView.setVisibility(View.GONE);//发射圆圈，即将循环动画View隐藏
                final LYJCircleView item=new LYJCircleView(XiuYiXiuActivity.this, mImageButton.getWidth(), mImageButton.getHeight(), statusBarHeight);
                Animator spreadAnim = AnimatorInflater.loadAnimator(XiuYiXiuActivity.this, R.animator.circle_spread_animator);
                spreadAnim.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        item.setIsSpreadFlag(true);//动画执行完成，标记一下
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                spreadAnim.setTarget(item);
                spreadAnim.start();
                lyjCircleViewList.add(item);
                relativeLayout.addView(item);
                relativeLayout.invalidate();
                Message message = handler.obtainMessage(1);
                handler.sendMessageDelayed(message, 10); //发送message,定时释放LYJCircleView
            }
        });
    }

    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what) {
                case 1:
                    for(int i=0;i<lyjCircleViewList.size();i++){
                        if(lyjCircleViewList.get(i).isSpreadFlag()){
                            relativeLayout.removeView(lyjCircleViewList.get(i));
                            lyjCircleViewList.remove(i);
                            relativeLayout.invalidate();
                        }
                    }
                    if(lyjCircleViewList.size()<=0){
                        lyjCircleView.setVisibility(View.VISIBLE);
                    }
                    Message message = handler.obtainMessage(1);
                    handler.sendMessageDelayed(message, 10);
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        //获取状态栏高度
        Rect frame = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        statusBarHeight = frame.top;
        this.mImageButton.post(new Runnable() {
            @Override
            public void run() {
                lyjCircleView = new LYJCircleView(XiuYiXiuActivity.this, mImageButton.getWidth(), mImageButton.getHeight(), statusBarHeight);
                relativeLayout.addView(lyjCircleView);
                relativeLayout.postInvalidate();
                // 加载动画
                anim = AnimatorInflater.loadAnimator(XiuYiXiuActivity.this, R.animator.circle_scale_animator);
                anim.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        anim.start();//循环执行动画
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                anim.setTarget(lyjCircleView);
                anim.start();
            }
        });
    }
}
