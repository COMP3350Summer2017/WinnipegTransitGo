package comp3350.WinnipegTransitGo.services;

import java.util.ArrayList;
import java.util.List;

import comp3350.WinnipegTransitGo.apiService.TransitAPI;
import comp3350.WinnipegTransitGo.apiService.TransitAPIProvider;
import comp3350.WinnipegTransitGo.apiService.TransitAPIResponse;
import comp3350.WinnipegTransitGo.objects.BusRoute;
import comp3350.WinnipegTransitGo.objects.BusRouteSchedule;
import comp3350.WinnipegTransitGo.objects.BusStop;
import comp3350.WinnipegTransitGo.objects.BusStopSchedule;
import comp3350.WinnipegTransitGo.objects.Display;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by nibras on 2017-05-24.
 */

public class ListViewDisplayService
{

    String lat="49.8049250";
    String longitude="-97.1569080";
    List<BusStop> nearByBusStops=new ArrayList<BusStop>();
    List<Integer> nearByBusStopNumbers=new ArrayList<Integer>();
    private String apiKey="IyNt0rkZbxXFyrS4KT3t";
    TransitAPIProvider api = TransitAPI.getAPI(apiKey);

    List<Display> displayObjs=new ArrayList<Display>();
    //gets a list of busstops near the given location
    public void getListOfBusStops()
    {
        Call<TransitAPIResponse> apiResponse = api.getBusStops("100",lat, longitude,true);
        apiResponse.enqueue(new Callback<TransitAPIResponse>() {
            @Override
            public void onResponse(Call<TransitAPIResponse> call, Response<TransitAPIResponse> response) {
                nearByBusStops=response.body().getBusStops();
                for(int i=0;i<nearByBusStops.size();i++)
                {
                    nearByBusStopNumbers.add(nearByBusStops.get(i).getNumber());    //gets the busstop number and adds it to the list
                }
                traverseBusStopList(nearByBusStopNumbers);
                printDisplayObj();
            }

            @Override
            public void onFailure(Call<TransitAPIResponse> call, Throwable t) {

            }
        });

    }

    //this method will be given a bus stop number and it'll deal with that
    public void extractBusInfo(final int busStopNumber, final String busStopName)
    {

        TransitAPIProvider api = TransitAPI.getAPI(apiKey);
        Call<TransitAPIResponse> apiResponse = api.getBusStopSchedule(busStopNumber);
        apiResponse.enqueue(new Callback<TransitAPIResponse>() {
            @Override
            public void onResponse(Call<TransitAPIResponse> call, Response<TransitAPIResponse> response) {
                int busNumber;
                String destination;
                BusStopSchedule stopSchedule=response.body().getBusStopSchedule();
                List<BusRouteSchedule> routeSchedule=new ArrayList<BusRouteSchedule>();
                routeSchedule=stopSchedule.getBusRouteSchedules();
                for(int i=0;i<routeSchedule.size();i++)
                {
                    BusRoute route=routeSchedule.get(i).getBusRoute();
                    busNumber=route.getNumber();
                    destination=route.getName();
                    displayObjs.add(new Display(busNumber,busStopNumber,busStopName,destination));
                }
                for(int j=0;j<displayObjs.size();j++)
                {
                    System.out.println("Bus Number: "+displayObjs.get(j).getBusNumber());
                    System.out.println("Bus Destination: "+displayObjs.get(j).getBusStopDestination());
                    System.out.println("Bus Stop Name: "+displayObjs.get(j).getBusStopName());

                }
            }

            @Override
            public void onFailure(Call<TransitAPIResponse> call, Throwable t) {

            }
        });

    }

    public void traverseBusStopList(List<Integer> busStopList)
    {

        for(int i=0;i<busStopList.size();i++)
        {
            extractBusInfo(busStopList.get(i), nearByBusStops.get(i).getName());
        }


    }

    public void printDisplayObj()
    {
        for(int j=0;j<displayObjs.size();j++)
        {
            System.out.println("Bus Number: "+displayObjs.get(j).getBusNumber());
            System.out.println("Bus Destination: "+displayObjs.get(j).getBusStopDestination());
            System.out.println("Bus Stop Name: "+displayObjs.get(j).getBusStopName());

        }
    }
}
