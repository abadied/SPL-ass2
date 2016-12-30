package bgu.spl.a2.sim;

import bgu.spl.a2.Deferred;
import bgu.spl.a2.Task;
import bgu.spl.a2.sim.tools.Tool;

public class UseToolTask extends Task<Long>{
	
	Product product;
	Deferred<Tool> dTool;
	
	public UseToolTask(Product product, Deferred<Tool> dTool){
		this.product = product;
		this.dTool = dTool;
	}
	
	@Override
	protected void start() {
		complete(dTool.get().useOn(product));
		Simulator.warehouse.releaseTool(dTool.get());
	}
}
