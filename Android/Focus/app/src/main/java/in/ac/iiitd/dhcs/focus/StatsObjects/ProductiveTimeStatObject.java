package in.ac.iiitd.dhcs.focus.StatsObjects;

/**
 * Created by vedantdasswain on 10/04/15.
 */
public class ProductiveTimeStatObject {
    long date, prod_dur, use_dur;

    public ProductiveTimeStatObject(long date,long prod_dur,long use_dur){
        this.date=date;
        this.prod_dur=prod_dur;
        this.use_dur=use_dur;
    }

    public long getDate(){
        return date;
    }


    public long getProdDur(){
        return prod_dur;
    }


    public long getUseDur(){
        return use_dur;
    }
}
