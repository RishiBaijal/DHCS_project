package in.ac.iiitd.dhcs.focus.CustomUIClasses;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import in.ac.iiitd.dhcs.focus.R;

/**
 * TODO: document your custom view class.
 */
public class TimerView extends View {
    private static final String TAG="TimerView";

    private Context mContext;
    private int mBackgroundColor,mBackgroundWidth,mPrimaryColor,mPrimaryWidth,
            mRegularTextSize,mPercentTextSize;
    private Paint mArcPaintBackground,mArcPaintPrimary,mTargetMarkPaint,
            mRegularText,mPercentText,mTargetText,mTitleText;
    private float mPadding,mProgressPercent=30,mTarget=30;
    private long mProgressValue=0;
    private RectF mDrawingRect;
    private static double M_PI_2 = Math.PI/2;

    //MeterColour
    private  int meterColour;
    //TargetColour
    private  int targetColour;

    public TimerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext=context;
        init();
    }

    private void init() {
        Resources res = mContext.getResources();
        float density = res.getDisplayMetrics().density;

        meterColour=res.getColor(R.color.accent);

        targetColour=res.getColor(R.color.meterGauge);

        mBackgroundColor = res.getColor(R.color.meterBackground);
        mBackgroundWidth = (int)(5 * density); // default to 20dp
        mPrimaryColor = meterColour;
        mPrimaryWidth = (int)(5 * density);  // default to 20dp

        mRegularTextSize = (int)(mBackgroundWidth * 10); //Double the size of the width;
        mPercentTextSize=(int)(mBackgroundWidth * 3);

        mArcPaintBackground = new Paint() {
            {
                setDither(true);
                setStyle(Style.STROKE);
                setStrokeCap(Cap.SQUARE);
                setStrokeJoin(Join.BEVEL);
                setAntiAlias(true);
            }
        };
        mArcPaintBackground.setColor(mBackgroundColor);
        mArcPaintBackground.setStrokeWidth(mBackgroundWidth);

        mArcPaintPrimary = new Paint() {
            {
                setDither(true);
                setStyle(Style.STROKE);
                setStrokeCap(Cap.ROUND);
                setStrokeJoin(Join.ROUND);
                setAntiAlias(true);
            }
        };
        mArcPaintPrimary.setColor(mPrimaryColor);
        mArcPaintPrimary.setStrokeWidth(mPrimaryWidth);

        // get widest drawn element to properly pad the rect we draw inside
        float maxW =  mBackgroundWidth;
        // arc is drawn with it's stroke center at the rect size provided, so we have to pad
        // it by half to bring it inside our bounding rect
        mPadding = maxW / 2;
//        mProgressPercent = 0;

        Typeface tf = Typeface.create("Helvetica",Typeface.NORMAL);
        mRegularText=new Paint();
        mRegularText.setColor(res.getColor(R.color.meterGauge));
        mRegularText.setTextSize(mRegularTextSize);
        mRegularText.setTextAlign(Paint.Align.CENTER);
        mRegularText.setTypeface(tf);

    }

    public void setProgress(float progress) {
        mProgressPercent = progress;
        mTarget=progress;
//        Log.v(TAG,progress+"%");

        invalidate();
        requestLayout();
    }

    public void setProgressValue(long value) {
        mProgressValue = value/1000;
//        Log.v(TAG,"time: "+mProgressValue);
        invalidate();
        requestLayout();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);// bound our drawable arc to stay fully within our canvas
        // full circle (start at 270, the "top")
        canvas.drawArc(mDrawingRect, 270, 360, false, mArcPaintBackground);

        float progress=mProgressPercent;
        if(mProgressPercent>100)
            progress=100f;

        // draw starting at top of circle in the negative (counter-clockwise) direction
        canvas.drawArc(mDrawingRect, 270 ,360*(progress/100f), false, mArcPaintPrimary);

        // draw target mark along, but perpendicular to the arc's line
        float radius = mDrawingRect.width() <= mDrawingRect.height()
                ? mDrawingRect.width()/2 : mDrawingRect.height()/2;

        float target=mTarget;


        long hours=mProgressValue/3600;
        long minutes=(mProgressValue%3600)/60;
        long seconds= (long) ((mProgressValue%3600)%60);

        String valueString = hours+":"+minutes+":"+seconds;
//        Log.v(TAG,valueString);
        canvas.drawText(valueString,mDrawingRect.centerX(),mDrawingRect.centerY()*1.15f,mRegularText);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int size = 0;
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int widthWithoutPadding = width - getPaddingLeft() - getPaddingRight();
        int heigthWithoutPadding = height - getPaddingTop() - getPaddingBottom();

        // set the dimensions
        if (widthWithoutPadding > heigthWithoutPadding) {
            size = heigthWithoutPadding;
        } else {
            size = widthWithoutPadding;
        }

        setMeasuredDimension(size + getPaddingLeft() + getPaddingRight(), size + getPaddingTop() + getPaddingBottom());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // bound our drawable arc to stay fully within our canvas
        mDrawingRect = new RectF(mPadding + getPaddingLeft(),
                mPadding + getPaddingTop(),
                w - mPadding - getPaddingRight(),
                h - mPadding - getPaddingBottom());
    }

}
