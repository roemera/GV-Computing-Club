/** ClassesActivity.java */
package com.ciscomputingclub.silencer;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ciscomputingclub.silencer.services.SilencerService;
import com.ciscomputingclub.silencer.views.ClassListItem;

/****************************************************************
 * com.ciscomputingclub.silencer.ClassesActivity
 * 
 * @author GVSU Computing Club
 * @version 1.0
 ***************************************************************/
public class ClassesActivity extends Activity implements
    OnClickListener {

	/** LinearLayout layout */
	LinearLayout layout;

	/** Button startService */
	Button startService;

	/****************************************************************
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 * @param savedInstanceState
	 ***************************************************************/
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.classes);

		layout = (LinearLayout) findViewById(R.id.classlist);

		SharedPreferences prefs = PreferenceManager
		    .getDefaultSharedPreferences(this);
		String classNames = prefs.getString("data", "NEVER");
		String bannerData = prefs.getString("banner_data", "logged_out");
		Log.d("debug", prefs.getString("banner_data", "logged_out"));// LOG
		if (!classNames.equals("")) {
//			String[] classNamesList = classNames.split("\t");
			String[] bannerdata = bannerData.split("<tr>");
//			for (String className : classNamesList) {
				for (String className : bannerdata) {
				ClassListItem classItem = new ClassListItem(
				    getApplicationContext(), className);
				layout.addView(classItem);
			}
		}

		startService = (Button) findViewById(R.id.start_service);
		startService.setOnClickListener(this);
	}

	/****************************************************************
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 * @param v
	 ***************************************************************/
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.start_service:
			Toast.makeText(this, "SilencerService Started",
			    Toast.LENGTH_LONG);
			Intent i = new Intent(getBaseContext(),
			    SilencerService.class);
			startService(i);
			break;
		}
	}

	/****************************************************************
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 * @param menu
	 * @return
	 ***************************************************************/
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.classes, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/****************************************************************
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 * @param item
	 * @return
	 ***************************************************************/
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean result = false;
		switch (item.getItemId()) {
		case R.id.logout_mybanner:
			// SharedPreferences classData = getSharedPreferences(
			// "ClassTimes", 0);
			// classData.edit().putString("data", "").commit();
			SharedPreferences prefs = PreferenceManager
			    .getDefaultSharedPreferences(getApplicationContext());
			Editor editor = prefs.edit();
			editor.putString("data", "logged_out");
			editor.putString("banner_data", "logged_out");
			editor.commit();
			Intent stopService = new Intent(getBaseContext(),
			    SilencerService.class);
			stopService(stopService);
			Intent loginIntent = new Intent(getBaseContext(),
			    LoginActivity.class);
			startActivity(loginIntent);
			finish();
			result = true;
			break;
		case R.id.stop_service:
			Intent i1 = new Intent(getBaseContext(),
			    SilencerService.class);
			stopService(i1);
			result = true;
			break;
		case R.id.prefs:
			Intent i2 = new Intent(getBaseContext(),
			    PrefsActivity.class);
			startActivity(i2);
			result = true;
			break;
		default:
			result = super.onOptionsItemSelected(item);
		}
		return result;
	}
}
