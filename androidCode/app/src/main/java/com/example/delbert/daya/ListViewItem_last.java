package com.example.delbert.daya;

/**
 * Created by YuJin on 2016-09-11.
 */
public class ListViewItem_last {
    private String dateStr, timeStr, keyStr, flagStr, pointStr ;

    public void setDate(String date) {
        dateStr = date;
    }
    public void setTime(String time) {
        timeStr = time;
    }
    public void setKey(String key) {
        keyStr = key ;
    }
    public void setFlag(String flag) {
        if(flag.equals("0")) {
            flagStr = "대기 중";
        } else {
            flagStr = "승인 완료";
        }
    }
    public void setPointStr() {
        if(flagStr.equals("1")) {
            pointStr = "100";
        } else {
            pointStr = "10";
        }
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
    public String getFlag() {
        return this.flagStr ;
    }
    public String getPoint() {
        return this.pointStr ;
    }
}