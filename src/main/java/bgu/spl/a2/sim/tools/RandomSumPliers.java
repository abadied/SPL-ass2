package bgu.spl.a2.sim.tools;

import java.math.BigInteger;

import bgu.spl.a2.sim.Product;

public class RandomSumPliers implements Tool {

	@Override
	public String getType() {
		//TODO:check if anything else needed in this function
		return new String("rs-pliers");
	}

	@Override
	public long useOn(Product p) {
		// TODO : check!!!!
		long result = 0;
		BigInteger b = new BigInteger(String.valueOf(p.getStartId() + 1));
		long prime = Long.parseLong(b.nextProbablePrime().toString());
		for(int i = 0 ; i < p.getParts().size() ; i++){
			result += prime;
		}
        return result;
	}

	
}
