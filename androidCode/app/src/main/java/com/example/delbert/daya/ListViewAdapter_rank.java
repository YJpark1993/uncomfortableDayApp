package com.example.delbert.daya;

/**
 * Created by YuJin on 2016-09-11.
 */
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ListViewAdapter_rank extends BaseAdapter {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<ListViewItem_rank> listViewItemList = new ArrayList<ListViewItem_rank>() ;
    private int order = 1;
    // ListViewAdapter의 생성자
    public ListViewAdapter_rank() {

    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return listViewItemList.size() ;
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();
        if(position == ShowRankActivity.idx) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_view_rank_my_rank, parent, false);
        }else {
            // "listview_item" Layout을 inflate하여 convertView 참조 획득.
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.list_view_rank, parent, false);
            }
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView txtRank = (TextView) convertView.findViewById(R.id.rank_rank) ;
        TextView txtId = (TextView) convertView.findViewById(R.id.rank_id) ;
        TextView txtTotal = (TextView) convertView.findViewById(R.id.rank_total) ;
        TextView txtOk = (TextView) convertView.findViewById(R.id.rank_ok) ;
        TextView txtPoint = (TextView) convertView.findViewById(R.id.rank_point) ;

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        ListViewItem_rank listViewItemLast = listViewItemList.get(position);


        // 아이템 내 각 위젯에 데이터 반영

        txtRank.setText(listViewItemLast.getRankStr());
        txtId.setText(listViewItemLast.getIdStr());
        txtTotal.setText(listViewItemLast.getTotalStr());
        txtOk.setText(listViewItemLast.getOkStr());
        txtPoint.setText(listViewItemLast.getPointStr());
        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position) ;
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(String rank, String point, String id, String total, String ok) {
        ListViewItem_rank item = new ListViewItem_rank();
        if(!id.equals("root@")){
            Log.d("???",id);
        item.setRankStr(rank);
        item.setIdStr(id);
        item.setTotalStr(total);
        item.setOkStr(ok);
        item.setPointStr(point);

        listViewItemList.add(item);
        }
    }

    public void clearItem() {
        listViewItemList.clear();
    }
    public void myRank() {

    }
}