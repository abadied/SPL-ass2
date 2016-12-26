package bgu.spl.a2;


import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * represents a work stealing thread pool - to understand what this class does
 * please refer to your assignment.
 *
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In addition, the methods you add can only be
 * private, protected or package protected - in other words, no new public
 * methods
 */
public class WorkStealingThreadPool {

	ArrayList<Processor> processors;
	ArrayList<ConcurrentLinkedDeque<Task<?>>> queues;
	ArrayList<Thread> threads;
	VersionMonitor versionMonitor;
	int nthreads;
	
	/**
	 * creates a {@link WorkStealingThreadPool} which has nthreads
	 * {@link Processor}s. Note, threads should not get started until calling to
	 * the {@link #start()} method.
	 *
	 * Implementors note: you may not add other constructors to this class nor
	 * you allowed to add any other parameter to this constructor - changing
	 * this may cause automatic tests to fail..
	 *
	 * @param nthreads
	 *            the number of threads that should be started by this thread
	 *            pool
	 */
	public WorkStealingThreadPool(int nthreads) {
		this.nthreads = nthreads;
		processors = new ArrayList<Processor>();
		queues = new ArrayList<ConcurrentLinkedDeque<Task<?>>>();
		threads = new ArrayList<Thread>();
		versionMonitor = new VersionMonitor();
		
		for(int i=0; i < nthreads; i++){
			processors.add(new Processor(i,this));
			queues.add(new ConcurrentLinkedDeque<Task<?>>());
			threads.add(new Thread(processors.get(i)));
		}
	}

	/**
	 * submits a task to be executed by a processor belongs to this thread pool
	 *
	 * @param task
	 *            the task to execute
	 */
	public void submit(Task<?> task) {
		int id = (int)(Math.random() * (processors.size() - 1));
		addTasksToProcessor(id, task);
	}

	/**
	 * closes the thread pool - this method interrupts all the threads and wait
	 * for them to stop - it is returns *only* when there are no live threads in
	 * the queue.
	 *
	 * after calling this method - one should not use the queue anymore.
	 *
	 * @throws InterruptedException
	 *             if the thread that shut down the threads is interrupted
	 * @throws UnsupportedOperationException
	 *             if the thread that attempts to shutdown the queue is itself a
	 *             processor of this queue
	 */
	public void shutdown() throws InterruptedException {
		for(Thread t: threads)
			t.interrupt();
		for(Thread t: threads)
			t.join();
	}

	/**
	 * start the threads belongs to this thread pool
	 */
	public void start() {
		for(Thread t: threads)
			t.start();
	}

	/**
	 * returns the next task in queue. If the queue is empty, will try to steal tasks from other queues
	 * 
	 * @param id the processor asking for next task
	 * @return a task to be handled
	 * @throws InterruptedException
	 * 				if the thread was interrupted while waiting for a task
	 */
	/* package */ Task<?> fetch(int id) throws InterruptedException {
		
		Task<?> t = null;
		int currVersion;
		
		while (t == null){
			currVersion = versionMonitor.getVersion();
			
			t = queues.get(id).pollFirst();
			if (t == null) { // nothing in queue
				t = stealAndFetch(id);
			}
			if (t == null) { // nothing to steal
				versionMonitor.await(currVersion); // wait until the pool gets new Tasks
			}
		}
		
		return t;
	}
	
	/**
	 * try to steal tasks from other processors, stops after one successful heist
	 * 
	 * @param robberId the stealing processor
	 * @return a task to be sent to the processor, null if nothing was stolen
	 */
	/* package */ Task<?> stealAndFetch(int robberId){
		
		Task<?> t = null;
		
		for (int i = robberId + 1; (i % nthreads) != robberId; i++) {
			t = stealFromVictimAndFetch(robberId, i % nthreads);
			if (t != null)
				break;
		}
		
		return t;
	}
	
	/**
	 * this method tries to steal tasks from a specific victim and add it to the robber's queue
	 * 
	 * @param robberId the stealing processor
	 * @param victimId the processor to try and steal from
	 * @return a task to be sent to the processor, null if victim's queue is empty
	 */
	/* package */ Task<?> stealFromVictimAndFetch(int robberId, int victimId) {
		ConcurrentLinkedDeque<Task<?>> victim = queues.get(victimId);
		
		int maxTasksToSteal = victim.size() / 2;
		Task<?> t = victim.pollLast();
		if (t == null)
			return null;
		
		Task<?> nextTask = null;
		
		for(int i = 1; i < maxTasksToSteal; i++){
			nextTask = victim.pollLast();
			if (nextTask == null) // victim queue may get empty while trying to steal
				break;
			addTasksToProcessor(robberId, nextTask);
		}
		
		return t;
		
	}
	
	
	/**
	 * 
	 * Adds tasks to a specific processor's queue
	 * 
	 * @param id the target processor
	 * @param tasks the tasks to be added
	 */
	/* package */ void addTasksToProcessor(int id, Task<?>... tasks) {
		for (Task<?> task : tasks) {
			queues.get(id).add(task);
		}
		versionMonitor.inc(); // informs all waiting processors that there's a new task in the pool
	}
}
