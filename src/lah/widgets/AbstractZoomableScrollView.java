package lah.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

/**
 * Abstract class to display a group of views, allow for zooming and navigation. This class maintains the logical
 * geometry information for views (extending this class) to make use when rendering themselves from user interactions
 * such as pinch-to-zoom or tap-and-move gestures.
 * 
 * @author L.A.H.
 * 
 */
public abstract class AbstractZoomableScrollView extends View {

	private static final boolean DEBUG = false;

	private static final String TAG = "AbstractZoomableScrollView";

	private GestureDetector common_gesture_detector;

	private boolean is_zooming;

	private ScaleGestureDetector scale_gesture_detector;

	/**
	 * Current zoom factor of this view
	 */
	private float zoom_factor;

	float zoom_in_threshold = 1.25f, zoom_out_threshold = 1.0f / zoom_in_threshold;

	public AbstractZoomableScrollView(Context context) {
		super(context);
		init();
	}

	public AbstractZoomableScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public AbstractZoomableScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	protected float getCurrentScaleFactor() {
		return zoom_factor;
	}

	void init() {
		common_gesture_detector = new GestureDetector(getContext(), new ZSVGGestureListener());
		scale_gesture_detector = new ScaleGestureDetector(getContext(), new ZSVGScaleGestureListener());

	}

	protected boolean isZooming() {
		return is_zooming;
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (DEBUG)
			Log.v(TAG, "onTouchEvent" + ev);
		if (scale_gesture_detector.onTouchEvent(ev))
			if (DEBUG)
				Log.v(TAG, "Scale detector consumes event " + ev);
		return common_gesture_detector.onTouchEvent(ev);
	}

	public void onZoom() {
		invalidate();
	}

	protected void reset() {
		zoom_factor = 1.0f;
	}

	class ZSVGGestureListener implements GestureDetector.OnGestureListener {

		@Override
		public boolean onDown(MotionEvent e) {
			if (DEBUG)
				Log.v(TAG, "onDown");
			return true;
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			if (DEBUG)
				Log.v(TAG, "onFling");
			return true;
		}

		@Override
		public void onLongPress(MotionEvent e) {
			if (DEBUG)
				Log.v(TAG, "onLongPress");
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			if (DEBUG)
				Log.v(TAG, "onScroll");
			if (Math.abs(distanceX) + Math.abs(distanceY) > 20) {
				scrollBy((int) distanceX, (int) distanceY);
				return true;
			}
			return false;
		}

		@Override
		public void onShowPress(MotionEvent e) {
			if (DEBUG)
				Log.v(TAG, "onShowPress");
		}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			if (DEBUG)
				Log.v(TAG, "onSingleTapUp");
			return true;
		}
	}

	class ZSVGScaleGestureListener implements ScaleGestureDetector.OnScaleGestureListener {

		@Override
		public boolean onScale(ScaleGestureDetector detector) {
			if (DEBUG)
				Log.v(TAG, "onScale");
			if (detector.getScaleFactor() >= zoom_in_threshold || detector.getScaleFactor() <= zoom_out_threshold) {
				// setScaleX(detector.getScaleFactor());
				// setScaleY(detector.getScaleFactor());
				// zoom_factor = zoom_factor * detector.getScaleFactor();
				onZoom();
				return true;
			} else
				return false;
		}

		@Override
		public boolean onScaleBegin(ScaleGestureDetector detector) {
			is_zooming = true;
			if (DEBUG)
				Log.v(TAG, "onScaleBegin");
			return true;
		}

		@Override
		public void onScaleEnd(ScaleGestureDetector detector) {
			is_zooming = false;
			if (DEBUG)
				Log.v(TAG, "onScaleEnd");
		}
	}

}
