package com.cheng.consult.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by cheng on 2017/11/22.
 */

public class CircularImage extends MaskedImage {
    private static final String TAG = "CircularImage";
    private boolean isDown;
    private boolean isEnableTouch = false;

    public CircularImage(Context paramContext) {
        super(paramContext);
    }

    public CircularImage(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        setWillNotDraw(false);
    }

    public CircularImage(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
    }

    public Bitmap createMask() {
        int i = getWidth();
        int j = getHeight();
        Bitmap.Config localConfig = Bitmap.Config.ARGB_8888;
        Bitmap localBitmap = Bitmap.createBitmap(i, j, localConfig);

        Canvas localCanvas = new Canvas(localBitmap);
        Paint localPaint = new Paint(1);
        localPaint.setColor(-16777216);
        float f1 = getWidth();
        float f2 = getHeight();
        if (getType() == TYPE_CIRCULAR) {// 圆的
            localCanvas = new Canvas(localBitmap);
            RectF localRectF = new RectF(0.0F, 0.0F, f1, f2);
            localCanvas.drawOval(localRectF, localPaint);
        } else if (getType() == TYPE_SQUARE) {// 方的
            localCanvas = new Canvas(localBitmap);
            RectF localRectF = new RectF(0.0F, 0.0F, f1, f2);
            localCanvas.drawRect(localRectF, localPaint);
        } else {// 菱形
            localCanvas = new Canvas(localBitmap);
            localPaint.setStrokeWidth(3);
            Path path = new Path();
            path.moveTo(f1 / 2, 0);// 此点为多边形的起点
            path.lineTo(f1, f2 / 2);
            path.lineTo(f1 / 2, f2);
            path.lineTo(0, f2 / 2);
            path.close(); // 使这些点构成封闭的多边形
            localCanvas.drawPath(path, localPaint);
        }
        return localBitmap;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isEnableTouch) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    isDown = true;
                    invalidate();
                    break;

                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    isDown = false;
                    invalidate();
                    break;
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas paramCanvas) {
        super.onDraw(paramCanvas);
        if (isDown) {
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            // 消除锯齿
            paint.setAntiAlias(true);
            // 设置画笔的颜色
            paint.setARGB(100, 0, 0, 0);

            paramCanvas.drawCircle((float) getWidth() / 2, (float) getHeight() / 2, getWidth() / 2, paint);
        }
    }

    public void setEnableTouch(boolean isEnableTouch) {
        this.isEnableTouch = isEnableTouch;
    }
}
