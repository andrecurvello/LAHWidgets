package lah.widgets;

import lah.widgets.fileview.FileArrayAdapter;
import lah.widgets.fileview.IFileSelectListener;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;

public class UniversalStorageNavigationFragment extends Fragment implements View.OnClickListener {

	private static final int[] button_ids = { R.id.btn_box, R.id.btn_dropbox, R.id.btn_gdrive, R.id.btn_sdcard,
			R.id.btn_done, R.id.btn_nav_up, R.id.btn_cancel, R.id.btn_create };

	public static UniversalStorageNavigationFragment newInstance() {
		UniversalStorageNavigationFragment fragment = new UniversalStorageNavigationFragment();
		return fragment;
	}

	FileArrayAdapter adapter;

	ImageButton button_create;

	ListView object_listview;

	public UniversalStorageNavigationFragment() {
		// Required empty public constructor
	}

	IFileSelectListener file_listener;

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.btn_cancel) {
			getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
		} else if (id == R.id.btn_nav_up) {
			adapter.setFilesInParent();
		} else if (id == R.id.btn_create) {
		} else if (id == R.id.btn_done) {
			if (file_listener != null)
				file_listener.onFileSelected(adapter.getCurrentFile());
		} else if (id == R.id.btn_box) {
			// TODO Implement
		} else if (id == R.id.btn_dropbox) {
			// TODO Implement
		} else if (id == R.id.btn_gdrive) {
			// TODO Implement
		} else if (id == R.id.btn_sdcard) {
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			// mParam1 = getArguments().getString(ARG_PARAM1);
			// mParam2 = getArguments().getString(ARG_PARAM2);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_universal_storage_navigation, container, false);
		object_listview = (ListView) view.findViewById(R.id.object_listview);
		adapter = new FileArrayAdapter(getActivity(), 0, Environment.getExternalStorageDirectory());
		object_listview.setAdapter(adapter);
		object_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				adapter.onItemSelected(position);
			}
		});
		button_create = (ImageButton) view.findViewById(R.id.btn_create);
		button_create.setVisibility(View.GONE);
		for (int id : button_ids) {
			View v = view.findViewById(id);
			v.setOnClickListener(this);
		}
		return view;
	}

}
