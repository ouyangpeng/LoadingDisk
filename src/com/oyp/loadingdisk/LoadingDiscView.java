package com.oyp.loadingdisk;

import java.io.InputStream;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.view.View;
/**
 * 自定义的View,用来显示加载的图片
 * @author ouyangpeng 
 * @link http://blog.csdn.net/ouyang_peng
 * 
 * <p>在画图的时候，图片如果旋转或缩放之后，总是会出现那些华丽的锯齿。<br>
 * 方法一：给Paint加上抗锯齿标志。然后将Paint对象作为参数传给canvas的绘制方法。<br>
 * 如：mypaint.setAntiAlias(true);<p>
 * 方法二：给Canvas加上抗锯齿标志。有些地方不能用paint的，就直接给canvas加抗锯齿，更方便。<br>
 * 如：
 * mSetfil = new PaintFlagsDrawFilter(0, Paint.FILTER_BITMAP_FLAG);<br>
 * canvas.setDrawFilter(mSetfil);
 */
public class LoadingDiscView extends View {
		private RefreshHandle refreshHandle;
		private Context context;
		/** 用于旋转的bitmap*/
		private Bitmap m_bmp_disc = null;
		private Matrix m_matrix_disc = new Matrix();
		/** 用于展现高亮背景的bitmap*/
		private Bitmap m_bmp_light = null;
		private Matrix m_matrix_light = new Matrix();
		/**Paint滤波器*/
		private PaintFlagsDrawFilter mSetfil = null;
		/**声明一个画笔*/
		private Paint mypaint = null;
		/**图像缩放比例*/
		private float m_scale =1.0f;
		/**图像旋转的速度*/
		private float m_disc_rot_speed = 0;
		/**图像旋转的状态*/
		private int m_state_play = 1;
		/**图像旋转的最大速度*/
		private float m_disc_max = 20f;

		public void setRefreshHandle(RefreshHandle refreshHandle) {
			this.refreshHandle = refreshHandle;
		}

		public LoadingDiscView(Context context) {
			super(context);
			this.context = context;
			mSetfil = new PaintFlagsDrawFilter(0, Paint.FILTER_BITMAP_FLAG);//设置画布绘图无锯齿
			initBitmap();
		}

		public boolean initBitmap() {
			mypaint = new Paint();
			//给Paint加上抗锯齿标志
			mypaint.setAntiAlias(true);//画笔的抗锯齿（用于线条等）

			Resources res = context.getResources();
			InputStream is = res.openRawResource(R.drawable.loading_disc);
			m_bmp_disc = BitmapFactory.decodeStream(is);
			matrixPostTranslate(m_matrix_disc,m_bmp_disc);

			is = res.openRawResource(R.drawable.loading_light);
			m_bmp_light = BitmapFactory.decodeStream(is);
			matrixPostTranslate(m_matrix_light,m_bmp_light);
			return true;
		}
		/**
		 * 旋转图像
		 * @param matrix 控制旋转的矩阵
		 * @param bitmap 要旋转的图像
		 */
		private void matrixPostTranslate(Matrix matrix,Bitmap bitmap) {
			int tmp_width = bitmap.getWidth();
			int tmp_height = bitmap.getHeight();
			matrix.postTranslate(-tmp_width / 2, -tmp_height / 2); //设置平移位置
			matrix.postScale(m_scale, m_scale);  //设置缩放比例
			matrix.postTranslate(123 * m_scale, 146 * m_scale);
		}

		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			//给Canvas加上抗锯齿标志
			canvas.setDrawFilter(mSetfil);//图片线条（通用）的抗锯齿
			canvas.drawBitmap(m_bmp_disc, m_matrix_disc, mypaint);
			canvas.drawBitmap(m_bmp_light, m_matrix_light, mypaint);
		}

		public void update() {
			if (m_disc_rot_speed > 0.01 || m_state_play == 1){
				if (m_state_play == 1 && m_disc_rot_speed<m_disc_max){
					m_disc_rot_speed += (m_disc_max+0.5f-m_disc_rot_speed)/30;
				}
				else if (m_disc_rot_speed>0.1){
					m_disc_rot_speed -= (m_disc_rot_speed)/40;
				}
				m_matrix_disc .postRotate(m_disc_rot_speed, 123*m_scale, 146*m_scale);
				invalidate();
			}
		}
		
		public void onPause(){
			refreshHandle.stop();
		}
		public void onResume(){
			refreshHandle.run();
		}
		
	}
