package in.ac.iiitd.dhcs.focus.Objects;

import android.graphics.drawable.Drawable;

/**
 * Created by vedantdasswain on 11/03/15.
 */
public class ProductivityObject {
    private String appName;
    private String packageName;
    private Drawable image;
    private String trackingDate;
    private long usageDuration;
    private long productivityDuration;
    private long productivityScore;

    public ProductivityObject(){
    }

    public String getName(){
        return appName;
    }
    public String getPackageName() {return  packageName;}
    public String getDate(){
        return trackingDate;
    }
    public long getUsageDuration(){
        return usageDuration;
    }
    public long getProductivityDuration(){
        return productivityDuration;
    }
    public long getProductivityScore() { return productivityScore; }

    public void putName(String appName){
        this.appName=appName;
    }
    public void putPackageName(String packageName) {this.packageName=packageName; }
    public void putReading(String trackingDate){
        this.trackingDate=trackingDate;
    }
    public void putUsageDuration(long duration){
        this.usageDuration=duration;
    }
    public void putProductivityDuration(long duration){
        this.productivityDuration=duration;
    }
    public void putProductivityScore(long Score) { this.productivityScore = Score; }
}
