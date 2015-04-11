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

    @Override
    public boolean equals(Object o) {
        //return super.equals(o);
       if(this.appName.equals(((TrackedAppObject) o).getName())
               && (this.productivityScore==(((TrackedAppObject) o).getReading())))
       {
           return true;
       }
       else{
           return false;
       }
    }

    public String getName(){
        return appName;
    }
    public float getReading(){
        return productivityScore;
    }

    public void putName(String appName){
        this.appName=appName;
    }
    public void putReading(float productivityScore){
        this.productivityScore=productivityScore;
    }

}
