package com.allyants.boardviewexample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
        list.add("Item 2");
        list.add("Item 3");
        list.add("Item 3");
        list.add("Item 3");
        list.add("Item 3");
        list.add("Item 3");
        list.add("Item 3");
        list.add("Item 3");
        list.add("Item 3");
        list.add("Item 3");
        list.add("Item 3");
        list.add("Item 3");
        list.add("Item 3");
        list.add("Item 3");
        list.add("Item 3");
        list.add("Item 3");
        list.add("Item 3");
        list.add("Item 3");
        list.add("Item 3");
        list.add("Item 3");
        list.add("Item 3");
        list.add("Item 3");
        list.add("Item 3");
        list.add("Item 3");
        list.add("Item 4");
        data.add(new SimpleBoardAdapter.SimpleColumn("Column 1",list));
        data.add(new SimpleBoardAdapter.SimpleColumn("Column 2",list));
        data.add(new SimpleBoardAdapter.SimpleColumn("Column 3",list));
        data.add(new SimpleBoardAdapter.SimpleColumn("Column 4",list));
        data.add(new SimpleBoardAdapter.SimpleColumn("Column 5",list));
        final SimpleBoardAdapter boardAdapter = new SimpleBoardAdapter(this,data);

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
            public void endDrag(View view, int i, int i1) {
                Log.e("End Drag Column",String.valueOf(i1));
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
            public void endDrag(View view, int i, int i1, int i2, int i3) {
                Log.e("End Drag Item","Item: "+String.valueOf(i2)+"; Column:"+String.valueOf(i3));
            }
        });

    }
}
