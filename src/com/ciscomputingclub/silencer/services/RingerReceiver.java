/** RingerReciever.java */
package com.ciscomputingclub.silencer.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.util.Log;

import com.ciscomputingclub.silencer.R;

/****************************************************************
 * com.ciscomputingclub.silencer.services.RingerReciever
 * 
 * @author Caleb Gomer
 * @version 1.0
 ***************************************************************/
public class RingerReceiver extends BroadcastReceiver {

	public static final int RINGER_NOTIFICATION = 2;

	/****************************************************************
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context,
	 *      android.content.Intent)
	 * @param context
	 * @param intent
	 ***************************************************************/
	public void onReceive(Context context, Intent intent) {
		int shouldBe = SilencerService.PHONEMODE;
		if (shouldBe != -1) {
			int ringerMode = ((AudioManager) context
					.getSystemService(Context.AUDIO_SERVICE)).getRingerMode();
			Log.d("debug", "RingerMode: " + ringerMode + " Should be: "
					+ SilencerService.PHONEMODE);// LOG
			if (ringerMode != SilencerService.PHONEMODE) {
				Log.d("debug", "Turning Off Service");// LOG
				Intent i = new Intent(context, SilencerService.class);
				context.stopService(i);
				notifyUser(context);
			} else {
				Log.d("debug", "Leaving Service On");
			}
		} else {
			Log.d("debug", "Service Not Started, Ignoring Volume Change");
		}
	}

	/****************************************************************
	 * void
	 ***************************************************************/
	private void notifyUser(Context context) {

		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(ns);
		int icon = R.drawable.silencer;
		CharSequence tickerText = "Auto-silencer disabled";
		long when = System.currentTimeMillis();
		Notification notification = new Notification(icon, tickerText, when);

		CharSequence contentTitle = "Silencer Disabled";
		CharSequence contentText = "Click here to re-enable";
		Intent notificationIntent = new Intent(context, SilencerService.class);
		notificationIntent
				.setAction("com.ciscomputingclub.silencer.services.SilencerService");
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
				notificationIntent, 0);
		notification.setLatestEventInfo(context, contentTitle, contentText,
				contentIntent);
		notificationManager.notify(RINGER_NOTIFICATION, notification);
	}

}
