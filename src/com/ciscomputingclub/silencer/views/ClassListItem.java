/** ClassListItem.java */
package com.ciscomputingclub.silencer.views;

import com.ciscomputingclub.silencer.R;

import android.content.Context;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

/****************************************************************
 * com.ciscomputingclub.silencer.views.ClassListItem
 * @author Caleb Gomer
 * @version 1.0
 ***************************************************************/
public class ClassListItem extends LinearLayout {

	/****************************************************************
	 * @param context
	 ***************************************************************/
	public ClassListItem(Context context, String className) {
		super(context);

		setOrientation(HORIZONTAL);
		setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
		    LayoutParams.WRAP_CONTENT));
		setMinimumHeight(50);
		setPadding(10, 10, 10, 10);
		setGravity(Gravity.CENTER_VERTICAL);

		ToggleButton toggleVibrate = new ToggleButton(getContext());
		toggleVibrate.setLayoutParams(new LayoutParams(
		    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1));
		toggleVibrate.setGravity(1);
		toggleVibrate.setText("Silent");
		toggleVibrate.setTextOff("Silent");
		toggleVibrate.setTextOn("Vibrate");
		addView(toggleVibrate);

		TextView viewClassName = new TextView(getContext());
		viewClassName.setLayoutParams(new LayoutParams(
		    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 9));
		viewClassName.setPadding(10, 0, 0, 0);
		viewClassName.setTextSize(25);
		viewClassName.setText(className);
		viewClassName.setTextColor(R.color.black);
		addView(viewClassName);
	}
}
