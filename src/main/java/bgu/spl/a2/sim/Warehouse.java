package bgu.spl.a2.sim;

import bgu.spl.a2.sim.tools.GcdScrewDriver;
import bgu.spl.a2.sim.tools.NextPrimeHammer;
import bgu.spl.a2.sim.tools.RandomSumPliers;
import bgu.spl.a2.sim.tools.Tool;
import bgu.spl.a2.sim.conf.ManufactoringPlan;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicInteger;

import bgu.spl.a2.Deferred;
import bgu.spl.a2.Task;

/**
 * A class representing the warehouse in your simulation
 * 
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In addition, the methods you add to this class can
 * only be private!!!
 *
 */
public class Warehouse {

	private LinkedList<ManufactoringPlan> plans;
	private GcdScrewDriver gsd;
	private NextPrimeHammer nph;
	private RandomSumPliers rsp;
	private AtomicInteger gsd_counter;
	private AtomicInteger nph_counter;
	private AtomicInteger rsp_counter;
	private ConcurrentLinkedDeque<Deferred<Tool>> gsd_deferreds;
	private ConcurrentLinkedDeque<Deferred<Tool>> nph_deferreds;
	private ConcurrentLinkedDeque<Deferred<Tool>> rsp_deferreds;

	/**
	 * Constructor
	 */
	public Warehouse() {
		plans = new LinkedList<ManufactoringPlan>();
		gsd = new GcdScrewDriver();
		nph = new NextPrimeHammer();
		rsp = new RandomSumPliers();
		gsd_counter = new AtomicInteger(0);
		nph_counter = new AtomicInteger(0);
		rsp_counter = new AtomicInteger(0);
		gsd_deferreds = new ConcurrentLinkedDeque<Deferred<Tool>>();
		nph_deferreds = new ConcurrentLinkedDeque<Deferred<Tool>>();
		rsp_deferreds = new ConcurrentLinkedDeque<Deferred<Tool>>();
		
	}

	/**
	 * Tool acquisition procedure Note that this procedure is non-blocking and
	 * should return immediately
	 * 
	 * @param type
	 *            - string describing the required tool
	 * @return a deferred promise for the requested tool
	 */
	public Deferred<Tool> acquireTool(String type) {
		Deferred<Tool> d = new Deferred<Tool>();
		switch(type){
		case "gs-driver":{
			if(gsd_counter.get() > 0){
				d.resolve(gsd);
				gsd_counter.decrementAndGet();
			}
			else	
				gsd_deferreds.add(d);
		}
			break;
		case "np-hammer":{
			if(nph_counter.get() > 0){
				d.resolve(nph);
				nph_counter.decrementAndGet();
			}
			else	
				nph_deferreds.add(d);
		}
			break;
		case "rs-pliers": {
			if(rsp_counter.get() > 0){
				d.resolve(rsp);
				rsp_counter.decrementAndGet();
			}
			else	
				rsp_deferreds.add(d);
		}
			break;
		default : break;
		}
		return d;
	}

	/**
	 * Tool return procedure - releases a tool which becomes available in the
	 * warehouse upon completion.
	 * 
	 * @param tool
	 *            - The tool to be returned
	 */
	public void releaseTool(Tool tool) {
		// TODO:check
		this.addTool(tool, 1);
		switch(tool.getType()){
		case "gs-driver":{
			gsd_counter.incrementAndGet();
			if(!gsd_deferreds.isEmpty())
				gsd_deferreds.poll().resolve(gsd);
		}
			break;
		case "np-hammer": {
			nph_counter.incrementAndGet();
			if(!nph_deferreds.isEmpty())
				nph_deferreds.poll().resolve(nph);
		}
			break;
		case "rs-pliers": {
			rsp_counter.incrementAndGet();
			if(!rsp_deferreds.isEmpty())
				rsp_deferreds.poll().resolve(rsp);
		}
			break;
		default : break;
		}
		
	}

	/**
	 * Getter for ManufactoringPlans
	 * 
	 * @param product
	 *            - a string with the product name for which a ManufactoringPlan
	 *            is desired
	 * @return A ManufactoringPlan for product
	 */
	public ManufactoringPlan getPlan(String product) {
		// TODO:check
		for(int i = 0 ; i < plans.size() ; i++){
			if(plans.get(i).getProductName().equals(product))
				return plans.remove(i);
				
		}
		return null;// change
	}

	/**
	 * Store a ManufactoringPlan in the warehouse for later retrieval
	 * 
	 * @param plan
	 *            - a ManufactoringPlan to be stored
	 */
	public void addPlan(ManufactoringPlan plan) {
		// TODO:check
		plans.addLast(plan);

	}

	/**
	 * Store a qty Amount of tools of type tool in the warehouse for later
	 * retrieval
	 * 
	 * @param tool
	 *            - type of tool to be stored
	 * @param qty
	 *            - amount of tools of type tool to be stored
	 */
	public void addTool(Tool tool, int qty) {
		// TODO:check
		switch(tool.getType()){
		case "gs-driver": gsd_counter.addAndGet(qty);
			break;
		case "np-hammer": nph_counter.addAndGet(qty);
			break;
		case "rs-pliers": rsp_counter.addAndGet(qty);
			break;
		default : break;
		}
	}
	


}
