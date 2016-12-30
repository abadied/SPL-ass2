package bgu.spl.a2;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Describes a monitor that supports the concept of versioning - its idea is
 * simple, the monitor has a version number which you can receive via the method
 * {@link #getVersion()} once you have a version number, you can call
 * {@link #await(int)} with this version number in order to wait until this
 * version number changes.
 *
 * you can also increment the version number by one using the {@link #inc()}
 * method.
 *
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In addition, the methods you add can only be
 * private, protected or package protected - in other words, no new public
 * methods
 */
public class VersionMonitor {
	
	private AtomicInteger i = new AtomicInteger(0);
	
	public int getVersion() {
		return i.get();
	}

	public void inc() {
		i.incrementAndGet();
		synchronized(this) {
			notifyAll();
		}
	}
	
	public void await(int version) throws InterruptedException {
		int curr_ver = i.get();
		synchronized(this) {
		while (curr_ver == version)
				wait();
		}
	}
}
