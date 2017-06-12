[![](https://jitpack.io/v/jakebonk/BoardView.svg)](https://jitpack.io/#jakebonk/BoardView)

# BoardView
BoardView is a custom view that allows you to be able to re-order items in a list as well as in a board. You can drag and drop items between columns as well as drag and drop columns.

## Download library with Jitpack.io
Add this to your build.gradle file for your app.

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}

Add this to your dependencies in build.gradle for your project.

	dependencies {
	        compile 'com.github.jakebonk:BoardView:1.0.0'
	}

## Usage

I'll probably switch this to some kind of adapter to simplify it.

	BoardView boardView = (BoardView)findViewById(R.id.boardview);
        final View item1 = View.inflate(this, R.layout.column_header, null);
        final View item2 = View.inflate(this, R.layout.column_header, null);
        final View item3 = View.inflate(this, R.layout.column_header, null);
        final View item4 = View.inflate(this, R.layout.column_header, null);
        ArrayList<View> items = new ArrayList<>();
        items.add(item1);
        items.add(item2);
        items.add(item3);
        items.add(item4);
        final View header = View.inflate(this, R.layout.column_header, null);
        boardView.addColumnList(header,items,null);
        final View header1 = View.inflate(this, R.layout.column_header, null);
        final View footer = View.inflate(this, R.layout.column_header, null);
        boardView.addColumnList(header1,new ArrayList<View>(),null);
        final View header2 = View.inflate(this, R.layout.column_header, null);
        boardView.addColumnList(header2,new ArrayList<View>(),null);
        final View header3 = View.inflate(this, R.layout.column_header, null);
        boardView.addColumnList(header3,new ArrayList<View>(),null);
        ((TextView)header.findViewById(R.id.textView)).setText("Hello World 1");
        ((TextView)header1.findViewById(R.id.textView)).setText("Hello World 2");
        ((TextView)header2.findViewById(R.id.textView)).setText("Hello World 3");
        ((TextView)header3.findViewById(R.id.textView)).setText("Hello World 4");
        ((TextView)item1.findViewById(R.id.textView)).setText("Item 1");
        ((TextView)item2.findViewById(R.id.textView)).setText("Item 2");
        ((TextView)item3.findViewById(R.id.textView)).setText("Item 3");
        ((TextView)item4.findViewById(R.id.textView)).setText("Item 4");
	
There are two types of drag listeners, the first is for columns

	 boardView.setOnDragColumnListener(new BoardView.DragColumnStartCallback() {
            @Override
            public void startDrag(View view, int startColumnPos) {

            }

            @Override
            public void changedPosition(View view, int startColumnPos, int newColumnPos) {

            }

            @Override
            public void endDrag(View view, int startColumnPos, int endColumnPos) {

            }
        });
	
Similarly we can get the drag listener for items

	 boardView.setOnDragItemListener(new BoardView.DragItemStartCallback() {
            @Override
            public void startDrag(View view, int startItemPos, int startColumnPos) {

            }

            @Override
            public void changedPosition(View view, int startItemPos, int startColumnPos, int newItemPos, int newColumnPos) {

            }

            @Override
            public void endDrag(View view, int startItemPos, int startColumnPos, int endItemPos, int endColumnPos) {

            }
        });

### Things to fix
There is a scaling issue when the column is beginning dragging or has ended dragging. I know this is an issue but I don't know of a good way to solve this at the moment. I eventually will fix it but for now I'm putting it on the back burners.
Another thing will be to create a custom list adapter that won't require the repition of creating views to add a column and it's items.
