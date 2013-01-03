package lah.widgets.fileview;

import java.io.File;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Environment;
import android.text.Layout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * An extension of {@link ListView} to allow users to browse file system. The
 * view is supposed to be embeddable to other UI containers such as
 * {@link Layout}, {@link Dialog} and {@link Activity}.
 * 
 * The {@link FileDialog} provides such a container that allows user to browse
 * and select a file or directory.
 * 
 * @author L.A.H.
 * 
 * @see FileDialog
 * 
 */
@SuppressLint("ViewConstructor")
public class FileListView extends ListView implements
		AdapterView.OnItemClickListener {

	private static final String DEFAULT_DIRECTORY = Environment
			.getExternalStorageDirectory().getPath();

	/**
	 * Adapter to adapt to directory navigation
	 */
	private FileArrayAdapter adapter;

	/**
	 * Listener that this view to respond to
	 */
	private IFileSelectListener listener;

	/**
	 * The file the user currently selected. Must be a file in current_directory
	 */
	private File selected_file;

	public FileListView(Context context, IFileSelectListener listener,
			File initial_directory) {
		super(context);

		adapter = new FileArrayAdapter(context, 0);
		if (initial_directory == null || !initial_directory.exists()) {
			initial_directory = new File(DEFAULT_DIRECTORY);
			adapter.gotoDirectory(initial_directory);
		} else {
			if (initial_directory.isDirectory())
				adapter.gotoDirectory(initial_directory);
			else
				adapter.gotoDirectory(initial_directory.getParentFile());
		}
		selected_file = initial_directory;

		setAdapter(adapter);
		setOnItemClickListener(this);

		this.listener = listener;
	}

	/**
	 * Get the currently selected file.
	 * 
	 * @return
	 */
	public File getSelectedFile() {
		return selected_file;
	}

	/**
	 * Display the parent directory
	 */
	public void goUp() {
		adapter.gotoParent();
	}

	/**
	 * Response to a click on an item. If it is a file, set the currently select
	 * file to the selected item and notify the caller that the user select a
	 * file. Otherwise, if the item is a directory, navigate into that directory
	 * and also notify the caller.
	 */
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		File f = (File) parent.getAdapter().getItem(position);

		if (f.isDirectory())
			adapter.gotoDirectory(f);

		// We will notify the container of the currently chosen file/directory
		selected_file = f;
		// notify the container about the changes check for 'null' to be
		// failsafe
		if (listener != null)
			listener.onFileSelected(selected_file);
	}

	/**
	 * Set the initial directory to display
	 * 
	 * @param dir
	 */
	public void setStartDirectory(File dir) {
		adapter.gotoDirectory(dir);
	}

}