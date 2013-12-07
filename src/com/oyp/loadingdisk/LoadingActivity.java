package com.oyp.loadingdisk;

import android.app.Activity;
import android.os.Bundle;
import android.widget.RelativeLayout;
/**
 * @author ouyangpeng 
 * @link http://blog.csdn.net/ouyang_peng
 */
public class LoadingActivity extends Activity {
	private RelativeLayout motionView;
	private LoadingDiscView disc_motion;
	private RefreshHandle refreshHandle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loading);
		disc_motion = new LoadingDiscView(this);
		refreshHandle = new RefreshHandle(disc_motion);
		motionView = (RelativeLayout) findViewById(R.id.loading_disc);
		motionView.addView(disc_motion);
		refreshHandle.sendEmptyMessage(0);
	}
	@Override
	protected void onResume() {
		super.onResume();
		disc_motion.onResume();
	}
}
