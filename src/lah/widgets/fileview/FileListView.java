package lah.widgets.fileview;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Environment;
import android.text.Layout;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Comparator to compare files, for display purpose. Rule: Directory always
 * smaller than file. Otherwise, they should be compared by names. This
 * comparator is used to compare files.
 * 
 * @author Vu An Hoa
 * 
 */
class FileComparator implements Comparator<File> {

	/**
	 * Compare two files, two directory or a file and a directory.
	 */
	public int compare(File arg0, File arg1) {
		assert (arg0 != null);
		assert (arg1 != null);
		if (arg0.isDirectory())
			return arg1.isDirectory() ? arg0.getName()
					.compareTo(arg1.getName()) : -1;
		else
			return arg1.isDirectory() ? 1 : arg0.getName().compareTo(
					arg1.getName());
	}

}

/**
 * An extension of {@link ListView} to allow users to browse file system. The
 * view is supposed to be embeddable to other UI containers such as
 * {@link Layout}, {@link Dialog} and {@link Activity}.
 * 
 * The {@link FileDialog} provides such a container that allows user to browse
 * and select a file or directory.
 * 
 * @author Vu An Hoa
 * 
 * @see FileDialog
 * 
 */
@SuppressLint("ViewConstructor")
public class FileListView extends ListView implements
		android.widget.AdapterView.OnItemClickListener {

	/**
	 * Interface for caller to register a call back function when the user
	 * select a file or navigate into a sub-directory, probably for display
	 * purpose
	 * 
	 * @author Vu An Hoa
	 * 
	 */
	public interface FileSelectListener {

		void onFileSelected(File result);

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
		 * Array of all files presented in {@code directory}
		 */
		private List<File> files;

		public FileViewArrayAdapter(Context context, int textViewResourceId) {
			super(context, textViewResourceId);
		}

		@Override
		public int getCount() {
			if (files != null)
				return files.size();
			else
				return 0;
		}

		@Override
		public File getItem(int position) {
			return files.get(position);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Apply the following guide to fix the list item view
			// http://docs.xamarin.com/android/tutorials/ListViews_and_Adapters
			Context context = parent.getContext();
			File f = getItem(position);

			LinearLayout layout = new LinearLayout(context);

			ImageView f_icon = new ImageView(context);
			f_icon.setImageResource(f.isDirectory() ? R.drawable.directory
					: R.drawable.generic_text);

			TextView v = new TextView(context);
			v.setText(f.getName());
			v.setTextSize(20);
			v.setGravity(Gravity.CENTER_VERTICAL);

			layout.addView(f_icon, LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			layout.addView(v, LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);

			return layout;
		}

		public void gotoDirectory(File directory) {
			if (directory.canRead()) {
				// List all files in the directory
				File[] all_files = directory.listFiles();

				// Sort the files
				Arrays.sort(all_files, file_comparator);

				// Construct the new file list to display
				// with hidden files removed, use a new list object and latter
				// swap with the main field to prevent interruption etc.
				List<File> new_file_list = new ArrayList<File>();
				for (File f : all_files)
					if (!f.isHidden())
						new_file_list.add(f);

				// Finally, swap new changes and notify for display changes
				current_directory = directory;
				files = new_file_list;
				notifyDataSetChanged();
			}
		}

		public void gotoParent() {
			File p = current_directory.getParentFile();
			if (p != null)
				gotoDirectory(p);
		}

	}

	private static final String DEFAULT_DIRECTORY = Environment
			.getExternalStorageDirectory().getPath();

	/**
	 * Adapter to adapt to directory navigation
	 */
	private FileViewArrayAdapter adapter;

	/**
	 * The current directory we are listing
	 */
	private File current_directory;

	/**
	 * Comparator used to sort files
	 */
	private Comparator<File> file_comparator = new FileComparator();

	/**
	 * Listener that this view to respond to
	 */
	FileSelectListener listener;

	/**
	 * The file the user currently selected. Must be a file in current_directory
	 */
	private File selected_file;

	public FileListView(Context context, FileSelectListener listener,
			File initial_directory) {
		super(context);

		adapter = new FileViewArrayAdapter(context, 0);
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