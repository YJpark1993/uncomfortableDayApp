package com.example.delbert.daya;

/**
 * Created by YuJin on 2016-09-11.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;

import mehdi.sakout.fancybuttons.FancyButton;

public class ListViewAdapter_road extends BaseAdapter {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<ListViewItem_road> listViewItemList = new ArrayList<ListViewItem_road>();
    private int order = 1;
    int mPostion = 0;
    FancyButton btnLoc;

    // ListViewAdapter의 생성자
    public ListViewAdapter_road() {

    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_view_road_submit, parent, false);
        }
        btnLoc = (FancyButton) convertView.findViewById(R.id.sub_road_loc);
        mPostion = position;

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView txtDate = (TextView) convertView.findViewById(R.id.sub_road_date);
        TextView txtTime = (TextView) convertView.findViewById(R.id.sub_road_time);
        TextView txtKey = (TextView) convertView.findViewById(R.id.sub_road_key);
        //TextView txtLoc = (TextView) convertView.findViewById(R.id.sub_road_loc) ;
        TextView txtPoint = (TextView) convertView.findViewById(R.id.sub_road_point);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        ListViewItem_road listViewItemLast = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        txtDate.setText(listViewItemLast.getDate());
        txtTime.setText(listViewItemLast.getTime());
        txtKey.setText(listViewItemLast.getKey());
        //txtLoc.setText(listViewItemLast.getLoc());
        txtPoint.setText(listViewItemLast.getPoint());

        btnLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = listViewItemList.get(mPostion).getLoc();
                TastyToast.makeText(context, address, TastyToast.LENGTH_SHORT, TastyToast.INFO);
            }
        });
        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position);
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(String date, String time, String key, String la, String lo, Context context) {
        ListViewItem_road item = new ListViewItem_road(context);

        item.setDate(date);
        item.setTime(time);
        item.setKey(key);
        item.setLatitude(la);
        item.setLongitude(lo);
        item.setPointStr();

        listViewItemList.add(item);
    }

    public void clearItem() {
        listViewItemList.clear();
    }
}
