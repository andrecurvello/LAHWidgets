package anhoavu.widgets.fileview;

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

	public FileListView(Context context, FileSelectListener listener) {
		super(context);
		System.out.println("Construct a file view, initially bind to "
				+ Environment.getExternalStorageDirectory().getPath());
		adapter = new FileViewArrayAdapter(context, 0);
		adapter.gotoDirectory(new File(Environment
				.getExternalStorageDirectory().getPath()));
		setAdapter(adapter);
		setOnItemClickListener(this);
		selected_file = null;
		this.listener = listener;
		// fill(currentDir);
	}

	public File getSelectedFile() {
		return selected_file;
	}

	public void setStartDirectory(File dir) {
		adapter.gotoDirectory(dir);
	}

	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
//		System.out.println("List item " + position + " click!");
		File f = (File) parent.getAdapter().getItem(position);
		if (f.isDirectory()) {
//			System.out.println("Change to directory " + f.getAbsolutePath());
			adapter.gotoDirectory(f);
		} else {
			selected_file = f;
			// notify the container about the changes
			// check for 'null' to be failsafe
			if (listener != null)
				listener.onFileSelected(selected_file);
		}
	}

}