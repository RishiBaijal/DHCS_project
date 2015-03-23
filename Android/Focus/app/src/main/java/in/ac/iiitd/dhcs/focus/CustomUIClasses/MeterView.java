package in.ac.iiitd.dhcs.focus.CustomUIClasses;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import java.text.SimpleDateFormat;

import in.ac.iiitd.dhcs.focus.R;

/**
 * Created by vedantdasswain on 12/03/15.
 */
public class MeterView extends View {
    private Context mContext;
    private int mBackgroundColor,mBackgroundWidth,mPrimaryColor,mPrimaryWidth,mTargetColor,mTargetLength,
            mRegularTextSize,mPercentTextSize;
    private Paint mArcPaintBackground,mArcPaintPrimary,mTargetMarkPaint,
            mRegularText,mPercentText,mTargetText,mTitleText;
    private float mPadding,mProgressPercent=5,mTarget=80;
    private long mProgressValue=0;
    private RectF mDrawingRect;
    private static double M_PI_2 = Math.PI/2;

    //MeterColour
    private  int meterColour;
    //TargetColour
    private  int targetColour;

    public MeterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext=context;
        init();
    }

    private void init() {
        Resources res = mContext.getResources();
        float density = res.getDisplayMetrics().density;

        meterColour=res.getColor(R.color.meterGauge);

        targetColour=res.getColor(R.color.target);

        mBackgroundColor = res.getColor(R.color.meterBackground);
        mBackgroundWidth = (int)(20 * density); // default to 20dp
        mPrimaryColor = meterColour;
        mPrimaryWidth = (int)(20 * density);  // default to 20dp
        mTargetColor = targetColour;
        mTargetLength = (int)(mPrimaryWidth * 1.25); // 100% longer than arc line width
        mRegularTextSize = (int)(mBackgroundWidth * 0.66); //Double the size of the width;
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

        mTargetMarkPaint = new Paint() {
            {
                setDither(true);
                setStyle(Style.STROKE);
                setStrokeCap(Cap.SQUARE);
                setStrokeJoin(Join.BEVEL);
                setAntiAlias(true);
            }
        };
        mTargetMarkPaint.setColor(mTargetColor);
        // make target tick mark 1/3 width of progress(primary) arc width
        mTargetMarkPaint.setStrokeWidth(mPrimaryWidth / 3);

        // get widest drawn element to properly pad the rect we draw inside
        float maxW = (mTargetLength >= mBackgroundWidth) ? mTargetLength : mBackgroundWidth;
        // arc is drawn with it's stroke center at the rect size provided, so we have to pad
        // it by half to bring it inside our bounding rect
        mPadding = maxW / 2;
        mProgressPercent = 0;

        Typeface tf = Typeface.create("Helvetica",Typeface.NORMAL);
        mRegularText=new Paint();
        mRegularText.setColor(res.getColor(android.R.color.black));
        mRegularText.setTextSize(mRegularTextSize);
        mRegularText.setTextAlign(Paint.Align.CENTER);
        mRegularText.setTypeface(tf);

        mPercentText=new Paint();
        mPercentText.setColor(meterColour);
        mPercentText.setTextSize(mPercentTextSize);
        mPercentText.setTypeface(tf);
        mPercentText.setTextAlign(Paint.Align.CENTER);

        mTargetText=new Paint();
        mTargetText.setColor(getResources().getColor(R.color.accent));
        mTargetText.setTextSize((float) (mPercentTextSize*0.5));
        mTargetText.setTypeface(tf);
        mTargetText.setTextAlign(Paint.Align.CENTER);

    }

    public void setProgress(float progress) {
        mProgressPercent = progress;

        invalidate();
        requestLayout();
    }

    public void setProgressValue(long duration){
        mProgressValue = duration/1000L;

        invalidate();
        requestLayout();
    }
    public void setTarget(float target) {
        mTarget = target;

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
        if(mTarget>100)
            target=100f;

        // Shift cos/sin by -90 deg (M_PI_2) to put start at 0 (top) and is in radians
        float circleX = mDrawingRect.centerX() + radius *
                (float)Math.sin((Math.PI * 2 * -target/ 100f) + Math.PI);
        float circleY = mDrawingRect.centerY() + radius *
                (float)Math.cos((Math.PI * 2 * -target/100f) + Math.PI);

        float slope = circleX - mDrawingRect.centerX() == 0 ? 999999
                : (circleY - mDrawingRect.centerY())/(circleX - mDrawingRect.centerX());

        float projectedX = (float)((mTargetLength/2.0)/Math.sqrt(1 + Math.pow(slope, 2.0)));
        float projectedY = (float)(((mTargetLength/2.0)*slope)
                /Math.sqrt(1 + Math.pow(slope, 2.0)));

        canvas.drawLine(circleX - projectedX,
                circleY - projectedY,
                circleX + projectedX,
                circleY + projectedY,
                mTargetMarkPaint);

        canvas.drawText("phone use has been productive,",mDrawingRect.centerX(),mDrawingRect.centerY()-(radius*0.1f),mRegularText);
        String valueString=((int)mTarget)+"%";
        canvas.drawText("your goal is "+valueString+" productivity ",mDrawingRect.centerX(),mDrawingRect.centerY(),mRegularText);
         valueString = Integer.toString((int)mProgressPercent)+"%";
        canvas.drawText(valueString,mDrawingRect.centerX(),mDrawingRect.centerY()-(radius*0.2f),mPercentText);

        canvas.drawText("have been spent productively",mDrawingRect.centerX(),mDrawingRect.centerY()+(radius*0.35f),mRegularText);
        long hours=mProgressValue/3600;
        long minutes=(mProgressValue%3600)/60;
        long seconds= (long) ((mProgressValue%3600)%60);
        valueString = hours+"hrs "+minutes+"mins";

        canvas.drawText(valueString,mDrawingRect.centerX(),mDrawingRect.centerY()+(radius*0.25f),mTargetText);

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
