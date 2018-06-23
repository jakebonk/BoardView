package com.allyants.boardview;

import android.content.Context;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.util.ArrayList;

/**
 * Created by jbonk on 6/12/2017.
 */

public abstract class BoardAdapter{

    Context context;
    Transition transition = new Fade();
    Transition boardViewTransition = new Fade();
    public ArrayList<Column> columns = new ArrayList<>();
    public BoardAdapter(Context context){
        this.context = context;
    }
    public BoardView boardView;

    public void createColumns(){
        ArrayList<Column> tmp = new ArrayList<>();
        for(int i = 0; i < getColumnCount();i++){
            ArrayList<View> views = new ArrayList<>();
            Column column = new Column(createHeaderView(context,createHeaderObject(i),i),views,createFooterView(context,createFooterObject(i),i));
            for(int j = 0; j < getItemCount(i);j++){
                column.objects.add(createItemObject(i,j));
                views.add(createItemView(context,createHeaderObject(i),createItemObject(i,j),i,j));
            }
            column.items_locked = isItemLocked(i);
            column.column_locked = isColumnLocked(i);
            column.header_object = createHeaderObject(i);
            column.footer_object = createFooterObject(i);
            tmp.add(column);
        }
        columns = tmp;
    }

    public void SetItemTransition(Transition t){
        transition = t;
    }

    public void SetColumnTransition(Transition t){
        boardViewTransition = t;
    }

    public static class Column{
        public View header;
        public Object header_object;
        public Object footer_object;
        public View footer;
        public Boolean column_locked = false;
        public Boolean items_locked =  false;
        public ArrayList<View> views = new ArrayList<>();
        public ArrayList<Object> objects = new ArrayList<>();

        public Column(){
            this.footer_object = "Foot";
            this.header_object = "Header";
        }

        public Column(View header, ArrayList<View> views,View footer){
            this.header = header;
            this.views = views;
            this.footer = footer;
        }

    }

    public Object getHeaderObject(int column_position){
        return columns.get(column_position).header_object;
    }

    public Object getItemObject(int column_position,int item_position){
        return columns.get(column_position).objects.get(item_position);
    }

    public void addColumn(int index, Column column){
        columns.add(index,column);
        for(int j = 0; j < getItemCount(index);j++){
            column.objects.add(createItemObject(index,j));
            column.views.add(createItemView(context,createHeaderObject(index),createItemObject(index,j),index,j));
        }
        column.items_locked = isItemLocked(index);
        column.column_locked = isColumnLocked(index);
        column.header_object = createHeaderObject(index);
        column.footer_object = createFooterObject(index);
        column.header = createHeaderView(context,column.header_object,index);
        column.footer = createFooterView(context,column.footer_object,index);
        TransitionManager.beginDelayedTransition(boardView, boardViewTransition);
        boardView.addColumnList(column.header,column.views,column.footer,index);
    }

    public void removeColumn(int index){
        BoardItem item = (BoardItem)((ViewGroup)((ViewGroup)boardView.getChildAt(0)).getChildAt(0)).getChildAt(index);
        ViewGroup group = ((ViewGroup) item.getParent());
        ((ViewGroup)item.getParent()).removeView(item);
        columns.remove(index);
        group.invalidate();
    }

    public void removeItem(int column, int index){
        BoardItem item = (BoardItem)((ViewGroup)((ViewGroup)boardView.getChildAt(0)).getChildAt(0)).getChildAt(column);
        ScrollView scrollView = (ScrollView)item.getChildAt(1);
        LinearLayout llColumn = (LinearLayout)scrollView.getChildAt(0);
        llColumn.removeViewAt(index);
        llColumn.invalidate();
        columns.get(column).objects.remove(index);
        columns.get(column).views.remove(index);
    }

    public void addItem(int column,int index, Object item){
        BoardItem boardItem = (BoardItem)((ViewGroup)((ViewGroup)boardView.getChildAt(0)).getChildAt(0)).getChildAt(column);
        TransitionManager.beginDelayedTransition(boardItem, transition);
        ScrollView scrollView = (ScrollView)boardItem.getChildAt(1);
        LinearLayout llColumn = (LinearLayout)scrollView.getChildAt(0);
        columns.get(column).objects.add(index,item);
        View v = createItemView(context,columns.get(column).header_object,item,column,index);
        llColumn.addView(v,index);
        boardView.addBoardItem(v,column);
        llColumn.invalidate();
        columns.get(column).views.add(index,v);
    }

    public abstract int getColumnCount();
    public abstract int getItemCount(int column_position);
    public abstract int maxItemCount(int column_position);
    public abstract Object createHeaderObject(int column_position);
    public abstract Object createFooterObject(int column_position);
    public abstract Object createItemObject(int column_position,int item_position);
    public abstract boolean isColumnLocked(int column_position);
    public abstract boolean isItemLocked(int column_position);
    public abstract View createItemView(Context context,Object header_object, Object item,int column_position,int item_position);
    public abstract View createHeaderView(Context context,Object header_object,int column_position);
    public abstract View createFooterView(Context context,Object footer_object,int column_position);

}
