package lah.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

/**
 * Abstract class to display a large (potentially infinite) rectangular view which can be zoom in/out (via pinch
 * gesture) and navigate (via tap-and-drag gesture). Example usecases are a map view or a PDF document view.
 * 
 * This abstract class only maintains the <b>logically local/relative</b> geometry information which views extending
 * this class can make use of when performing the rendering. At the basic, this view maintains how a viewport changes.
 * 
 * @author L.A.H.
 * 
 */
public abstract class AbstractZoomableScrollView extends View {

	private static final boolean DEBUG = false;

	private static final String TAG = "Abstract Zoomable Scroll View";

	private GestureDetector common_gesture_detector;

	private boolean is_zooming;

	private ScaleGestureDetector scale_gesture_detector;

	/**
	 * Viewport left-top position, the viewport's width and height are obtained from the view's available methods
	 * (namely, {@link #getWidth()} and {@link #getHeight()} respectively).
	 */
	protected float viewport_X, viewport_Y;

	/**
	 * Current zoom factor of this view, initialize to 1.0f
	 */
	protected float zoom_factor = 1.0f;

	protected float zoom_in_threshold = 1.25f, zoom_out_threshold = 1.0f / zoom_in_threshold;

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

	protected float getViewportX() {
		return viewport_X;
	}

	protected float getViewportY() {
		return viewport_Y;
	}

	protected float getZoomFactor() {
		return zoom_factor;
	}

	void init() {
		common_gesture_detector = new GestureDetector(getContext(), new ZSVGGestureListener());
		scale_gesture_detector = new ScaleGestureDetector(getContext(), new ZSVGScaleGestureListener());
		viewport_X = viewport_Y = 0.0f;
		zoom_factor = 1.0f;
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
				setViewportX(viewport_X + distanceX);
				setViewportY(viewport_Y + distanceY);
				if (getParent() instanceof View)
					((View) getParent()).invalidate();
				invalidate();
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

	protected void setViewportX(float x) {
		viewport_X = x;
		if (viewport_X < 0)
			viewport_X = 0;
	}

	protected void setViewportY(float y) {
		viewport_Y = y;
		if (viewport_Y < 0)
			viewport_Y = 0;
	}

	protected void setZoomFactor(float zf) {
		zoom_factor = zf;
	}

	class ZSVGScaleGestureListener implements ScaleGestureDetector.OnScaleGestureListener {

		@Override
		public boolean onScale(ScaleGestureDetector detector) {
			if (DEBUG)
				Log.v(TAG, "onScale");
			if (detector.getScaleFactor() >= zoom_in_threshold || detector.getScaleFactor() <= zoom_out_threshold) {
				zoom_factor *= detector.getScaleFactor();
				if (getParent() instanceof View)
					((View) getParent()).invalidate();
				invalidate();
				return true;
			} else
				return false;
		}

		@Override
		public boolean onScaleBegin(ScaleGestureDetector detector) {
			if (DEBUG)
				Log.v(TAG, "onScaleBegin");
			is_zooming = true;
			return true;
		}

		@Override
		public void onScaleEnd(ScaleGestureDetector detector) {
			if (DEBUG)
				Log.v(TAG, "onScaleEnd");
			is_zooming = false;
		}
	}

}
