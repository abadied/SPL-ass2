package bgu.spl.a2.sim;

import bgu.spl.a2.Task;
import bgu.spl.a2.sim.conf.ManufactoringPlan;

public class BuildProductTask extends Task<Product>{
	
	String productName;
	int startId;
	
	public BuildProductTask(String productName, int startId){
		this.productName = productName;
	}
	
	@Override
	protected void start() {
		// TODO: make it build
	}
	
}
