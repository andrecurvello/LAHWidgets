package anhoavu.widgets.fileview;

import java.io.File;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Environment;
import android.text.Layout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import anhoavu.widgets.fileview.R;

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
			listener.onFileSelected(selected_file);
		}
	}

}

/**
 * An internal implementation of a {@link ListAdapter} to support
 * {@link FileListView}. This {@link Adapter} will watch a directory and its
 * content.
 * 
 * @author Vu An Hoa
 * 
 */
class FileViewArrayAdapter extends ArrayAdapter<File> {

	/**
	 * The current directory in tracking
	 */
	private File directory;

	/**
	 * Array of all files presented in {@code directory}
	 */
	private File[] files;

	public FileViewArrayAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
	}

	public void gotoDirectory(File directory) {
		if (directory.canRead()) {
			this.directory = directory;
			files = directory.listFiles();
			// System.out.println("Files in " + directory.getAbsolutePath() +
			// ":");
			// for (File f : files)
			// System.out.println(f.getAbsolutePath());
			notifyDataSetChanged();
		}
	}

	public void gotoParent() {
		File p = directory.getParentFile();
		if (p != null)
			gotoDirectory(p);
	}

	@Override
	public File getItem(int position) {
		return files[position];
	}

	@Override
	public int getCount() {
		return files.length;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Apply the following guide to fix the list item view
		// http://docs.xamarin.com/android/tutorials/ListViews_and_Adapters
		Context context = parent.getContext();
		File f = getItem(position);
		
		LinearLayout layout = new LinearLayout(context);
		
		ImageView f_icon = new ImageView(context);		
		f_icon.setImageResource(f.isDirectory() ? R.drawable.directory : R.drawable.generic_text);
		
		TextView v = new TextView(context);
		v.setText(f.getName());
		
		layout.addView(f_icon, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		layout.addView(v, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		
		return layout;
	}

}
