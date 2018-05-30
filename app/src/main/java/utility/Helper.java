package utility;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;
import java.util.Date;
import java.util.regex.Pattern;

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