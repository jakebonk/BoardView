package com.allyants.boardview;

import android.content.Context;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by jbonk on 6/12/2017.
 */

public abstract class BoardAdapter{

    Context context;
    ArrayList<Column> columns = new ArrayList<>();

    public BoardAdapter(Context context){
        this.context = context;
    }

    public void createColumns(){
        for(int i = 0; i < getColumnCount();i++){
            ArrayList<View> views = new ArrayList<>();
            for(int j = 0; j < getItemCount(i);j++){
                views.add(createItemView(context,i,j));
            }
            columns.add(new Column(createHeaderView(context,i),views));
        }
    }

    public class Column{
        public View header;
        public ArrayList<View> views = new ArrayList<>();
        public Column(View header, ArrayList<View> views){
            this.header = header;
            this.views = views;
        }

    }

    public abstract int getColumnCount();
    public abstract int getItemCount(int column_position);
    public abstract View createItemView(Context context,int column_position,int item_position);
    public abstract View createHeaderView(Context context,int column_position);

}
