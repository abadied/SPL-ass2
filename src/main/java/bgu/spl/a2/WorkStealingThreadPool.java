package bgu.spl.a2;

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
		// TODO: replace method body with real implementation
		
		// create array of processors
		// create array of queues
		
		throw new UnsupportedOperationException("Not Implemented Yet.");
	}

	/**
	 * submits a task to be executed by a processor belongs to this thread pool
	 *
	 * @param task
	 *            the task to execute
	 */
	public void submit(Task<?> task) {
		
		int id = 0; // TODO: check in assignment what ID needs to be selected
		
		addTasksToProccessor(id, task);
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
		
		// TODO: call shutdown on each processor
		
		throw new UnsupportedOperationException("Not Implemented Yet.");
	}

	/**
	 * start the threads belongs to this thread pool
	 */
	public void start() {
		
		// TODO: call start on each thread of processors
		
		throw new UnsupportedOperationException("Not Implemented Yet.");
	}

	
	/* package */ Task<?> giveTask(int id) {
		
		// TODO: if queue is empty, steal
		// TODO: return task from queue
		
		throw new UnsupportedOperationException("Not Implemented Yet.");
	}
	
	// adds any number of tasks to processor's queue by ID
	/* package */ void addTasksToProccessor(int id, Task<?>... tasks) {
		for (Task<?> task : tasks) {
			// TODO: add task to queue by id
		}
		
		throw new UnsupportedOperationException("Not Implemented Yet.");
	}
}
