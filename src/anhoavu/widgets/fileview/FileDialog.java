package anhoavu.widgets.fileview;

import java.io.File;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

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
 * TODO privatize the original {@link AlertDialog} methods
 * 
 * @author Vu An Hoa
 * 
 */
public class FileDialog extends AlertDialog implements FileSelectListener,
		android.content.DialogInterface.OnClickListener {

	/**
	 * The current file selected
	 */
	private File result;

	/**
	 * The {@link FileListView} that list files in the current directory
	 */
	private FileListView file_browse;

	/**
	 * An {@link EditText}
	 */
	private EditText current_selection;

	/**
	 * The {@link FileSelectListener} to update when the user click on 'Select'
	 * to select a file
	 */
	private FileSelectListener listener;

	/**
	 * Construct a dialog and register a listener who will get notified of the
	 * selected file
	 * 
	 * @param context
	 * @param listener
	 */
	public FileDialog(Context context, FileSelectListener listener) {
		super(context);

		File initial_directory = new File(Environment
				.getExternalStorageDirectory().getPath());

		LinearLayout dialog_layout = new LinearLayout(context);
		dialog_layout.setOrientation(LinearLayout.VERTICAL);

		current_selection = new EditText(context);
		current_selection.setTextSize(16);
		current_selection.setText(initial_directory.getAbsolutePath());
		current_selection.setFocusable(false);

		file_browse = new FileListView(context, this, initial_directory);

		dialog_layout.addView(current_selection);
		dialog_layout.addView(file_browse);

		setView(dialog_layout);
		setButton(BUTTON_NEUTRAL, "Cancel", this);
		setButton(BUTTON_POSITIVE, "Select", this);

		this.listener = listener;
	}

	/**
	 * Process the update of current directory
	 */
	public void onFileSelected(File result) {
		this.result = file_browse.getSelectedFile();
		current_selection.setText(result.getAbsolutePath());
	}

	/**
	 * Process when the user click a button ('Cancel' or 'Select')
	 */
	public void onClick(DialogInterface dialog, int which) {
		if (which == BUTTON_NEUTRAL)
			result = null;
		else if (listener != null) // notify the listener
			listener.onFileSelected(result);
	}

}
