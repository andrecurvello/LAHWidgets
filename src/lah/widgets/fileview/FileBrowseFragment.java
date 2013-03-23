package lah.widgets.fileview;

import java.io.File;

import lah.widgets.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * A fragment which allows for user to browse file system
 * 
 * @author L.A.H.
 * 
 */
public class FileBrowseFragment extends Fragment {

	public static final String ARG_INITIAL_DIRECTORY = "initial_directory";

	public static FileBrowseFragment newInstance(String initial_directory,
			IFileSelectListener listener) {
		FileBrowseFragment fragment = new FileBrowseFragment();
		fragment.listener = listener;
		fragment.initial_file = initial_directory;
		Bundle args = new Bundle();
		args.putString(ARG_INITIAL_DIRECTORY, initial_directory);
		fragment.setArguments(args);
		return fragment;
	}

	private FileArrayAdapter file_system_adapter;

	private String initial_file;

	private IFileSelectListener listener;

	public FileBrowseFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			initial_file = getArguments().getString(ARG_INITIAL_DIRECTORY);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		file_system_adapter = new FileArrayAdapter(container.getContext(), 0,
				new File(initial_file));
		View fragment_view = inflater.inflate(R.layout.fragment_file_browse,
				container, false);
		ListView file_list = (ListView) fragment_view
				.findViewById(R.id.file_list);
		OnItemClickListener file_listener = new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				file_system_adapter.onItemSelected(position);
				listener.onFileSelected(file_system_adapter.getItem(position));
			}

		};
		file_list.setAdapter(file_system_adapter);
		file_list.setOnItemClickListener(file_listener);
		return fragment_view;
	}

}
