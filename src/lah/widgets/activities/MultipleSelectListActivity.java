package lah.widgets.activities;

import lah.widgets.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

public abstract class MultipleSelectListActivity extends Activity {

	private ListView choices_listview;

	private boolean select_all = true;

	protected ListView getListView() {
		return choices_listview;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_multiple_select_list);
		choices_listview = (ListView) findViewById(R.id.choice_list);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.multiple_select, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_save) {
			save();
			finish();
		} else if (item.getItemId() == R.id.action_cancel) {
			finish();
		} else if (item.getItemId() == R.id.action_select_all) {
			for (int i = 0; i < choices_listview.getCount(); i++)
				choices_listview.setItemChecked(i, select_all);
			select_all = !select_all;
		}
		return super.onOptionsItemSelected(item);
	}

	protected abstract void save();

}
