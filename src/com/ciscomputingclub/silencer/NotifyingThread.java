/** NotifyingThread.java */
package com.ciscomputingclub.silencer;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/****************************************************************
 * com.ciscomputingclub.silencer.NotifyingThread
 * @author Caleb Gomer
 * @version 1.0
 ***************************************************************/
public abstract class NotifyingThread extends Thread {
	private final Set<ThreadCompleteListener> listeners = new CopyOnWriteArraySet<ThreadCompleteListener>();

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

	@Override
	public final void run() {
		try {
			runThenNotify();
		} finally {
			notifyListeners();
		}
	}

	public abstract void runThenNotify();
}