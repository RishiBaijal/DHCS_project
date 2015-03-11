package in.ac.iiitd.dhcs.focus.Objects;

/**
 * Created by vedantdasswain on 11/03/15.
 */
public class ProductivityObject {
    private String appName;
    private String trackingDate;
    private long usageDuration;
    private long productivityDuration;

    public ProductivityObject(String name,String date,long uDuration,long pDuration){
        this.appName=name;
        this.trackingDate=date;
        this.usageDuration=uDuration;
        this.productivityDuration=pDuration;
    }

    public String getName(){
        return appName;
    }
    public String getDate(){
        return trackingDate;
    }
    public long getUsageDuration(){
        return usageDuration;
    }
    public long getProductivityDuration(){
        return productivityDuration;
    }

    public void putName(String appName){
        this.appName=appName;
    }
    public void putReading(String trackingDate){
        this.trackingDate=trackingDate;
    }
    public void putUsageDuration(long duration){
        this.usageDuration=duration;
    }
    public void putProductivityDuration(long duration){
        this.usageDuration=duration;
    }
}
