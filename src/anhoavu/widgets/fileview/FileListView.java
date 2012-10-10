package anhoavu.widgets.fileview;

import java.io.File;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.Layout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * An extension of {@link ListView} to allow users to browse file system. The
 * view is supposed to be embeddable to other UI containers such as
 * {@link Layout}, {@link Dialog} and {@link Activity}. The {@link FileDialog}
 * provides such a container that allows user to browse and select a file or
 * directory.
 * 
 * @author Vu An Hoa
 * 
 */
@SuppressLint("ViewConstructor")
public class FileListView extends ListView implements
		android.widget.AdapterView.OnItemClickListener {

	private FileViewArrayAdapter adapter;

	private File selected_file;

	FileSelectListener listener;

	public FileListView(Context context, FileSelectListener listener,
			File initial_directory) {
		super(context);

		adapter = new FileViewArrayAdapter(context, 0);
		adapter.gotoDirectory(initial_directory);
		setAdapter(adapter);
		setOnItemClickListener(this);

		selected_file = initial_directory;
		this.listener = listener;
	}

	public File getSelectedFile() {
		return selected_file;
	}

	public void setStartDirectory(File dir) {
		adapter.gotoDirectory(dir);
	}

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

	public void goUp() {
		adapter.gotoParent();
	}

}