package com.ids.ict.animation;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.ids.ict.R;

public class SimpleDrawingView extends View {
	// setup initial color
	private final int paintColor = Color.WHITE;
	// defines paint and canvas
	private Paint drawPaint;
	int color;
	float width;
	Context cnxt;

	public SimpleDrawingView(Context context) {
		super(context);
		setFocusable(true);
		setFocusableInTouchMode(true);
		color = Color.parseColor("#b5b5ba");
		cnxt = context;
		setupPaint(color);
	}

	public SimpleDrawingView(Context context, int clr) {
		super(context);
		setFocusable(true);
		setFocusableInTouchMode(true);
		color = clr;
		cnxt = context;
		setupPaint(color);
	}

	// Setup paint with color and stroke styles
	private void setupPaint(int color) {
		WindowManager wm = (WindowManager) cnxt
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		width = display.getWidth()/53;  
		drawPaint = new Paint();
		drawPaint.setColor(color);
		drawPaint.setAntiAlias(true);
		drawPaint.setStrokeWidth(width);
		drawPaint.setStyle(Paint.Style.STROKE);
		drawPaint.setStrokeJoin(Paint.Join.ROUND);
		drawPaint.setStrokeCap(Paint.Cap.ROUND);
		drawPaint.setStrokeWidth(getResources().getDimension(R.dimen.circlewidth));
	}

	@Override
	protected void onDraw(Canvas canvas) {
		int wirth = getWidth();
		int height=getHeight();
		Log.d("width",wirth + "");
		if(wirth<1000) {
			if(wirth<600)
			canvas.drawCircle(getWidth() / 2, getHeight() / 2,
					(float) (getWidth() * .135), drawPaint);
			else
			{
				canvas.drawCircle(getWidth() / 2, getHeight() / 2,
						(float) (getWidth() * .140), drawPaint);
			}
		}
		else if(wirth>1400)

			canvas.drawCircle(getWidth() / 2, getHeight() / 2,
					(float) (getWidth() * .145), drawPaint);
		else

			canvas.drawCircle(getWidth() / 2, getHeight() / 2,
					(float) (getWidth() * .135), drawPaint);
	}

}