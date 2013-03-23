package lah.widgets.fileview;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import lah.widgets.R;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Implementation of {@link ArrayAdapter} that adapts to file system interaction
 * 
 * @author L.A.H.
 * 
 */
public class FileArrayAdapter extends ArrayAdapter<File> {

	private static final String DEFAULT_DIRECTORY = Environment
			.getExternalStorageDirectory().getPath();

	/**
	 * The current file
	 */
	private File current_file;

	/**
	 * Comparator to sort files
	 */
	private Comparator<File> file_comparator;

	/**
	 * The list of files, should be in the same directory
	 */
	private List<File> files;

	/**
	 * Icons for file/directory
	 */
	private final Drawable ic_file, ic_directory;

	/**
	 * Construct a new adapter, with the selected file set to init_file or a
	 * safe default directory /sdcard if this init_file does not exist, cannot
	 * be read, or is null
	 * 
	 * @param context
	 * @param textViewResourceId
	 * @param init_file
	 *            Initial file to bind this adapter to
	 */
	public FileArrayAdapter(Context context, int textViewResourceId,
			File init_file) {
		super(context, textViewResourceId);
		ic_directory = context.getResources().getDrawable(
				R.drawable.ic_directory);
		ic_directory.setBounds(0, 0, ic_directory.getIntrinsicWidth(),
				ic_directory.getIntrinsicHeight());
		ic_file = context.getResources().getDrawable(R.drawable.ic_file);
		ic_file.setBounds(0, 0, ic_file.getIntrinsicWidth(),
				ic_file.getIntrinsicHeight());
		file_comparator = new FileComparator(); // default file comparator
		setCurrentFile(init_file);
	}

	@Override
	public int getCount() {
		if (files != null)
			return files.size();
		else
			return 0;
	}

	/**
	 * Get the current file
	 * 
	 * @return The current file
	 */
	public File getCurrentFile() {
		return current_file;
	}

	@Override
	public File getItem(int position) {
		return files.get(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Context context = parent.getContext();
		File f = getItem(position); // get file at indicated position
		TextView f_view = new TextView(context);
		f_view.setGravity(Gravity.CENTER_VERTICAL);
		f_view.setCompoundDrawables(f.isDirectory() ? ic_directory : ic_file,
				null, null, null);
		f_view.setText(f.getName());
		f_view.setTextAppearance(getContext(),
				android.R.style.TextAppearance_Medium);
		return f_view;
	}

	/**
	 * Binder view should invoke this first when a file item is selected
	 * 
	 * @param position
	 */
	public void onItemSelected(int position) {
		setCurrentFile(getItem(position));
	}

	/**
	 * Set the current file
	 * 
	 * @param file
	 *            The new file
	 */
	public void setCurrentFile(File file) {
		if (file == null || !file.exists())
			current_file = new File(DEFAULT_DIRECTORY);
		else
			current_file = file;
		setFilesInDirectory(current_file.isFile() ? current_file
				.getParentFile() : current_file);
	}

	/**
	 * Update the list of files to contain all files in the same directory
	 * 
	 * @param directory
	 *            The directory where new files are located in
	 */
	public void setFilesInDirectory(File directory) {
		// Input check
		if (directory == null || directory.isFile() || !directory.canRead())
			return;

		// List and sort all files in the directory
		File[] all_files = directory.listFiles();
		Arrays.sort(all_files, file_comparator);

		// Construct the new file list without hidden files removed, use a
		// new list object and latter swap with the main field to prevent
		// interruption etc.
		List<File> new_file_list = new ArrayList<File>();
		for (File f : all_files) {
			if (!f.isHidden())
				new_file_list.add(f);
		}

		// Finally, swap new changes and notify for display changes
		files = new_file_list;
		notifyDataSetChanged();
	}

	/**
	 * Set the files list to contain all files in the parent directory of the
	 * currently selected file (if it is a directory) or its grandparent if it
	 * is a file.
	 */
	public void setFilesInParent() {
		if (current_file != null && current_file.exists()) {
			File parent = current_file.getParentFile();
			if (current_file.isFile())
				parent = parent.getParentFile();
			if (parent != null)
				setCurrentFile(parent);
		}
	}

	/**
	 * Update the files list
	 */
	public void reList() {
		setCurrentFile(current_file);
	}

}
