package com.example.delbert.daya;

/**
 * Created by YuJin on 2016-09-11.
 */

public class ListViewItem_rank {
    private String rankStr, idStr, totalStr, okStr, pointStr ;

    public void setRankStr(String rankStr) {
        this.rankStr = rankStr;
    }

    public void setIdStr(String idStr) {
        int idx = idStr.indexOf('@');
        idStr = idStr.substring(0,idx);
        this.idStr = idStr;
    }

    public void setTotalStr(String totalStr) {
        this.totalStr = totalStr;
    }

    public void setOkStr(String okStr) {
        this.okStr = okStr;
    }

    public void setPointStr(String point) {
        this.pointStr = point;
    }

    public String getRankStr() {
        return rankStr;
    }

    public String getIdStr() {
        return idStr;
    }

    public String getTotalStr() {
        return totalStr;
    }

    public String getOkStr() {
        return okStr;
    }

    public String getPointStr() {
        return pointStr;
    }
}