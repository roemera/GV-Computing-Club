/** SilencerService.java */
package com.ciscomputingclub.silencer.services;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import com.ciscomputingclub.silencer.R;

/****************************************************************
 * com.ciscomputingclub.silencer.services.SilencerService
 * @author Caleb Gomer
 * @version 1.0
 ***************************************************************/
public class SilencerService extends Service {

	public static boolean isRunning;
	// private static boolean isSilent;
	// private static boolean isVibrate;
	public static int PHONEMODE = -1;
	public static final int SILENCER_NOTIFICATION = 1;
	private static final String TAG = "debug";
	private final long s = 1000L;
	private Timer timer;
	private TimerTask silencerTask = new TimerTask() {

		/****************************************************************
		 * @see java.util.TimerTask#run()
		 ***************************************************************/
		@Override
		public void run() {
			Log.d(TAG, "Timer task starting work");// LOG
			/**
			 * Check to see if user manually silenced phone, and
			 * act accordingly
			 * do not turn ringer back on if user silenced it manually,
			 * and make sure ringer comes back on after event passes
			 * (as long as Silencer initially silenced volume)
			 **/

			AudioManager aManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
			int ringerMode = aManager.getRingerMode();
			// SharedPreferences data = getSharedPreferences(
			// "ClassTimes", 0);

			// int newRingerMode = ringerMode(data.getString("data",
			// "NEVER"));
			int newRingerMode = ringerMode(PreferenceManager
			    .getDefaultSharedPreferences(getBaseContext())
			    .getString("data", "NEVER"));
			switch (newRingerMode) {
			case 0:
				ringerMode = AudioManager.RINGER_MODE_NORMAL;
				PHONEMODE = ringerMode;
				break;
			case 1:
				ringerMode = AudioManager.RINGER_MODE_SILENT;
				PHONEMODE = ringerMode;
				break;
			case 2:
				ringerMode = AudioManager.RINGER_MODE_VIBRATE;
				PHONEMODE = ringerMode;
				break;
			default:
				/*
				 * notify user of error and ask what ringer mode they
				 * would like
				 */
				break;
			}
			if (!(ringerMode == aManager.getRingerMode())) {
				Intent intent = new Intent(
				    "AudioManager.RINGER_MODE_NORMAL");
				intent.putExtra("silenced", true);

				aManager.setRingerMode(ringerMode);
				notifyUser("Silencer", "Ringer Mode Changed: "
				    + stringify(PHONEMODE), "Silencer Notification");
			}
			Log.d(TAG, "Timer task done working");// LOG
		}
	};

	/****************************************************************
	 * @see android.app.Service#onBind(android.content.Intent)
	 * @param arg0
	 * @return
	 ***************************************************************/
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	/****************************************************************
	 * @see android.app.Service#onCreate()
	 ***************************************************************/
	public void onCreate() {
		super.onCreate();
		Log.d(TAG, "SilencerService Creating");// LOG
		timer = new Timer("SilencerTimer");
		timer.scheduleAtFixedRate(silencerTask, 1 * s, 30 * s);
		((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE))
		    .cancel(SILENCER_NOTIFICATION);
		PHONEMODE = -1;
	}

	/****************************************************************
	 * @see android.app.Service#onDestroy()
	 ***************************************************************/
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "SilencerService Destroying");
		timer.cancel();
		timer = null;
		((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE))
		    .cancel(SILENCER_NOTIFICATION);
		PHONEMODE = -1;
	}

	/****************************************************************
	 * void
	 ***************************************************************/
	private void notifyUser(String contentTitle, String contentText,
	    String tickerText) {
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager notificationManager = (NotificationManager) getSystemService(ns);
		int icon = R.drawable.silencer;
		long when = System.currentTimeMillis();
		Notification notification = new Notification(icon,
		    tickerText, when);

		Context context = getApplicationContext();
		Intent notificationIntent = new Intent(this,
		    SilencerService.class);
		PendingIntent contentIntent = PendingIntent.getActivity(this,
		    0, notificationIntent, 0);
		notification.setLatestEventInfo(context, contentTitle,
		    contentText, contentIntent);
		notificationManager.notify(SILENCER_NOTIFICATION,
		    notification);
	}

	/****************************************************************
	 * @param timesToSilence
	 * @return int
	 ***************************************************************/
	private int ringerMode(String timesToSilence) {
		if (timesToSilence.equals("NEVER"))
			return 0;

		int result = 0;
		boolean silenced = false;
		boolean vibration = false;
		GregorianCalendar now = new GregorianCalendar();
		now.setTimeInMillis(System.currentTimeMillis());
		int hr = now.get(Calendar.HOUR_OF_DAY);
		String hour = ((hr < 10) ? "0" : "") + hr;
		int min = now.get(Calendar.MINUTE);
		String minute = ((min < 10) ? "0" : "") + min;
		String currentTime = hour + minute;

		String[][] silenceArray = new String[timesToSilence
		    .split("\n").length][];

		for (int i = 0; i < silenceArray.length; i++) {
			silenceArray[i] = timesToSilence.split("\n")[i]
			    .split("\t");
		}

		int dayOfWeek = now.get(Calendar.DAY_OF_WEEK) - 1;
		for (int i = 1; i < silenceArray[dayOfWeek].length
		    && !silenced; i++) {

			String[] silenceBetween = silenceArray[dayOfWeek][i]
			    .split("\r");

			int currentTimeInt = Integer.parseInt(currentTime);
			int startEvent = Integer.parseInt(silenceBetween[0]);
			int endEvent = Integer.parseInt(silenceBetween[1]);
			if (currentTimeInt >= startEvent - 1
			    && currentTimeInt <= endEvent) {
				silenced = true;
				if (silenceBetween.length > 2)
					vibration = silenceBetween[2].equals("V");
			}
		}
		if (silenced) {
			if (vibration)
				result = 2;
			else
				result = 1;
		}
		return result;
	}

	/****************************************************************
	 * @param phoneMode
	 * @return Stringified version of phone mode
	 ***************************************************************/
	private String stringify(int phoneMode) {
		String stringified = "";
		switch (phoneMode) {
		case -1:
			stringified = "Service Not Started";
			break;
		case 0:
			stringified = "Silent";
			break;
		case 1:
			stringified = "Vibrate";
			break;
		case 2:
			stringified = "Normal";
			break;
		}
		return stringified;
	}
}
