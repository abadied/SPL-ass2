package bgu.spl.a2.sim.tools;

import java.util.Random;

import bgu.spl.a2.sim.Product;

public class RandomSumPliers implements Tool {

	@Override
	public String getType() {
		return new String("rs-pliers");
	}

	@Override
	public long useOn(Product p) {
		
		long sum = 0;
		for(Product part : p.getParts()){
			Random r = new Random(part.getFinalId());
			long partsum = 0;
			for(int j = 0; j < (part.getFinalId()%10000); j++){
				partsum += r.nextInt();
			}
			sum += Math.abs(partsum);
		}
		return sum;
	}

	
}
