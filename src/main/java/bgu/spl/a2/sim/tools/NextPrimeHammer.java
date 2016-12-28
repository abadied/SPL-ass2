/**
 * 
 */
package bgu.spl.a2.sim.tools;

import bgu.spl.a2.sim.Product;
import java.util.Random;


public class NextPrimeHammer implements Tool {


	@Override
	public String getType() {
		//TODO:check if anything else needed in this function
		return new String("np-hammer");
	}


	@Override
	public long useOn(Product p) {
		//TODO:check!!!!
		Random r = new Random(p.getStartId());
		long sum = 0;
		for(int i = 0; i < (p.getStartId()%10000); i++){
			sum += r.nextInt();
		}
		return sum;
	}

}
