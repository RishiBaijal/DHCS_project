package in.ac.iiitd.dhcs.focus;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import in.ac.iiitd.dhcs.focus.R;

/**
 * Created by Simran on 3/24/2015.
 */
public class OverlayActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.overlay_layout);
    }

    public void nextOverlay(){
        setContentView(R.layout.fragment_productivity);
    }
}

