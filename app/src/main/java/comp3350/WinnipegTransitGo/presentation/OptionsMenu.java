package comp3350.WinnipegTransitGo.presentation;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import comp3350.WinnipegTransitGo.R;
import comp3350.WinnipegTransitGo.businessLogic.UserPreference;

/**
 * Created by nibras on 2017-06-23.
 */

public class OptionsMenu
{

    private Context parentActivity;

    public void setRadiusManually(MainActivity parentActivityContext)
    {
        parentActivity=parentActivityContext;
        final UserPreference userPreference = UserPreference.getUserPreference();

        LayoutInflater layoutInflater = LayoutInflater.from(parentActivity);
        final ViewGroup nullParent = null;  //used to get rid of warning of passing null to layoutInflater
        View promptView = layoutInflater.inflate(R.layout.set_radius_dialog, nullParent);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(parentActivity);
        alertDialogBuilder.setView(promptView);
        final EditText radiusInput = (EditText) promptView.findViewById(R.id.radiusInput);

        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Preferences service access:
                        if(!TextUtils.isEmpty(radiusInput.getText()))
                        {
                            String radiusInputText=radiusInput.getText().toString();
                            if(userPreference.verifyAndSetRadius(radiusInputText))
                            {
                                Toast.makeText(parentActivity, parentActivity.getResources().getString(R.string.Radius_Toast_message)+ radiusInputText,
                                        Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                Toast.makeText(parentActivity, parentActivity.getResources().getString(R.string.Radius_inputLimit_message),
                                        Toast.LENGTH_LONG).show();
                            }

                        }
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }
}
