package bgu.spl.a2.sim;

import bgu.spl.a2.sim.tools.GcdScrewDriver;
import bgu.spl.a2.sim.tools.NextPrimeHammer;
import bgu.spl.a2.sim.tools.RandomSumPliers;
import bgu.spl.a2.sim.tools.Tool;
import bgu.spl.a2.sim.conf.ManufactoringPlan;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedDeque;

import bgu.spl.a2.Deferred;

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
	private ConcurrentLinkedDeque<GcdScrewDriver> gsDrivers;
	private ConcurrentLinkedDeque<NextPrimeHammer> npHammers;
	private ConcurrentLinkedDeque<RandomSumPliers> rsPliers;
	private ConcurrentLinkedDeque<Deferred<Tool>> gsd_deferreds;
	private ConcurrentLinkedDeque<Deferred<Tool>> nph_deferreds;
	private ConcurrentLinkedDeque<Deferred<Tool>> rsp_deferreds;

	/**
	 * Constructor
	 */
	public Warehouse() {
		plans = new LinkedList<ManufactoringPlan>();
		gsDrivers = new ConcurrentLinkedDeque<GcdScrewDriver>();
		npHammers = new ConcurrentLinkedDeque<NextPrimeHammer>();
		rsPliers = new ConcurrentLinkedDeque<RandomSumPliers>();
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
		switch (type){
		case "gs-driver":
			gsd_deferreds.add(d);
			break;
		case "np-hammer":
			nph_deferreds.add(d);
			break;
		case "rs-pliers": 
			rsp_deferreds.add(d);
		}
		
		checkQueue(type); // try to check for tools right when a task asks for it
		
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
		addTool(tool, 1);
		checkQueue(tool.getType());
	}
	
	private void checkQueue(String type){
		Deferred<Tool> dTool;
		switch (type){
		case "gs-driver":
			synchronized (gsd_deferreds) {
				dTool = gsd_deferreds.poll();
				if (dTool != null) {
					GcdScrewDriver gsd = gsDrivers.poll();
					if (gsd != null)
						dTool.resolve(gsd);
					else 
						gsd_deferreds.add(dTool);
				}
			}
			break;
		case "np-hammer":
			synchronized (nph_deferreds) {
				dTool = nph_deferreds.poll();
				if (dTool != null) {
					NextPrimeHammer nph = npHammers.poll();
					if (nph != null)
						dTool.resolve(nph);
					else 
						nph_deferreds.add(dTool);
				}
			}
			break;
		case "rs-pliers": 
			synchronized (rsp_deferreds) {
				dTool = rsp_deferreds.poll();
				if (dTool != null) {
					RandomSumPliers rsp = rsPliers.poll();
					if (rsp != null)
						dTool.resolve(rsp);
					else 
						rsp_deferreds.add(dTool);
				}
			}
			break;
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
		for (int i = 0 ; i < plans.size() ; i++){
			if (plans.get(i).getProductName().equals(product))
				return plans.get(i);
		}
		return null; // if none found
	}

	/**
	 * Store a ManufactoringPlan in the warehouse for later retrieval
	 * 
	 * @param plan
	 *            - a ManufactoringPlan to be stored
	 */
	public void addPlan(ManufactoringPlan plan) {
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
		switch (tool.getType()){
		case "gs-driver":
			for (int i = 0; i < qty; i++)
				gsDrivers.add(new GcdScrewDriver());
			break;
		case "np-hammer":
			for (int i = 0; i < qty; i++)
				npHammers.add(new NextPrimeHammer());
			break;
		case "rs-pliers":
			for (int i = 0; i < qty; i++)
				rsPliers.add(new RandomSumPliers());
			break;
		}
	}

}
