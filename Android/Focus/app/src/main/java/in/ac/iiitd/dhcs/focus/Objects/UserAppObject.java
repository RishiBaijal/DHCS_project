package in.ac.iiitd.dhcs.focus.Objects;

import android.content.pm.PackageInfo;

/**
 * Created by Shubham on 26 Mar 15.
 */
public class UserAppObject {

    private PackageInfo packageInfo;
    private boolean isChecked;
    private float prodscore;

    public UserAppObject() {

    }

    public UserAppObject(PackageInfo packageInfo1, boolean isChecked)
    {
        this.packageInfo = packageInfo1;
        this.isChecked = isChecked;
    }

    public PackageInfo getPackageInfo()
    {
        return packageInfo;
    }

    public boolean getIsChecked()
    {
        return isChecked;
    }

    public float getProdscore() {return  prodscore; }

    public void setPackageInfo(PackageInfo packageInfo1)
    {
        this.packageInfo = packageInfo1;
    }

    public void setChecked(boolean isChecked)
    {
        this.isChecked = isChecked;
    }

    public void setProdscore(float score) {this.prodscore = score;}

}
