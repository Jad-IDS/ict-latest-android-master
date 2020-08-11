package com.ids.ict.animation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;

public class HorizantalAnimatedImageView extends ImageView {

	private float radius;
	private Paint paint = null;
	private boolean shouldDrawOpening = false;

	public HorizantalAnimatedImageView(Context context) {
		super(context);  
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		radius = (float) (display.getHeight()*1.5);
	}

	public HorizantalAnimatedImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		radius = (float) (display.getHeight()*1.5);
	}

	public void start() {
		shouldDrawOpening = true;
		radius += 20 ;
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

		canvas.drawColor(Color.TRANSPARENT); 
		if (shouldDrawOpening) {
			canvas.drawCircle(getWidth() / 2, getHeight() * -2, radius, paint);
			if (radius < getHeight() * 3.1)
				start();
		}
	}
}