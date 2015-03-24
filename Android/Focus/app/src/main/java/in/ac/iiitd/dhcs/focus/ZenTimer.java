package in.ac.iiitd.dhcs.focus;

import android.os.CountDownTimer;
import android.widget.ViewFlipper;

import in.ac.iiitd.dhcs.focus.CustomUIClasses.TimerView;

/**
 * Created by vedantdasswain on 24/03/15.
 */
public class ZenTimer extends CountDownTimer {
    private String TAG="ZenTimer";
    TimerView timerView;
    ViewFlipper viewFlipper;
    long millisInFuture;

    /**
     * @param millisInFuture    The number of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receive
     *                          {@link #onTick(long)} callbacks.
     */
    public ZenTimer(long millisInFuture, long countDownInterval,TimerView timerView, ViewFlipper viewFlipper) {
        super(millisInFuture, countDownInterval);
        this.timerView=timerView;
        this.millisInFuture=millisInFuture;
        this.viewFlipper=viewFlipper;
    }

    @Override
    public void onTick(long millisUntilFinished) {
//        Log.v(TAG, "" + millisUntilFinished);
        timerView.setProgress(((float)(millisInFuture-millisUntilFinished)/(float)millisInFuture)*100);
        timerView.setProgressValue(millisUntilFinished);
    }

    @Override
    public void onFinish() {
        viewFlipper.showPrevious();
    }
}
