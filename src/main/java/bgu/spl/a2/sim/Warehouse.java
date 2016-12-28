package bgu.spl.a2.sim;

import bgu.spl.a2.sim.tools.Tool;
import bgu.spl.a2.sim.conf.ManufactoringPlan;

import java.util.ArrayList;
import java.util.LinkedList;

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
	private ArrayList<LinkedList<Tool>> tools;
	/**
	 * Constructor
	 */
	public Warehouse() {
		// TODO
		plans = new LinkedList<ManufactoringPlan>();
		tools = new ArrayList<LinkedList<Tool>>();
	}

	/**
	 * Tool acquisition procedure Note that this procedure is non-blocking and
	 * should return immediatly
	 * 
	 * @param type
	 *            - string describing the required tool
	 * @return a deferred promise for the requested tool
	 */
	public Deferred<Tool> acquireTool(String type) {
		// TODO
		
		return null;// change
	}

	/**
	 * Tool return procedure - releases a tool which becomes available in the
	 * warehouse upon completion.
	 * 
	 * @param tool
	 *            - The tool to be returned
	 */
	public void releaseTool(Tool tool) {
		// TODO
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
		// TODO
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
		// TODO
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
		// TODO
		switch(tool.getType()){
		case "gs-driver": this.addToolByType(tool,qty ,0);
			break;
		case "np-hammer": this.addToolByType(tool,qty ,1);
			break;
		case "rs-pliers": this.addToolByType(tool,qty ,2);
			break;
		default : break;
		}
	}
	
	//add the tool by his type "qty" times
	private void addToolByType(Tool tool, int qty , int type){
		for(int i = 0 ; i < qty ; i++){
			tools.get(0).add(tool);
		}
	}

}
