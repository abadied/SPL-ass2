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
		BigInteger b = new BigInteger(String.valueOf(p.getStartId() + 1));
        return Long.parseLong(b.nextProbablePrime().toString());
	}

	
}
