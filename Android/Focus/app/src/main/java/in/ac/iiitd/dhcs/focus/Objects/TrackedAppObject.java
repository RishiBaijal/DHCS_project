package in.ac.iiitd.dhcs.focus.Objects;

/**
 * Created by vedantdasswain on 11/03/15.
 */
public class TrackedAppObject {
    private float productivityScore;
    private String appName;

    public TrackedAppObject(String name,float score){
        this.appName=name;
        this.productivityScore=score;
    }

    public String getName(){
        return appName;
    }
    public Float getReading(){
        return productivityScore;
    }

    public void putName(String appName){
        this.appName=appName;
    }
    public void putReading(Float productivityScore){
        this.productivityScore=productivityScore;
    }
}
