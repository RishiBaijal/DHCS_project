package in.ac.iiitd.dhcs.focus;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;


public class TrackedAppsAcitivity extends ActionBarActivity {

    private static String TAG = "TrackedAppsActivity";

    private static TrackedAppListAdapter trackedAppsListAdapter;
    private static ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracked_apps);
        listView = (ListView) findViewById(R.id.listView);
        trackedAppsListAdapter = new TrackedAppListAdapter(TrackedAppsAcitivity.this,R.layout.tracked_apps_list_item);
        listView.setAdapter(trackedAppsListAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tracked_apps_acitivity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
