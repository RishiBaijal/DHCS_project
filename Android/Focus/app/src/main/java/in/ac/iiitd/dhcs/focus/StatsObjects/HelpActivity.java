package in.ac.iiitd.dhcs.focus.StatsObjects;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ScrollView;
import android.widget.TextView;

import in.ac.iiitd.dhcs.focus.R;

public class HelpActivity extends ActionBarActivity {

    public String help = "Tracked Apps\n" +
            "1. The first activity screen after install lets you select the apps that you wish to be tracked. \n" +
            "\n" +
            "2. The scale on this page would allow you to set the productivity metric for that app- on a scale of 0 to 10 (0 if you don't use that app for productivity ever and 10 if it is highly productive for you).\n" +
            "\n" +
            "3. After you click \"Save\" you will be directed to the main screen\n" +
            " You can visit this screen in future from the action bar and make changes\n" +
            "\n" +
            "Main Screen-\"Productivity\" tab\n" +
            "4. The meter on the main tab reflects today's productivity stats. And the red marker demarcates today's goal.\n" +
            "\n" +
            "5. Below the dial, you can see the contribution of tracked apps to your total productivity and clicking on each would allow you to see that particular app's detailed usage statistics.\n" +
            "\n" +
            "Statistics Screen\n" +
            "6. The \"Statistics\" tab to the right allows you to view overall statistics in detail( since you starting using FOCUS).\n" +
            "\n" +
            "Zen Mode Screen\n" +
            "7. The \"Zen Mode\" tab on the left enables you to \"Take a break\" from the phone and focus on other important things. You can set a duration on the timer and during this time, FOCUS monitors if you did or didn't try to open any other app.\n" +
            "\n" +
            "Settings Hamburger\n" +
            "8. You can change your \"tracked apps\" anytime from this settings hamburger.\n" +
            "9. The \"Share\" option allows us to access the time you spent on each feature/part of FOCUS. We would need you to share this at the end of the week.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        TextView mTextStatus = (TextView) findViewById(R.id.TEXT_STATUS_ID);
        ScrollView mScrollView = (ScrollView) findViewById(R.id.SCROLLER_ID);

        mTextStatus.setText(help);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_help, menu);
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
