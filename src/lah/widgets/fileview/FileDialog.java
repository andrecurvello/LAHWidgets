package lah.widgets.fileview;

import java.io.File;

import lah.widgets.R;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
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
 * This dialog is safe for reused
 * 
 * TODO privatize the original {@link AlertDialog} methods
 * 
 * @author L.A.H.
 * 
 */
public class FileDialog extends AlertDialog implements DialogInterface.OnClickListener {

	/**
	 * The {@link IFileSelectListener} to update when the user click on 'Select' to select a file
	 */
	private IFileSelectListener file_listener;

	/**
	 * {@link ArrayAdapter} to adapt to the file system
	 */
	private final FileArrayAdapter file_system_adapter;

	/**
	 * Construct a dialog and register a listener who will get notified of the selected file
	 * 
	 * @param context
	 * @param listener
	 */
	public FileDialog(Context context, IFileSelectListener listener, String init_file) {
		super(context);
		file_listener = listener;
		file_system_adapter = new FileArrayAdapter(context, 0, new File(init_file));
		setButton(BUTTON_NEGATIVE, context.getResources().getString(R.string.action_cancel), this);
		setButton(BUTTON_NEUTRAL, context.getResources().getString(R.string.action_up), this);
		setButton(BUTTON_POSITIVE, context.getResources().getString(R.string.action_select), this);
		View view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
				R.layout.file_dialog, null);
		ListView file_list = (ListView) view.findViewById(R.id.file_list);
		final TextView file_path_textview = (TextView) view.findViewById(R.id.current_file);
		OnItemClickListener file_listener = new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				file_system_adapter.onItemSelected(position);
				file_path_textview.setText(file_system_adapter.getCurrentFile().getAbsolutePath());
			}

		};
		file_list.setAdapter(file_system_adapter);
		file_list.setOnItemClickListener(file_listener);
		setView(view);
	}

	/**
	 * Override the {@link AlertDialog}'s dismiss to prevent dialog closing after one of the buttons is clicked. The
	 * dismissal of the dialog is controlled by onClick().
	 */
	@Override
	public void dismiss() {
	}

	/**
	 * Process when the user click a button ('Cancel' or 'Select')
	 */
	public void onClick(DialogInterface dialog, int which) {
		switch (which) {
		case BUTTON_NEGATIVE:
			super.dismiss();
			break;
		case BUTTON_NEUTRAL:
			file_system_adapter.setFilesInParent();
			break;
		case BUTTON_POSITIVE:
		default:
			// notify the listener about the selected file or directory
			if (file_listener != null)
				file_listener.onFileSelected(file_system_adapter.getCurrentFile());
			super.dismiss();
			break;
		}
	}

	@Override
	public void show() {
		super.show();
		file_system_adapter.reList();
	}

}
