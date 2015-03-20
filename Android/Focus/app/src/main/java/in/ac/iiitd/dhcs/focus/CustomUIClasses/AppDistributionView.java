package in.ac.iiitd.dhcs.focus.CustomUIClasses;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import in.ac.iiitd.dhcs.focus.R;

/**
 * Created by vedantdasswain on 20/03/15.
 */
public class AppDistributionView extends LinearLayout {
    Context mContext;
    ImageView imageView;
    TextView textView;
    HorizontalGaugeView hgView;

    public AppDistributionView(Context context, AttributeSet attrs) {
        super(context);
        mContext=context;
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.layout_app_distribution, this);
        imageView=(ImageView)findViewById(R.id.appIcon);
        textView=(TextView)findViewById(R.id.durationText);
        hgView=(HorizontalGaugeView)findViewById(R.id.progressView);
    }

    public void setProgress(float progress) {
        hgView.setProgress(progress);
        invalidate();
        requestLayout();
    }
}
