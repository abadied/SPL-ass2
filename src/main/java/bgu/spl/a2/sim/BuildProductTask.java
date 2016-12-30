package bgu.spl.a2.sim;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;

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
		ArrayList<BuildProductTask> tasksArr = new ArrayList<>();
		for(int i = 0; i<parts.length; i++) {
			BuildProductTask bpt = new BuildProductTask(new Product(product.getStartId() + 1,parts[i]));
			spawn(bpt);
			tasksArr.add(bpt);
		}
		
		// Once we have all parts, we'll get the tools and use them
		whenResolved(tasksArr, ()-> {
			acquireTools();
			});
		
	}
	
	private void acquireTools(){
		uttList = new ArrayList<>();
		for(String s: Simulator.warehouse.getPlan(product.getName()).getTools()){
			Deferred<Tool> dTool = Simulator.warehouse.acquireTool(s);
			UseToolTask utt = new UseToolTask(product, dTool);
			uttList.add(utt);
			dTool.whenResolved(() -> spawn(utt)); // when received the tool needed, spawn the task
		}
		
		whenResolved(uttList, () -> sumIDs());
	}
	
	private void sumIDs(){
		long ID = product.getStartId();
		
		for(UseToolTask utt : uttList)
			ID += utt.getResult().get().longValue();
		
		product.setFinalId(ID);
		
		complete(product);
	}
}
