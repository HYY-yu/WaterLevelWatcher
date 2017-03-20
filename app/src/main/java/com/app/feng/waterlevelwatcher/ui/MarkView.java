package com.app.feng.waterlevelwatcher.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MarkerOptions;
import com.app.feng.waterlevelwatcher.R;

/**
 * Created by feng on 2017/3/10.
 */

public class MarkView extends View {

    public static int MODE_NORMAL = 1;
    public static int MODE_ALARM = 2; // 预警模式

    private int widthOval = 45;
    private int heightOval = 40;

    private String text;

    public Paint paint;

    private RectF ovalRect;
    private Path trianglePath;

    private int offset = 6;

    private int colorAccent;

    private int mode = MODE_NORMAL;

    private LatLng laLng;

    public MarkView(Context context) {
        this(context,null);
    }

    public MarkView(
            Context context,@Nullable AttributeSet attrs) {
        this(context,attrs,0);
    }

    public MarkView(Context context,@Nullable AttributeSet attrs,int defStyleAttr) {
        super(context,attrs,defStyleAttr);

        TypedArray a = context.getTheme()
                .obtainStyledAttributes(attrs,R.styleable.markView,defStyleAttr,0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.markView_widthOval:
                    widthOval = a.getDimensionPixelOffset(attr,100);
                    break;
                case R.styleable.markView_heightOval:
                    // 默认颜色设置为黑色
                    heightOval = a.getDimensionPixelOffset(attr,100);
                    break;
                case R.styleable.markView_offset:
                    offset = a.getDimensionPixelOffset(attr,30);
                    break;
            }
        }
        a.recycle();

        widthOval = context.getResources()
                .getDimensionPixelOffset(R.dimen.map_marker_width);
        heightOval = context.getResources()
                .getDimensionPixelOffset(R.dimen.map_marker_height);
        offset = context.getResources()
                .getDimensionPixelOffset(R.dimen.map_offset);

        colorAccent = context.getResources()
                .getColor(R.color.colorAccent);
        paint = new Paint();
        paint.setColor(colorAccent);
        paint.setAntiAlias(true);
        setLayerType(View.LAYER_TYPE_SOFTWARE,null);

        ovalRect = new RectF();
        trianglePath = new Path();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec,int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);

        setMeasuredDimension(widthOval,heightOval + (int) (heightOval * 0.5f));
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawShadow(canvas);

        drawOval(canvas);

        drawTriangle(canvas);

        if (mode == MODE_NORMAL) {
            drawCenterCircleShadow(canvas);
            drawCenterCircle(canvas);
        } else {
            drawAlarm(canvas);
        }
    }

    private void drawShadow(Canvas canvas) {
        paint.setColor(Color.GRAY);
        paint.setAlpha(130);

        float shadowHeight = heightOval * 0.5f;

        ovalRect.top = getHeight() - shadowHeight;
        ovalRect.left = widthOval * 0.15f;
        ovalRect.right = widthOval * 0.85f;
        ovalRect.bottom = ovalRect.top + shadowHeight * 0.666f;

        canvas.save();
        canvas.clipRect(ovalRect.left,ovalRect.top,ovalRect.right,
                        ovalRect.top + ovalRect.height() / 2f);
        canvas.drawOval(ovalRect,paint);

        canvas.restore();

        trianglePath.reset();
        trianglePath.moveTo(ovalRect.left,ovalRect.centerY());
        trianglePath.quadTo(ovalRect.left + widthOval / 8f - offset,ovalRect.bottom + offset,
                            widthOval * 0.5f,getHeight());
        trianglePath.quadTo(ovalRect.right - widthOval / 8f + offset,ovalRect.bottom + offset,
                            ovalRect.right,ovalRect.centerY());
        trianglePath.close();

        canvas.drawPath(trianglePath,paint);

        paint.setAlpha(255);
        paint.setColor(colorAccent);
    }

    private void drawAlarm(Canvas canvas) {
        float radius = heightOval / 8f;
        float newH = heightOval / 2f;

        paint.setColor(Color.WHITE);
        canvas.drawCircle(widthOval * 0.5f,heightOval,radius,paint);

        ovalRect.left = widthOval * 0.5f - radius;
        ovalRect.right = widthOval * 0.5f + radius;
        ovalRect.top = newH - radius * 2f;
        ovalRect.bottom = newH + radius * 2f;

        canvas.drawRoundRect(ovalRect,offset,offset,paint);
        paint.setColor(colorAccent);
    }

    private void drawCenterCircleShadow(Canvas canvas) {
        paint.setColor(Color.GRAY);
        paint.setAlpha(80);
        canvas.drawCircle(widthOval * 0.5f + 3,heightOval * 0.5f + 3,heightOval * 0.25f,paint);

        paint.setAlpha(255);
        paint.setColor(colorAccent);
    }

    private void drawCenterCircle(Canvas canvas) {

        paint.setColor(Color.WHITE);
        canvas.drawCircle(widthOval * 0.5f,heightOval * 0.5f,heightOval * 0.25f,paint);
        paint.setColor(colorAccent);
    }

    private void drawTriangle(Canvas canvas) {
        trianglePath.reset();

        trianglePath.moveTo(0,heightOval * 0.5f);
        trianglePath.quadTo(0.25f * widthOval - offset,heightOval + offset,widthOval * 0.5f,
                            heightOval * 1.5f);
        trianglePath.quadTo(0.75f * widthOval + offset,heightOval + offset,widthOval,
                            heightOval * 0.5f);
        trianglePath.close();

        canvas.drawPath(trianglePath,paint);
    }

    private void drawOval(Canvas canvas) {
        ovalRect.top = 0;
        ovalRect.left = 0;
        ovalRect.right = widthOval;
        ovalRect.bottom = heightOval;

        canvas.save();
        canvas.clipRect(ovalRect.left,ovalRect.top,ovalRect.right,ovalRect.bottom * 0.5f);
        canvas.drawOval(ovalRect,paint);

        canvas.restore();
    }

    public MarkView setMode(int mode) {
        this.mode = mode;
        invalidate();
        return this;
    }

    //经纬度
    public MarkView setPosition(double longitude,double latitude) {
        laLng = new LatLng(latitude,longitude);
        return this;
    }

    public LatLng getPosition(){
        return laLng;
    }


    public MarkerOptions getMarkerOptions() {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(laLng);
        markerOptions.icon(BitmapDescriptorFactory.fromView(this));

        return markerOptions;
    }

    public void setWidthOval(int widthOval) {
        this.widthOval = widthOval;
    }

    public void setHeightOval(int heightOval) {
        this.heightOval = heightOval;
    }

    public void setText(String text) {
        this.text = text;
    }

    private int flag;

    public MarkView setFlag(int flag) {
        this.flag = flag;
        return this;
    }

    public int getFlag() {
        return flag;
    }
}
