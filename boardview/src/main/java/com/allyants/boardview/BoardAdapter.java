package com.allyants.boardview;

import android.content.Context;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by jbonk on 6/12/2017.
 */

public abstract class BoardAdapter{

    Context context;
    public ArrayList<Column> columns = new ArrayList<>();

    public BoardAdapter(Context context){
        this.context = context;
    }

    public void createColumns(){
        for(int i = 0; i < getColumnCount();i++){
            ArrayList<View> views = new ArrayList<>();
            Column column = new Column(createHeaderView(context,createHeaderObject(i),i),views);
            for(int j = 0; j < getItemCount(i);j++){
                column.objects.add(createItemObject(i,j));
                views.add(createItemView(context,createHeaderObject(i),createItemObject(i,j),i,j));
            }
            column.header_object = createHeaderObject(i);
            columns.add(column);
        }
    }

    public class Column{
        public View header;
        public Object header_object;
        public ArrayList<View> views = new ArrayList<>();
        public ArrayList<Object> objects = new ArrayList<>();
        public Column(View header, ArrayList<View> views){
            this.header = header;
            this.views = views;
        }

    }

    public Object getHeaderObject(int column_position){
        return columns.get(column_position).header_object;
    }

    public Object getItemObject(int column_position,int item_position){
        return columns.get(column_position).objects.get(item_position);
    }

    public abstract int getColumnCount();
    public abstract int getItemCount(int column_position);
    public abstract Object createHeaderObject(int column_position);
    public abstract Object createItemObject(int column_position,int item_position);
    public abstract View createItemView(Context context,Object header_object, Object item,int column_position,int item_position);
    public abstract View createHeaderView(Context context,Object header_object,int column_position);

}
