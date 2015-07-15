package com.skytech.android.util;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;

public class TouchHelper {
	public static void setOnTouchListener(View view, final float x,final float y) {

		view.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View view, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					scale(view, x, y);
				}
				return false;
			}
		});
	}
	
	public static void scale(View v, float tox, float toy) {

		ScaleAnimation scalezoomin = new ScaleAnimation(1.0f, tox, 1.0f, toy,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		scalezoomin.setInterpolator(new DecelerateInterpolator());
		scalezoomin.setDuration(100);

		ScaleAnimation scalezoomout = new ScaleAnimation(1f, 1 / tox, 1f,
				1 / toy, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		scalezoomout.setDuration(100);
		scalezoomout.setInterpolator(new DecelerateInterpolator());
		scalezoomout.setStartOffset(100);

		AnimationSet iconAnimationSet = new AnimationSet(true);
		iconAnimationSet.addAnimation(scalezoomin);
		iconAnimationSet.addAnimation(scalezoomout);
		v.startAnimation(iconAnimationSet);
	}
	
	
	
}
