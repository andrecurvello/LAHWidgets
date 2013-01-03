package lah.widgets.fileview;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import lah.widgets.R;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

/**
 * An internal implementation of a {@link ListAdapter} to support
 * {@link FileListView}. This {@link Adapter} will watch a directory and its
 * content.
 * 
 * @author L.A.H.
 * 
 */
public class FileArrayAdapter extends ArrayAdapter<File> {

	/**
	 * The current directory we are listing
	 */
	private File current_directory;

	/**
	 * Comparator used to sort files
	 */
	private Comparator<File> file_comparator = new FileComparator();

	/**
	 * Array of all files presented in {@code directory}
	 */
	private List<File> files;

	public FileArrayAdapter(Context context, int textViewResourceId) {
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
		f_icon.setImageResource(f.isDirectory() ? R.drawable.ic_directory
				: R.drawable.ic_file);

		TextView v = new TextView(context);
		v.setText(f.getName());
		v.setTextSize(20);
		v.setGravity(Gravity.CENTER_VERTICAL);

		layout.addView(f_icon, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		layout.addView(v, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

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
		if (current_directory != null && current_directory.exists()) {
			File p = current_directory.getParentFile();
			if (p != null)
				gotoDirectory(p);
		}
	}

}
