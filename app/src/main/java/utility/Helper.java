package utility;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by Aashish Sharma on 28/5/18.
 */

public class Helper {

    private String TAG="HelperClass";

    Activity activity;

    public Helper(Activity activity) {
        this.activity = activity;
    }

    public void showToast(Activity context, String message) {
        if (context != null) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }

}