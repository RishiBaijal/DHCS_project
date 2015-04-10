package in.ac.iiitd.dhcs.focus.StatsObjects;

/**
 * Created by vedantdasswain on 10/04/15.
 */
public class WeeklyPercentStatObject {
    String day;
    float prod_percent;

    public WeeklyPercentStatObject(String day, float prod_percent){
        this.day=day;
        this.prod_percent=prod_percent;
    }

    public String getday(){
        return day;
    }


    public float getProdPercent(){
        return prod_percent;
    }

}
