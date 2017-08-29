package com.app.feng.waterlevelwatcher.ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;


/**
 * Created by feng on 2016/5/25.
 */
public class FullscreenImageView extends android.support.v7.widget.AppCompatImageView {

    private static final String TAG = "FullscreenImageView";

    private Matrix matrix = new Matrix();

    private MoveImageAnimator moveImageAnimator;

    private boolean back = false;

    private int imageWidth;
    private int imageHeight;

    private int distance;

    private int resId;

    public FullscreenImageView(Context context,AttributeSet attrs,int defStyleAttr) {
        super(context,attrs,defStyleAttr);

        setScaleType(ScaleType.MATRIX);
        setClickable(false);
    }

    public FullscreenImageView(Context context,AttributeSet attrs) {
        this(context,attrs,0);
    }

    public FullscreenImageView(Context context) {
        this(context,null);
    }

    @Override
    protected void onSizeChanged(int w,int h,int oldw,int oldh) {
        super.onSizeChanged(w,h,oldw,oldh);
        //获取图片宽高
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(),resId,options);
        imageWidth = options.outWidth;
        imageHeight = options.outHeight;
        int inSampleSize = 1;

        //如果检测到图片宽度小于高度
        if (imageWidth < imageHeight) {
            //不必加载.
            Log.e(TAG,"onSizeChanged: 图片尺寸有误");
            return;
        }

        //防止图片过大，出现OOM
        if (imageHeight > h || imageWidth > w) {
            final int halfHeight = imageHeight / 2;
            final int haleWidth = imageWidth / 2;

            while ((halfHeight / inSampleSize) > h || (haleWidth / inSampleSize) > w) {
                inSampleSize *= 2;
            }
        }
        options.inSampleSize = inSampleSize;
        options.inPreferredConfig = Bitmap.Config.ALPHA_8;
        options.inJustDecodeBounds = false;

        Bitmap background = BitmapFactory.decodeResource(getResources(),resId,options);
        setImageBitmap(background);

        //获取图片的宽高
        int dw = background.getWidth();
        int dh = background.getHeight();

        Log.i(TAG,"onPostExecute: dw: " + dw + " dh :" + dh);

        float scale; //缩放值

        scale = getHeight() * 1f / dh;

        matrix.postScale(scale,scale);
        setImageMatrix(matrix);
        startAnimation();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAnimation();
    }

    public void startAnimation() {
        moveImageAnimator = new MoveImageAnimator();
        moveImageAnimator.setDuration(10000);

        moveImageAnimator.setInterpolator(new LinearInterpolator());
        moveImageAnimator.setRepeatCount(Animation.INFINITE);
        moveImageAnimator.setRepeatMode(Animation.REVERSE);
        startAnimation(moveImageAnimator);

    }

    public void stopAnimation() {
        moveImageAnimator.cancel();
        moveImageAnimator = null;
        System.gc();
    }

    /**
     * 此方法能获取View图片的位置，大小等，并通过RectF类（一个数据类型为Float的矩形）返回
     *
     * @return
     */
    private RectF getMatrixRectF() {
        Matrix m = matrix;
        RectF rectF = new RectF();

        Drawable d = getDrawable();
        if (d != null) {
            rectF.set(0,0,d.getIntrinsicWidth(),d.getIntrinsicHeight());//当前图片宽高(不改变的)
            m.mapRect(rectF);//将matrix中的值映射到rectF中
        }

        return rectF;
    }

    public void setBgImage(int resId) {
        this.resId = resId;
    }

    private class MoveImageAnimator extends Animation {
        @Override
        public void initialize(int width,int height,int parentWidth,int parentHeight) {
            super.initialize(width,height,parentWidth,parentHeight);
            distance = imageWidth - width;
        }

        @Override
        protected void applyTransformation(float interpolatedTime,Transformation t) {
            super.applyTransformation(interpolatedTime,t);
            /*if (interpolatedTime == 1) {
                back = true;
                Log.i(TAG,"applyTransformation: Time : 1");

            }
            if (interpolatedTime == 0) {
                back = false;
                Log.i(TAG,"applyTransformation: Time : 0");
            }*/
            RectF r = getMatrixRectF();

            if (r.left > 0 || r.right < getWidth()) {
                back = !back;
            }

            if (back) {
                matrix.postTranslate(2,0);

            } else {
                matrix.postTranslate(-2,0);
            }
            setImageMatrix(matrix);
        }
    }
}
