package lah.widgets;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

public class UniversalHierarchicalBrowsingActivity extends FragmentActivity implements View.OnClickListener {

	private static final int[] button_ids = { R.id.btn_done, R.id.btn_nav_up, R.id.btn_cancel, R.id.btn_create };

	protected static final int header_fragment_id = R.id.header_fragment;

	protected ImageButton button_create;

	protected ListView object_listview;

	protected void goUp() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.btn_cancel) {
			setResult(RESULT_CANCELED);
			finish();
		} else if (id == R.id.btn_nav_up) {
			goUp();
		} else if (id == R.id.btn_create) {
			showCreate();
		} else if (id == R.id.btn_done) {
			setResult(RESULT_OK, null);
			finish();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_universal_hierarchical_browsing);
		object_listview = (ListView) findViewById(R.id.object_listview);
		button_create = (ImageButton) findViewById(R.id.btn_create);
		for (int id : button_ids) {
			View v = findViewById(id);
			v.setOnClickListener(this);
		}
	}

	protected void showCreate() {
		// TODO Auto-generated method stub
	}

}
