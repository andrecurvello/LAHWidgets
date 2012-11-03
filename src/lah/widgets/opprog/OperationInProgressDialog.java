package lah.widgets.opprog;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;

public class OperationInProgressDialog extends AlertDialog {

	public OperationInProgressDialog(Context context) {
		super(context);
		initializeView();
	}

	private void initializeView() {
		Context context = getContext();

		LinearLayout content = new LinearLayout(context);
		content.setOrientation(LinearLayout.VERTICAL);

		message_textview = new TextView(context);
		message_textview.setPadding(10, 0, 10, 0);

		operation_progress_bar = new ProgressBar(context, null,
				android.R.attr.progressBarStyleHorizontal);
		operation_progress_bar.setPadding(10, 5, 10, 0);

		progress_textview = new TextView(context);
		progress_textview.setPadding(0, 0, 10, 0);
		progress_textview.setGravity(Gravity.RIGHT);

		content.addView(message_textview, LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		content.addView(operation_progress_bar, LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		content.addView(progress_textview, LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);

		setView(content);
	}

	String progress_text_format = "%1d";

	TextView message_textview;

	ProgressBar operation_progress_bar;

	TextView progress_textview;

	public void setMessage(CharSequence msg) {
		message_textview.setText(msg);
	}

	public void setProgressTextFormat(String format) {
		progress_text_format = format;
	}

	public void setProgress(int progress) {
		System.out.println("Update progress to " + progress);
		String progress_txt = String.format(progress_text_format, progress);
		progress_textview.setText(progress_txt);
		operation_progress_bar.setProgress(progress);
	}

	public void setMaxProgress(int max_progress) {
		operation_progress_bar.setMax(max_progress);
	}

	public void setIndeterminate(boolean indeterminate) {
		operation_progress_bar.setIndeterminate(indeterminate);
	}

	public static void testDialog(final Activity activity) {
		final OperationInProgressDialog dialog = new OperationInProgressDialog(
				activity);
		dialog.setTitle("A Very Long Operation Is In Progress");
		dialog.setMessage("Operating");
		dialog.show();

		final AtomicInteger value = new AtomicInteger(0);
		final int max = 100000;
		final int delta = 200;
		final int multiplier = 10;

		dialog.setMaxProgress(max);
		dialog.setIndeterminate(true);
		dialog.setProgressTextFormat("%1d bytes downloaded.");
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				value.addAndGet(delta * multiplier);
				if (value.intValue() <= max) {
					activity.runOnUiThread(new Runnable() {

						public void run() {
							// switch to determinate mode in the middle
							if (value.intValue() > 20000) {
								dialog.setIndeterminate(false);
							}
							dialog.setProgress(value.intValue());
						}
					});
				}
			}
		};
		Timer t = new Timer();
		t.schedule(task, 0, 1000);
	}

}
