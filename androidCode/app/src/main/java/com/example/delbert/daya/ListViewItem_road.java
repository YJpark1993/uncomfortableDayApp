package com.example.delbert.daya;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by YuJin on 2016-09-11.
 */

public class ListViewItem_road {
    Context mContext;
    public ListViewItem_road(Context context) {
        this.mContext = context;
    }
    private String dateStr, timeStr, keyStr, latitudeStr, longitudeStr, pointStr ;

    public void setDate(String date) {
        dateStr = date;
    }
    public void setTime(String time) {
        timeStr = time;
    }
    public void setKey(String key) {
        keyStr = key ;
    }
    public void setLatitude(String latitude) {
        latitudeStr = latitude;
    }
    public void setLongitude(String longitude) {
        longitudeStr = longitude;
    }
    public void setPointStr() {
        pointStr = "10";
    }

    public String getDate() {
        return this.dateStr ;
    }
    public String getTime() {
        return this.timeStr ;
    }
    public String getKey() {
        return this.keyStr ;
    }
    public String getLoc() {
        // 위치정보
        Geocoder geocoder = new Geocoder(mContext, Locale.KOREA);
        String nowAddress = "부정확한 위치";
        List<Address> address = null;
        try {
            if(geocoder != null) {
                address = geocoder.getFromLocation(Double.parseDouble(latitudeStr), Double.parseDouble(longitudeStr), 1);
            }
            if(address != null && address.size() > 0) {
                String currentLocation = address.get(0).getAddressLine(0).toString();
                nowAddress = currentLocation;
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        return nowAddress ;
    }
    public String getPoint() {
        return this.pointStr ;
    }
}