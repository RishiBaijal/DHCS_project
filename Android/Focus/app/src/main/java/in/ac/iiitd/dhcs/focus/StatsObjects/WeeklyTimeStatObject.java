package in.ac.iiitd.dhcs.focus.StatsObjects;

/**
 * Created by vedantdasswain on 10/04/15.
 */
public class WeeklyTimeStatObject {
    long  prod_dur, use_dur;
    String day;

    public WeeklyTimeStatObject(String day,long prod_dur,long use_dur){
        this.day=day;
        this.prod_dur=prod_dur;
        this.use_dur=use_dur;
    }

    public String getday(){
        return day;
    }


    public long getProdDur(){
        return prod_dur;
    }


    public long getUseDur(){
        return use_dur;
    }
}
