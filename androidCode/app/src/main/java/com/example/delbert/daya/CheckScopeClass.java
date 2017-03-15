package com.example.delbert.daya;

import android.util.Log;

import java.util.Arrays;

/**
 * Created by pkaus on 2016-08-27.
 */
public class CheckScopeClass {
    double coordinate[][];
    String keyWord[];

    double newCoordinate[][];
    String newKeyWord[];
    int newCount[];

    int [] count;
    boolean [] check;

    CheckScopeClass(){
        Log.d("xxx","CheckScopeClass");
    }
    public void CheckScopeFunction(String tableKeyWord[], double table[][]){
        coordinate = table;
        keyWord = tableKeyWord;
        Log.d("xxx","CheckScopeFunction");
        CheckWordRepeat(tableKeyWord);
    }

    public int[] ReturnCount(){
        int k=0;
        for(int i=0; i<keyWord.length; i++){
            if(Boolean.FALSE.equals(check[i])){
                newCount[k] = count[i]+1;
                k++;
            }
        }
        return newCount;
    }

    public double[][] ReturnCoordinate(){
        int k=0;
        for(int i=0; i<keyWord.length; i++){
            if(Boolean.FALSE.equals(check[i])){
                newCoordinate[k][0] = coordinate[i][0];
                newCoordinate[k][1] = coordinate[i][1];
                k++;
            }
        }

        return newCoordinate;
    }
    public String[] ReturnKeyWord(){
        int k=0;
        for(int i=0; i<keyWord.length; i++){
            if(Boolean.FALSE.equals(check[i])){
                newKeyWord[k] = keyWord[i];
                k++;
            }
        }
        return newKeyWord;
    }


    private void CheckWordRepeat(String tableKeyWord[]){
        count = new int[tableKeyWord.length];
        check = new boolean[tableKeyWord.length];
        Arrays.fill(count, 0);
        Arrays.fill(check, false);

        for(int i=0; i<tableKeyWord.length;i++){
            for(int j=i+1; j<tableKeyWord.length; j++){

                if(j!=(tableKeyWord.length+1)&&(tableKeyWord[i].equalsIgnoreCase(tableKeyWord[j])) && Boolean.FALSE.equals(check[j])){
                    count[i] = CheckScope(count[i],coordinate[i][0],coordinate[i][1],coordinate[j][0],coordinate[j][1]);
                    check[j] = CheckBoolean(check[j],coordinate[i][0],coordinate[i][1],coordinate[j][0],coordinate[j][1]);
                }
            }
        }
        int counter =0;

        for(int j=0; j<tableKeyWord.length; j++){
            if(!check[j])
                counter++;
        }
        newCoordinate = new double[counter][2];
        newKeyWord = new String[counter];
        newCount = new int[counter];
    }

    private int CheckScope(int counts, double latitude1, double longitude1, double latitude2, double longitude2){
        if(Math.abs(latitude1-latitude2) <= 0.002 && Math.abs(longitude1-longitude2) <= 0.002){
            counts = counts+1;
        }
        return counts;
    }

    private boolean CheckBoolean(boolean checks,double latitude1, double longitude1, double latitude2, double longitude2){
        if(Math.abs(latitude1-latitude2) <= 0.002 && Math.abs(longitude1-longitude2) <= 0.002) {
            checks = true;
            return checks;
        }
        return false;
    }
}
