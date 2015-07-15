package com.skytech.moa.doc.widgets.slate;

import android.graphics.*;

/**
 * Created by huangzf on 14-1-3.
 */
public interface CanvasLite {
    public void drawRect(float l, float t, float r, float b, Paint paint);
    public void drawCircle(float x, float y, float r, Paint paint);
    public void drawColor(int color, PorterDuff.Mode mode);
    public void drawBitmap(Bitmap bitmap, Rect src, RectF dst, Paint paint);
    public void drawBitmap(Bitmap bitmap, Matrix matrix, Paint paint);

    public void drawTo(Canvas drawCanvas, float left, float top, Paint paint, boolean dirtyOnly);
    public Bitmap toBitmap();
    public void recycleBitmaps();
    public int getWidth();
    public int getHeight();
}