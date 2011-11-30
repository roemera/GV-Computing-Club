/** LoginActivity.java */
package com.ciscomputingclub.silencer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/****************************************************************
 * com.ciscomputingclub.silencer.LoginActivity
 * 
 * @author Caleb Gomer
 * @version 1.0 DIFFERENT TEXT
 ***************************************************************/
public class LoginActivity extends Activity implements
    OnClickListener, ThreadCompleteListener {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// SharedPreferences prefs =
		// getSharedPreferences("ClassTimes",
		// 0);
		// String data = prefs.getString("data", "");
		SharedPreferences prefs = PreferenceManager
		    .getDefaultSharedPreferences(getApplicationContext());
		String harddata = prefs.getString("data", "");
		String bannerdata = prefs.getString("banner_data", "");
		if (harddata.equals("logged_out") || harddata.equals("")
		    || bannerdata.equals("logged_out")
		    || bannerdata.equals("")) {

			setContentView(R.layout.login);
			Button buttonLogin = (Button) findViewById(R.id.button_start);
			buttonLogin.setOnClickListener(this);
		} else {
			gotoClassScreen();
		}
	}

	/****************************************************************
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 * @param v
	 ***************************************************************/
	public void onClick(View v) {
		((Button) v).setEnabled(false);
		((Button) v).setText("Working...");

		String tempGNumber = ((TextView) findViewById(R.id.username))
		    .getText().toString();
		final String gNumber = (tempGNumber.substring(0, 1)
		    .equalsIgnoreCase("G")) ? (tempGNumber)
		    : ("G" + tempGNumber);
		final String password = ((TextView) findViewById(R.id.password))
		    .getText().toString();

		NotifyingThread scrapeThread = new NotifyingThread() {
			@Override
			public void runThenNotify() {
				try {
					BannerScraper myBannerScraper = new BannerScraper(
					    gNumber, password);
					String bannerScrapeData = myBannerScraper
					    .fetchWeek();
					PreferenceManager
					    .getDefaultSharedPreferences(getBaseContext())
					    .edit()
					    .putString("banner_data", bannerScrapeData)
					    .commit();
				} catch (Exception e) {
					Log.d("debug", e.getMessage());// LOG
				}
			}
		};
		scrapeThread.addListener(this);
		scrapeThread.start();

		SharedPreferences prefs = PreferenceManager
		    .getDefaultSharedPreferences(this);
		// Log.d("debug", prefs.getString("banner_data",
		// "No Data Yet"));
		// showDialog(prefs.getString("banner_data", "No Data Yet"),
		// "Data for " + gNumber.toUpperCase());

		String data = ""
		    + "Su\t0000\r0500\rS\rSleeping\rXXX\t0630\r1530\rV\rWork\rXXX\n"
		    + "Mo\t1300\r1400\rV\rPhysics\rPHY\r231\t1600\r1800\rV\rCalculus\t2100\r2200\rS\rXXX\n"
		    + "Tu\t0000\r0900\rS\t1130\r1245\rV\t1300\r1350\rV\t2100\r2400\rS\n"
		    + "We\t0000\r0500\rS\t0630\r1130\rV\t1300\r1500\rV\t1600\r1800\rS\n"
		    + "Th\t0000\r0900\rS\t0800\r0900\rV\t1200\r1400\rV\n"
		    + "Fr\t2015\r2020\rV\t2030\r2045\rV\n"
		    + "Sa\t0630\r1530\rV\t2120\r2124\rS\t2100\r2359\rS\n";

		PreferenceManager.getDefaultSharedPreferences(this).edit()
		    .putString("data", data).commit();

		// Intent intent = new Intent(getBaseContext(),
		// ClassesActivity.class);
		// startActivity(intent);
		// finish();
	}

	private void showDialog(String info, String title) {

		AlertDialog.Builder builder;
		AlertDialog alertDialog;

		LayoutInflater inflater = (LayoutInflater) this
		    .getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.text_dialog,
		    (ViewGroup) findViewById(R.id.layout_root));

		TextView text = (TextView) layout
		    .findViewById(R.id.text_level);
		text.setText(info);

		builder = new AlertDialog.Builder(this);
		builder.setView(layout);
		alertDialog = builder.create();
		alertDialog.setTitle(title);
		alertDialog.show();
	}

	/****************************************************************
	 * @see com.ciscomputingclub.silencer.ThreadCompleteListener#notifyOfThreadComplete(java.lang.Thread)
	 * @param thread
	 ***************************************************************/
	public void notifyOfThreadComplete(Thread thread) {
		gotoClassScreen();
	}

	private void gotoClassScreen() {
		Intent intent = new Intent(getBaseContext(),
		    ClassesActivity.class);
		startActivity(intent);
		finish();
	}
}
