package bgu.spl.a2.sim.tasks;

import bgu.spl.a2.Deferred;
import bgu.spl.a2.Task;
import bgu.spl.a2.sim.Product;
import bgu.spl.a2.sim.Warehouse;
import bgu.spl.a2.sim.tools.Tool;

/**
 * A task to use a tool on a product
 * 
 * Spawn this task only once dTool is resolved
 */
public class UseToolTask extends Task<Long>{
	
	Product product;
	Deferred<Tool> dTool;
	Warehouse warehouse;
	
	/**
	 * Constructor
	 * 
	 * @param product the product that requires work
	 * @param dTool the tool to work on the given product
	 */
	public UseToolTask(Product product, Deferred<Tool> dTool, Warehouse warehouse){
		this.product = product;
		this.dTool = dTool;
		this.warehouse = warehouse;
	}
	
	
	/**
	 * use the tool on the product
	 * assumes the deferred is already resolved
	 */
	@Override
	protected void start() {
		complete(dTool.get().useOn(product));
		warehouse.releaseTool(dTool.get());
	}
}
