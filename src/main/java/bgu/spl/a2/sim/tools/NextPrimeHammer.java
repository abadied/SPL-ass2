/**
 * 
 */
package bgu.spl.a2.sim.tools;

import bgu.spl.a2.sim.Product;

import java.math.BigInteger;

public class NextPrimeHammer implements Tool {


	@Override
	public String getType() {
		//TODO:check if anything else needed in this function
		return new String("np-hammer");
	}


	
	
	@Override
	public long useOn(Product p) {
		
		long result = 0;
		BigInteger prime;
		for(Product part : p.getParts()){
			BigInteger b = new BigInteger(String.valueOf(part.getFinalId()));
			prime = b.nextProbablePrime();
			result += prime.longValue();
		}
        return result;
	}

}
