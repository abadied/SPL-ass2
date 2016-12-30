package bgu.spl.a2.sim;

import java.util.ArrayList;

import bgu.spl.a2.Deferred;
import bgu.spl.a2.Task;
import bgu.spl.a2.sim.tools.Tool;

public class UseToolTask extends Task<Long>{
	
	Product product;
	ArrayList<Deferred<Tool>> tools;
	
	public UseToolTask(Product product){
		this.product = product;
		tools = new ArrayList<Deferred<Tool>>();
	}
	
	@Override
	protected void start() {
		
		for(String s: Simulator.warehouse.getPlan(product.getName()).getTools()){
			tools.add(Simulator.warehouse.acquireTool(s));
		}
	}
}
