package bgu.spl.a2.sim.tasks;

import java.util.ArrayList;

import bgu.spl.a2.Deferred;
import bgu.spl.a2.Task;
import bgu.spl.a2.sim.Product;
import bgu.spl.a2.sim.Warehouse;
import bgu.spl.a2.sim.tools.Tool;

/**
 * A task to construct the product from it's parts
 */
public class BuildProductTask extends Task<Product>{
	
	Product product;
	ArrayList<UseToolTask> uttList;
	ArrayList<Deferred<Product>> dParts;
	Warehouse warehouse;
	
	public BuildProductTask(long StartId, String name, Warehouse warehouse){
		this.product = new Product(StartId, name);
		this.warehouse = warehouse;
	}
	
	@Override
	protected void start() {
		// Acquire parts
		String[]  parts = warehouse.getPlan(product.getName()).getParts();
		
		if (parts.length == 0) { // no parts needed
			complete(product);
		}
		else {
			ArrayList<BuildProductTask> tasksArr = new ArrayList<>();
			dParts = new ArrayList<>();
			for(int i = 0; i < parts.length; i++) {
				BuildProductTask bpt = new BuildProductTask(product.getStartId() + 1 ,parts[i], warehouse);
				spawn(bpt);
				dParts.add(bpt.getResult());
				tasksArr.add(bpt);
			}
			
			// Once we have all parts, we'll get the tools and use them
			whenResolved(tasksArr, () -> acquireTools());
		}
	}
	
	/**
	 * getting the tools and using them
	 */
	private void acquireTools() {
		
		for(Deferred<Product> dPart : dParts)
			product.addPart(dPart.get());
		
		uttList = new ArrayList<>();
		for(String s: warehouse.getPlan(product.getName()).getTools()){
			Deferred<Tool> dTool = warehouse.acquireTool(s);
			UseToolTask utt = new UseToolTask(product, dTool, warehouse);
			uttList.add(utt);
			dTool.whenResolved(() -> spawn(utt)); // only once the tool is acquired, spawn the task
		}
		
		if(uttList.size() == 0) // no tools needed
			complete(product);
		
		else
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
