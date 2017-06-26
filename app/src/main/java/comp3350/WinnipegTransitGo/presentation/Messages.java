package comp3350.WinnipegTransitGo.presentation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import comp3350.WinnipegTransitGo.R;

/**
 * Created by habib on 6/23/2017.
 */
/**
 * Messages class
 * Used by presentation layer to display errors or warnings in
 * a dialog box to the user.
 *
 * @author Syed Habib
 * @version 1.0
 * @since 2017-06-23
 */

public class Messages {
    public static void fatalError(final Activity owner, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(owner).create();

        alertDialog.setTitle(owner.getString(R.string.fatalError));
        alertDialog.setMessage(message);
        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                owner.finish();
            }
        });

        alertDialog.show();
    }

    public static void warning(Activity owner, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(owner).create();

        alertDialog.setTitle(owner.getString(R.string.warning));
        alertDialog.setMessage(message);

        alertDialog.show();
    }
}
