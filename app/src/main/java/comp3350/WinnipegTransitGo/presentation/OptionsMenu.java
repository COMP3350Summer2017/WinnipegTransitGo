package comp3350.WinnipegTransitGo.presentation;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import comp3350.WinnipegTransitGo.R;
import comp3350.WinnipegTransitGo.businessLogic.TransitListPopulator;
import comp3350.WinnipegTransitGo.businessLogic.UserPreference;
import comp3350.WinnipegTransitGo.objects.TransitListItem;


/**
 * An optinos menu class involving options to modify app behaviour
 * Provides users the option to modify and view additional information about bus schedules
 * Usage:
 * Call setRadiusManually(MainActivity parentActivityContext) to set bus stop search radius manually
 * Call showBusPopup(ArrayList<String> stopFeatures) to show pop up dialog box with bus info and bus stop info
 * @author Nibras Ohin
 * @version 1.0
 * @since 2017-06-23.
 */

public class OptionsMenu implements BusStopFeaturesListener {

    private ListView listView = null;
    private Context parentActivity;
    private TransitListItem currSelectedItem;

    //This functions allows users to manually moodify bus stop search radius
    public void setRadiusManually(MainActivity parentActivityContext) {
        parentActivity = parentActivityContext;

        LayoutInflater layoutInflater = LayoutInflater.from(parentActivity);
        final ViewGroup nullParent = null;  //used to get rid of warning of passing null to layoutInflater
        View promptView = layoutInflater.inflate(R.layout.set_radius_dialog, nullParent);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(parentActivity);
        alertDialogBuilder.setView(promptView);
        final EditText radiusInput = (EditText) promptView.findViewById(R.id.radiusInput);
        radiusInput.setText(String.valueOf(UserPreference.getRadius()));
        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Preferences service access:
                        if (!TextUtils.isEmpty(radiusInput.getText())) {
                            String radiusInputText = radiusInput.getText().toString();
                            if (UserPreference.verifyAndSetRadius(radiusInputText)) {
                                Toast.makeText(parentActivity, parentActivity.getResources().getString(R.string.Radius_Toast_message) + radiusInputText,
                                        Toast.LENGTH_LONG).show();
                            } else {
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
    }//function ends


    public void showBusInfo(final MainActivity parentActivityContext, TransitListItem item) {

        parentActivity = parentActivityContext;
        listView = new ListView(parentActivity);
        currSelectedItem = item;

        TransitListPopulator listGenerator = parentActivityContext.getPopulator();
        listGenerator.getBusStopFeatures(item.getBusStopNumber(), this);
    }

    /*
      *This function creates a pop up dialog box upon click on a bus schedule
      *The method also fetches bus information and bus stop information
      * Then displays these information in the popup dialog box
    */
    public void showBusPopup(ArrayList<String> stopFeatures) {
        //bus info
        String rack = parentActivity.getResources().getString(R.string.bikeRackNo);
        if(currSelectedItem.isBikeRackAvailable())
            rack = parentActivity.getResources().getString(R.string.bikeRackYes);
        String easyAccess = parentActivity.getResources().getString(R.string.easyAccessNo);
        if(currSelectedItem.isEasyAccessAvailable())
            easyAccess = parentActivity.getResources().getString(R.string.easyAccessYes);
        String busInfo = parentActivity.getResources().getString(R.string.busInfo) + rack + easyAccess;


        //bus stop info
        String stringStopFeatures =  parentActivity.getResources().getString(R.string.busStopInfo);
        for (int i = 0; i < stopFeatures.size(); i++) {
                stringStopFeatures += stopFeatures.get(i) + "\n";
        }

        if (stopFeatures.size() == 0)//don't show anything if there is no info
            stringStopFeatures = "";

        String[] popupItems = {parentActivity.getResources().getString(R.string.setReminders), busInfo, stringStopFeatures};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(parentActivity,
                R.layout.busclick_listview, R.id.busClickItem, popupItems);

        listView.setAdapter(adapter);
        // Perform action when an item is clicked
        AlertDialog.Builder builder = new
                AlertDialog.Builder(parentActivity);
        builder.setCancelable(true);
        builder.setPositiveButton("OK", null);
        builder.setView(listView);
        final AlertDialog dialog = builder.create();
        dialog.show();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int
                    position, long id) {

                ViewGroup viewGroup= (ViewGroup) view;

                TextView onBusClickPopup = (TextView) viewGroup.findViewById(R.id.busClickItem);
                if(onBusClickPopup.getText().toString().equals(parentActivity.getResources().getString(R.string.setReminders))) {

                    ((MainActivity) parentActivity).showDetailedViewForBus(currSelectedItem);
                    dialog.dismiss();
                }
            }
        });
    }

}
