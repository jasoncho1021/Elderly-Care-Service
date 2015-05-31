package com.swdm.jskapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

public class SplashActivity extends Activity {

	private static final long SPLASH_TIME = 4000; //3 seconds
	Handler mHandler;
	Runnable mJumpRunnable;
	AnimationDrawable mAnimation;

	@SuppressLint("HandlerLeak")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.splash);

		ImageView img = (ImageView)findViewById(R.id.imgsp);
		img.setBackgroundResource(R.drawable.ani_splash);
		mAnimation = (AnimationDrawable)img.getBackground();


		mJumpRunnable = new Runnable() {

			public void run() {
				jump();
			}
		};
		mHandler = new Handler();
		mHandler.postDelayed(mJumpRunnable, SPLASH_TIME);

		//		Handler handler = new Handler() {
		//			public void handleMessage(Message msg) { 				
		//				finish();
		//			}
		//		};
		//		
		//		//startActivity(new Intent(this, MainActivity.class));
		//		handler.sendEmptyMessageDelayed(0, 4000);
	}

	private void jump() {
		//it is safe to use this code even if you
		//do not intend to allow users to skip the splash
		if(isFinishing())
			return;
		startActivity(new Intent(this, MainActivity.class));
		finish();
	}

	@Override
	public void onWindowFocusChanged (boolean hasFocus)
	{
		super.onWindowFocusChanged(hasFocus);
		if(hasFocus) {
			mAnimation.start();
		} else {
			mAnimation.stop();
		}
	}
}