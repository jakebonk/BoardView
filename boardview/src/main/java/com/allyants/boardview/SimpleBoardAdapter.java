package com.allyants.boardview;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jbonk on 6/12/2017.
 */

public class SimpleBoardAdapter extends BoardAdapter{
    int header_resource;
    int item_resource;
    public SimpleBoardAdapter instance;

    public SimpleBoardAdapter(Context context, ArrayList<SimpleColumn> data) {
        super(context);
        instance = this;
        this.columns = (ArrayList)data;
        this.header_resource = R.layout.column_header;
        this.item_resource = R.layout.column_item;
    }

    @Override
    public int getColumnCount() {
        return columns.size();
    }

    @Override
    public int getItemCount(int column_position) {
        return columns.get(column_position).objects.size();
    }

    @Override
    public Object createHeaderObject(int column_position) {
        return columns.get(column_position).header_object;
    }

    @Override
    public Object createFooterObject(int column_position) {
        return "Footer "+ String.valueOf(column_position);
    }

    @Override
    public Object createItemObject(int column_position, int item_position) {
        return columns.get(column_position).objects.get(item_position);
    }

    @Override
    public boolean isColumnLocked(int column_position) {
        return false;
    }

    @Override
    public boolean isItemLocked(int column_position) {
        return false;
    }

    @Override
    public View createItemView(Context context,Object header_object,Object item_object,int column_position, int item_position) {
        View item = View.inflate(context, item_resource, null);
        TextView textView = (TextView)item.findViewById(R.id.textView);
        textView.setText(columns.get(column_position).objects.get(item_position).toString());
        return item;
    }

    @Override
    public int maxItemCount(int column_position) {
        return 4;
    }

    @Override
    public void createColumns() {
        super.createColumns();
    }

    @Override
    public View createHeaderView(Context context,Object header_object,int column_position) {
        View column = View.inflate(context, header_resource, null);
        TextView textView = (TextView)column.findViewById(R.id.textView);
        textView.setText(columns.get(column_position).header_object.toString());
        return column;
    }

    @Override
    public View createFooterView(Context context, Object footer_object, int column_position) {
        View footer = View.inflate(context, header_resource, null);
        TextView textView = (TextView)footer.findViewById(R.id.textView);
        textView.setText((String)footer_object);
        return footer;
    }

    public static class SimpleColumn extends Column{
        public String title;
        public SimpleColumn(String title, ArrayList<Object> items){
            super();
            this.title = title;
            this.header_object = new Item(title);
            this.objects = items;
        }
    }

}
