package anhoavu.widgets.fileview;

import java.io.File;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * A reusable file/directory {@link ListView}.
 * 
 * In TeXPortal project, {@link FileListView} allow users to
 * <ul>
 * <li>choose a TeX or LaTeX source (input) file; and</li>
 * <li>choose a local TeX package repository.</li>
 * <li>[some time in the future] choose a local installation location.</li>
 * </ul>
 * 
 * @author Vu An Hoa
 * 
 */
public class FileDialog extends Dialog implements
		android.view.View.OnClickListener, FileSelectListener {

	private File result;

	private View dialog_layout;

	private FileListView file_browse;

	private TextView current_selection;

	private Button select_button, cancel_button;

	private FileSelectListener listener;

	public FileDialog(Context context, FileSelectListener listener) {
		super(context);
//		this.setContentView(R.layout.file_dialog);
//		LinearLayout v = (LinearLayout) this.findViewById(R.id.file_view);
//		v.addView(new FileListView(context, this));
		initialize(context);
		this.listener = listener;
	}

	public void initialize(Context context) {
		dialog_layout = new LinearLayout(context);
		((LinearLayout) dialog_layout).setOrientation(LinearLayout.VERTICAL);

		// Chosen item
		current_selection = new TextView(context);

		// List view for the directories
		file_browse = new FileListView(context, this);
		/*
		 * http://stackoverflow.com/questions/6798867/android-how-to-
		 * programmatically-set-the-size-of-a-layout
		 */
		LinearLayout.LayoutParams file_browse_layout_params = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, 400);
		file_browse.setLayoutParams(file_browse_layout_params);

		// The buttons
		LinearLayout buttons_layout = new LinearLayout(context);
		buttons_layout.setOrientation(LinearLayout.HORIZONTAL);

		select_button = new Button(context);
		select_button.setText("Select");
		select_button.setOnClickListener(this);

		cancel_button = new Button(context);
		cancel_button.setText("Cancel");
		cancel_button.setOnClickListener(this);

		// Add items to the button group and the main dialog
		buttons_layout.addView(cancel_button);
		buttons_layout.addView(select_button);
		((ViewGroup) dialog_layout).addView(current_selection);
		((ViewGroup) dialog_layout).addView(file_browse);
		((ViewGroup) dialog_layout).addView(buttons_layout);

		// Set the content
		setContentView(dialog_layout);
		setTitle("Select TeX input file");
	}

	public void onClick(View v) {
		if (v != select_button)
			result = null;
		// notify the listener
		listener.onFileSelected(result);
	}

	public void onFileSelected(File result) {
		this.result = file_browse.getSelectedFile();
		current_selection.setText(result.getAbsolutePath());
	}

}
