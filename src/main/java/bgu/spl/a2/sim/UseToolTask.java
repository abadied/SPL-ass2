package bgu.spl.a2.sim;

import bgu.spl.a2.Deferred;
import bgu.spl.a2.Task;
import bgu.spl.a2.sim.tools.Tool;

/**
 * A task to use a tool on a product
 * 
 * Spawn this task only once dTool is resolved
 */
public class UseToolTask extends Task<Long>{
	
	Product product;
	Deferred<Tool> dTool;
	
	/**
	 * Constructor
	 * 
	 * @param product the product that requires work
	 * @param dTool the tool to work on the given product
	 */
	public UseToolTask(Product product, Deferred<Tool> dTool){
		this.product = product;
		this.dTool = dTool;
	}
	
	
	/**
	 * use the tool on the product
	 * assumes the deferred is already resolved
	 */
	@Override
	protected void start() {
		complete(dTool.get().useOn(product));
		Simulator.warehouse.releaseTool(dTool.get());
	}
}
