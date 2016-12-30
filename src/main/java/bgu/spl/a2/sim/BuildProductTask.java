package bgu.spl.a2.sim;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedDeque;

import bgu.spl.a2.Deferred;
import bgu.spl.a2.Task;
import bgu.spl.a2.sim.conf.ManufactoringPlan;
import bgu.spl.a2.sim.tools.Tool;
import bgu.spl.a2.test.MergeSort;

public class BuildProductTask extends Task<Product>{
	
	Product product;
	
	public BuildProductTask(Product product){
		this.product = product;
	}
	
	@Override
	protected void start() {
		String[]  parts = Simulator.warehouse.getPlan(product.getName()).getParts();
		BuildProductTask[] tasks = new BuildProductTask[parts.length];
		for(int i = 0; i<parts.length; i++){
			tasks[i] = new BuildProductTask(new Product(product.getStartId() + 1,parts[i]));
		}
		spawn(tasks);
		ArrayList<BuildProductTask> tasksArr = new ArrayList<>();
		for(BuildProductTask t: tasks){
			tasksArr.add(t);
		}
		whenResolved(tasksArr, ()-> {
			acquireTools(product);
			complete(product);
			});
		
	}
	
	private void acquireTools(Product product){
		ConcurrentLinkedDeque<Deferred<Tool>> tools = new ConcurrentLinkedDeque<Deferred<Tool>>();
		for(String s: Simulator.warehouse.getPlan(product.getName()).getTools()){
			tools.add(Simulator.warehouse.acquireTool(s));
		}
		for(Deferred<Tool> d: tools){
			d.whenResolved(()->{
				product.setFinalId(d.get().useOn(product));
				Simulator.warehouse.releaseTool(d.get());
			});
		}
	}
		
}
