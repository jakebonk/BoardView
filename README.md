[![](https://jitpack.io/v/jakebonk/BoardView.svg)](https://jitpack.io/#jakebonk/BoardView)

# BoardView
BoardView is a custom view that allows you to be able to re-order items in a list as well as in a board. You can drag and drop items between columns as well as drag and drop columns.

## Example

![Basic Example](https://thumbs.gfycat.com/DeadUntidyHartebeest-size_restricted.gif)

## Download library with Jitpack.io
Add this to your build.gradle file for your app.
```java
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```	

Add this to your dependencies in build.gradle for your project.
```java
	dependencies {
	        compile 'com.github.jakebonk:BoardView:1.1.6'
	}
```
## Usage

BoardView utilizes a BoardAdapter, SimpleBoardAdapter is an example of how to extend BoardAdapter.
```java
	BoardView boardView = (BoardView)findViewById(R.id.boardview);
	ArrayList<SimpleBoardAdapter.SimpleColumn> data = new ArrayList<>();
        ArrayList<String> list = new ArrayList<String>();
        list.add("Item 1");
        list.add("Item 2");
        list.add("Item 3");
        list.add("Item 4");
        data.add(new SimpleBoardAdapter.SimpleColumn("Column 1",list));
        data.add(new SimpleBoardAdapter.SimpleColumn("Column 2",list));
        data.add(new SimpleBoardAdapter.SimpleColumn("Column 3",list));
        data.add(new SimpleBoardAdapter.SimpleColumn("Column 4",list));
        data.add(new SimpleBoardAdapter.SimpleColumn("Column 5",list));
        SimpleBoardAdapter boardAdapter = new SimpleBoardAdapter(this,data);
        boardView.setAdapter(boardAdapter);
```
There are two types of drag listeners, the first is for columns
```java
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
```
Similarly we can get the drag listener for items
```java
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
```

There is also a listener for when the BoardView has finished creating and assigning its views.

```java 

	 boardView.setOnDoneListener(new BoardView.DoneListener() {
            @Override
            public void onDone() {
                Log.e("ee","Done");
            }
        });

```

This is how to set the click listener for a item and header, which gives their respective positions.
	
```java
	
	boardView.setOnItemClickListener(new BoardView.ItemClickListener() {
            @Override
            public void onClick(View v, int column_pos, int item_pos) {
                
            }
        });
        boardView.setOnHeaderClickListener(new BoardView.HeaderClickListener() {
            @Override
            public void onClick(View v, int column_pos) {
                
            }
        });

```
	
### Creating your own BoardAdapter

Creating a custom BoardAdapter is pretty similar to that of a BaseAdapter, the main focus being to create some type of object that help you create your custom views for both headers and items.

### Things to fix
There is a scaling issue when the column is beginning dragging or has ended dragging. I know this is an issue but I don't know of a good way to solve this at the moment. I eventually will fix it but for now I'm putting it on the back burners.
