package com.example.delbert.daya;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by pkaus on 2016-08-29.
 */
public class SelectIcon{

    SelectIcon(){};

    public GoogleMap ConnectIcon(GoogleMap map, double[][] Coordinate, String[] KeyWord, int[] Count){
        for (int i = 0; i < KeyWord.length; i++) {
            LatLng latLng = new LatLng(Coordinate[i][0], Coordinate[i][1]);
            if (KeyWord[i].equalsIgnoreCase("방지턱")) {
                map.addMarker(new MarkerOptions()
                        .position(latLng)
                        .snippet(KeyWord[i])
                        .title("누적신고:" + Count[i])
                        .alpha(Count[i] * 0.3f)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.bump)));
            } else if (KeyWord[i].equalsIgnoreCase("가로등")) {
                map.addMarker(new MarkerOptions()
                        .position(latLng)
                        .snippet(KeyWord[i])
                        .title("누적신고:" + Count[i])
                        .alpha(Count[i]* 0.3f)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.lamp)));
            } else if(KeyWord[i].equalsIgnoreCase("도로")){
                map.addMarker(new MarkerOptions()
                        .position(latLng)
                        .snippet(KeyWord[i])
                        .title("누적신고:" + Count[i])
                        .alpha(Count[i]* 0.3f)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.road)));
            }else if(KeyWord[i].equalsIgnoreCase("신호등")){
                map.addMarker(new MarkerOptions()
                        .position(latLng)
                        .snippet(KeyWord[i])
                        .title("누적신고:" + Count[i])
                        .alpha(Count[i]* 0.3f)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.trafficlight)));
            }else if(KeyWord[i].equalsIgnoreCase("펜스")){
                map.addMarker(new MarkerOptions()
                        .position(latLng)
                        .snippet(KeyWord[i])
                        .title("누적신고:" + Count[i])
                        .alpha(Count[i]* 0.3f)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.fence)));
            }else if(KeyWord[i].equalsIgnoreCase("낙석")){
                map.addMarker(new MarkerOptions()
                        .position(latLng)
                        .snippet(KeyWord[i])
                        .title("누적신고:" + Count[i])
                        .alpha(Count[i]* 0.3f)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.danger)));
           }else if(KeyWord[i].equalsIgnoreCase("자전거도로")){
                map.addMarker(new MarkerOptions()
                        .position(latLng)
                        .snippet(KeyWord[i])
                        .title("누적신고:" + Count[i])
                        .alpha(Count[i] * 0.3f)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.bicycle)));


            }else if(KeyWord[i].equalsIgnoreCase("교차로")){
                map.addMarker(new MarkerOptions()
                        .position(latLng)
                        .snippet(KeyWord[i])
                        .title("누적신고:" + Count[i])
                        .alpha(Count[i]* 0.3f)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.crossroad)));
            } else if(KeyWord[i].equalsIgnoreCase("표지판")) {
                map.addMarker(new MarkerOptions()
                        .position(latLng)
                        .snippet(KeyWord[i])
                        .title("누적신고:" + Count[i])
                        .alpha(Count[i]* 0.3f)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.polesign)));
            }else if(KeyWord[i].equalsIgnoreCase("간판")) {
                map.addMarker(new MarkerOptions()
                        .position(latLng)
                        .snippet(KeyWord[i])
                        .title("누적신고:" + Count[i])
                        .alpha(Count[i]* 0.3f)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.sign)));
            }


        }
        return map;
    }
}
