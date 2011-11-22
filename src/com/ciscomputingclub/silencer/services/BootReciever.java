/** BootReciever.java */
package com.ciscomputingclub.silencer.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/****************************************************************
 * com.ciscomputingclub.silencer.services.BootReciever
 * 
 * @author Caleb Gomer
 * @version 1.0
 ***************************************************************/
public class BootReciever extends BroadcastReceiver {

	/****************************************************************
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context,
	 *      android.content.Intent)
	 * @param context
	 * @param intent
	 ***************************************************************/
	public void onReceive(Context context, Intent intent) {
		Log.v("debug", "Service Started on Startup");// LOG
		Intent serviceIntent = new Intent(context, SilencerService.class);
		context.startService(serviceIntent);
	}

}
