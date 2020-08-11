package com.ids.ict.classes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.widget.ImageView;

public class PeekThroughImageView extends ImageView {

    private float radius = 200;
    private Paint paint = null;
    private boolean shouldDrawOpening = false;

    public PeekThroughImageView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public PeekThroughImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void start() {
        shouldDrawOpening = true;
        radius += 10;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (paint == null) {

            Bitmap original = Bitmap.createBitmap(getWidth(), getHeight(),
                    Bitmap.Config.ARGB_8888);

            Canvas originalCanvas = new Canvas(original);
            super.onDraw(originalCanvas); // ImageView

            Shader shader = new BitmapShader(original, Shader.TileMode.CLAMP,
                    Shader.TileMode.CLAMP);

            paint = new Paint();
            paint.setShader(shader);
        }

        // Bitmap mBitmap = BitmapFactory.decodeResource(getResources(),
        // R.drawable.background_colors);
        // Bitmap scaled = Bitmap.createScaledBitmap(null, getWidth(),
        // getHeight(), true);
        // canvas.drawBitmap(scaled, 0, 0, null);
        canvas.drawColor(Color.TRANSPARENT);
        if (shouldDrawOpening) {
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, radius, paint);
            if (radius < getHeight()) {
                start();
            }
        }
    }
}