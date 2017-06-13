package com.allyants.boardview;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jbonk on 6/12/2017.
 */

public class SimpleBoardAdapter extends BoardAdapter{
    ArrayList<SimpleColumn> data = new ArrayList<>();
    int header_resource;
    int item_resource;

    public SimpleBoardAdapter(Context context, ArrayList<SimpleColumn> data) {
        super(context);
        this.data = data;
        this.header_resource = R.layout.column_header;
        this.item_resource = R.layout.column_item;
    }

    @Override
    public int getColumnCount() {
        return data.size();
    }

    @Override
    public int getItemCount(int column_position) {
        return data.get(column_position).items.size();
    }

    @Override
    public View createItemView(Context context,int column_position, int item_position) {
        View item = View.inflate(context, item_resource, null);
        TextView textView = (TextView)item.findViewById(R.id.textView);
        textView.setText(data.get(column_position).items.get(item_position));
        return item;
    }

    @Override
    public View createHeaderView(Context context,int column_position) {
        View column = View.inflate(context, header_resource, null);
        TextView textView = (TextView)column.findViewById(R.id.textView);
        textView.setText(data.get(column_position).header);
        return column;
    }

    public static class SimpleColumn{
        public ArrayList<String> items;
        public String header;
        public SimpleColumn(String header, ArrayList<String> items){
            this.header = header;
            this.items = items;
        }
    }

}
