package com.oyp.loadingdisk;

import android.os.Handler;
import android.os.Message;
/**
 * 用来发送消息和处理消息的
 * @author ouyangpeng
 * @link http://blog.csdn.net/ouyang_peng
 */
public class RefreshHandle extends Handler {
	LoadingDiscView loadingDiscView;

	public RefreshHandle(LoadingDiscView loadingDiscView) {
		this.loadingDiscView = loadingDiscView;
		loadingDiscView.setRefreshHandle(this);
	}

	public void run() {
		loadingDiscView.update();
		removeCallbacksAndMessages(null);
		sendEmptyMessageDelayed(0, 65);
	}

	public void stop() {
		removeCallbacksAndMessages(null);
	}

	@Override
	public void handleMessage(Message msg) {
		switch (msg.what) {
		case 0:
			run();
			break;
		}
	}
}