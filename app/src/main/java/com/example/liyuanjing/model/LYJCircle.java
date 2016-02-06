package com.example.liyuanjing.model;

/**
 * Created by liyuanjing on 2016/2/3.
 */
public class LYJCircle {
    private int roundX;//圆中心点X坐标
    private int roundY;//圆中心点Y坐标
    private int radiu;//圆半径
    private int currentRadiu;//当前radiu
    private int lastRadiu;//历史radiu
    private int spreadRadiu;//加速半径
    private int[] speed=new int[]{6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6};//半径扩大速度。这里为匀速
    private int speedLast=0;//记录历史值
    public LYJCircle(int roundX,int roundY,int radiu){
        this.roundX=roundX;
        this.roundY=roundY;
        this.radiu=radiu;
        this.spreadRadiu=radiu;
        this.currentRadiu=this.radiu;
        this.lastRadiu=this.currentRadiu;
    }

    //获取半径
    public int getRadiu() {
        return radiu;
    }

    public void setRadiu(int radiu) {
        this.radiu = radiu;
    }

    //获取加速半径
    public int getSpreadRadiu(){
        if(speedLast>=speed.length){
            return 0;
        }
        spreadRadiu+=speed[speedLast];
        ++speedLast;
        return spreadRadiu;
    }

    //获取循环缩放半径
    public int getRadiuLoop() {
        if(currentRadiu==lastRadiu){
            ++currentRadiu;
        }else if(currentRadiu>lastRadiu){
            if(currentRadiu>(radiu+20)){
                currentRadiu=19+radiu;
                lastRadiu=20+radiu;
            }else{
                lastRadiu=currentRadiu;
                currentRadiu+=5;
            }
        }else{
            if(currentRadiu<(radiu+9)){
                currentRadiu=10+radiu;
                lastRadiu=9+radiu;
            }else{
                lastRadiu=currentRadiu;
                currentRadiu-=5;
            }
        }
        return currentRadiu;
    }

    public int getRoundX() {
        return roundX;
    }

    public int getRoundY() {
        return roundY;
    }
}
