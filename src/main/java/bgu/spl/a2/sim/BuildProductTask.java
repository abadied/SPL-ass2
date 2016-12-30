package bgu.spl.a2.sim;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedDeque;

import bgu.spl.a2.Deferred;
import bgu.spl.a2.Task;
import bgu.spl.a2.sim.conf.ManufactoringPlan;
import bgu.spl.a2.sim.tools.Tool;
import bgu.spl.a2.test.MergeSort;

public class BuildProductTask extends Task<Product>{
	
	Warehouse warehouse;
	Product product;
	
	public BuildProductTask(Warehouse warehouse, Product product){
		this.warehouse = warehouse;
		this.product = product;
	}
	
	@Override
	protected void start() {
		String[]  parts = warehouse.getPlan(product.getName()).getParts();
		BuildProductTask[] tasks = new BuildProductTask[parts.length];
		for(int i = 0; i<parts.length; i++){
			tasks[i] = new BuildProductTask(warehouse , new Product(product.getStartId() + 1,parts[i]));
		}
		spawn(tasks);
		ArrayList<BuildProductTask> tasksArr = new ArrayList<>();
		for(BuildProductTask t: tasks){
			tasksArr.add(t);
		}
		whenResolved(tasksArr, ()-> {
			acquireTools(warehouse, product);
			complete(product);
			});
		
	}
	
	private void acquireTools(Warehouse warehouse , Product product){
		ConcurrentLinkedDeque<Deferred<Tool>> tools = new ConcurrentLinkedDeque<Deferred<Tool>>();
		for(String s: warehouse.getPlan(product.getName()).getTools()){
			tools.add(warehouse.acquireTool(s));
		}
		for(Deferred<Tool> d: tools){
			d.whenResolved(()->{
				product.setFinalId(d.get().useOn(product));
				warehouse.releaseTool(d.get());
			});
		}
	}
		
}
