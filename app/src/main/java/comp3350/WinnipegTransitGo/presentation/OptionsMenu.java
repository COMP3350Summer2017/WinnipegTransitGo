package comp3350.WinnipegTransitGo.presentation;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
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

import comp3350.WinnipegTransitGo.R;
import comp3350.WinnipegTransitGo.businessLogic.UserPreference;
import comp3350.WinnipegTransitGo.objects.TransitListItem;

/**
 * Created by nibras on 2017-06-23.
 * Purpose: Creates an alert dialog box to ask user for input for the search radius
 * Then calls method from business logic to set the radius
 */

public class OptionsMenu {

    private ListView listView = null;
    private Context parentActivity;

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


    public void showOnBusClickPopUp(final MainActivity parentActivityContext,TransitListItem item) {

        parentActivity = parentActivityContext;
        final TransitListItem itemsList=item;
        listView = new ListView(parentActivity);

        String rack = "Bike Rack Available - No";
        if(item.isBikeRackAvailable())
            rack = "Bike Rack Available - Yes";
        String easyAccess = "Easy Access Available - No";
        if(item.isEasyAccessAvailable())
            easyAccess = "Easy Access Available - Yes";

        String busInfo = "Bus Info:" + rack + "\n" + easyAccess;


  //      TransitListPopulator listGenerator = parentActivityContext.getPopulator();
  //      listGenerator.getBusStopFeatures(item.getBusStopNumber());

//        try{Thread.sleep(10000);}catch (Exception e){}


  //      ArrayList<String> stopFeatures = listGenerator.getFeatures();
//        while(stopFeatures == null) {
 //           stopFeatures = listGenerator.getFeatures();
  //      }
        String stringStopFeatures = "Bus Stop Info\n";

   //     for(int i = 0; i<stopFeatures.size(); i++)
    //        stringStopFeatures+= stopFeatures.get(i)+ "\n";



//        String[] items = {"Set Reminders", busInfo, "Bus Stop Info: Bus stop is shelter equipped"};
        String[] items = {"Set Reminders", busInfo, stringStopFeatures};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(parentActivity,
                R.layout.busclick_listview, R.id.busClickItem, items);

        listView.setAdapter(adapter);

        // Perform action when an item is clicked
        AlertDialog.Builder builder = new
                AlertDialog.Builder(parentActivity);

        builder.setCancelable(true);

        builder.setPositiveButton("OK", null);

        builder.setView(listView);

        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setLayout(700, 500);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override

            public void onItemClick(AdapterView<?> parent, View view, int
                    position, long id) {

                ViewGroup viewGroup= (ViewGroup) view;

                TextView txt = (TextView) viewGroup.findViewById(R.id.busClickItem);
                txt.setTextColor(Color.parseColor("#FFFFFF"));
                if(txt.getText().toString().equals("Set Reminders")) {

                    parentActivityContext.showDetailedViewForBus(itemsList);
                    dialog.dismiss();
                    //Toast.makeText(parentActivity, txt.getText().toString(), Toast.LENGTH_LONG).show();
                }

            }

        });
    }

}
