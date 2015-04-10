package in.ac.iiitd.dhcs.focus.StatsObjects;

/**
 * Created by vedantdasswain on 10/04/15.
 */
public class ProductivePercentStatObject {

    long date;
    float prod_percent;

    public ProductivePercentStatObject(long date, float prod_percent){
        this.date=date;
        this.prod_percent=prod_percent;
    }

    public long getDate(){
        return date;
    }


    public float getProdPercent(){
        return prod_percent;
    }

}
