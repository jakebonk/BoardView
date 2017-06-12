[![](https://jitpack.io/v/jakebonk/BoardView.svg)](https://jitpack.io/#jakebonk/BoardView)

# BoardView
BoardView is a custom view that allows you to be able to re-order items in a list as well as in a board. You can drag and drop items between columns as well as drag and drop columns.

### Download library with Jitpack.io
Add this to your build.gradle file for your app

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}

Add this to your dependencies in build.gradle for your project

	dependencies {
	        compile 'com.github.jakebonk:BoardView:0.1.0'
	}

### Things to fix
There is a scaling issue when the column is beginning dragging or has ended dragging. I know this is an issue but I don't know of a good way to solve this at the moment. I eventually will fix it but for now I'm putting it on the back burners.
