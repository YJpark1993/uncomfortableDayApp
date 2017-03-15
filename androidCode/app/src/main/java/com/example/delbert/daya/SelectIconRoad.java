package com.example.delbert.daya;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by pkaus on 2016-09-09.
 */
public class SelectIconRoad {
    SelectIconRoad(){};

    public GoogleMap ConnectRoadIcon(GoogleMap map, double[][] Coordinate, String[] KeyWord, String[] curTime){
        for (int i = 0; i < KeyWord.length; i++) {
            LatLng latLng = new LatLng(Coordinate[i][0], Coordinate[i][1]);
            if (KeyWord[i].equalsIgnoreCase("교통사고")) {
                map.addMarker(new MarkerOptions()
                        .position(latLng)
                        .snippet("신고시간: "+curTime[i])
                        .title(KeyWord[i])
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.accident)));
            } else if (KeyWord[i].equalsIgnoreCase("화재")) {
                map.addMarker(new MarkerOptions()
                        .position(latLng)
                        .snippet("신고시간: "+curTime[i])
                        .title(KeyWord[i])
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.fire)));
            } else if(KeyWord[i].equalsIgnoreCase("낙석")){
                map.addMarker(new MarkerOptions()
                        .position(latLng)
                        .snippet("신고시간: "+curTime[i])
                        .title(KeyWord[i])
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.rockfall)));
            }else if(KeyWord[i].equalsIgnoreCase("단순 정체")){
                map.addMarker(new MarkerOptions()
                        .position(latLng)
                        .snippet("신고시간: "+curTime[i])
                        .title(KeyWord[i])
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.traffic)));
            }else if(KeyWord[i].equalsIgnoreCase("공사중")){
                map.addMarker(new MarkerOptions()
                        .position(latLng)
                        .snippet("신고시간: "+curTime[i])
                        .title(KeyWord[i])
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.construction)));
            }
        }
        return map;
    }
}
