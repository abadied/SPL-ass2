package bgu.spl.a2;


import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingDeque;

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
	ArrayList<LinkedBlockingDeque<Task<?>>> queues;
	ArrayList<Thread> threads;
	VersionMonitor version;
	
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
		
		processors = new ArrayList<Processor>();
		queues = new ArrayList<LinkedBlockingDeque<Task<?>>>();
		for(int i=0; i < nthreads; i++){
			processors.add(new Processor(i,this));
			queues.add(new LinkedBlockingDeque<Task<?>>());
			threads.add(new Thread(processors.get(i)));
		}
		//TODO: check!!!
	}

	/**
	 * submits a task to be executed by a processor belongs to this thread pool
	 *
	 * @param task
	 *            the task to execute
	 */
	public void submit(Task<?> task) {
		
		int id = (int)(Math.random() * (processors.size() - 1));
		addTasksToProccessor(id, task);
		version.inc();
		
		//TODO:check!!!!
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
		
		// TODO: change to interrupt->Test
		for(Thread t: threads)
			t.interrupt();
	}

	/**
	 * start the threads belongs to this thread pool
	 */
	public void start() {
		
		// TODO: test
		
		for(Thread t: threads)
			t.start();
	}

	
	/* package */ Task<?> fetch(int id){
		//TODO:test
		Task<?> t = queues.get(id).pollFirst();
		if(t != null){
			return t;
		}
		else
			return steal(id);
	}
	
	
	private Task<?> steal(int id){
		//TODO:test
		while(queues.get(id).isEmpty()){
			int tmp_ver = version.getVersion();
			for(LinkedBlockingDeque<Task<?>> q: queues){
				if(!q.isEmpty() && q != queues.get(id)){
					for(int i = 0;i < (int)(q.size()/2) ; i++)
						queues.get(id).add(q.pollFirst());
					
				}
			}
			if(queues.get(id).isEmpty())
				try{
					version.await(tmp_ver);
				}
				catch (InterruptedException e){
					//remove if not necessary 
					System.err.println("InterruptedException: " + e.getMessage());//for testing!
				}
		}
		return queues.get(id).pollFirst();
	}
	
	
	// adds any number of tasks to processor's queue by ID
	/* package */ void addTasksToProccessor(int id, Task<?>... tasks) {
		for (Task<?> task : tasks) {
			queues.get(id).add(task);
		}
		//TODO::check!!
	}
}
