package kr.flyegg.egg.ui.event;

import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

public class DisplayNextView implements Animation.AnimationListener {

	private boolean mCurrentView;
	ImageView image1;
	TextView text1;

	public DisplayNextView(boolean currentView, ImageView image1, TextView text1) {
	mCurrentView = currentView;
	this.image1 = image1;
	this.text1 = text1;
	}

	public void onAnimationStart(Animation animation) {
	}

	public void onAnimationEnd(Animation animation) {
	image1.post(new SwapViews(mCurrentView, image1, text1));
	}

	public void onAnimationRepeat(Animation animation) {
	}
	
}
