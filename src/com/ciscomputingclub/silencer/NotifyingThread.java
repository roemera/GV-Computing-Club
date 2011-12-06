/** NotifyingThread.java */
package com.ciscomputingclub.silencer;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import android.os.Looper;

/****************************************************************
 * com.ciscomputingclub.silencer.NotifyingThread
 * @author Caleb Gomer
 * @version 1.0
 ***************************************************************/
public abstract class NotifyingThread extends Thread {
	private final Set<ThreadCompleteListener> listeners = new CopyOnWriteArraySet<ThreadCompleteListener>();
	private String extras = new String();

	public final void addListener(
	    final ThreadCompleteListener listener) {
		listeners.add(listener);
	}

	public final void removeListener(
	    final ThreadCompleteListener listener) {
		listeners.remove(listener);
	}

	private final void notifyListeners() {
		for (ThreadCompleteListener listener : listeners) {
			listener.notifyOfThreadComplete(this);
		}
	}
	
	public final String getExtras() {
		return extras;
	}

	@Override
	public final void run() {
		Looper.prepare();
		try {
			extras = runThenNotify();
		} finally {
			notifyListeners();
		}
	}

	public abstract String runThenNotify();
}