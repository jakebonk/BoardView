package com.allyants.boardviewexample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.allyants.boardview.BoardView;
import com.allyants.boardview.SimpleBoardAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final BoardView boardView = (BoardView)findViewById(R.id.boardView);
        ArrayList<SimpleBoardAdapter.SimpleColumn> data = new ArrayList<>();
        ArrayList<String> list = new ArrayList<String>();
        list.add("Item 1");
        list.add("Item 1");
        list.add("Item 1");
        list.add("Item 1");
        list.add("Item 1");
        list.add("Item 1");
        list.add("Item 1");
        list.add("Item 1");
        list.add("Item 1");
        list.add("Item 1");
        list.add("Item 1");
        list.add("Item 1");
        list.add("Item 1");
        list.add("Item 1");
        list.add("Item 1");
        list.add("Item 1");
        list.add("Item 1");
        list.add("Item 1");
        list.add("Item 1");
        list.add("Item 1");
        list.add("Item 1");
        list.add("Item 1");
        list.add("Item 1");
        list.add("Item 1");
        list.add("Item 1");
        list.add("Item 1");
        list.add("Item 1");
        list.add("Item 1");
        list.add("Item 1");
        list.add("Item 1");
        list.add("Item 1");
        list.add("Item 1");
        list.add("Item 1");
        list.add("Item 1");
        list.add("Item 1");
        list.add("I am a very long list that is not the same size as the others. I am a multiline");
        list.add("Item 1");
        list.add("Item 1");
        list.add("Item 1");
        list.add("Item 1");
        list.add("Item 1");
        list.add("Item 1");
        list.add("Item 1");
        list.add("Item 1");
        list.add("Item 1");
        list.add("Item 1");
        list.add("Item 1");
        list.add("Item 1");
        list.add("Item 1");
        list.add("Item 1");
        ArrayList<String> empty = new ArrayList<String>();
        data.add(new SimpleBoardAdapter.SimpleColumn("Column 1",list));
        data.add(new SimpleBoardAdapter.SimpleColumn("Column 2",empty));
        data.add(new SimpleBoardAdapter.SimpleColumn("Column 3",list));
        data.add(new SimpleBoardAdapter.SimpleColumn("Column 4",list));
        data.add(new SimpleBoardAdapter.SimpleColumn("Column 5",list));
        final SimpleBoardAdapter boardAdapter = new SimpleBoardAdapter(this,data);
        boardView.setAdapter(boardAdapter);
        boardView.setAdapter(boardAdapter);
        boardView.setOnDoneListener(new BoardView.DoneListener() {
            @Override
            public void onDone() {
                Log.e("scroll","done");
            }
        });
        boardView.setOnItemClickListener(new BoardView.ItemClickListener() {
            @Override
            public void onClick(View v, int column_pos, int item_pos) {
                boardView.scrollToColumn(column_pos,false);
            }
        });
        boardView.setOnHeaderClickListener(new BoardView.HeaderClickListener() {
            @Override
            public void onClick(View v, int column_pos) {
                Log.e("ee",String.valueOf(column_pos));
            }
        });
        boardView.setOnDragColumnListener(new BoardView.DragColumnStartCallback() {
            @Override
            public void startDrag(View view, int i) {
                Log.e("Start Drag Column",String.valueOf(i));
            }

            @Override
            public void changedPosition(View view, int i, int i1) {
                Log.e("Change Drag Column",String.valueOf(i1));
            }

            @Override
            public void dragging(View itemView, MotionEvent event) {
                Log.e("Pos X",String.valueOf(event.getRawX()));
                Log.e("Pos Y",String.valueOf(event.getRawY()));
            }

            @Override
            public void endDrag(View view, int i, int i1) {
                Log.e("End Drag Column",String.valueOf(i1));
            }
        });
        boardView.setOnFooterClickListener(new BoardView.FooterClickListener() {
            @Override
            public void onClick(View v, int column_pos) {
                Log.e("Footer Click","Column: "+String.valueOf(column_pos));
            }
        });
        boardView.setOnDragItemListener(new BoardView.DragItemStartCallback() {
            @Override
            public void startDrag(View view, int i, int i1) {
                Log.e("Start Drag Item","Item: "+String.valueOf(i1)+"; Column:"+String.valueOf(i));
            }

            @Override
            public void changedPosition(View view, int i, int i1, int i2, int i3) {
                Log.e("Change Drag Item","Item: "+String.valueOf(i2)+"; Column:"+String.valueOf(i3));
            }

            @Override
            public void dragging(View itemView, MotionEvent event) {
                Log.e("Pos X",String.valueOf(event.getRawX()));
                Log.e("Pos Y",String.valueOf(event.getRawY()));
            }

            @Override
            public void endDrag(View view, int i, int i1, int i2, int i3) {
                Log.e("End Drag Item","Item: "+String.valueOf(i2)+"; Column:"+String.valueOf(i3));
            }
        });

    }
}
