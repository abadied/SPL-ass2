package bgu.spl.a2.sim;

import java.util.ArrayList;

import bgu.spl.a2.Deferred;
import bgu.spl.a2.Task;
import bgu.spl.a2.sim.tools.Tool;

public class BuildProductTask extends Task<Product>{
	
	Product product;
	ArrayList<UseToolTask> uttList;
	
	public BuildProductTask(Product product){
		this.product = product;
	}
	
	@Override
	protected void start() {
		// Acquire parts
		String[]  parts = Simulator.warehouse.getPlan(product.getName()).getParts();
		
		if (parts.length == 0) {
			complete(product);
		}
		
		else {
			ArrayList<BuildProductTask> tasksArr = new ArrayList<>();
			for(int i = 0; i < parts.length; i++) {
				BuildProductTask bpt = new BuildProductTask(new Product(product.getStartId() + 1,parts[i]));
				spawn(bpt);
				tasksArr.add(bpt);
			}
			
			// Once we have all parts, we'll get the tools and use them
			whenResolved(tasksArr, () -> acquireTools());
		}
	}
	
	/**
	 * getting the tools and using them
	 */
	private void acquireTools(){
		uttList = new ArrayList<>();
		for(String s: Simulator.warehouse.getPlan(product.getName()).getTools()){
			Deferred<Tool> dTool = Simulator.warehouse.acquireTool(s);
			UseToolTask utt = new UseToolTask(product, dTool);
			uttList.add(utt);
			dTool.whenResolved(() -> spawn(utt)); // only once the tool is acquired, spawn the task
		}
		
		whenResolved(uttList, () -> sumIDs()); // once all tool tasks are done, sum all their results
	}
	
	/**
	 * sums all the IDs for this product finalID
	 */
	private void sumIDs(){
		long ID = product.getStartId();
		
		for(UseToolTask utt : uttList)
			ID += utt.getResult().get().longValue(); // adding the results from the tools
		
		product.setFinalId(ID);
		
		complete(product);
	}
}
