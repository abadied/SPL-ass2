package bgu.spl.a2.sim;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;

import bgu.spl.a2.Deferred;
import bgu.spl.a2.Task;
import bgu.spl.a2.sim.tools.Tool;

public class BuildProductTask extends Task<Product>{
	
	Product product;
	
	public BuildProductTask(Product product){
		this.product = product;
		
	}
	
	@Override
	protected void start() {
		// Acquire parts
		String[]  parts = Simulator.warehouse.getPlan(product.getName()).getParts();
		ArrayList<BuildProductTask> tasksArr = new ArrayList<>();
		for(int i = 0; i<parts.length; i++) {
			BuildProductTask bpt = new BuildProductTask(new Product(product.getStartId() + 1,parts[i]));
			spawn(bpt);
			tasksArr.add(bpt);
		}
		
		// Once we have all parts, we'll get the tools and use them
		whenResolved(tasksArr, ()-> {
			//acquireTools(product);
			});
		
	}
	
	
}
